package continualAssistants;

import OSPABA.*;
import simulation.*;
import agents.*;

//meta! id="64"
public class PlanovacZatvoreniaAutomatu extends Scheduler
{
	public PlanovacZatvoreniaAutomatu(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! sender="AgentAutomatu", id="65", type="Start"
	public void processStart(MessageForm message)
	{
		message.setCode(Mc.zatvorenieAutomatu);
		hold(28800, message);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
			case Mc.zatvorenieAutomatu:
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
	public AgentAutomatu myAgent()
	{
		return (AgentAutomatu)super.myAgent();
	}

}