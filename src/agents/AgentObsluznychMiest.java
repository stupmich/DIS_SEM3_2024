package agents;

import OSPABA.*;
import simulation.*;
import managers.*;
import continualAssistants.*;
import instantAssistants.*;

//meta! id="6"
public class AgentObsluznychMiest extends Agent
{
	public AgentObsluznychMiest(int id, Simulation mySim, Agent parent)
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
		new ManagerObsluznychMiest(Id.managerObsluznychMiest, mySim(), this);
		new ProcesVyzdvihnutieVelkehoTovaru(Id.procesVyzdvihnutieVelkehoTovaru, mySim(), this);
		new ProcesDiktovanieObjednavky(Id.procesDiktovanieObjednavky, mySim(), this);
		new ProcesPripravaObjednavky(Id.procesPripravaObjednavky, mySim(), this);
		addOwnMessage(Mc.jeCasObedu);
		addOwnMessage(Mc.pripravaObjednavky);
		addOwnMessage(Mc.vyzdvihnutieVelkejObjednavky);
	}
	//meta! tag="end"
}
