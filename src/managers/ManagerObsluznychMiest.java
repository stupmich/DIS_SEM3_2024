package managers;

import OSPABA.*;
import OSPDataStruct.SimQueue;
import simulation.*;
import agents.*;
import continualAssistants.*;

//meta! id="6"
public class ManagerObsluznychMiest extends Manager
{
	public ManagerObsluznychMiest(int id, Simulation mySim, Agent myAgent)
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

	//meta! sender="AgentElektra", id="21", type="Notice"
	public void processJeCasObedu(MessageForm message)
	{
	}

	//meta! sender="AgentElektra", id="27", type="Request"
	public void processPripravaObjednavky(MessageForm message)
	{
	}

	//meta! sender="AgentElektra", id="30", type="Request"
	public void processVyzdvihnutieVelkejObjednavky(MessageForm message)
	{
	}

	//meta! sender="ProcesPripravaObjednavky", id="47", type="Finish"
	public void processFinishProcesPripravaObjednavky(MessageForm message)
	{
	}

	//meta! sender="ProcesDiktovanieObjednavky", id="51", type="Finish"
	public void processFinishProcesDiktovanieObjednavky(MessageForm message)
	{
	}

	//meta! sender="ProcesVyzdvihnutieVelkehoTovaru", id="49", type="Finish"
	public void processFinishProcesVyzdvihnutieVelkehoTovaru(MessageForm message)
	{
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
		}
	}

	//meta! sender="AgentElektra", id="57", type="Request"
	public void processDajPocetMiestVCakarni(MessageForm message)
	{
		message.setCode(Mc.dajPocetMiestVCakarni);
		message.setAddressee(mySim().findAgent(Id.agentElektra));
		((MyMessage) message).setNumberOfFreePlacesWaitingRoom(9 - myAgent().get_customersWaitingForService().size());

		response(message);
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
		case Mc.dajPocetMiestVCakarni:
			processDajPocetMiestVCakarni(message);
		break;

		case Mc.pripravaObjednavky:
			processPripravaObjednavky(message);
		break;

		case Mc.vyzdvihnutieVelkejObjednavky:
			processVyzdvihnutieVelkejObjednavky(message);
		break;

		case Mc.finish:
			switch (message.sender().id())
			{
			case Id.procesVyzdvihnutieVelkehoTovaru:
				processFinishProcesVyzdvihnutieVelkehoTovaru(message);
			break;

			case Id.procesDiktovanieObjednavky:
				processFinishProcesDiktovanieObjednavky(message);
			break;

			case Id.procesPripravaObjednavky:
				processFinishProcesPripravaObjednavky(message);
			break;
			}
		break;

		case Mc.jeCasObedu:
			processJeCasObedu(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

	@Override
	public AgentObsluznychMiest myAgent()
	{
		return (AgentObsluznychMiest)super.myAgent();
	}

}