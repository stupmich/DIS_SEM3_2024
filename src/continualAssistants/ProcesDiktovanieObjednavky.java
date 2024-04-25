package continualAssistants;

import OSPABA.*;
import simulation.*;
import agents.*;
import OSPABA.Process;

import java.util.Random;

//meta! id="50"
public class ProcesDiktovanieObjednavky extends Process
{
	private static Random durationOrderingGenerator;

	public ProcesDiktovanieObjednavky(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
		durationOrderingGenerator = new Random(((MySimulation)mySim()).getSeedGenerator().nextInt());
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! sender="AgentObsluznychMiest", id="51", type="Start"
	public void processStart(MessageForm message)
	{
		message.setCode(Mc.koniecDiktovania);
		hold(durationOrderingGenerator.nextDouble(60.0,900.0), message);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
			case Mc.koniecDiktovania:
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