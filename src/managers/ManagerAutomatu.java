package managers;

import OSPABA.*;
import simulation.*;
import agents.*;
import continualAssistants.*;

//meta! id="23"
public class ManagerAutomatu extends Manager {
    public ManagerAutomatu(int id, Simulation mySim, Agent myAgent) {
        super(id, mySim, myAgent);
        init();
    }

    @Override
    public void prepareReplication() {
        super.prepareReplication();
        // Setup component for the next replication

        if (petriNet() != null) {
            petriNet().clear();
        }
    }

	//meta! sender="ProcesInterakciaAutomat", id="44", type="Finish"
	public void processFinishProcesInterakciaAutomat(MessageForm message) {
        myAgent().set_isOccupied(false);
        myAgent().get_customerInteractingWithTicketDispenser().remove(message);
        //myAgent().casCakania().addSample(((MyMessage)message).celkoveCakanie());

        // TODO sus daj pozor
        message.setCode(Mc.vydanieListku);
        response(message);

        if (myAgent().get_queueCustomersTicketDispenser().size() > 0) {
            MyMessage nextMessage = new MyMessage(((MyMessage) message));
            nextMessage.setPickNextFromQueueTicketDispenser(true);
            nextMessage.setCode(Mc.dajPocetMiestVCakarni);
            nextMessage.setAddressee(mySim().findAgent(Id.agentElektra));

            request(nextMessage);
        }
    }

	//meta! sender="AgentElektra", id="26", type="Request"
	public void processVydanieListku(MessageForm message) {
        message.setCode(Mc.dajPocetMiestVCakarni);
        message.setAddressee(mySim().findAgent(Id.agentElektra));

        request(message);
    }

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message) {
        switch (message.code()) {
        }
    }

	//meta! sender="AgentElektra", id="56", type="Response"
	public void processDajPocetMiestVCakarni(MessageForm message) {
        message.setCode(Mc.vydanieListku);

        if (((MyMessage) message).isPickNextFromQueueTicketDispenser() == false) { // New Customer came to ticket dispenser
            if (!myAgent().is_isOccupied() && ((MyMessage) message).getNumberOfFreePlacesWaitingRoom() > 0) {
                startInteractionWithTicketDispenser(message);
            } else {
                //((MyMessage)message).setZaciatokCakania(mySim().currentTime());
                myAgent().get_queueCustomersTicketDispenser().enqueue(message);
            }
        } else { // Customer from queue was picked
            if (!myAgent().is_isOccupied() && ((MyMessage) message).getNumberOfFreePlacesWaitingRoom() > 0) {
                MyMessage nextMessage = (MyMessage) myAgent().get_queueCustomersTicketDispenser().dequeue();
//				nextMessage.setCelkoveCakanie(mySim().currentTime() - nextMessage.zaciatokCakania());
                startInteractionWithTicketDispenser(nextMessage);
            } else {
                System.out.println("uhmm"  + mySim().currentTime()); // nothing should happen, customer is waiting but there are no free places
                // or ticket dispenser is in use (should not be in this case tbh)
            }
        }
    }

	//meta! sender="AgentElektra", id="61", type="Notice"
	public void processUvolniloSaMiesto(MessageForm message) {
        if (!myAgent().is_isOccupied() && ((MyMessage) message).getNumberOfFreePlacesWaitingRoom() > 0
                && myAgent().get_queueCustomersTicketDispenser().size() > 0) {
            MyMessage nextMessage = (MyMessage) myAgent().get_queueCustomersTicketDispenser().dequeue();
//				nextMessage.setCelkoveCakanie(mySim().currentTime() - nextMessage.zaciatokCakania());
            startInteractionWithTicketDispenser(nextMessage);
        } else {
            System.out.println("uhmm" + mySim().currentTime()); // nothing should happen, customer is waiting but there are no free places
            // or ticket dispenser is in use (should not be in this case tbh)
        }
    }

	//meta! sender="PlanovacZatvoreniaAutomatu", id="65", type="Finish"
	public void processFinishPlanovacZatvoreniaAutomatu(MessageForm message)
	{
        int size = myAgent().get_queueCustomersTicketDispenser().size();
        for (int i = 0; i < size; i++) {
            MyMessage nextMessage = (MyMessage) myAgent().get_queueCustomersTicketDispenser().dequeue();
            nextMessage.getCustomer().setNotServed(true);
            response(nextMessage);
        }
	}

	//meta! sender="AgentElektra", id="70", type="Notice"
	public void processInicializuj(MessageForm message)
	{
        message.setAddressee(myAgent().findAssistant(Id.planovacZatvoreniaAutomatu));
        startContinualAssistant(message);
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
		case Mc.uvolniloSaMiesto:
			processUvolniloSaMiesto(message);
		break;

		case Mc.dajPocetMiestVCakarni:
			processDajPocetMiestVCakarni(message);
		break;

		case Mc.vydanieListku:
			processVydanieListku(message);
		break;

		case Mc.inicializuj:
			processInicializuj(message);
		break;

		case Mc.finish:
			switch (message.sender().id())
			{
			case Id.planovacZatvoreniaAutomatu:
				processFinishPlanovacZatvoreniaAutomatu(message);
			break;

			case Id.procesInterakciaAutomat:
				processFinishProcesInterakciaAutomat(message);
			break;
			}
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

    @Override
    public AgentAutomatu myAgent() {
        return (AgentAutomatu) super.myAgent();
    }

    private void startInteractionWithTicketDispenser(MessageForm message) {
        myAgent().set_isOccupied(true);
        myAgent().get_customerInteractingWithTicketDispenser().add(message);
        message.setAddressee(myAgent().findAssistant(Id.procesInterakciaAutomat));
        startContinualAssistant(message);
    }
}