import java.util.List;

public class CPUSchedulersSimulator {
    public static void main(String[] args) {
        simulatePriorityScheduling(List.of(
                new Process("P1", 0, 11, 2),
                new Process("P2", 5, 28, 4),
                new Process("P3", 12, 2, 1),
                new Process("P4", 2, 10, 3),
                new Process("P5", 9, 16, 0)
        ));
        simulateRoundRobinScheduling(List.of(
                new Process("A", 0, 2, 0),
                new Process("B", 2, 4, 0),
                new Process("C", 4, 6, 0),
                new Process("D", 6, 8, 0),
                new Process("E", 8, 10, 0)
        ));
    }

    private static void simulatePriorityScheduling(List<Process> allProcesses) {
        CPUScheduler pScheduler = new PriorityScheduler();

        pScheduler.start(allProcesses);

        for (Interval interval : pScheduler.executionOrder) {
            System.out.print(interval.getStart() + " [" + interval.getProcessName() + "] ");
        }
        if (pScheduler.executionOrder.size() > 0)
            System.out.println(pScheduler.executionOrder.get(pScheduler.executionOrder.size() - 1).getEnd());

        calculateWaitingTime(allProcesses);
        calculateTurnAround(allProcesses);
    }

    private static void simulateRoundRobinScheduling(List<Process> allProcesses) {
        CPUScheduler rrScheduler = new RoundRobinScheduler(3);

        rrScheduler.start(allProcesses);

        for (Interval interval : rrScheduler.executionOrder) {
            System.out.print(interval.getStart() + " [" + interval.getProcessName() + "] ");
        }
        System.out.println(rrScheduler.executionOrder.get(rrScheduler.executionOrder.size() - 1).getEnd());

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
