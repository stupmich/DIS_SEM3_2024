package managers;

import OSPABA.*;
import simulation.*;
import agents.*;
import continualAssistants.*;

//meta! id="17"
public class ManagerObed extends Manager
{
	public ManagerObed(int id, Simulation mySim, Agent myAgent)
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

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
		}
	}

	//meta! sender="AgentElektra", id="81", type="Notice"
	public void processInicializuj(MessageForm message)
	{
		message.setAddressee(myAgent().findAssistant(Id.planovacZaciatkuObedu));
		startContinualAssistant(message);
	}

	//meta! sender="PlanovacZaciatkuObedu", id="83", type="Finish"
	public void processFinishPlanovacZaciatkuObedu(MessageForm message)
	{
		message.setAddressee(myAgent().findAssistant(Id.planovacKoncaObedu));
		startContinualAssistant(message);

		MyMessage nextMessage = new MyMessage(((MyMessage) message));
		nextMessage.setCode(Mc.jeCasObedu);
		nextMessage.setAddressee(mySim().findAgent(Id.agentElektra));
		notice(nextMessage);
	}

	//meta! sender="PlanovacKoncaObedu", id="88", type="Finish"
	public void processFinishPlanovacKoncaObedu(MessageForm message)
	{
		message.setCode(Mc.jeKoniecCasuObedu);
		message.setAddressee(mySim().findAgent(Id.agentElektra));
		notice(message);
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
		case Mc.finish:
			switch (message.sender().id())
			{
			case Id.planovacKoncaObedu:
				processFinishPlanovacKoncaObedu(message);
			break;

			case Id.planovacZaciatkuObedu:
				processFinishPlanovacZaciatkuObedu(message);
			break;
			}
		break;

		case Mc.inicializuj:
			processInicializuj(message);
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