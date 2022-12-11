import java.util.List;

public class RoundRobinScheduler extends CPUScheduler {
    public final int Q;
    private int counter = 0;
    private int contextSwitching;
    private int varContextSwitching = 0;
    private Process running = null;

    public RoundRobinScheduler(int contextSwitchingCost, int q) {
        Q = q;
        contextSwitching = contextSwitchingCost;
    }

    public void start(List<Process> processes) {
        super.start(processes);
        if (executionOrder.size() > 0)
            executionOrder.get(executionOrder.size() - 1).setEnd(currentTime - 1);
    }

    @Override
    protected void step(List<Process> processes) {
        for (Process p : processes) {
            if (p.arrivalTime == currentTime) {
                readyQueue.add(p);
//                System.out.println(p.name + " has arrived");
            }
        }

        if (varContextSwitching > 0) {
//            System.out.println("Switching...");
            varContextSwitching--;
            return;
        }

        if (running == null) {
//            System.out.println("No process is running");
            running = readyQueue.poll();
            assert running != null;
            if (executionOrder.size() == 0) {
                Interval interval = new Interval(running);
                interval.setStart(currentTime);
                executionOrder.add(interval);
            } else if (executionOrder.get(executionOrder.size() - 1).process != running) {
                executionOrder.get(executionOrder.size() - 1).setEnd(currentTime - contextSwitching - 1);

                Interval interval = new Interval(running);

                interval.setStart(currentTime - 1);
                executionOrder.add(interval);
            }
        }

//        System.out.println(running.name + " is now running");

        if (currentTime > 0) {
            running.remainingTime--;
            counter++;

//            System.out.println(running.name + "'s remaining time = " + running.remainingTime);
//            System.out.println("counter = " + counter);
            if (running.remainingTime == 0) {
                running.turnAroundTime = currentTime - running.arrivalTime;
                running.waitingTime = running.turnAroundTime - running.burstTime;
//                System.out.println(running.name + " finished");
                running = null;
                varContextSwitching = contextSwitching;
                counter = 0;
                finished++;
                return;
            }

            if (counter == Q) {
                counter = 0;
                readyQueue.add(running);
                running = null;
                varContextSwitching = contextSwitching;
//                System.out.println("RR timeout");
//                System.out.println("counter = " + counter);

            }
        }
    }
}
