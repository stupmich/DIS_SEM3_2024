package agents;

import Entities.Customer;
import Entities.Worker;
import OSPABA.*;
import OSPDataStruct.SimQueue;
import OSPStat.WStat;
import simulation.*;
import managers.*;
import continualAssistants.*;

import java.util.LinkedList;

//meta! id="7"
public class AgentPokladni extends Agent
{
	private int highestWorkersPaymentID;
	private SimQueue<Worker> workersPayment;
	private SimQueue<Worker> workersPaymentWorking;
	private SimQueue<Worker> workersPaymentLunch;
	private SimQueue<SimQueue< MessageForm >> queuesCustomersWaitingForPayment;
	private boolean lunchTime = false;

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
		this.workersPayment = new SimQueue<Worker>(new WStat(mySim()));
		this.workersPaymentWorking = new SimQueue<Worker>(new WStat(mySim()));

		for (int i = 0; i < ((MySimulation)mySim()).getNumberOfWorkersPayment(); i++ ) {
			Worker worker = new Worker(this.highestWorkersPaymentID, Worker.WorkerType.PAYMENT, mySim());
			workersPayment.add(worker);
			this.highestWorkersPaymentID++;
		}

		this.queuesCustomersWaitingForPayment = new SimQueue<SimQueue<MessageForm>>();
		for (int i = 0; i < ((MySimulation)mySim()).getNumberOfWorkersPayment(); i++) {
			this.queuesCustomersWaitingForPayment.add(new SimQueue<MessageForm>());
		}

		this.workersPaymentLunch = new SimQueue<Worker>();
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		new ManagerPokladni(Id.managerPokladni, mySim(), this);
		new ProcesPlatba(Id.procesPlatba, mySim(), this);
		addOwnMessage(Mc.jeCasObedu);
		addOwnMessage(Mc.platenie);
		addOwnMessage(Mc.jeKoniecCasuObedu);
	}
	//meta! tag="end"


	public SimQueue<Worker> getWorkersPayment() {
		return workersPayment;
	}

	public SimQueue<Worker> getWorkersPaymentWorking() {
		return workersPaymentWorking;
	}

	public SimQueue<SimQueue<MessageForm>> getQueuesCustomersWaitingForPayment() {
		return queuesCustomersWaitingForPayment;
	}

	public SimQueue<Worker> getWorkersPaymentLunch() {
		return workersPaymentLunch;
	}

	public boolean isLunchTime() {
		return lunchTime;
	}

	public void setLunchTime(boolean lunchTime) {
		this.lunchTime = lunchTime;
	}
}