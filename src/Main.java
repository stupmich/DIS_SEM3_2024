import simulation.MySimulation;

public class Main {
    public static void main(String[] args) {
        MySimulation sim = new MySimulation();

        sim.onSimulationWillStart(s ->{
            System.out.println("Simulating...");
        });

        sim.simulate(10, 32000d);

    }
}