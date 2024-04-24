package GUI;

import Entities.Worker;
import OSPABA.ISimDelegate;
import OSPABA.MessageForm;
import OSPABA.SimState;
import OSPABA.Simulation;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import simulation.Config;
import simulation.MyMessage;
import simulation.MySimulation;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.TimeUnit;

public class ElektrokomponentyGUI extends JFrame implements ActionListener, ISimDelegate, ChangeListener {
    private JTabbedPane tabbedPane1;
    private JPanel panel1;
    private JTextField textFieldReplications;
    private JTextField textFieldCashWatchTime;
    private JTextField textFieldOrderPlacesWatchTime;
    private JSlider sliderSpeed;
    private JLabel timeUser;
    private JLabel timeProgrammer;
    private JLabel executedReplications;
    private JLabel numberWorkersOrderNormal;
    private JLabel numberWorkersPayment;
    private JLabel numberCustomersTicket;
    private JLabel numberCarsGarage;
    private JLabel numberCustomersPayment;
    private JLabel averageTimeSystem;
    private JButton startWatchTimeButton;
    private JButton pauseWatchTimeButton;
    private JButton endWatchTimeButton;
    private JPanel panelButtons;
    private JPanel panelTablesWorkersOrder;
    private JTable tableFreeW1;
    private JTable tableWorkingW1;
    private JPanel panelTablesWorkersPayment;
    private JTable tableFreeW2;
    private JTable tableWorkingW2;
    private JPanel panelTablesQueues;
    private JTable tableTicketQueue;
    private JTable tableOrderQueue;
    private JTable tablePaymentQueue;
    private JButton startTurboButton;
    private JButton pauseTurboButton;
    private JButton endTurboButton;
    private JTextField textFieldCashTurbo;
    private JTextField textFieldOrderPlacesTurbo;
    private JTextField textFieldReplicationsTurbo;
    private JLabel executedReplicationsTurbo;
    private JLabel numberCustomersQueueService;
    private JLabel numberWorkersOrderOnline;
    private JLabel numberCustomersIn;
    private JLabel numberCustomersOut;
    private JLabel numberCustomersCurrent;
    private JLabel averageTimeSystemTurbo;
    private JLabel averageNumberServedCustomersTurbo;
    private JLabel averageTimeQueueTicketTurbo;
    private JLabel averageNumberQueueTicketTurbo;
    private JLabel averageTimeLeaveSystemTurbo;
    private JLabel averageUsePercTicketTurbo;
    private JTable tableTicketDispenser;
    private JTextField textFieldReplicationsGraph;
    private JTextField textFieldWorkersOrderGraph;
    private JButton startButtonGraph;
    private JButton pauseButtonGraph;
    private JButton endButtonGraph;
    private JPanel JPanelGraph1;
    private JLabel averageTimeSystemWatchTime;
    private JLabel averageTimeQueueTicketWatchTime;
    private JLabel averageNumberQueueTicketWatchTime;
    private JLabel averageUsePercTicketWatchTime;
    private JLabel averageUsePercOrderTurbo;
    private JLabel averageUsePercPaymentTurbo;
    private JLabel averageUsePercOrderWatchTime;
    private JLabel averageUsePercPaymentWatchTime;
    private JLabel averageTimeLeaveSystemWatchTime;
    private JLabel averageNumberServedCustomersWatchTime;
    private JLabel confIntervalTimeInSystemWatchTime;
    private JLabel confIntervalTimeInSystemTurbo;
    private JSlider simSpeedIntervalSlider;
    private MySimulation simulation;
    private boolean turboMode;
    private DefaultTableModel modelWorkersOrder;
    private DefaultTableModel modelWorkersPayment;
    private DefaultTableModel modelWorkersOrderW;
    private DefaultTableModel modelWorkersPaymentW;
    private DefaultTableModel modelCustomersWaitingTicket;
    private DefaultTableModel modelCustomersWaitingOrder;
    private DefaultTableModel modelCustomersPayment;
    private DefaultTableModel modelCustomerTicketDispenser;
    private Thread threadSimulationOuter1;
    private Thread threadSimulationInner1;
    private XYSeries series1 = new XYSeries("Priemerná dĺžka radu - automat");
    private XYPlot plot1;
    private XYSeriesCollection dataset1 = new XYSeriesCollection(series1);
    private JFreeChart chart1;
    private ChartPanel chartPanel1;


    public ElektrokomponentyGUI() {
        this.setContentPane(panel1);
        this.setTitle("Simulácia elektra");
        this.setSize(1920, 1080);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panelTablesWorkersOrder.setPreferredSize(new Dimension(500, 200));
        panelTablesWorkersPayment.setPreferredSize(new Dimension(500, 200));
        panelTablesQueues.setPreferredSize(new Dimension(500, 400));

        modelWorkersOrder = new DefaultTableModel();
        modelWorkersOrder.addColumn("Worker ID");
        modelWorkersOrder.addColumn("Worker type");
        modelWorkersOrder.addColumn("Customer ID");
        tableFreeW1.setModel(modelWorkersOrder);

        modelWorkersOrderW = new DefaultTableModel();
        modelWorkersOrderW.addColumn("Worker ID");
        modelWorkersOrderW.addColumn("Worker type");
        modelWorkersOrderW.addColumn("Customer ID");
        modelWorkersOrderW.addColumn("Order size");
        tableWorkingW1.setModel(modelWorkersOrderW);

        modelWorkersPayment = new DefaultTableModel();
        modelWorkersPayment.addColumn("Worker ID");
        modelWorkersPayment.addColumn("Customer ID");
        tableFreeW2.setModel(modelWorkersPayment);

        modelWorkersPaymentW = new DefaultTableModel();
        modelWorkersPaymentW.addColumn("Worker ID");
        modelWorkersPaymentW.addColumn("Customer ID");
        tableWorkingW2.setModel(modelWorkersPaymentW);

        modelCustomersWaitingTicket = new DefaultTableModel();
        modelCustomersWaitingTicket.addColumn("Customer ID");
        tableTicketQueue.setModel(modelCustomersWaitingTicket);

        modelCustomersWaitingOrder = new DefaultTableModel();
        modelCustomersWaitingOrder.addColumn("Customer ID");
        modelCustomersWaitingOrder.addColumn("Customer type");
        modelCustomersWaitingOrder.addColumn("Arrival time");
        tableOrderQueue.setModel(modelCustomersWaitingOrder);

        modelCustomersPayment = new DefaultTableModel();
        modelCustomersPayment.addColumn("Customer ID");
        modelCustomersPayment.addColumn("Number of queue");
        tablePaymentQueue.setModel(modelCustomersPayment);

        modelCustomerTicketDispenser = new DefaultTableModel();
        modelCustomerTicketDispenser.addColumn("Customer ID");
        tableTicketDispenser.setModel(modelCustomerTicketDispenser);

        this.startTurboButton.addActionListener(this);
        this.startWatchTimeButton.addActionListener(this);
        this.startButtonGraph.addActionListener(this);
        this.pauseWatchTimeButton.addActionListener(this);
        this.pauseTurboButton.addActionListener(this);
        this.pauseButtonGraph.addActionListener(this);
        this.endWatchTimeButton.addActionListener(this);
        this.endTurboButton.addActionListener(this);
        this.endButtonGraph.addActionListener(this);
        this.sliderSpeed.addChangeListener(this);
        this.simSpeedIntervalSlider.addChangeListener(this);

        pauseWatchTimeButton.setEnabled(false);
        endWatchTimeButton.setEnabled(false);
        pauseTurboButton.setEnabled(false);
        endTurboButton.setEnabled(false);

        chart1 = ChartFactory.createXYLineChart("Závislosť čakajúcich v rade na automat/počet pokladní",
                "Počet pokladní", "Priemerný počet čakajúcich", dataset1,
                PlotOrientation.VERTICAL, false, true, false);
        plot1 = chart1.getXYPlot();

        ValueAxis axis1 = plot1.getDomainAxis();
        axis1.setRange(2, 6);

        NumberAxis rangeAxis1 = (NumberAxis) plot1.getRangeAxis();
        rangeAxis1.setAutoRange(true);
        rangeAxis1.setAutoRangeIncludesZero(false);

        chartPanel1 = new ChartPanel(chart1);
        this.JPanelGraph1.setLayout(new BorderLayout());
        this.JPanelGraph1.add(chartPanel1, BorderLayout.CENTER);
        this.JPanelGraph1.validate();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startTurboButton) {
            this.turboMode = true;
            this.startTurboButton.setEnabled(false);
            this.startWatchTimeButton.setEnabled(false);
            this.pauseTurboButton.setEnabled(true);
            this.pauseWatchTimeButton.setEnabled(false);
            this.endTurboButton.setEnabled(true);
            this.endWatchTimeButton.setEnabled(false);
            this.startButtonGraph.setEnabled(false);
            this.pauseButtonGraph.setEnabled(false);
            this.endButtonGraph.setEnabled(false);

            this.simulation = new MySimulation(Integer.parseInt(textFieldOrderPlacesTurbo.getText()), Integer.parseInt(textFieldCashTurbo.getText()));
            this.simulation.registerDelegate(this);
            this.simulation.onReplicationWillStart((sim)
                    ->{
                this.changeSpeed(this.simulation);
            });

            this.simulation.simulateAsync(Integer.parseInt(textFieldReplications.getText()), Config.simEndTime);

        } else if (e.getSource() == pauseTurboButton) {
            if (simulation.isPaused()) {
                simulation.resumeSimulation();
            } else {
                simulation.pauseSimulation();
            }
        } else if (e.getSource() == endTurboButton) {
            startTurboButton.setEnabled(true);
            startWatchTimeButton.setEnabled(true);
            pauseTurboButton.setEnabled(false);
            endTurboButton.setEnabled(false);
            this.startButtonGraph.setEnabled(true);
            this.pauseButtonGraph.setEnabled(false);
            this.endButtonGraph.setEnabled(false);

            simulation.stopSimulation();
        } else if (e.getSource() == startWatchTimeButton) {
            this.turboMode = false;
            startTurboButton.setEnabled(false);
            startWatchTimeButton.setEnabled(false);
            pauseWatchTimeButton.setEnabled(true);
            endWatchTimeButton.setEnabled(true);
            pauseTurboButton.setEnabled(false);
            endTurboButton.setEnabled(false);
            this.startButtonGraph.setEnabled(false);
            this.pauseButtonGraph.setEnabled(false);
            this.endButtonGraph.setEnabled(false);

            simulation = new MySimulation(Integer.parseInt(textFieldOrderPlacesTurbo.getText()), Integer.parseInt(textFieldCashTurbo.getText()));
            simulation.registerDelegate(this);
            this.simulation.onReplicationWillStart((sim)
                    ->{
                this.changeSpeed(this.simulation);
            });

            simulation.simulateAsync(Integer.parseInt(textFieldReplications.getText()), Config.simEndTime);

//            for (Worker worker : simulation.agentObsluznychMiest().getWorkersOrderNormal()) {
//                modelWorkersOrder.addRow(new Object[]{worker.getId(), worker.getType(), "Free"});
//            }
//
//            for (Worker worker : simulation.agentObsluznychMiest().getWorkersOrderOnline()) {
//                modelWorkersOrder.addRow(new Object[]{worker.getId(), worker.getType(), "Free"});
//            }
//
//            for (Worker worker : simulation.agentPokladni().getWorkersPayment()) {
//                modelWorkersPayment.addRow(new Object[]{worker.getId(), worker.getType(), "Free"});
//            }

        } else if (e.getSource() == pauseWatchTimeButton) {
            if (simulation.isPaused()) {
                simulation.resumeSimulation();
            } else {
                simulation.pauseSimulation();
            }
        } else if (e.getSource() == endWatchTimeButton) {
            startTurboButton.setEnabled(true);
            startWatchTimeButton.setEnabled(true);
            pauseWatchTimeButton.setEnabled(false);
            endWatchTimeButton.setEnabled(false);
            this.startButtonGraph.setEnabled(true);
            this.pauseButtonGraph.setEnabled(false);
            this.endButtonGraph.setEnabled(false);

            simulation.stopSimulation();
        } else if (e.getSource() == startButtonGraph) {
            this.series1.clear();

            this.turboMode = true;
            startTurboButton.setEnabled(false);
            startWatchTimeButton.setEnabled(false);
            pauseTurboButton.setEnabled(false);
            pauseWatchTimeButton.setEnabled(false);
            endTurboButton.setEnabled(false);
            endWatchTimeButton.setEnabled(false);

            this.startButtonGraph.setEnabled(false);
            this.pauseButtonGraph.setEnabled(true);
            this.endButtonGraph.setEnabled(true);

//            threadSimulationOuter1 = new Thread(new Runnable() {
//                public void run() {
//
//                    for (int i = 2; i <= 6; i++) {
//                        simulation = new Sem2(Integer.parseInt(textFieldWorkersOrderGraph.getText()), i, 30600.0, true, 0);
//                        simulation.setRunning(true);
//
//                        threadSimulationInner1 = new Thread(new Runnable() {
//                            public void run() {
//                                simulation.executeReplications(Integer.parseInt(textFieldReplicationsGraph.getText()));
//                                series1.add(simulation.getNumberOfWorkersPayment(), simulation.getAverageNumberOfCustomersWaitingTicket());
//                                chart1.fireChartChanged();
//                            }
//                        });
//                        threadSimulationInner1.start();
//
//                        while (threadSimulationInner1.isAlive()) {
//                            try {
//                                Thread.sleep(1);
//                            } catch (InterruptedException ex) {
//                                return;
//                            }
//                        }
//
//                    }
//                }
//            });
//            threadSimulationOuter1.start();
        } else if (e.getSource() == pauseButtonGraph) {
            if (simulation.isPaused()) {
                simulation.resumeSimulation();
            } else {
                simulation.pauseSimulation();
            }
        } else if (e.getSource() == endButtonGraph) {
            startTurboButton.setEnabled(true);
            startWatchTimeButton.setEnabled(true);
            pauseTurboButton.setEnabled(false);
            endTurboButton.setEnabled(false);
            this.startButtonGraph.setEnabled(true);
            this.pauseButtonGraph.setEnabled(false);
            this.endButtonGraph.setEnabled(false);

            simulation.stopSimulation();
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() == this.sliderSpeed || e.getSource() == this.simSpeedIntervalSlider) {
            if (simulation != null) {
                this.changeSpeed(simulation);
            }
        }
    }

    public void changeSpeed(Simulation simulation) {
        double speedMax = this.sliderSpeed.getMaximum() * .1;
        double speedValue = this.sliderSpeed.getValue() * .1;
        double intervalValue = this.simSpeedIntervalSlider.getValue();

        if (simulation != null) {
            if (!turboMode) {
                simulation.setSimSpeed(intervalValue * .01, (speedMax - speedValue + .001) * .05);
            } else {
                simulation.setMaxSimSpeed();
            }
        }
    }

    @Override
    public void simStateChanged(Simulation simulation, SimState simState) {

    }

    @Override
    public void refresh(Simulation simulation) {
        if (turboMode) {
//            this.executedReplicationsTurbo.setText(Integer.toString(simulation.getExecutedReplications()));
//            this.confIntervalTimeInSystemTurbo.setText("<" + String.format("%.3f", simulation.getConfIntTimeInSystemLower() / 60.0)
//                    + " ; " + String.format("%.3f", simulation.getConfIntTimeInSystemUpper() / 60.0)
//                    + ">");
//            this.averageTimeSystemTurbo.setText(String.format("%.3f", simulation.getAverageTimeInSystem()) + " sekúnd / " + String.format("%.3f", simulation.getAverageTimeInSystem() / 60.0) + " minút");
//            this.averageNumberServedCustomersTurbo.setText(String.format("%.3f", simulation.getAverageServedCustomer()));
//            this.averageTimeQueueTicketTurbo.setText(String.format(String.format("%.3f", simulation.getAverageTimeTicket()) + " sekúnd / " + "%.3f", simulation.getAverageTimeTicket() / 60.0) + " minút");
//            this.averageNumberQueueTicketTurbo.setText(String.format("%.3f", simulation.getAverageNumberOfCustomersWaitingTicket()));
//            this.averageUsePercTicketTurbo.setText(String.format("%.2f", simulation.getAverageUsePercentTicket()) + "%");
//            this.averageUsePercOrderTurbo.setText(String.format("%.2f", simulation.getAverageUsePercentOrder()) + "%");
//            this.averageUsePercPaymentTurbo.setText(String.format("%.2f", simulation.getAverageUsePercentPayment()) + "%");
//
//            // Convert seconds to hours, minutes and remaining seconds
//            long hours = TimeUnit.SECONDS.toHours((long) simulation.getAverageTimeLeaveSystem());
//            long minutes = TimeUnit.SECONDS.toMinutes((long) simulation.getAverageTimeLeaveSystem()) - (hours * 60);
//            long remainingSeconds = (long) (simulation.getAverageTimeLeaveSystem() - TimeUnit.HOURS.toSeconds(hours) - TimeUnit.MINUTES.toSeconds(minutes));
//            // Format the time as "hours:minutes:seconds"
//            String time = String.format("%d:%02d:%02d", hours, minutes, remainingSeconds);
//            this.averageTimeLeaveSystemTurbo.setText(time);

        } else {
            this.timeProgrammer.setText(Double.toString(simulation.currentTime()));
            // Convert seconds to hours, minutes and remaining seconds
            long actualTime = (long) simulation.currentTime() + 32400;
            long hours = TimeUnit.SECONDS.toHours(actualTime);
            long minutes = TimeUnit.SECONDS.toMinutes(actualTime) - (hours * 60);
            long remainingSeconds = (long) (actualTime - TimeUnit.HOURS.toSeconds(hours) - TimeUnit.MINUTES.toSeconds(minutes));
            // Format the time as "hours:minutes:seconds"
            String timeString = String.format("%d:%02d:%02d", hours, minutes, remainingSeconds);
            this.timeUser.setText(timeString);

//            this.numberWorkersOrderNormal.setText(Integer.toString(simulation.getWorkersOrderNormal().size()));
//            this.numberWorkersOrderOnline.setText(Integer.toString(simulation.getWorkersOrderOnline().size()));
//            this.numberWorkersPayment.setText(Integer.toString(simulation.getWorkersPayment().size()));
//            this.numberCustomersTicket.setText(Integer.toString(simulation.getQueueCustomersWaitingTicketDispenser().size()));
//            this.numberCustomersQueueService.setText(Integer.toString(simulation.getCustomersWaitingInShopBeforeOrder().size()));

            int numberCustomersPayment = 0;
//            for (LinkedList<Customer> queue : simulation.getQueuesCustomersWaitingForPayment()) {
//                numberCustomersPayment += queue.size();
//            }
            this.numberCustomersPayment.setText(Integer.toString(numberCustomersPayment));
            this.executedReplications.setText(Integer.toString(simulation.currentReplication()));

            this.numberCustomersIn.setText(Integer.toString(((MySimulation)simulation).agentOkolia().getCountCustomersIn()));
            this.numberCustomersOut.setText(Integer.toString(((MySimulation)simulation).agentOkolia().getCountCustomersOut()));
//            this.numberCustomersCurrent.setText(Integer.toString(simulation.getAllCustomers().size()));

            try {
                SwingUtilities.invokeAndWait(new Runnable() {
                    public void run() {

                        modelWorkersOrder.setRowCount(0);
                        for (Worker worker : ((MySimulation) simulation).agentObsluznychMiest().getWorkersOrderNormal()) {
                            if (worker.getIdCustomer() == -1) {
                                modelWorkersOrder.addRow(new Object[]{worker.getId(), worker.getType(), "Free"});
                            } else {
                                modelWorkersOrder.addRow(new Object[]{worker.getId(), worker.getType(), Integer.toString(worker.getIdCustomer())});
                            }
                        }

                        for (Worker worker : ((MySimulation) simulation).agentObsluznychMiest().getWorkersOrderOnline()) {
                            if (worker.getIdCustomer() == -1) {
                                modelWorkersOrder.addRow(new Object[]{worker.getId(), worker.getType(), "Free"});
                            } else {
                                modelWorkersOrder.addRow(new Object[]{worker.getId(), worker.getType(), Integer.toString(worker.getIdCustomer())});
                            }
                        }

                        modelWorkersOrderW.setRowCount(0);
                        for (Worker worker : ((MySimulation) simulation).agentObsluznychMiest().getWorkersOrderWorkingNormal()) {
                            modelWorkersOrderW.addRow(new Object[]{worker.getId(), worker.getType(), Integer.toString(worker.getIdCustomer()), worker.getCustomer().getSizeOfOrder()});
                        }

                        for (Worker worker : ((MySimulation) simulation).agentObsluznychMiest().getWorkersOrderWorkingOnline()) {
                            modelWorkersOrderW.addRow(new Object[]{worker.getId(), worker.getType(), Integer.toString(worker.getIdCustomer()), worker.getCustomer().getSizeOfOrder()});
                        }

                        modelWorkersPayment.setRowCount(0);
                        for (Worker worker : ((MySimulation) simulation).agentPokladni().getWorkersPayment()) {
                            if (worker.getIdCustomer() == -1) {
                                modelWorkersPayment.addRow(new Object[]{worker.getId(), "Free"});

                            } else {
                                modelWorkersPayment.addRow(new Object[]{worker.getId(), Integer.toString(worker.getIdCustomer())});
                            }
                        }

                        modelWorkersPaymentW.setRowCount(0);
                        for (Worker worker : ((MySimulation) simulation).agentPokladni().getWorkersPaymentWorking()) {
                            modelWorkersPaymentW.addRow(new Object[]{worker.getId(), Integer.toString(worker.getIdCustomer())});
                        }

                        modelCustomersWaitingTicket.setRowCount(0);
                        for (MessageForm mess :  ((MySimulation) simulation).agentAutomatu().get_queueCustomersTicketDispenser()) {
                            modelCustomersWaitingTicket.addRow(new Object[]{((MyMessage)mess).getCustomer().getId()});
                        }

                        modelCustomerTicketDispenser.setRowCount(0);
                        for (MessageForm mess : ((MySimulation) simulation).agentAutomatu().get_customerInteractingWithTicketDispenser()) {
                            modelCustomerTicketDispenser.addRow(new Object[]{((MyMessage)mess).getCustomer().getId()});
                        }

//                        modelCustomersWaitingOrder.setRowCount(0);
//                        for (Customer customer : simulation.getCustomersWaitingInShopBeforeOrder()) {
//                            modelCustomersWaitingOrder.addRow(new Object[]{customer.getId(), customer.getCustomerType(), customer.getTimeArrival()});
//                        }
//
//                        modelCustomersPayment.setRowCount(0);
//                        int index = 0;
//                        for (LinkedList<Customer> queue : simulation.getQueuesCustomersWaitingForPayment()) {
//                            for (Customer customer : queue) {
//                                modelCustomersPayment.addRow(new Object[]{((Customer) customer).getId(), Integer.toString(index)});
//                            }
//                            index++;
//                        }

                    }
                });
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }

//            this.confIntervalTimeInSystemWatchTime.setText("<" + String.format("%.3f", simulation.getConfIntTimeInSystemLower() / 60.0)
//                    + " ; " + String.format("%.3f", simulation.getConfIntTimeInSystemUpper() / 60.0)
//                    + ">");
//
//            this.averageTimeSystemWatchTime.setText(String.format("%.3f", simulation.getAverageTimeInSystem()) + " sekúnd / " + String.format("%.3f", simulation.getAverageTimeInSystem() / 60.0) + " minút");
//            this.averageTimeQueueTicketWatchTime.setText(String.format(String.format("%.3f", simulation.getAverageTimeTicket()) + " sekúnd / " + "%.3f", simulation.getAverageTimeTicket() / 60.0) + " minút");
//            this.averageNumberServedCustomersWatchTime.setText(String.format("%.3f", simulation.getAverageServedCustomer()));
//            this.averageNumberQueueTicketWatchTime.setText(String.format("%.3f", simulation.getAverageNumberOfCustomersWaitingTicket()));
//            this.averageUsePercTicketWatchTime.setText(String.format("%.2f", simulation.getAverageUsePercentTicket()) + "%");
//            this.averageUsePercOrderWatchTime.setText(String.format("%.2f", simulation.getAverageUsePercentOrder()) + "%");
//            this.averageUsePercPaymentWatchTime.setText(String.format("%.2f", simulation.getAverageUsePercentPayment()) + "%");
//
//            // Convert seconds to hours, minutes and remaining seconds
//            long hoursTimeLeaveSystem = TimeUnit.SECONDS.toHours((long) simulation.getAverageTimeLeaveSystem());
//            long minutesTimeLeaveSystem = TimeUnit.SECONDS.toMinutes((long) simulation.getAverageTimeLeaveSystem()) - (hoursTimeLeaveSystem * 60);
//            long remainingSecondsTimeLeaveSystem = (long) (simulation.getAverageTimeLeaveSystem() - TimeUnit.HOURS.toSeconds(hoursTimeLeaveSystem) - TimeUnit.MINUTES.toSeconds(minutesTimeLeaveSystem));
//            // Format the time as "hours:minutes:seconds"
//            String time = String.format("%d:%02d:%02d", hoursTimeLeaveSystem, minutesTimeLeaveSystem, remainingSecondsTimeLeaveSystem);
//            this.averageTimeLeaveSystemWatchTime.setText(time);
        }
    }
}