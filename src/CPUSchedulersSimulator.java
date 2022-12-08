import java.util.List;

public class CPUSchedulersSimulator {
    public static void main(String[] args) {
        simulateFirstComeFirstServeScheduling(List.of(
                new Process("A", 0, 10, 0),
                new Process("B", 1, 8, 0),
                new Process("C", 2, 6, 0),
                new Process("D", 3, 4, 0),
                new Process("E", 4, 2, 0)
        ));
        simulateShortestJobFirstScheduling(List.of(
                new Process("A", 0, 10, 0),
                new Process("B", 1, 8, 0),
                new Process("C", 2, 6, 0),
                new Process("D", 3, 4, 0),
                new Process("E", 4, 2, 0)
        ));
        simulatePriorityScheduling(List.of(
                new Process("A", 0, 200, 2),
                new Process("B", 1, 200, 1),
                new Process("C", 2, 200, 0),
                new Process("D", 3, 10, 3)
        ));
        simulateRoundRobinScheduling(List.of(
                new Process("A", 0, 2, 0),
                new Process("B", 2, 4, 0),
                new Process("C", 4, 6, 0),
                new Process("D", 6, 8, 0),
                new Process("E", 8, 10, 0)
        ));
    }

    private static void simulateFirstComeFirstServeScheduling(List<Process> allProcesses) {
        CPUScheduler fcfsScheduler = new FirstComeFirstServeScheduler();

        fcfsScheduler.start(allProcesses);

        for (Interval interval : fcfsScheduler.executionOrder) {
            System.out.print(interval.getStart() + " [" + interval.getProcessName() + "] ");
        }
        if (fcfsScheduler.executionOrder.size() > 0)
            System.out.println(fcfsScheduler.executionOrder.get(fcfsScheduler.executionOrder.size() - 1).getEnd());

        calculateWaitingTime(allProcesses);
        calculateTurnAround(allProcesses);
    }

    private static void simulatePriorityScheduling(List<Process> allProcesses) {
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

    private static void simulateShortestJobFirstScheduling(List<Process> allProcesses) {
        CPUScheduler sjfScheduler = new ShortestJobFirstScheduler();

        sjfScheduler.start(allProcesses);
        sjfScheduler.addContextSwitching(1);

        for (Interval interval : sjfScheduler.executionOrder) {
            System.out.print(interval.getStart() + " [" + interval.getProcessName() + "] " + interval.getEnd() + " | ");
        }
        System.out.println(sjfScheduler.executionOrder.get(sjfScheduler.executionOrder.size() - 1).getEnd());

        calculateWaitingTime(allProcesses);
        calculateTurnAround(allProcesses);
    }

    private static void simulateRoundRobinScheduling(List<Process> allProcesses) {
        CPUScheduler rrScheduler = new RoundRobinScheduler(3);

        rrScheduler.start(allProcesses);
        rrScheduler.addContextSwitching(1);

        for (Interval interval : rrScheduler.executionOrder) {
            System.out.print(interval.getStart() + " [" + interval.getProcessName() + "] " + interval.getEnd() + " | ");
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
