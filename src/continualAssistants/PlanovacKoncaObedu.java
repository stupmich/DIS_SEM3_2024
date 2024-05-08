package continualAssistants;

import OSPABA.*;
import simulation.*;
import agents.*;

//meta! id="87"
public class PlanovacKoncaObedu extends Scheduler
{
	public PlanovacKoncaObedu(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! sender="AgentObed", id="88", type="Start"
	public void processStart(MessageForm message)
	{
		message.setCode(Mc.skoncenieObednajsejPrestavky);
		hold(Config.durationLunchTime, message);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
			case Mc.skoncenieObednajsejPrestavky:
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
	public AgentObed myAgent()
	{
		return (AgentObed)super.myAgent();
	}

}