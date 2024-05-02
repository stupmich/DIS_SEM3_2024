package agents;

import Entities.Worker;
import OSPABA.*;
import OSPDataStruct.SimQueue;
import OSPStat.Stat;
import OSPStat.WStat;
import simulation.*;
import managers.*;
import continualAssistants.*;

import java.util.LinkedList;

//meta! id="6"
public class AgentObsluznychMiest extends Agent
{
	private int highestWorkersOrderID;
	private SimQueue< MessageForm > CustomersWaitingInShopBeforeOrder;
	private SimQueue<Worker> workersOrderNormal;
	private SimQueue<Worker> workersOrderOnline;
	private SimQueue<Worker> workersOrderWorkingNormal;
	private SimQueue<Worker> workersOrderWorkingOnline;
	private Stat averageTimeWaitingServiceStat;

	public AgentObsluznychMiest(int id, Simulation mySim, Agent parent)
	{
		super(id, mySim, parent);
		init();
		addOwnMessage(Mc.koniecDiktovania);
		addOwnMessage(Mc.koniecPripravyObjednavky);
		addOwnMessage(Mc.koniecVyzdvihnutiaVelkejObjednavky);
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
		highestWorkersOrderID = 0;
		CustomersWaitingInShopBeforeOrder = new SimQueue<>(new WStat(mySim()));

		this.workersOrderNormal = new SimQueue<Worker>(new WStat(mySim()));
		this.workersOrderOnline = new SimQueue<Worker>(new WStat(mySim()));
		this.workersOrderWorkingNormal = new SimQueue<Worker>(new WStat(mySim()));
		this.workersOrderWorkingOnline = new SimQueue<Worker>(new WStat(mySim()));

		for (int i = 0; i < ((MySimulation)mySim()).getNumberOfNormalWorkers(); i++ ) {
			Worker worker = new Worker(highestWorkersOrderID, Worker.WorkerType.ORDER_REGULAR_AND_CONTRACT, mySim());
			workersOrderNormal.add(worker);
			highestWorkersOrderID++;
		}

		for (int i = 0; i < ((MySimulation)mySim()).getNumberOfOnlineWorkers(); i++ ) {
			Worker worker = new Worker(highestWorkersOrderID, Worker.WorkerType.ORDER_ONLINE, mySim());
			workersOrderOnline.add(worker);
			highestWorkersOrderID++;
		}

		this.averageTimeWaitingServiceStat = new Stat();
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		new ManagerObsluznychMiest(Id.managerObsluznychMiest, mySim(), this);
		new ProcesDiktovanieObjednavky(Id.procesDiktovanieObjednavky, mySim(), this);
		new ProcesPripravaObjednavky(Id.procesPripravaObjednavky, mySim(), this);
		new ProcesVyzdvihnutieVelkehoTovaru(Id.procesVyzdvihnutieVelkehoTovaru, mySim(), this);
		addOwnMessage(Mc.jeCasObedu);
		addOwnMessage(Mc.pripravaObjednavky);
		addOwnMessage(Mc.dajPocetMiestVCakarni);
		addOwnMessage(Mc.vyzdvihnutieVelkejObjednavky);
	}
	//meta! tag="end"

	public SimQueue<MessageForm> getCustomersWaitingInShopBeforeOrder() {
		return CustomersWaitingInShopBeforeOrder;
	}

	public SimQueue<Worker> getWorkersOrderNormal() {
		return workersOrderNormal;
	}

	public SimQueue<Worker> getWorkersOrderOnline() {
		return workersOrderOnline;
	}

	public SimQueue<Worker> getWorkersOrderWorkingNormal() {
		return workersOrderWorkingNormal;
	}

	public SimQueue<Worker> getWorkersOrderWorkingOnline() {
		return workersOrderWorkingOnline;
	}

	public Stat getAverageTimeWaitingServiceStat() {
		return averageTimeWaitingServiceStat;
	}
}