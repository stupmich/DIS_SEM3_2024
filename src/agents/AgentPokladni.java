package agents;

import Entities.Worker;
import OSPABA.*;
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

	public AgentPokladni(int id, Simulation mySim, Agent parent)
	{
		super(id, mySim, parent);
		init();
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
}