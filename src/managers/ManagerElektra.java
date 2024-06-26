package managers;

import Entities.Customer;
import OSPABA.*;
import simulation.*;
import agents.*;
import continualAssistants.*;

//meta! id="5"
public class ManagerElektra extends Manager
{
	public ManagerElektra(int id, Simulation mySim, Agent myAgent)
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

	//meta! sender="AgentObed", id="20", type="Notice"
	public void processJeCasObedu(MessageForm message)
	{
		message.setAddressee(mySim().findAgent(Id.agentObsluznychMiest));
		notice(message);

		MyMessage nextMessage = new MyMessage(((MyMessage) message));
		nextMessage.setAddressee(mySim().findAgent(Id.agentPokladni));
		notice(nextMessage);
	}

	//meta! sender="AgentObsluznychMiest", id="27", type="Response"
	public void processPripravaObjednavky(MessageForm message)
	{
		message.setCode(Mc.platenie);
		message.setAddressee(mySim().findAgent(Id.agentPokladni));

		request(message);
	}

	//meta! sender="AgentModelu", id="15", type="Request"
	public void processObsluhaZakaznika(MessageForm message)
	{
		message.setCode(Mc.vydanieListku);
		message.setAddressee(mySim().findAgent(Id.agentAutomatu));

		request(message);
	}

	//meta! sender="AgentObsluznychMiest", id="30", type="Response"
	public void processVyzdvihnutieVelkejObjednavky(MessageForm message)
	{
		message.setCode(Mc.obsluhaZakaznika);
		response(message);
	}

	//meta! sender="AgentPokladni", id="29", type="Response"
	public void processPlatenie(MessageForm message)
	{
		if (((MyMessage)message).getCustomer().getSizeOfOrder() == Customer.SizeOfOrder.REGULAR) {
			message.setCode(Mc.obsluhaZakaznika);
			response(message);
		} else {
			message.setCode(Mc.vyzdvihnutieVelkejObjednavky);
			((MyMessage)message).setWorker(((MyMessage)message).getCustomer().getBlockingWorker());
			message.setAddressee(mySim().findAgent(Id.agentObsluznychMiest));
			request(message);
		}
	}

	//meta! sender="AgentAutomatu", id="26", type="Response"
	public void processVydanieListku(MessageForm message)
	{
		if (((MyMessage)message).getCustomer().isNotServed()) {
			message.setCode(Mc.obsluhaZakaznika);
			response(message);
		} else {
			message.setCode(Mc.pripravaObjednavky);
			message.setAddressee(mySim().findAgent(Id.agentObsluznychMiest));
			request(message);
		}
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
		}
	}

	//meta! sender="AgentObsluznychMiest", id="57", type="Response"
	public void processDajPocetMiestVCakarniAgentObsluznychMiest(MessageForm message)
	{
		message.setAddressee(mySim().findAgent(Id.agentAutomatu));
		response(message);
	}

	//meta! sender="AgentAutomatu", id="56", type="Request"
	public void processDajPocetMiestVCakarniAgentAutomatu(MessageForm message)
	{
		message.setAddressee(mySim().findAgent(Id.agentObsluznychMiest));
		request(message);
	}

	//meta! sender="AgentObsluznychMiest", id="59", type="Notice"
	public void processUvolniloSaMiesto(MessageForm message)
	{
		message.setAddressee(mySim().findAgent(Id.agentAutomatu));
		notice(message);
	}

	//meta! sender="AgentModelu", id="69", type="Notice"
	public void processInicializuj(MessageForm message)
	{
		message.setAddressee(mySim().findAgent(Id.agentAutomatu));
		notice(message);

		if (!((MySimulation)mySim()).isValidationMode()) {
			MyMessage nextMessage = new MyMessage(((MyMessage) message));
			nextMessage.setAddressee(mySim().findAgent(Id.agentObed));
			notice(nextMessage);
		}
	}

	//meta! sender="AgentObed", id="89", type="Notice"
	public void processJeKoniecCasuObedu(MessageForm message)
	{
		message.setAddressee(mySim().findAgent(Id.agentObsluznychMiest));
		notice(message);

		MyMessage nextMessage = new MyMessage(((MyMessage) message));
		nextMessage.setAddressee(mySim().findAgent(Id.agentPokladni));
		notice(nextMessage);
	}

	//meta! sender="AgentObsluznychMiest", id="97", type="Response"
	public void processDajPracovnikaAgentObsluznychMiest(MessageForm message)
	{
		response(message);
	}

	//meta! sender="AgentPokladni", id="96", type="Request"
	public void processDajPracovnikaAgentPokladni(MessageForm message)
	{
		message.setAddressee(mySim().findAgent(Id.agentObsluznychMiest));
		request(message);
	}

	//meta! sender="AgentPokladni", id="98", type="Notice"
	public void processVrateniePracovnika(MessageForm message)
	{
		message.setAddressee(mySim().findAgent(Id.agentObsluznychMiest));
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
		case Mc.jeCasObedu:
			processJeCasObedu(message);
		break;

		case Mc.inicializuj:
			processInicializuj(message);
		break;

		case Mc.pripravaObjednavky:
			processPripravaObjednavky(message);
		break;

		case Mc.vydanieListku:
			processVydanieListku(message);
		break;

		case Mc.uvolniloSaMiesto:
			processUvolniloSaMiesto(message);
		break;

		case Mc.jeKoniecCasuObedu:
			processJeKoniecCasuObedu(message);
		break;

		case Mc.dajPocetMiestVCakarni:
			switch (message.sender().id())
			{
			case Id.agentObsluznychMiest:
				processDajPocetMiestVCakarniAgentObsluznychMiest(message);
			break;

			case Id.agentAutomatu:
				processDajPocetMiestVCakarniAgentAutomatu(message);
			break;
			}
		break;

		case Mc.vyzdvihnutieVelkejObjednavky:
			processVyzdvihnutieVelkejObjednavky(message);
		break;

		case Mc.dajPracovnika:
			switch (message.sender().id())
			{
			case Id.agentObsluznychMiest:
				processDajPracovnikaAgentObsluznychMiest(message);
			break;

			case Id.agentPokladni:
				processDajPracovnikaAgentPokladni(message);
			break;
			}
		break;

		case Mc.vrateniePracovnika:
			processVrateniePracovnika(message);
		break;

		case Mc.obsluhaZakaznika:
			processObsluhaZakaznika(message);
		break;

		case Mc.platenie:
			processPlatenie(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

	@Override
	public AgentElektra myAgent()
	{
		return (AgentElektra)super.myAgent();
	}

}