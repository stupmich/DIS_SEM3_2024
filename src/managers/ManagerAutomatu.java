package managers;

import OSPABA.*;
import simulation.*;
import agents.*;
import continualAssistants.*;
import instantAssistants.*;

//meta! id="23"
public class ManagerAutomatu extends Manager
{
	public ManagerAutomatu(int id, Simulation mySim, Agent myAgent)
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

	//meta! sender="ProcesInterakciaAutomat", id="44", type="Finish"
	public void processFinish(MessageForm message)
	{

	}

	//meta! sender="AgentElektra", id="26", type="Request"
	public void processVydanieListku(MessageForm message)
	{
		//TODO SPYTAT SA REQUEST/RESPONSE SPRAVOU AGENTA OBSLUZNYCH MIEST CI JE TAM MIESTO PRE NOVEHO ZAKAZNIKA (<9)
		if (!myAgent().is_isOccupied()) {
			startInteractionWithTicketDispenser(message);
		} else {
			//((MyMessage)message).setZaciatokCakania(mySim().currentTime());
			myAgent().get_queueCustomersTicketDispenser().enqueue(message);
		}
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
		case Mc.vydanieListku:
			processVydanieListku(message);
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
	public AgentAutomatu myAgent()
	{
		return (AgentAutomatu)super.myAgent();
	}

	private void startInteractionWithTicketDispenser(MessageForm message)
	{
		myAgent().set_isOccupied(true);
		message.setAddressee(myAgent().findAssistant(Id.procesInterakciaAutomat));
		startContinualAssistant(message);
	}
}
