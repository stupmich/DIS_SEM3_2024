package managers;

import Entities.Customer;
import Entities.Worker;
import OSPABA.*;
import OSPDataStruct.SimQueue;
import simulation.*;
import agents.*;
import continualAssistants.*;

//meta! id="6"
public class ManagerObsluznychMiest extends Manager {
    public ManagerObsluznychMiest(int id, Simulation mySim, Agent myAgent) {
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

	//meta! sender="AgentElektra", id="21", type="Notice"
	public void processJeCasObedu(MessageForm message) {
    }

	//meta! sender="AgentElektra", id="27", type="Request"
	public void processPripravaObjednavky(MessageForm message) {
        Customer customer = ((MyMessage) message).getCustomer();
        customer.setStartTimeWaitingService(mySim().currentTime());

        if (customer.getCustomerType() == Customer.CustomerType.REGULAR || customer.getCustomerType() == Customer.CustomerType.CONTRACT) {
            if (myAgent().getWorkersOrderNormal().size() != 0) {
                // there are free workers for regular customers and with contract
                Worker worker = myAgent().getWorkersOrderNormal().removeFirst();
                worker.setIdCustomer(customer.getId());
                worker.setCustomer(customer);

//                TODO STATS
                //myAgent().getAverageUsePercentOrderNormalStat().updateStatistics(core, myAgent().getWorkersOrderWorkingNormal());
                myAgent().getWorkersOrderWorkingNormal().add(worker);

                ((MyMessage) message).setWorker(worker);
                message.setAddressee(myAgent().findAssistant(Id.procesDiktovanieObjednavky));
                startContinualAssistant(message);
            } else {
                myAgent().getCustomersWaitingInShopBeforeOrder().add(message);
            }
        } else {
            if (myAgent().getWorkersOrderOnline().size() != 0) {
                // there are free workers for online customers
                Worker worker = myAgent().getWorkersOrderOnline().removeFirst();
                worker.setIdCustomer(customer.getId());
                worker.setCustomer(customer);

//                TODO STATS
//                myAgent().getAverageUsePercentOrderOnlineStat().updateStatistics(core, myAgent().getWorkersOrderWorkingOnline());
                myAgent().getWorkersOrderWorkingOnline().add(worker);

                ((MyMessage) message).setWorker(worker);
                message.setAddressee(myAgent().findAssistant(Id.procesPripravaObjednavky));
                startContinualAssistant(message);
            } else {
                myAgent().getCustomersWaitingInShopBeforeOrder().add(message);
            }
        }
    }

	//meta! sender="AgentElektra", id="30", type="Request"
	public void processVyzdvihnutieVelkejObjednavky(MessageForm message) {
    }

	//meta! sender="ProcesPripravaObjednavky", id="47", type="Finish"
	public void processFinishProcesPripravaObjednavky(MessageForm message) {
        Customer customer = ((MyMessage)message).getCustomer();
        Worker worker = ((MyMessage)message).getWorker();
        boolean newFreePlace = false;
        
        if (customer.getSizeOfOrder() == Customer.SizeOfOrder.REGULAR)
        {
            // Customer picked up order -> worker is free again and can serve another customer
            if (customer.getCustomerType() == Customer.CustomerType.REGULAR || customer.getCustomerType() == Customer.CustomerType.CONTRACT) {
                MyMessage nextCustomerMessageNormal = null;

                for (MessageForm m : myAgent().getCustomersWaitingInShopBeforeOrder()) {
                    MyMessage messInQueue = ((MyMessage) m);
                    Customer customerInQueue = messInQueue.getCustomer();

                    if (customerInQueue.getCustomerType() == Customer.CustomerType.CONTRACT) {
                        nextCustomerMessageNormal = messInQueue;
                        break;
                    }

                    if (customerInQueue.getCustomerType() == Customer.CustomerType.REGULAR && nextCustomerMessageNormal == null) {
                        nextCustomerMessageNormal = messInQueue;
                    }
                }

                if (nextCustomerMessageNormal != null) {
                    newFreePlace = true;
                    myAgent().getCustomersWaitingInShopBeforeOrder().remove(nextCustomerMessageNormal);
                    worker.setIdCustomer(nextCustomerMessageNormal.getCustomer().getId());
                    worker.setCustomer(nextCustomerMessageNormal.getCustomer());

//                    MyMessage nextMessage = new MyMessage(((MyMessage)message));
                    nextCustomerMessageNormal.setWorker(worker);
                    nextCustomerMessageNormal.setCode(Mc.pripravaObjednavky);
                    nextCustomerMessageNormal.setAddressee(myAgent().findAssistant(Id.procesDiktovanieObjednavky));
                    startContinualAssistant(nextCustomerMessageNormal);
                    
                } else { // no new customer -> worker is free
                    worker.setIdCustomer(-1);
                    worker.setCustomer(null);
                    myAgent().getWorkersOrderNormal().add(worker);

//                    myAgent().getAverageUsePercentOrderNormalStat().updateStatistics(core, myAgent().getWorkersOrderWorkingNormal());
                    myAgent().getWorkersOrderWorkingNormal().remove(worker);
                }

            } else {
                MyMessage nextMessageCustomerOnline = null;

                for (MessageForm m : myAgent().getCustomersWaitingInShopBeforeOrder()) {
                    MyMessage messInQueue = ((MyMessage) m);
                    Customer customerInQueue = messInQueue.getCustomer();

                    if (customerInQueue.getCustomerType() == Customer.CustomerType.ONLINE) {
                        nextMessageCustomerOnline = messInQueue;
                        break;
                    }
                }

                if (nextMessageCustomerOnline != null) {
                    newFreePlace = true;
                    myAgent().getCustomersWaitingInShopBeforeOrder().remove(nextMessageCustomerOnline);
                    worker.setIdCustomer(nextMessageCustomerOnline.getCustomer().getId());
                    worker.setCustomer(nextMessageCustomerOnline.getCustomer());

//                    MyMessage nextMessage = new MyMessage(((MyMessage)message));
                    nextMessageCustomerOnline.setWorker(worker);
                    nextMessageCustomerOnline.setCode(Mc.pripravaObjednavky);
                    nextMessageCustomerOnline.setAddressee(myAgent().findAssistant(Id.procesPripravaObjednavky));
                    startContinualAssistant(nextMessageCustomerOnline);
                } else { // no new customer -> worker is free
                    worker.setIdCustomer(-1);
                    worker.setCustomer(null);
                    myAgent().getWorkersOrderOnline().add(worker);

//                    myAgent().getAverageUsePercentOrderOnlineStat().updateStatistics(core, myAgent().getWorkersOrderWorkingOnline());
                    myAgent().getWorkersOrderWorkingOnline().remove(worker);
                }
            }

            if (newFreePlace) {
                //            TODO uvolnilo sa miesto zacni novu interakciu s automatom, ale ako uhmmmm
                //            notice uvolnilo sa miesto -> agentautomatu

            MyMessage nextMessage = new MyMessage(mySim());
            nextMessage.setNumberOfFreePlacesWaitingRoom(9 - myAgent().getCustomersWaitingInShopBeforeOrder().size());
            nextMessage.setCode(Mc.uvolniloSaMiesto);
            nextMessage.setAddressee(mySim().findAgent(Id.agentElektra));

            notice(nextMessage);
            }
        }

        message.setCode(Mc.pripravaObjednavky);
        response(message);
    }

	//meta! sender="ProcesDiktovanieObjednavky", id="51", type="Finish"
	public void processFinishProcesDiktovanieObjednavky(MessageForm message) {
        message.setAddressee(myAgent().findAssistant(Id.procesPripravaObjednavky));
        startContinualAssistant(message);
    }

	//meta! sender="ProcesVyzdvihnutieVelkehoTovaru", id="49", type="Finish"
	public void processFinishProcesVyzdvihnutieVelkehoTovaru(MessageForm message) {
    }

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message) {
        switch (message.code()) {
        }
    }

	//meta! sender="AgentElektra", id="57", type="Request"
	public void processDajPocetMiestVCakarni(MessageForm message) {
        message.setCode(Mc.dajPocetMiestVCakarni);
        message.setAddressee(mySim().findAgent(Id.agentElektra));
        ((MyMessage) message).setNumberOfFreePlacesWaitingRoom(9 - myAgent().getCustomersWaitingInShopBeforeOrder().size());

        response(message);
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
		case Mc.vyzdvihnutieVelkejObjednavky:
			processVyzdvihnutieVelkejObjednavky(message);
		break;

		case Mc.finish:
			switch (message.sender().id())
			{
			case Id.procesPripravaObjednavky:
				processFinishProcesPripravaObjednavky(message);
			break;

			case Id.procesDiktovanieObjednavky:
				processFinishProcesDiktovanieObjednavky(message);
			break;

			case Id.procesVyzdvihnutieVelkehoTovaru:
				processFinishProcesVyzdvihnutieVelkehoTovaru(message);
			break;
			}
		break;

		case Mc.dajPocetMiestVCakarni:
			processDajPocetMiestVCakarni(message);
		break;

		case Mc.jeCasObedu:
			processJeCasObedu(message);
		break;

		case Mc.pripravaObjednavky:
			processPripravaObjednavky(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

    @Override
    public AgentObsluznychMiest myAgent() {
        return (AgentObsluznychMiest) super.myAgent();
    }

}