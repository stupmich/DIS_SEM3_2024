package managers;

import Entities.Customer;
import Entities.Worker;
import OSPABA.*;
import OSPDataStruct.SimQueue;
import simulation.*;
import agents.*;
import continualAssistants.*;

import java.sql.SQLOutput;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Random;

//meta! id="7"
public class ManagerPokladni extends Manager
{
	private Random[] indexPaymentSameLengthOfQueueGenerator;
	private Random[] indexPaymentEmptyQueueGenerator;

	public ManagerPokladni(int id, Simulation mySim, Agent myAgent)
	{
		super(id, mySim, myAgent);
		init();

		this.indexPaymentSameLengthOfQueueGenerator = new Random[((MySimulation)mySim()).getNumberOfWorkersPayment() - 1];
		for (int i = 0; i < ((MySimulation)mySim()).getNumberOfWorkersPayment() - 1; i++) {
			this.indexPaymentSameLengthOfQueueGenerator[i] = new Random(((MySimulation)mySim()).getSeedGenerator().nextInt());
		}

		this.indexPaymentEmptyQueueGenerator = new Random[((MySimulation)mySim()).getNumberOfWorkersPayment() - 1];
		for (int i = 0; i < ((MySimulation)mySim()).getNumberOfWorkersPayment() - 1; i++) {
			this.indexPaymentEmptyQueueGenerator[i] = new Random(((MySimulation)mySim()).getSeedGenerator().nextInt());
		}
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication

		if (petriNet() != null)
		{
			petriNet().clear();
		}
	}

	//meta! sender="AgentElektra", id="22", type="Notice"
	public void processJeCasObedu(MessageForm message)
	{
		// send all free workers to lunch
		myAgent().getWorkersPaymentLunch().addAll(myAgent().getWorkersPayment());
		myAgent().getWorkersPayment().clear();
		myAgent().setLunchTime(true);

		for (Worker worker : myAgent().getWorkersPaymentLunch()) {
			if (worker.getId() != 0) {
				// people in queues go to first cash register
				myAgent().getQueuesCustomersWaitingForPayment().get(0).addAll(myAgent().getQueuesCustomersWaitingForPayment().get(worker.getId()));
				myAgent().getQueuesCustomersWaitingForPayment().get(worker.getId()).clear();
			} else {
				myAgent().setFirstWorkerCashierOnLunch(true);
			}
		}
		// TODO MOZNO SORT PODLA CASU ZACIATKU CAKANIA

		// send message to get worker for first cash register from first service place
//		MyMessage nextMessage = new MyMessage(((MyMessage) message));
		message.setCode(Mc.dajPracovnika);
		message.setAddressee(mySim().findAgent(Id.agentElektra));
		request(message);
	}

	//meta! sender="AgentElektra", id="29", type="Request"
	public void processPlatenie(MessageForm message)
	{
		int numberOfFreeWorkersPayment = myAgent().getWorkersPayment().size();

		if (numberOfFreeWorkersPayment == 0) {
			addCustomerToQueue(message);
		} else if (numberOfFreeWorkersPayment == 1) {
			Worker workerPayment = myAgent().getWorkersPayment().removeLast();
			workerPayment.setIdCustomer(((MyMessage)message).getCustomer().getId());
			workerPayment.setCustomer(((MyMessage)message).getCustomer());

			myAgent().getWorkersPaymentWorking().add(workerPayment);

			message.setAddressee(myAgent().findAssistant(Id.procesPlatba));
			((MyMessage) message).setWorker(workerPayment);
			startContinualAssistant(message);
		} else {
			int indexOfWorkerPayment = indexPaymentEmptyQueueGenerator[numberOfFreeWorkersPayment - 2].nextInt(numberOfFreeWorkersPayment);

			Worker workerPayment = myAgent().getWorkersPayment().remove(indexOfWorkerPayment);
			workerPayment.setIdCustomer(((MyMessage)message).getCustomer().getId());
			workerPayment.setCustomer(((MyMessage)message).getCustomer());

			myAgent().getWorkersPaymentWorking().add(workerPayment);

			message.setAddressee(myAgent().findAssistant(Id.procesPlatba));
			((MyMessage) message).setWorker(workerPayment);
			startContinualAssistant(message);
		}
	}

	//meta! sender="ProcesPlatba", id="54", type="Finish"
	public void processFinish(MessageForm message) {
		Worker worker = ((MyMessage) message).getWorker();
		Customer c = ((MyMessage) message).getCustomer();

		if (myAgent().isLunchTime() && worker.getType() == Worker.WorkerType.PAYMENT) {
			// if it is lunch time -> send worker to lunch
			worker.setIdCustomer(-1);
			worker.setCustomer(null);
			myAgent().getWorkersPaymentLunch().add(worker);
			myAgent().getWorkersPaymentWorking().remove(worker);

			if (worker.getId() != 0) {
				// move his queue to first cash register queue
				myAgent().getQueuesCustomersWaitingForPayment().get(0).addAll(myAgent().getQueuesCustomersWaitingForPayment().get(worker.getId()));
				myAgent().getQueuesCustomersWaitingForPayment().get(worker.getId()).clear();
			} else {
				// worker from first cash register ended payment and  goes to lunch
				myAgent().setFirstWorkerCashierOnLunch(true);

				if (myAgent().getWorkerFromService() != null) {
					// there is worker from service ready to work at first cash register
					startNewPaymentLunchTime(myAgent().getWorkerFromService());
					myAgent().setWorkerFromService(null);
				} else {
					// np worker from service is not free yet
					// cash register will be empty
				}
			}

		} else if (!myAgent().isLunchTime() && worker.getType() != Worker.WorkerType.PAYMENT) {
			// it is end of lunch break and worker from service ended payment -> goes back to its service place
			myAgent().setFirstWorkerCashierOnLunch(false);

			startNewPaymentLunchTime(myAgent().getWorkerFirstCashier());

			myAgent().getWorkersPaymentWorking().remove(worker);

			// send message to return worker to its service place
//			MyMessage nextMessage = new MyMessage(((MyMessage) message));
			MyMessage nextMessage = new MyMessage(mySim());
			nextMessage.setCustomer(null);
			worker.clearCustomer();
			nextMessage.setWorker(worker);
			nextMessage.setCode(Mc.vrateniePracovnika);
			nextMessage.setAddressee(mySim().findAgent(Id.agentElektra));
			notice(nextMessage);
		} else {
			// standard scenario when worker tries to serve next customer or is free
			if (myAgent().getQueuesCustomersWaitingForPayment().get(worker.getId()).isEmpty()) {
				// no customers in queue for this worker -> worker free
				worker.setIdCustomer(-1);
				worker.setCustomer(null);
				myAgent().getWorkersPayment().add(worker);
				myAgent().getWorkersPaymentWorking().remove(worker);
			} else {
				// customer is waiting in queue for this worker -> new payment
				MessageForm nextCustomerMessage = myAgent().getQueuesCustomersWaitingForPayment().get(worker.getId()).removeFirst();
				Customer nextCustomer = ((MyMessage)nextCustomerMessage).getCustomer();

				worker.setIdCustomer(nextCustomer.getId());
				worker.setCustomer(nextCustomer);

				nextCustomerMessage.setAddressee(myAgent().findAssistant(Id.procesPlatba));
				((MyMessage) nextCustomerMessage).setWorker(worker);
				startContinualAssistant(nextCustomerMessage);
			}
		}

		message.setCode(Mc.platenie);
		((MyMessage)message).setWorker(null);
//		response(message);
		try {
			response(message);
		} catch (NoSuchElementException e) {
			System.out.println("Error: Tried to remove an element from an empty stack.");
			System.out.println(message.deliveryTime());
			System.out.println(mySim().currentReplication());
		}
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
		}
	}

	//meta! sender="AgentElektra", id="91", type="Notice"
	public void processJeKoniecCasuObedu(MessageForm message)
	{
		if (mySim().currentReplication() == 241) {
			System.out.println();
		}

		if (mySim().currentReplication() == 240) {
			System.out.println();
		}

		myAgent().setLunchTime(false);

		if (myAgent().getWorkersPaymentWorking().size() == 1) {
			// worker from service is working at this time
			for ( Worker workerLunch : myAgent().getWorkersPaymentLunch()) {
				if (workerLunch.getId() == 0) {
					// save first cashier
					myAgent().setWorkerFirstCashier(workerLunch);
				}
			}
			myAgent().getWorkersPaymentLunch().remove(myAgent().getWorkerFirstCashier());
		} else if (myAgent().getWorkersPayment().size() == 1 ) {
			// worker is free -> returning to service place
			myAgent().setFirstWorkerCashierOnLunch(false);

			try {
				Worker workerFromService = myAgent().getWorkersPayment().removeFirst();
				workerFromService.clearCustomer();

				((MyMessage)message).setWorker(workerFromService);
				message.setCode(Mc.vrateniePracovnika);
				message.setAddressee(mySim().findAgent(Id.agentElektra));
				notice(message);
			} catch (NoSuchElementException e) {
				System.out.println("Error: Tried to remove an element from an empty stack.");
				System.out.println(message.deliveryTime());
				System.out.println(mySim().currentReplication());
			}
		} else {
			// no worker from service came
			System.out.println(mySim().currentReplication());
			myAgent().setFirstWorkerCashierOnLunch(false);
		}

		// workers come back from lunch -> free to work
		for (Worker worker : myAgent().getWorkersPaymentLunch()) {
			startNewPaymentLunchTime(worker);
		}

		myAgent().getWorkersPaymentLunch().clear();
	}

	//meta! sender="AgentElektra", id="96", type="Response"
	public void processDajPracovnika(MessageForm message)
	{
		if (mySim().currentReplication() == 241) {
			System.out.println();
		}

		Worker worker = ((MyMessage)message).getWorker();

		if (myAgent().isFirstWorkerCashierOnLunch()) {
			startNewPaymentLunchTime(worker);
		} else {
			// original worker is still working on its place
			myAgent().setWorkerFromService(worker);
		}
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	public void init()
	{
	}

	@Override
	public void processMessage(MessageForm message)
	{
		switch (message.code())
		{
		case Mc.finish:
			processFinish(message);
		break;

		case Mc.jeCasObedu:
			processJeCasObedu(message);
		break;

		case Mc.platenie:
			processPlatenie(message);
		break;

		case Mc.jeKoniecCasuObedu:
			processJeKoniecCasuObedu(message);
		break;

		case Mc.dajPracovnika:
			processDajPracovnika(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

	@Override
	public AgentPokladni myAgent()
	{
		return (AgentPokladni)super.myAgent();
	}

	public void addCustomerToQueue(MessageForm mess) {
		if (myAgent().isLunchTime()) {
			myAgent().getQueuesCustomersWaitingForPayment().get(0).add(mess);
		} else {
			int minQueueLength = Integer.MAX_VALUE;
			SimQueue<SimQueue< MessageForm >> shortestQueues = new SimQueue<>();

			// get shortest queues
			for (SimQueue<MessageForm> queue : myAgent().getQueuesCustomersWaitingForPayment()) {
				if (queue.size() < minQueueLength) {
					shortestQueues.clear();
					shortestQueues.add(queue);
					minQueueLength = queue.size();
				} else if (queue.size() == minQueueLength) {
					shortestQueues.add(queue);
				}
			}

			// pick randomly queue where customer goes
			if (shortestQueues.size() == 1 ) {
				shortestQueues.get(0).add(mess);
			} else {
				int selectedQueueIndex = this.indexPaymentSameLengthOfQueueGenerator[shortestQueues.size() - 2].nextInt(0, shortestQueues.size());
				shortestQueues.get(selectedQueueIndex).add(mess);
			}
		}
	}

	public void startNewPaymentLunchTime(Worker worker) {
		if (myAgent().getQueuesCustomersWaitingForPayment().get(worker.getId()).isEmpty()) {
			// no customers in queue for first cash register
			myAgent().getWorkersPayment().add(worker);
		} else {
			// customer is waiting in queue for first cash register -> new payment
			myAgent().getWorkersPaymentWorking().add(worker);

			MessageForm nextCustomerMessage = myAgent().getQueuesCustomersWaitingForPayment().get(worker.getId()).removeFirst();
			Customer nextCustomer = ((MyMessage)nextCustomerMessage).getCustomer();

			worker.setIdCustomer(nextCustomer.getId());
			worker.setCustomer(nextCustomer);

			nextCustomerMessage.setAddressee(myAgent().findAssistant(Id.procesPlatba));
			((MyMessage) nextCustomerMessage).setWorker(worker);
			startContinualAssistant(nextCustomerMessage);
		}
	}
}