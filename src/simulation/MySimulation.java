package simulation;

import Entities.Worker;
import OSPABA.*;
import OSPStat.Stat;
import agents.*;

import java.util.LinkedList;
import java.util.Random;

public class MySimulation extends Simulation
{
	private int numberOfWorkersOrder;
	private int numberOfWorkersPayment;
	private int numberOfNormalWorkers;
	private int numberOfOnlineWorkers;
	private boolean	validationMode = true;
	private Random seedGenerator;
	private Stat timeInSystemStat;
	private Stat averageTimeWaitingServiceStat;
	private Stat averageTimeTicketStat;
	private Stat averageServedCustomerStat;
	private Stat timeLastLeaveSystemStat;
	private Stat numberOfCustomersWaitingTicketStat;
	private Stat averageUsePercentTicketStat;
	private Stat averageUsePercentOrderStat;
	private Stat averageUsePercentPaymentStat;

	public MySimulation(int numberOfWorkersOrder, int numberOfWorkersPayment)
	{
		seedGenerator = new Random();
		this.numberOfWorkersOrder = numberOfWorkersOrder;
		this.numberOfWorkersPayment = numberOfWorkersPayment;
		this.numberOfOnlineWorkers = this.numberOfWorkersOrder / 3;
		this.numberOfNormalWorkers = this.numberOfWorkersOrder - numberOfOnlineWorkers;

		init();
	}

	@Override
	public void prepareSimulation()
	{
		super.prepareSimulation();
		// Create global statistcis

		timeInSystemStat = new Stat();
		timeLastLeaveSystemStat = new Stat();
		averageTimeWaitingServiceStat = new Stat();
		averageTimeTicketStat = new Stat();
		averageServedCustomerStat = new Stat();
		numberOfCustomersWaitingTicketStat = new Stat();
		averageUsePercentTicketStat = new Stat();
		averageUsePercentOrderStat = new Stat();
		averageUsePercentPaymentStat = new Stat();
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Reset entities, queues, local statistics, etc...
		agentModelu().spustiSimulaciu();
		agentObsluznychMiest().prepareReplication();
		agentPokladni().prepareReplication();
		agentOkolia().prepareReplication();
	}

	@Override
	public void replicationFinished()
	{
		// Collect local statistics into global, update UI, etc...
		super.replicationFinished();

		this.timeInSystemStat.addSample(agentOkolia().getTimeInSystemStat().mean());
		double time = agentOkolia().getLastCustomer().getTimeLeaveSystem();
		time += 32400.0;
		this.timeLastLeaveSystemStat.addSample(time);
		this.averageTimeWaitingServiceStat.addSample(agentObsluznychMiest().getAverageTimeWaitingServiceStat().mean());
		this.averageServedCustomerStat.addSample(agentOkolia().getCountCustomersServed());
		this.averageTimeTicketStat.addSample(agentAutomatu().getAverageTimeTicketStat().mean());
		this.numberOfCustomersWaitingTicketStat.addSample(agentAutomatu().get_queueCustomersTicketDispenser().lengthStatistic().mean());
		this.averageUsePercentTicketStat.addSample(agentAutomatu().get_customerInteractingWithTicketDispenser().lengthStatistic().mean());
		double averageUsePercentOrderNormal = agentObsluznychMiest().getWorkersOrderWorkingNormal().lengthStatistic().mean();
		double averageUsePercentOrderOnline = agentObsluznychMiest().getWorkersOrderWorkingOnline().lengthStatistic().mean();
		this.averageUsePercentOrderStat.addSample((averageUsePercentOrderNormal + averageUsePercentOrderOnline) / ((double)this.numberOfWorkersOrder));
		this.averageUsePercentPaymentStat.addSample(agentPokladni().getWorkersPaymentWorking().lengthStatistic().mean() / ((double)this.numberOfWorkersPayment));
	}

	@Override
	public void simulationFinished()
	{
		// Dysplay simulation results
		super.simulationFinished();
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		setAgentModelu(new AgentModelu(Id.agentModelu, this, null));
		setAgentOkolia(new AgentOkolia(Id.agentOkolia, this, agentModelu()));
		setAgentElektra(new AgentElektra(Id.agentElektra, this, agentModelu()));
		setAgentObsluznychMiest(new AgentObsluznychMiest(Id.agentObsluznychMiest, this, agentElektra()));
		setAgentPokladni(new AgentPokladni(Id.agentPokladni, this, agentElektra()));
		setAgentObed(new AgentObed(Id.agentObed, this, agentElektra()));
		setAgentAutomatu(new AgentAutomatu(Id.agentAutomatu, this, agentElektra()));
	}

	private AgentModelu _agentModelu;

public AgentModelu agentModelu()
	{ return _agentModelu; }

	public void setAgentModelu(AgentModelu agentModelu)
	{_agentModelu = agentModelu; }

	private AgentOkolia _agentOkolia;

public AgentOkolia agentOkolia()
	{ return _agentOkolia; }

	public void setAgentOkolia(AgentOkolia agentOkolia)
	{_agentOkolia = agentOkolia; }

	private AgentElektra _agentElektra;

public AgentElektra agentElektra()
	{ return _agentElektra; }

	public void setAgentElektra(AgentElektra agentElektra)
	{_agentElektra = agentElektra; }

	private AgentObsluznychMiest _agentObsluznychMiest;

public AgentObsluznychMiest agentObsluznychMiest()
	{ return _agentObsluznychMiest; }

	public void setAgentObsluznychMiest(AgentObsluznychMiest agentObsluznychMiest)
	{_agentObsluznychMiest = agentObsluznychMiest; }

	private AgentPokladni _agentPokladni;

public AgentPokladni agentPokladni()
	{ return _agentPokladni; }

	public void setAgentPokladni(AgentPokladni agentPokladni)
	{_agentPokladni = agentPokladni; }

	private AgentObed _agentObed;

public AgentObed agentObed()
	{ return _agentObed; }

	public void setAgentObed(AgentObed agentObed)
	{_agentObed = agentObed; }

	private AgentAutomatu _agentAutomatu;

public AgentAutomatu agentAutomatu()
	{ return _agentAutomatu; }

	public void setAgentAutomatu(AgentAutomatu agentAutomatu)
	{_agentAutomatu = agentAutomatu; }
	//meta! tag="end"


	public boolean isValidationMode() {
		return validationMode;
	}

	public Random getSeedGenerator() {
		return seedGenerator;
	}

	public int getNumberOfWorkersOrder() {
		return numberOfWorkersOrder;
	}

	public int getNumberOfWorkersPayment() {
		return numberOfWorkersPayment;
	}

	public int getNumberOfNormalWorkers() {
		return numberOfNormalWorkers;
	}

	public int getNumberOfOnlineWorkers() {
		return numberOfOnlineWorkers;
	}

	public Stat getTimeInSystemStat() {
		return timeInSystemStat;
	}

	public Stat getTimeLastLeaveSystemStat() {
		return timeLastLeaveSystemStat;
	}

	public Stat getAverageTimeWaitingServiceStat() {
		return averageTimeWaitingServiceStat;
	}

	public Stat getAverageTimeTicketStat() {
		return averageTimeTicketStat;
	}

	public Stat getAverageServedCustomerStat() {
		return averageServedCustomerStat;
	}

	public Stat getNumberOfCustomersWaitingTicketStat() {
		return numberOfCustomersWaitingTicketStat;
	}

	public Stat getAverageUsePercentTicketStat() {
		return averageUsePercentTicketStat;
	}

	public Stat getAverageUsePercentOrderStat() {
		return averageUsePercentOrderStat;
	}

	public Stat getAverageUsePercentPaymentStat() {
		return averageUsePercentPaymentStat;
	}
}