package agents;

import OSPABA.*;
import OSPDataStruct.SimQueue;
import OSPStat.Stat;
import OSPStat.WStat;
import simulation.*;
import managers.*;
import continualAssistants.*;

//meta! id="23"
public class AgentAutomatu extends Agent
{
	private SimQueue< MessageForm > _queueCustomersTicketDispenser;
	private SimQueue< MessageForm > _customerInteractingWithTicketDispenser;
	private boolean _isOccupied;

	public AgentAutomatu(int id, Simulation mySim, Agent parent)
	{
		super(id, mySim, parent);
		init();
		addOwnMessage(Mc.koniecInterakcie);
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
		_queueCustomersTicketDispenser = new SimQueue<>(new WStat(mySim()));
		_customerInteractingWithTicketDispenser = new SimQueue<>(new WStat(mySim()));
		_isOccupied = false;
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		new ManagerAutomatu(Id.managerAutomatu, mySim(), this);
		new ProcesInterakciaAutomat(Id.procesInterakciaAutomat, mySim(), this);
		addOwnMessage(Mc.dajPocetMiestVCakarni);
		addOwnMessage(Mc.uvolniloSaMiesto);
		addOwnMessage(Mc.vydanieListku);
	}
	//meta! tag="end"


	public SimQueue<MessageForm> get_queueCustomersTicketDispenser() {
		return _queueCustomersTicketDispenser;
	}

	public SimQueue<MessageForm> get_customerInteractingWithTicketDispenser() {
		return _customerInteractingWithTicketDispenser;
	}

	public boolean is_isOccupied() {
		return _isOccupied;
	}

	public void set_isOccupied(boolean _isOccupied) {
		this._isOccupied = _isOccupied;
	}
}