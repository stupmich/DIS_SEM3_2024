package managers;

import OSPABA.*;
import simulation.*;
import agents.*;
import continualAssistants.*;
import instantAssistants.*;

//meta! id="7"
public class ManagerPokladni extends Manager
{
	public ManagerPokladni(int id, Simulation mySim, Agent myAgent)
	{
		super(id, mySim, myAgent);
		init();
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication

		if (petriNet() != null)
		{
			petriNet().clear();
		}
	}

	//meta! sender="AgentElektra", id="22", type="Notice"
	public void processJeCasObedu(MessageForm message)
	{
	}

	//meta! sender="AgentElektra", id="29", type="Request"
	public void processPlatenie(MessageForm message)
	{
	}

	//meta! sender="ProcesPlatba", id="54", type="Finish"
	public void processFinish(MessageForm message)
	{
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
		}
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	public void init()
	{
	}

	@Override
	public void processMessage(MessageForm message)
	{
		switch (message.code())
		{
		case Mc.jeCasObedu:
			processJeCasObedu(message);
		break;

		case Mc.platenie:
			processPlatenie(message);
		break;

		case Mc.finish:
			processFinish(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

	@Override
	public AgentPokladni myAgent()
	{
		return (AgentPokladni)super.myAgent();
	}

}
