import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;

public class ShortestJobFirstScheduler extends CPUScheduler {
    private int contextSwitchingCost;
    private int varContextSwitching = 0;
    private Process running = null;

    public ShortestJobFirstScheduler(int contextSwitchingCost) {
        this.contextSwitchingCost = contextSwitchingCost;
    }

    public void start(List<Process> processes) {
        super.start(processes);
        if (this.executionOrder.size() > 0)
            this.executionOrder.get(this.executionOrder.size() - 1).setEnd(currentTime - 1);
    }

    @Override
    protected void step(List<Process> processes) {
//        Check if a process arrived and push it to the process queue
        for (Process p : processes) {
            if (p.arrivalTime == this.currentTime) {
//                System.out.println(p.name + " has arrived");
                this.readyQueue.add(p);
//                Sort by priority
                sortByRemainingTime(this.readyQueue);
            }
        }

        if (varContextSwitching > 0) {
//            System.out.println("switching");
            varContextSwitching--;
            return;
        }
//        printReadyQueue();

//        Get the current running process
        Process next = this.readyQueue.peek();
        assert next != null;
        if (executionOrder.size() == 0) {
            Interval interval = new Interval(next);
            interval.setStart(currentTime);
            executionOrder.add(interval);
        } else if (executionOrder.get(executionOrder.size() - 1).process != running) {
            executionOrder.get(executionOrder.size() - 1).setEnd(currentTime - contextSwitchingCost - 1);

            Interval interval;
            if (running == null)
                interval = new Interval(next);
            else
                interval = new Interval(running);

            interval.setStart(currentTime - 1);
            executionOrder.add(interval);
        }

        if (running != null && running != next) {
            running.remainingTime--;
//            System.out.println(running.name + " is running");
//            System.out.println(next.name + " is next");
            varContextSwitching = contextSwitchingCost;
            running = next;
            return;
        }
        running = next;

//        If the current process has just started at this point in time
//        if (this.executionOrder.size() == 0 || !running.name.equals(this.executionOrder.get(this.executionOrder.size() - 1).getProcessName())) {
////            Update the end of the last added interval
//            if (this.executionOrder.size() > 0)
//                this.executionOrder.get(this.executionOrder.size() - 1).setEnd(this.currentTime);
////            Create an interval for the process, and add it to the execution order
//            Interval interval = new Interval(running);
//            interval.setStart(this.currentTime);
//            this.executionOrder.add(interval);
//        } else {
////            Update the end of the last added interval
//            if (this.executionOrder.size() > 0)
//                this.executionOrder.get(this.executionOrder.size() - 1).setEnd(this.currentTime);
//        }
//        Update the remaining time of the process at the top of the process queue
        if (currentTime > 0) {
            running.remainingTime--;
//            System.out.println(running.name + "'s remaining time = " + running.remainingTime);
            if (running.remainingTime == 0) {
                running.turnAroundTime = currentTime - running.arrivalTime;
                running.waitingTime = running.turnAroundTime - running.burstTime;
                this.readyQueue.poll();
                this.finished++;
//                System.out.println(running.name + " finished");
                running = null;
                varContextSwitching = contextSwitchingCost;
            }
        }
    }

    private void sortByRemainingTime(Deque<Process> processes) {
        List<Process> ps = new ArrayList<>(processes.stream().sorted(Comparator.comparingInt(o -> o.remainingTime)).toList());
        processes.clear();
//        Collections.reverse(ps);
        processes.addAll(ps);
    }
}
