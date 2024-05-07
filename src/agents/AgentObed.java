package agents;

import OSPABA.*;
import simulation.*;
import managers.*;
import continualAssistants.*;

//meta! id="17"
public class AgentObed extends Agent
{
	public AgentObed(int id, Simulation mySim, Agent parent)
	{
		super(id, mySim, parent);
		init();
		addOwnMessage(Mc.zacatieObednajsejPrestavky);
		addOwnMessage(Mc.skoncenieObednajsejPrestavky);
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
		new ManagerObed(Id.managerObed, mySim(), this);
		new PlanovacZaciatkuObedu(Id.planovacZaciatkuObedu, mySim(), this);
		new PlanovacKoncaObedu(Id.planovacKoncaObedu, mySim(), this);
		addOwnMessage(Mc.inicializuj);
	}
	//meta! tag="end"
}