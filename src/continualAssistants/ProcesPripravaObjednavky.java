package continualAssistants;

import Entities.Customer;
import OSPABA.*;
import OSPRNG.*;
import simulation.*;
import agents.*;
import OSPABA.Process;

import java.util.ArrayList;
import java.util.Random;

//meta! id="46"
public class ProcesPripravaObjednavky extends Process
{
	private static Random orderTypeGenerator;
	private static Random orderSizeGenerator;
	private static EmpiricRNG durationSimpleOrderPreparationGenerator;
	private static Random durationMediumOrderPreparationGenerator;
	private static EmpiricRNG durationDifficultOrderPreparationGenerator;
	private static TriangularRNG durationOrderHandoverGenerator;
	
	public ProcesPripravaObjednavky(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);

		this.orderTypeGenerator = new Random(((MySimulation)mySim()).getSeedGenerator().nextInt());
		this.orderSizeGenerator = new Random(((MySimulation)mySim()).getSeedGenerator().nextInt());
		this.durationSimpleOrderPreparationGenerator = new EmpiricRNG(
				((MySimulation)mySim()).getSeedGenerator(),
				new EmpiricPair(new UniformContinuousRNG(2.0,5.0,((MySimulation)mySim()).getSeedGenerator()), 0.6),
				new EmpiricPair(new UniformContinuousRNG(5.0,9.0,((MySimulation)mySim()).getSeedGenerator()), 0.4));
		this.durationMediumOrderPreparationGenerator = new Random(((MySimulation)mySim()).getSeedGenerator().nextInt());
		this.durationDifficultOrderPreparationGenerator = new EmpiricRNG(
				((MySimulation)mySim()).getSeedGenerator(),
				new EmpiricPair(new UniformContinuousRNG(11.0,12.0,((MySimulation)mySim()).getSeedGenerator()), 0.1),
				new EmpiricPair(new UniformContinuousRNG(12.0,20.0,((MySimulation)mySim()).getSeedGenerator()), 0.6),
				new EmpiricPair(new UniformContinuousRNG(20.0,25.0,((MySimulation)mySim()).getSeedGenerator()), 0.3));
		this.durationOrderHandoverGenerator = new TriangularRNG(60.0, 120.0, 480.0, ((MySimulation)mySim()).getSeedGenerator());
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! sender="AgentObsluznychMiest", id="47", type="Start"
	public void processStart(MessageForm message)
	{
		Customer customer = ((MyMessage)message).getCustomer();
		double holdDuration = 0.0;

		if (customer.getCustomerType() == Customer.CustomerType.REGULAR || customer.getCustomerType() == Customer.CustomerType.CONTRACT) {
			double type = orderTypeGenerator.nextDouble();

			if (type < 0.3) {
				customer.setOrderType(Customer.OrderType.SIMPLE);
				holdDuration = ((double) durationSimpleOrderPreparationGenerator.sample());
			} else if (type < 0.7) {
				customer.setOrderType(Customer.OrderType.MEDIUM);
				holdDuration = durationMediumOrderPreparationGenerator.nextDouble(9.0,11.0);
			} else {
				customer.setOrderType(Customer.OrderType.DIFFICULT);
				holdDuration = ((double) durationDifficultOrderPreparationGenerator.sample());
			}
			holdDuration = holdDuration * 60.0;
		} else {
			holdDuration = durationOrderHandoverGenerator.sample();
		}

		double orderSize = orderTypeGenerator.nextDouble();

		if (orderSize < 0.6) {
			// order is too big -> worker stays blocked till customer comes back
			customer.setBlockingWorker(((MyMessage)message).getWorker());
			customer.setSizeOfOrder(Customer.SizeOfOrder.BIG);
		} else {
			customer.setSizeOfOrder(Customer.SizeOfOrder.REGULAR);
		}

		message.setCode(Mc.koniecPripravyObjednavky);
		hold(holdDuration, message);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
			case Mc.koniecPripravyObjednavky:
				assistantFinished(message);
				break;
		}
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	@Override
	public void processMessage(MessageForm message)
	{
		switch (message.code())
		{
		case Mc.start:
			processStart(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

	@Override
	public AgentObsluznychMiest myAgent()
	{
		return (AgentObsluznychMiest)super.myAgent();
	}

}