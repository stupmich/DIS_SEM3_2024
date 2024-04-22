package agents;

import OSPABA.*;
import simulation.*;
import managers.*;
import continualAssistants.*;
import instantAssistants.*;

//meta! id="7"
public class AgentPokladni extends Agent
{
	public AgentPokladni(int id, Simulation mySim, Agent parent)
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
		new ManagerPokladni(Id.managerPokladni, mySim(), this);
		new ProcesPlatba(Id.procesPlatba, mySim(), this);
		addOwnMessage(Mc.jeCasObedu);
		addOwnMessage(Mc.platenie);
	}
	//meta! tag="end"
}
