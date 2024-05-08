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
                myAgent().getWorkersOrderWorkingNormal().add(worker);

                worker.setIdCustomer(customer.getId());
                worker.setCustomer(customer);

                customer.setEndTimeWaitingService(mySim().currentTime());
                double timeWaitingService = customer.getEndTimeWaitingService() - customer.getStartTimeWaitingService();
                myAgent().getAverageTimeWaitingServiceStat().addSample(timeWaitingService);

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
                myAgent().getWorkersOrderWorkingOnline().add(worker);

                worker.setIdCustomer(customer.getId());
                worker.setCustomer(customer);

                customer.setEndTimeWaitingService(mySim().currentTime());
                double timeWaitingService = customer.getEndTimeWaitingService() - customer.getStartTimeWaitingService();
                myAgent().getAverageTimeWaitingServiceStat().addSample(timeWaitingService);

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
        message.setAddressee(myAgent().findAssistant(Id.procesVyzdvihnutieVelkehoTovaru));
        startContinualAssistant(message);
    }

    //meta! sender="ProcesPripravaObjednavky", id="47", type="Finish"
    public void processFinishProcesPripravaObjednavky(MessageForm message) {
        Customer customer = ((MyMessage) message).getCustomer();
        Worker worker = ((MyMessage) message).getWorker();
        boolean newFreePlace = false;

        if (myAgent().getRequestForWorker() != null && worker.getId() == 0) {
            // if there is request for worker and worker which ended service is from first service place -> send him to cash register
            myAgent().getWorkersOrderWorkingNormal().remove(worker);

            MyMessage mess = ((MyMessage) myAgent().getRequestForWorker());
            worker.clearCustomer();
            mess.setWorker(worker);

            myAgent().setRequestForWorker(null);

            response(mess);

            customer.setBlockingWorker(null); // although customer has big order worker will not be blocked and goes to cash register
        } else {
            if (customer.getSizeOfOrder() == Customer.SizeOfOrder.REGULAR) {
                // only when order size is regular worker ended its service, other than that worker is blocked till customer comes back

                // Customer picked up order -> worker is free again and can serve another customer
                newFreePlace = tryToServeNextCustomer(message);

                if (newFreePlace) {
                    MyMessage nextMessage = new MyMessage(mySim());
                    nextMessage.setNumberOfFreePlacesWaitingRoom(9 - myAgent().getCustomersWaitingInShopBeforeOrder().size());
                    nextMessage.setCode(Mc.uvolniloSaMiesto);
                    nextMessage.setAddressee(mySim().findAgent(Id.agentElektra));

                    notice(nextMessage);
                }
            }
        }

        ((MyMessage) message).setWorker(null);
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
        Worker worker = ((MyMessage) message).getWorker();

        if (worker != null) {
            // worker waits for customer to return from payment
            boolean newFreePlace = false;

            // Customer picked up order -> worker is free again and can serve another customer
            newFreePlace = tryToServeNextCustomer(message);

            if (newFreePlace) {
                MyMessage nextMessage = new MyMessage(mySim());
                nextMessage.setNumberOfFreePlacesWaitingRoom(9 - myAgent().getCustomersWaitingInShopBeforeOrder().size());
                nextMessage.setCode(Mc.uvolniloSaMiesto);
                nextMessage.setAddressee(mySim().findAgent(Id.agentElektra));

                notice(nextMessage);
            }

            ((MyMessage) message).setWorker(null);
        } // otherwise worker from service place went to cash register

        message.setCode(Mc.vyzdvihnutieVelkejObjednavky);
        response(message);
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

    //meta! sender="AgentElektra", id="94", type="Notice"
    public void processJeKoniecCasuObedu(MessageForm message) {
        myAgent().setRequestForWorker(null);
    }

    //meta! sender="AgentElektra", id="97", type="Request"
    public void processDajPracovnika(MessageForm message) {
        Worker workerFirstServicePlace = null;

        // find worker from first service place among free workers
        for (Worker worker : myAgent().getWorkersOrderNormal()) {
            if (worker.getId() == 0) {
                workerFirstServicePlace = worker;
                break;
            }
        }

        // find worker from first service place among working workers
        for (Worker worker : myAgent().getWorkersOrderWorkingNormal()) {
            if (worker.getId() == 0 && worker.getCustomer().getSizeOfOrder() == Customer.SizeOfOrder.BIG) {
                // customer has big order -> means worker is waiting till customer ends payment, ignore that go to cash register
                workerFirstServicePlace = worker;
                workerFirstServicePlace.getCustomer().setBlockingWorker(null);
                workerFirstServicePlace.clearCustomer();
                break;
            }
        }

        if (workerFirstServicePlace != null) {
            // worker from first service place can work as cashier
            myAgent().getWorkersOrderNormal().remove(workerFirstServicePlace);
            myAgent().getWorkersOrderWorkingNormal().remove(workerFirstServicePlace);
            ((MyMessage) message).setWorker(workerFirstServicePlace);
            response(message);
        } else {
            // worker still works, have to wait till he ends service
            myAgent().setRequestForWorker(message);
        }
    }

    //meta! sender="AgentElektra", id="101", type="Notice"
    public void processVrateniePracovnika(MessageForm message) {
        Customer dummy = new Customer(-1, mySim(), -1, Customer.CustomerType.REGULAR);
        ((MyMessage) message).setCustomer(dummy);

        // try to immediately serve next customer
        boolean newFreePlace = tryToServeNextCustomer(message);

        if (newFreePlace) {
            MyMessage nextMessage = new MyMessage(mySim());
            nextMessage.setNumberOfFreePlacesWaitingRoom(9 - myAgent().getCustomersWaitingInShopBeforeOrder().size());
            nextMessage.setCode(Mc.uvolniloSaMiesto);
            nextMessage.setAddressee(mySim().findAgent(Id.agentElektra));

            notice(nextMessage);
        }
    }

    //meta! userInfo="Generated code: do not modify", tag="begin"
    public void init() {
    }

    @Override
    public void processMessage(MessageForm message) {
        switch (message.code()) {
            case Mc.finish:
                switch (message.sender().id()) {
                    case Id.procesPripravaObjednavky:
                        processFinishProcesPripravaObjednavky(message);
                        break;

                    case Id.procesVyzdvihnutieVelkehoTovaru:
                        processFinishProcesVyzdvihnutieVelkehoTovaru(message);
                        break;

                    case Id.procesDiktovanieObjednavky:
                        processFinishProcesDiktovanieObjednavky(message);
                        break;
                }
                break;

            case Mc.jeKoniecCasuObedu:
                processJeKoniecCasuObedu(message);
                break;

            case Mc.vrateniePracovnika:
                processVrateniePracovnika(message);
                break;

            case Mc.vyzdvihnutieVelkejObjednavky:
                processVyzdvihnutieVelkejObjednavky(message);
                break;

            case Mc.dajPracovnika:
                processDajPracovnika(message);
                break;

            case Mc.pripravaObjednavky:
                processPripravaObjednavky(message);
                break;

            case Mc.jeCasObedu:
                processJeCasObedu(message);
                break;

            case Mc.dajPocetMiestVCakarni:
                processDajPocetMiestVCakarni(message);
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

    public boolean tryToServeNextCustomer(MessageForm message) {
        Customer customer = ((MyMessage) message).getCustomer();
        Worker worker = ((MyMessage) message).getWorker();
        boolean newFreePlace = false;

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
                Customer nextCustomerNormal = nextCustomerMessageNormal.getCustomer();
                myAgent().getCustomersWaitingInShopBeforeOrder().remove(nextCustomerMessageNormal);
                worker.setIdCustomer(nextCustomerNormal.getId());
                worker.setCustomer(nextCustomerNormal);

                nextCustomerMessageNormal.getCustomer().setEndTimeWaitingService(mySim().currentTime());
                double timeWaitingService = nextCustomerNormal.getEndTimeWaitingService() - nextCustomerNormal.getStartTimeWaitingService();
                myAgent().getAverageTimeWaitingServiceStat().addSample(timeWaitingService);

                nextCustomerMessageNormal.setWorker(worker);
                nextCustomerMessageNormal.setCode(Mc.pripravaObjednavky);
                nextCustomerMessageNormal.setAddressee(myAgent().findAssistant(Id.procesDiktovanieObjednavky));
                startContinualAssistant(nextCustomerMessageNormal);

                if (!myAgent().getWorkersOrderWorkingNormal().contains(worker)) {
                    myAgent().getWorkersOrderWorkingNormal().add(worker);
                }

            } else { // no new customer -> worker is free
                worker.setIdCustomer(-1);
                worker.setCustomer(null);
                myAgent().getWorkersOrderNormal().add(worker);
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
                Customer nextCustomerOnline = nextMessageCustomerOnline.getCustomer();
                myAgent().getCustomersWaitingInShopBeforeOrder().remove(nextMessageCustomerOnline);
                worker.setIdCustomer(nextCustomerOnline.getId());
                worker.setCustomer(nextCustomerOnline);

                nextMessageCustomerOnline.getCustomer().setEndTimeWaitingService(mySim().currentTime());
                double timeWaitingService = nextCustomerOnline.getEndTimeWaitingService() - nextCustomerOnline.getStartTimeWaitingService();
                myAgent().getAverageTimeWaitingServiceStat().addSample(timeWaitingService);

                nextMessageCustomerOnline.setWorker(worker);
                nextMessageCustomerOnline.setCode(Mc.pripravaObjednavky);
                nextMessageCustomerOnline.setAddressee(myAgent().findAssistant(Id.procesPripravaObjednavky));
                startContinualAssistant(nextMessageCustomerOnline);
            } else { // no new customer -> worker is free
                worker.setIdCustomer(-1);
                worker.setCustomer(null);
                myAgent().getWorkersOrderOnline().add(worker);
                myAgent().getWorkersOrderWorkingOnline().remove(worker);
            }
        }

        return newFreePlace;
    }
}