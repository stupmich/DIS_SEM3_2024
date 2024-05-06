package continualAssistants;

import Entities.Customer;
import OSPABA.*;
import OSPRNG.ExponentialRNG;
import simulation.*;
import agents.*;

import java.util.Random;

//meta! id="40"
public class PlanovacPrichodovZakaznikovValidMod extends Scheduler
{
	private static ExponentialRNG _exp;
	private static Random customerTypeRand;

	public PlanovacPrichodovZakaznikovValidMod(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
		_exp = new ExponentialRNG(60.0/30.0, ((MySimulation)mySim()).getSeedGenerator());
		customerTypeRand = new Random(((MySimulation)mySim()).getSeedGenerator().nextInt());
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! sender="AgentOkolia", id="41", type="Start"
	public void processStart(MessageForm message)
	{
		message.setCode(Mc.novyZakaznik);
		hold(_exp.sample() * 60.0, message);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
			case Mc.novyZakaznik:
				double next = _exp.sample();
				next = next * 60.0;

				if (mySim().currentTime() + next <= Config.closeTicketDispenserTime) {
					MessageForm copy = message.createCopy();
					hold(next, copy);
				}

				this.createCustomer(message);
				assistantFinished(message);

				break;
		}
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	@Override
	public void processMessage(MessageForm message)
	{
		switch (message.code())
		{
		case Mc.start:
			processStart(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

	@Override
	public AgentOkolia myAgent()
	{
		return (AgentOkolia)super.myAgent();
	}

	public void createCustomer(MessageForm message) {
		double type = this.customerTypeRand.nextDouble();
		if (type < 0.5) {
			Customer newCustomer = new Customer(myAgent().getHighestCustomerID(), _mySim, mySim().currentTime(), Customer.CustomerType.REGULAR);
			((MyMessage) message).setCustomer(newCustomer);
		} else if (type < 0.65) {
			Customer newCustomer = new Customer(myAgent().getHighestCustomerID(), _mySim, mySim().currentTime(), Customer.CustomerType.CONTRACT);
			((MyMessage) message).setCustomer(newCustomer);
		} else {
			Customer newCustomer = new Customer(myAgent().getHighestCustomerID(), _mySim, mySim().currentTime(), Customer.CustomerType.ONLINE);
			((MyMessage) message).setCustomer(newCustomer);
		}
		myAgent().incHighestCustomerID();
	}
}