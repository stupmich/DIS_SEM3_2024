import GUI.ElektrokomponentyGUI;
import simulation.MySimulation;

import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        ElektrokomponentyGUI gui = new ElektrokomponentyGUI();
//
//        int minI = 5;
//        int maxI = 10;
//        int minJ = 16;
//        int maxJ = 25;
//
//        // Initialize a 2D array to hold the simulation results
//        double[][] resultsTime = new double[maxI - minI + 1][maxJ - minJ + 1];
//        double[][] resultsTicket = new double[maxI - minI + 1][maxJ - minJ + 1];
//
//        // Nested loops to iterate through i (5 to 10) and j (16 to 25)
//        for (int i = minI; i <= maxI; i++) {
//            for (int j = minJ; j <= maxJ; j++) {
//                // Create and start the simulation instance
//                MySimulation simulation = new MySimulation(j, i, false, true);
//                simulation.simulate(10000);
//
//                // Extract the mean value after simulation
//                double meanTimeInSystem = ((MySimulation) simulation).getTimeInSystemStat().mean() / 60.0;
//                double meanTimeTicket = ((MySimulation) simulation).getAverageTimeTicketStat().mean() / 60.0;
//
//                // Store the result in the array
//                resultsTime[i - minI][j - minJ] = meanTimeInSystem;
//                resultsTicket[i - minI][j - minJ] = meanTimeTicket;
//                System.out.println(i);
//                System.out.println(j);
//            }
//        }
//
//        // Write results to a CSV file
//        try (FileWriter csvWriter = new FileWriter("simulation_resultsTime11.csv")) {
//            // Write the header row with j values
//            csvWriter.append("i/j");
//            for (int j = minJ; j <= maxJ; j++) {
//                csvWriter.append(";").append(Integer.toString(j));
//            }
//            csvWriter.append("\n");
//
//            // Write each row of results
//            for (int i = 0; i < resultsTime.length; i++) {
//                csvWriter.append(Integer.toString(minI + i));
//                for (int j = 0; j < resultsTime[i].length; j++) {
//                    csvWriter.append(";").append(Double.toString(resultsTime[i][j]));
//                }
//                csvWriter.append("\n");
//            }
//
//            csvWriter.flush();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        // Write results to a CSV file
//        try (FileWriter csvWriter = new FileWriter("simulation_resultsTicket11.csv")) {
//            // Write the header row with j values
//            csvWriter.append("i/j");
//            for (int j = minJ; j <= maxJ; j++) {
//                csvWriter.append(";").append(Integer.toString(j));
//            }
//            csvWriter.append("\n");
//
//            // Write each row of results
//            for (int i = 0; i < resultsTicket.length; i++) {
//                csvWriter.append(Integer.toString(minI + i));
//                for (int j = 0; j < resultsTicket[i].length; j++) {
//                    csvWriter.append(";").append(Double.toString(resultsTicket[i][j]));
//                }
//                csvWriter.append("\n");
//            }
//
//            csvWriter.flush();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}