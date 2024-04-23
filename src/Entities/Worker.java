package Entities;

import OSPABA.Entity;
import OSPABA.Simulation;

public class Worker extends Entity {
    public enum WorkerType {
        ORDER_REGULAR_AND_CONTRACT,
        ORDER_ONLINE,
        PAYMENT
    }
    private int id;
    private int idCustomer = -1;
    private Customer customer = null;
    private WorkerType type;


    public Worker(WorkerType type, Simulation sim) {
        super(sim);
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(int idCustomer) {
        this.idCustomer = idCustomer;
    }
    
    public WorkerType getType() {
        return type;
    }

    public void setType(WorkerType type) {
        this.type = type;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
