package continualAssistants;

import OSPABA.*;
import OSPRNG.ExponentialRNG;
import simulation.*;
import agents.*;

//meta! id="38"
public class PlanovacPrichodovOnlineZakaznikov extends Scheduler
{
	private static ExponentialRNG _exp;

	public PlanovacPrichodovOnlineZakaznikov(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
		_exp = new ExponentialRNG(60/10.0, ((MySimulation)mySim()).getSeedGenerator());
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! sender="AgentOkolia", id="39", type="Start"
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
				MessageForm copy = message.createCopy();
				hold(_exp.sample(), copy);

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
