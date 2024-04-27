package agents;

import Entities.Customer;
import Entities.Worker;
import OSPABA.*;
import OSPDataStruct.SimQueue;
import simulation.*;
import managers.*;
import continualAssistants.*;

import java.util.LinkedList;

//meta! id="7"
public class AgentPokladni extends Agent
{
	private int highestWorkersPaymentID;
	private LinkedList<Worker> workersPayment;
	private LinkedList<Worker> workersPaymentWorking;
	private SimQueue<SimQueue< MessageForm >> queuesCustomersWaitingForPayment;

	public AgentPokladni(int id, Simulation mySim, Agent parent)
	{
		super(id, mySim, parent);
		init();
		addOwnMessage(Mc.koniecPlatby);
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
		highestWorkersPaymentID = 0;
		this.workersPayment = new LinkedList<Worker>();
		this.workersPaymentWorking = new LinkedList<Worker>();

		for (int i = 0; i < ((MySimulation)mySim()).getNumberOfWorkersPayment(); i++ ) {
			Worker worker = new Worker(this.highestWorkersPaymentID, Worker.WorkerType.PAYMENT, mySim());
			workersPayment.add(worker);
			this.highestWorkersPaymentID++;
		}

		this.queuesCustomersWaitingForPayment = new SimQueue<SimQueue<MessageForm>>();
		for (int i = 0; i < ((MySimulation)mySim()).getNumberOfWorkersPayment(); i++) {
			this.queuesCustomersWaitingForPayment.add(new SimQueue<MessageForm>());
		}
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		new ManagerPokladni(Id.managerPokladni, mySim(), this);
		new ProcesPlatba(Id.procesPlatba, mySim(), this);
		addOwnMessage(Mc.jeCasObedu);
		addOwnMessage(Mc.platenie);
	}
	//meta! tag="end"


	public LinkedList<Worker> getWorkersPayment() {
		return workersPayment;
	}

	public LinkedList<Worker> getWorkersPaymentWorking() {
		return workersPaymentWorking;
	}

	public SimQueue<SimQueue<MessageForm>> getQueuesCustomersWaitingForPayment() {
		return queuesCustomersWaitingForPayment;
	}
}