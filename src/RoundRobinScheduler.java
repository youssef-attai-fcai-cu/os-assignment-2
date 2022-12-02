import java.util.List;

public class RoundRobinScheduler extends CPUScheduler {
    private final int Q;
    private int counter;
    private int contextSwitchingTime;

    public RoundRobinScheduler(int q, int contextSwitchingTime) {
        this.Q = q;
        this.contextSwitchingTime = contextSwitchingTime;
        this.counter = this.Q;
    }

    @Override
    protected void step(List<Process> processes) {
//        Check if a process arrived, if so, push it to the process queue
        for (Process p : processes)
            if (p.arrivalTime == this.currentTime) {
                System.out.println(p.name + " has arrived");
                this.processes.add(p);
            }

//        Get the current running process
        Process currentRunningProcess = this.processes.peek();
        assert currentRunningProcess != null;
        System.out.println("currentRunningProcess: " + currentRunningProcess.name);

//        If the current process has just started at this point in time
        if (currentRunningProcess.remainingTime == currentRunningProcess.burstTime || this.switching) {
            System.out.println(currentRunningProcess.name + " is added to the execution order");
//            Create an interval for the process, and add it to the execution order
            Interval interval = new Interval(currentRunningProcess);
            interval.setStart(this.currentTime);
            interval.setEnd(this.currentTime + Utils.clamp(currentRunningProcess.remainingTime, this.Q));
            System.out.println(interval.getProcessName() +
                    " should start at " + interval.getStart() +
                    " and finish at " + interval.getEnd());
            this.executionOrder.add(interval);
            this.switching = false;
        }

//        Update the remaining time of the process at the top of the process queue
        currentRunningProcess.remainingTime--;
        System.out.println(currentRunningProcess.name + "'s remaining time: " + currentRunningProcess.remainingTime);

        boolean switched = false;
        if (currentRunningProcess.remainingTime == 0) {
            System.out.println(currentRunningProcess.name +
                    "has finished execution and has been removed from the process queue");
            this.processes.poll();
            this.switching = true;
            this.finished++;
            this.counter = Q;
            System.out.println(this.finished + " processes are finished");
        }
        if (this.counter == 0 && currentRunningProcess.remainingTime > 0) {
            System.out.println("RR timeout, pushing " + currentRunningProcess.name + " to the back of the queue");
//            If the RR Q has passed, poll the current process and push it back to the queue
            currentRunningProcess = this.processes.poll();
            this.switching = true;
            this.processes.add(currentRunningProcess);
            assert this.processes.peek() != null;
            System.out.println(this.processes.peek().name + " is next");
            this.counter = Q;
        }

        this.counter--;
        System.out.println("Q counter: " + this.counter);
    }
}
