package managers;

import OSPABA.*;
import simulation.*;
import agents.*;
import continualAssistants.*;
import instantAssistants.*;

//meta! id="3"
public class ManagerOkolia extends Manager
{
	public ManagerOkolia(int id, Simulation mySim, Agent myAgent)
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

	//meta! sender="AgentModelu", id="11", type="Notice"
	public void processInicializuj(MessageForm message)
	{
		zacniPlanovanieZakaznikov();
	}

	//meta! sender="PlanovacPrichodovZakaznikovValidMod", id="41", type="Finish"
	public void processFinishPlanovacPrichodovZakaznikovValidMod(MessageForm message)
	{
		message.setCode(Mc.prichodZakaznika);
		message.setAddressee(mySim().findAgent(Id.agentModelu));

		notice(message);
	}

	//meta! sender="PlanovacPrichodovOnlineZakaznikov", id="39", type="Finish"
	public void processFinishPlanovacPrichodovOnlineZakaznikov(MessageForm message)
	{
		message.setCode(Mc.prichodZakaznika);
		message.setAddressee(mySim().findAgent(Id.agentModelu));

		notice(message);
	}

	//meta! sender="PlanovacPrichodovBeznychZakaznikov", id="35", type="Finish"
	public void processFinishPlanovacPrichodovBeznychZakaznikov(MessageForm message)
	{
		message.setCode(Mc.prichodZakaznika);
		message.setAddressee(mySim().findAgent(Id.agentModelu));

		notice(message);
	}

	//meta! sender="PlanovacPrichodovZmluvnychZakaznikov", id="37", type="Finish"
	public void processFinishPlanovacPrichodovZmluvnychZakaznikov(MessageForm message)
	{
		message.setCode(Mc.prichodZakaznika);
		message.setAddressee(mySim().findAgent(Id.agentModelu));

		notice(message);
	}

	//meta! sender="AgentModelu", id="14", type="Notice"
	public void processOdchodZakaznika(MessageForm message)
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
		case Mc.inicializuj:
			processInicializuj(message);
		break;

		case Mc.finish:
			switch (message.sender().id())
			{
			case Id.planovacPrichodovZakaznikovValidMod:
				processFinishPlanovacPrichodovZakaznikovValidMod(message);
			break;

			case Id.planovacPrichodovOnlineZakaznikov:
				processFinishPlanovacPrichodovOnlineZakaznikov(message);
			break;

			case Id.planovacPrichodovBeznychZakaznikov:
				processFinishPlanovacPrichodovBeznychZakaznikov(message);
			break;

			case Id.planovacPrichodovZmluvnychZakaznikov:
				processFinishPlanovacPrichodovZmluvnychZakaznikov(message);
			break;
			}
		break;

		case Mc.odchodZakaznika:
			processOdchodZakaznika(message);
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

	public void zacniPlanovanieZakaznikov()
	{
		if(((MySimulation)mySim()).isValidationMode()) {
			MyMessage message = new MyMessage(mySim());
			message.setAddressee(myAgent().findAssistant(Id.planovacPrichodovZakaznikovValidMod));
			startContinualAssistant(message);
		} else {
			MyMessage message1 = new MyMessage(mySim());
			message1.setAddressee(myAgent().findAssistant(Id.planovacPrichodovBeznychZakaznikov));
			startContinualAssistant(message1);

			MyMessage message2 = new MyMessage(mySim());
			message2.setAddressee(myAgent().findAssistant(Id.planovacPrichodovZmluvnychZakaznikov));
			startContinualAssistant(message2);

			MyMessage message3 = new MyMessage(mySim());
			message3.setAddressee(myAgent().findAssistant(Id.planovacPrichodovOnlineZakaznikov));
			startContinualAssistant(message3);
		}
	}
}
