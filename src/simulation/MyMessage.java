package simulation;

import Entities.Customer;
import Entities.Worker;
import OSPABA.*;

public class MyMessage extends MessageForm
{
	private int numberOfFreePlacesWaitingRoom;
	private Customer customer;
	private Worker worker;
	private boolean pickNextFromQueueTicketDispenser;

	public MyMessage(Simulation sim)
	{
		super(sim);
	}

	public MyMessage(MyMessage original)
	{
		super(original);
		// copy() is called in superclass
		this.numberOfFreePlacesWaitingRoom = original.numberOfFreePlacesWaitingRoom;
		this.customer = original.customer;
		this.worker = original.worker;
		this.pickNextFromQueueTicketDispenser = original.pickNextFromQueueTicketDispenser;
	}

	@Override
	public MessageForm createCopy()
	{
		return new MyMessage(this);
	}

	@Override
	protected void copy(MessageForm message)
	{
		super.copy(message);
		MyMessage original = (MyMessage)message;
		// Copy attributes
		this.numberOfFreePlacesWaitingRoom = original.numberOfFreePlacesWaitingRoom;
		this.customer = original.customer;
		this.worker = original.worker;
		this.pickNextFromQueueTicketDispenser = original.pickNextFromQueueTicketDispenser;
	}

	public int getNumberOfFreePlacesWaitingRoom() {
		return numberOfFreePlacesWaitingRoom;
	}

	public void setNumberOfFreePlacesWaitingRoom(int numberOfFreePlacesWaitingRoom) {
		this.numberOfFreePlacesWaitingRoom = numberOfFreePlacesWaitingRoom;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Worker getWorker() {
		return worker;
	}

	public void setWorker(Worker worker) {
		this.worker = worker;
	}

	public boolean isPickNextFromQueueTicketDispenser() {
		return pickNextFromQueueTicketDispenser;
	}

	public void setPickNextFromQueueTicketDispenser(boolean pickNextFromQueueTicketDispenser) {
		this.pickNextFromQueueTicketDispenser = pickNextFromQueueTicketDispenser;
	}
}