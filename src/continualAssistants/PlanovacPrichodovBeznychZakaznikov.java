package continualAssistants;

import Entities.Customer;
import OSPABA.*;
import OSPRNG.ExponentialRNG;
import simulation.*;
import agents.*;

//meta! id="34"
public class PlanovacPrichodovBeznychZakaznikov extends Scheduler
{
	private static ExponentialRNG _exp;

	public PlanovacPrichodovBeznychZakaznikov(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
		_exp = new ExponentialRNG(60/15.0, ((MySimulation)mySim()).getSeedGenerator());
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! sender="AgentOkolia", id="35", type="Start"
	public void processStart(MessageForm message)
	{
		message.setCode(Mc.novyZakaznik);
		hold(_exp.sample(), message);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
			case Mc.novyZakaznik:
				double next = _exp.sample();
				next = next * 60.0;

				if (mySim().currentTime() + next <= 28800.0) {
					MessageForm copy = message.createCopy();
					hold(next, copy);

					Customer newCustomer = new Customer(_mySim, mySim().currentTime(), Customer.CustomerType.REGULAR);
					((MyMessage) message).setCustomer(newCustomer);
					assistantFinished(message);
				}
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