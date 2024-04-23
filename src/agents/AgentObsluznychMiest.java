package agents;

import OSPABA.*;
import OSPDataStruct.SimQueue;
import OSPStat.WStat;
import simulation.*;
import managers.*;
import continualAssistants.*;

//meta! id="6"
public class AgentObsluznychMiest extends Agent
{
	private SimQueue< MessageForm > _customersWaitingForService;

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
		_customersWaitingForService = new SimQueue<>(new WStat(mySim()));
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		new ManagerObsluznychMiest(Id.managerObsluznychMiest, mySim(), this);
		new ProcesVyzdvihnutieVelkehoTovaru(Id.procesVyzdvihnutieVelkehoTovaru, mySim(), this);
		new ProcesPripravaObjednavky(Id.procesPripravaObjednavky, mySim(), this);
		new ProcesDiktovanieObjednavky(Id.procesDiktovanieObjednavky, mySim(), this);
		addOwnMessage(Mc.jeCasObedu);
		addOwnMessage(Mc.pripravaObjednavky);
		addOwnMessage(Mc.dajPocetMiestVCakarni);
		addOwnMessage(Mc.vyzdvihnutieVelkejObjednavky);
	}
	//meta! tag="end"

	public SimQueue<MessageForm> get_customersWaitingForService() {
		return _customersWaitingForService;
	}
}