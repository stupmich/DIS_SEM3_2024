package managers;

import Entities.Customer;
import Entities.Worker;
import OSPABA.*;
import OSPDataStruct.SimQueue;
import simulation.*;
import agents.*;
import continualAssistants.*;

import java.util.LinkedList;
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

//			TODO STATS
//			myAgent().getAverageUsePercentPaymentStat().updateStatistics(core, myAgent().getWorkersPaymentWorking());
			myAgent().getWorkersPaymentWorking().add(workerPayment);

			message.setAddressee(myAgent().findAssistant(Id.procesPlatba));
			((MyMessage) message).setWorker(workerPayment);
			startContinualAssistant(message);
		} else {
			int indexOfWorkerPayment = indexPaymentEmptyQueueGenerator[numberOfFreeWorkersPayment - 2].nextInt(numberOfFreeWorkersPayment);

			Worker workerPayment = myAgent().getWorkersPayment().remove(indexOfWorkerPayment);
			workerPayment.setIdCustomer(((MyMessage)message).getCustomer().getId());
			workerPayment.setCustomer(((MyMessage)message).getCustomer());

//			TODO STATS
//			myAgent().getAverageUsePercentPaymentStat().updateStatistics(core, myAgent().getWorkersPaymentWorking());
			myAgent().getWorkersPaymentWorking().add(workerPayment);

			message.setAddressee(myAgent().findAssistant(Id.procesPlatba));
			((MyMessage) message).setWorker(workerPayment);
			startContinualAssistant(message);
		}
	}

	//meta! sender="ProcesPlatba", id="54", type="Finish"
	public void processFinish(MessageForm message)
	{
		Worker worker = ((MyMessage)message).getWorker();

		if (myAgent().getQueuesCustomersWaitingForPayment().get(worker.getId()).isEmpty()) {
			// no customers in queue for this worker -> worker free
			worker.setIdCustomer(-1);
			worker.setCustomer(null);
			myAgent().getWorkersPayment().add(worker);

//			TODO STATS
//			((Sem2) core).getAverageUsePercentPaymentStat().updateStatistics(core, ((Sem2) core).getWorkersPaymentWorking());
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

		message.setCode(Mc.platenie);
		((MyMessage)message).setWorker(null);
		response(message);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
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
		case Mc.platenie:
			processPlatenie(message);
		break;

		case Mc.jeCasObedu:
			processJeCasObedu(message);
		break;

		case Mc.finish:
			processFinish(message);
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