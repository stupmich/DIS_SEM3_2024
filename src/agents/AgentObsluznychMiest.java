package agents;

import Entities.Worker;
import OSPABA.*;
import OSPDataStruct.SimQueue;
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
	private LinkedList<Worker> workersOrderNormal;
	private LinkedList<Worker> workersOrderOnline;
	private LinkedList<Worker> workersOrderWorkingNormal;
	private LinkedList<Worker> workersOrderWorkingOnline;

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

		this.workersOrderNormal = new LinkedList<Worker>();
		this.workersOrderOnline = new LinkedList<Worker>();
		this.workersOrderWorkingNormal = new LinkedList<Worker>();
		this.workersOrderWorkingOnline = new LinkedList<Worker>();

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
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		new ManagerObsluznychMiest(Id.managerObsluznychMiest, mySim(), this);
		new ProcesPripravaObjednavky(Id.procesPripravaObjednavky, mySim(), this);
		new ProcesVyzdvihnutieVelkehoTovaru(Id.procesVyzdvihnutieVelkehoTovaru, mySim(), this);
		new ProcesDiktovanieObjednavky(Id.procesDiktovanieObjednavky, mySim(), this);
		addOwnMessage(Mc.jeCasObedu);
		addOwnMessage(Mc.pripravaObjednavky);
		addOwnMessage(Mc.dajPocetMiestVCakarni);
		addOwnMessage(Mc.vyzdvihnutieVelkejObjednavky);
	}
	//meta! tag="end"

	public SimQueue<MessageForm> getCustomersWaitingInShopBeforeOrder() {
		return CustomersWaitingInShopBeforeOrder;
	}

	public LinkedList<Worker> getWorkersOrderNormal() {
		return workersOrderNormal;
	}

	public LinkedList<Worker> getWorkersOrderOnline() {
		return workersOrderOnline;
	}

	public LinkedList<Worker> getWorkersOrderWorkingNormal() {
		return workersOrderWorkingNormal;
	}

	public LinkedList<Worker> getWorkersOrderWorkingOnline() {
		return workersOrderWorkingOnline;
	}
}