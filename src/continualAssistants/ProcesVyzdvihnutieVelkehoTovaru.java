package continualAssistants;

import OSPABA.*;
import OSPRNG.UniformContinuousRNG;
import OSPRNG.UniformDiscreteRNG;
import simulation.*;
import agents.*;
import OSPABA.Process;

//meta! id="48"
public class ProcesVyzdvihnutieVelkehoTovaru extends Process
{
	private static UniformContinuousRNG durationBigOrderPickUpGenerator;
	public ProcesVyzdvihnutieVelkehoTovaru(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
		durationBigOrderPickUpGenerator = new UniformContinuousRNG(30.0,70.0,((MySimulation)mySim()).getSeedGenerator());
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! sender="AgentObsluznychMiest", id="49", type="Start"
	public void processStart(MessageForm message)
	{
		message.setCode(Mc.koniecVyzdvihnutiaVelkejObjednavky);
		hold(durationBigOrderPickUpGenerator.sample(), message);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
			case Mc.koniecVyzdvihnutiaVelkejObjednavky:
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