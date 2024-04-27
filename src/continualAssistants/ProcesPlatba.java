package continualAssistants;

import OSPABA.*;
import OSPRNG.UniformContinuousRNG;
import OSPRNG.UniformDiscreteRNG;
import simulation.*;
import agents.*;
import OSPABA.Process;

import java.util.Random;

//meta! id="53"
public class ProcesPlatba extends Process
{
	private static Random paymentTypeGenerator;
	private static UniformDiscreteRNG durationPaymentCashGenerator;
	private static UniformDiscreteRNG durationPaymentCardGenerator;
	public ProcesPlatba(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
		paymentTypeGenerator = new Random(((MySimulation)mySim()).getSeedGenerator().nextInt());
		durationPaymentCashGenerator = new UniformDiscreteRNG(180,480,((MySimulation)mySim()).getSeedGenerator());
		durationPaymentCardGenerator = new UniformDiscreteRNG(180,360,((MySimulation)mySim()).getSeedGenerator());
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! sender="AgentPokladni", id="54", type="Start"
	public void processStart(MessageForm message)
	{
		double typePayment = paymentTypeGenerator.nextDouble();
		double duration = 0.0;

		if (typePayment < 0.4) {
			duration = durationPaymentCashGenerator.sample();
		} else {
			duration = durationPaymentCardGenerator.sample();
		}

		message.setCode(Mc.koniecPlatby);
		hold(duration, message);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
			case Mc.koniecPlatby:
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
	public AgentPokladni myAgent()
	{
		return (AgentPokladni)super.myAgent();
	}

}