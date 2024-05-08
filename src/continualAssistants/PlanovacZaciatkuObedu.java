package continualAssistants;

import OSPABA.*;
import simulation.*;
import agents.*;

//meta! id="82"
public class PlanovacZaciatkuObedu extends Scheduler
{
	public PlanovacZaciatkuObedu(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! sender="AgentObed", id="83", type="Start"
	public void processStart(MessageForm message)
	{
		message.setCode(Mc.zacatieObednajsejPrestavky);
		hold(Config.startLunchTime, message);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
			case Mc.zacatieObednajsejPrestavky:
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