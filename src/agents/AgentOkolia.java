package agents;

import Entities.Customer;
import OSPABA.*;
import OSPStat.Stat;
import simulation.*;
import managers.*;
import continualAssistants.*;

import java.util.LinkedList;

//meta! id="3"
public class AgentOkolia extends Agent
{
	private int countCustomersIn;
	private int countCustomersOut;
	private int countCustomersServed;;
	private int highestCustomerID;
	private Stat timeInSystemStat;
	private Customer lastCustomer;
	private LinkedList<Customer> allCustomers;

	public AgentOkolia(int id, Simulation mySim, Agent parent)
	{
		super(id, mySim, parent);
		init();
		addOwnMessage(Mc.novyZakaznik);
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
		this.countCustomersIn = 0;
		this.countCustomersOut = 0;
		this.countCustomersServed = 0;
		this.highestCustomerID = 0;

		this.lastCustomer = null;
		this.allCustomers = new LinkedList<>();

		timeInSystemStat = new Stat();
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		new ManagerOkolia(Id.managerOkolia, mySim(), this);
		new PlanovacPrichodovBeznychZakaznikov(Id.planovacPrichodovBeznychZakaznikov, mySim(), this);
		new PlanovacPrichodovZmluvnychZakaznikov(Id.planovacPrichodovZmluvnychZakaznikov, mySim(), this);
		new PlanovacPrichodovZakaznikovValidMod(Id.planovacPrichodovZakaznikovValidMod, mySim(), this);
		new PlanovacPrichodovOnlineZakaznikov(Id.planovacPrichodovOnlineZakaznikov, mySim(), this);
		addOwnMessage(Mc.inicializuj);
		addOwnMessage(Mc.odchodZakaznika);
	}
	//meta! tag="end"

	public void incCountCustomersIn() {
		this.countCustomersIn++;
	}

	public void incCountCustomersOut() {
		this.countCustomersOut++;
	}

	public int getCountCustomersIn() {
		return countCustomersIn;
	}

	public int getCountCustomersOut() {
		return countCustomersOut;
	}

	public int getHighestCustomerID() {
		return highestCustomerID;
	}

	public void incHighestCustomerID() {
		this.highestCustomerID++;
	}

	public Stat getTimeInSystemStat() {
		return timeInSystemStat;
	}

	public Customer getLastCustomer() {
		return lastCustomer;
	}

	public void setLastCustomer(Customer lastCustomer) {
		this.lastCustomer = lastCustomer;
	}

	public void incServedCustomers() {
		this.countCustomersServed++;
	}

	public int getCountCustomersServed() {
		return countCustomersServed;
	}

	public LinkedList<Customer> getAllCustomers() {
		return allCustomers;
	}
}