package agents;

import OSPABA.*;
import simulation.*;
import managers.*;
import continualAssistants.*;

//meta! id="5"
public class AgentElektra extends Agent
{
	public AgentElektra(int id, Simulation mySim, Agent parent)
	{
		super(id, mySim, parent);
		init();
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		new ManagerElektra(Id.managerElektra, mySim(), this);
		addOwnMessage(Mc.jeCasObedu);
		addOwnMessage(Mc.inicializuj);
		addOwnMessage(Mc.pripravaObjednavky);
		addOwnMessage(Mc.dajPocetMiestVCakarni);
		addOwnMessage(Mc.obsluhaZakaznika);
		addOwnMessage(Mc.vyzdvihnutieVelkejObjednavky);
		addOwnMessage(Mc.platenie);
		addOwnMessage(Mc.uvolniloSaMiesto);
		addOwnMessage(Mc.vydanieListku);
		addOwnMessage(Mc.jeKoniecCasuObedu);
	}
	//meta! tag="end"
}