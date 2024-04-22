package agents;

import OSPABA.*;
import simulation.*;
import managers.*;
import continualAssistants.*;
import instantAssistants.*;

//meta! id="3"
public class AgentOkolia extends Agent
{
	public AgentOkolia(int id, Simulation mySim, Agent parent)
	{
		super(id, mySim, parent);
		init();
		addOwnMessage(Mc.novyZakaznik);
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
		new ManagerOkolia(Id.managerOkolia, mySim(), this);
		new PlanovacPrichodovBeznychZakaznikov(Id.planovacPrichodovBeznychZakaznikov, mySim(), this);
		new PlanovacPrichodovZakaznikovValidMod(Id.planovacPrichodovZakaznikovValidMod, mySim(), this);
		new PlanovacPrichodovOnlineZakaznikov(Id.planovacPrichodovOnlineZakaznikov, mySim(), this);
		new PlanovacPrichodovZmluvnychZakaznikov(Id.planovacPrichodovZmluvnychZakaznikov, mySim(), this);
		addOwnMessage(Mc.inicializuj);
		addOwnMessage(Mc.odchodZakaznika);
	}
	//meta! tag="end"
}
