package continualAssistants;

import OSPABA.*;
import OSPRNG.ExponentialRNG;
import simulation.*;
import agents.*;
import OSPABA.Process;

import java.util.Random;

//meta! id="43"
public class ProcesInterakciaAutomat extends Process
{
	private static Random durationInteractionGenerator;

	public ProcesInterakciaAutomat(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
		durationInteractionGenerator = new Random(((MySimulation)mySim()).getSeedGenerator().nextInt());
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! sender="AgentAutomatu", id="44", type="Start"
	public void processStart(MessageForm message)
	{
		message.setCode(Mc.koniecInterakcie);
		hold(durationInteractionGenerator.nextDouble(30.0,120.0), message);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
			case Mc.koniecInterakcie:
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
	public AgentAutomatu myAgent()
	{
		return (AgentAutomatu)super.myAgent();
	}

}