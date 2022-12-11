import java.util.List;
import java.util.Scanner;

public class CPUSchedulersSimulator {
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        simulateShortestJobFirstScheduling(List.of(
                new Process("A", 0, 10, 0),
                new Process("B", 1, 8, 0),
                new Process("C", 2, 6, 0)
//                new Process("D", 3, 4, 0),
//                new Process("E", 4, 2, 0)
        ));
        simulateRoundRobinScheduling(List.of(
                new Process("A", 0, 2, 0),
                new Process("B", 2, 4, 0),
                new Process("C", 4, 6, 0),
                new Process("D", 6, 8, 0)
        ));
        simulatePriorityScheduling(List.of(
                new Process("A", 0, 20, 2),
                new Process("B", 1, 20, 1),
                new Process("C", 2, 20, 0),
                new Process("D", 3, 10, 3)
        ));
        simulateAGScheduling(List.of(
                new Process("P1", 0, 17, 4, 7),
                new Process("P2", 2, 6, 7, 9),
                new Process("P3", 5, 11, 3, 4),
                new Process("P4", 15, 4, 6, 6)
        ));
    }

    private static void simulateShortestJobFirstScheduling(List<Process> allProcesses) {
        System.out.println("==================================================================");
        System.out.println("      Preemptive Shortest Job First  (With context switching)     ");
        System.out.println("==================================================================");

        int cs = 1;
        CPUScheduler sjfScheduler = new ShortestJobFirstScheduler(cs);

        sjfScheduler.start(allProcesses);

        for (Interval interval : sjfScheduler.executionOrder) {
            System.out.print(interval.getStart() + " [" + interval.getProcessName() + "] " + interval.getEnd() + " | ");
        }
        System.out.println();

        calculateWaitingTime(allProcesses);
        calculateTurnAround(allProcesses);
    }

    private static void simulateRoundRobinScheduling(List<Process> allProcesses) {
        System.out.println("===============================================");
        System.out.println("      Round Robin  (With context switching)    ");
        System.out.println("===============================================");

        int cs = 1;
        CPUScheduler rrScheduler = new RoundRobinScheduler(cs, 3);

        rrScheduler.start(allProcesses);
//        rrScheduler.addContextSwitching(1);

        for (Interval interval : rrScheduler.executionOrder) {
            System.out.print(interval.getStart() + " [" + interval.getProcessName() + "] " + interval.getEnd() + " | ");
        }
        System.out.println();

        calculateWaitingTime(allProcesses);
        calculateTurnAround(allProcesses);
    }

    private static void simulatePriorityScheduling(List<Process> allProcesses) {
        System.out.println("===========================================");
        System.out.println("     Preemptive Priority  (With aging)     ");
        System.out.println("===========================================");

        CPUScheduler pScheduler = new PriorityScheduler(15);

        pScheduler.start(allProcesses);

        for (Interval interval : pScheduler.executionOrder) {
            System.out.print(interval.getStart() + " [" + interval.getProcessName() + "] ");
        }
        if (pScheduler.executionOrder.size() > 0)
            System.out.println(pScheduler.executionOrder.get(pScheduler.executionOrder.size() - 1).getEnd());

        calculateWaitingTime(allProcesses);
        calculateTurnAround(allProcesses);
    }

    private static void simulateAGScheduling(List<Process> allProcesses) {
        System.out.println("============");
        System.out.println("     AG     ");
        System.out.println("============");

        AGScheduler agScheduler = new AGScheduler();
        agScheduler.start(allProcesses);

        for (Interval interval : agScheduler.executionOrder) {
            System.out.print(interval.getStart() + " [" + interval.getProcessName() + "] ");
        }
        if (agScheduler.executionOrder.size() > 0)
            System.out.println(agScheduler.executionOrder.get(agScheduler.executionOrder.size() - 1).getEnd());

        calculateWaitingTime(allProcesses);
        calculateTurnAround(allProcesses);
    }

    private static void calculateWaitingTime(List<Process> allProcesses) {
        float sumWaitingTime = 0;

        for (Process p : allProcesses) {
            System.out.println(p.name + " waiting time = " + p.waitingTime);
            sumWaitingTime += p.waitingTime;
        }

        System.out.println("Average waiting time = " + sumWaitingTime / allProcesses.size());
    }

    private static void calculateTurnAround(List<Process> allProcesses) {
        float sumTurnAroundTime = 0;

        for (Process p : allProcesses) {
            System.out.println(p.name + " turnaround time = " + p.turnAroundTime);
            sumTurnAroundTime += p.turnAroundTime;
        }

        System.out.println("Average turnaround time = " + sumTurnAroundTime / allProcesses.size());
    }
}
