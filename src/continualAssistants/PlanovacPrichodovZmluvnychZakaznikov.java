package continualAssistants;

import Entities.Customer;
import OSPABA.*;
import OSPRNG.ExponentialRNG;
import simulation.*;
import agents.*;

//meta! id="36"
public class PlanovacPrichodovZmluvnychZakaznikov extends Scheduler
{
	private static ExponentialRNG _exp;

	public PlanovacPrichodovZmluvnychZakaznikov(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);

		if (((MySimulation)mySim()).isIncreasedFlowCustomers()) {
			_exp = new ExponentialRNG(60/(5.0 * 1.3), ((MySimulation)mySim()).getSeedGenerator());
		} else {
			_exp = new ExponentialRNG(60/5.0, ((MySimulation)mySim()).getSeedGenerator());
		}
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! sender="AgentOkolia", id="37", type="Start"
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

				Customer newCustomer = new Customer(myAgent().getHighestCustomerID(), _mySim, mySim().currentTime(), Customer.CustomerType.CONTRACT);
				myAgent().incHighestCustomerID();
				((MyMessage) message).setCustomer(newCustomer);

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

}