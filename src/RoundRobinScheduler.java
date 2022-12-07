import java.util.List;

public class RoundRobinScheduler extends CPUScheduler {
    public final int Q;
    private int counter = 0;

    public RoundRobinScheduler(int q) {
        this.Q = q;
    }

    public void start(List<Process> processes) {
        super.start(processes);
        if (this.executionOrder.size() > 0)
            this.executionOrder.get(this.executionOrder.size() - 1).setEnd(this.currentTime);
    }

    @Override
    protected void step(List<Process> processes) {
//        Check if a process arrived and push it to the process queue
        for (Process p : processes) {
            if (p.arrivalTime == this.currentTime) {
                this.readyQueue.add(p);
//                System.out.println(p.name + " has arrived");
            }
        }

        if (this.counter == this.Q) {
//            System.out.println("RR timeout");
            this.readyQueue.add(this.readyQueue.poll());
            this.counter = 0;
        }

//        printReadyQueue();

//        Get the current running process
        Process currentRunningProcess = this.readyQueue.peek();
        assert currentRunningProcess != null;
//        System.out.println("Currently processing: " + currentRunningProcess.name);

        if (
//                If this is the first process
                this.executionOrder.size() == 0 ||
//                        Or if this is a different process than the last added in execution order
                        !currentRunningProcess.name.equals(
                                this.executionOrder.get(this.executionOrder.size() - 1).getProcessName()
                        )
        ) {
//            Update the end of the last added interval
            if (this.executionOrder.size() > 0)
                this.executionOrder.get(this.executionOrder.size() - 1).setEnd(this.currentTime);
//            Create an interval for the process, and add it to the execution order
            Interval interval = new Interval(currentRunningProcess);
            interval.setStart(this.currentTime);
            this.executionOrder.add(interval);
        } else {
//            Update the end of the last added interval
            if (this.executionOrder.size() > 0)
                this.executionOrder.get(this.executionOrder.size() - 1).setEnd(this.currentTime);
        }

//        Update the remaining time of the process at the top of the process queue
        currentRunningProcess.remainingTime--;
        this.counter++;
//        System.out.println(currentRunningProcess.name + " remaining time: " + currentRunningProcess.remainingTime);
//        System.out.println("counter: " + this.counter);
//        System.out.println("Process " + currentRunningProcess.name + " remaining time: " + currentRunningProcess.remainingTime);

        if (currentRunningProcess.remainingTime == 0) {
            currentRunningProcess.turnAroundTime = currentTime - currentRunningProcess.arrivalTime + 1;
            currentRunningProcess.waitingTime = currentRunningProcess.turnAroundTime - currentRunningProcess.burstTime;
            Process done = this.readyQueue.poll();
            assert done != null;
//            System.out.println(done.name + " is done");
            this.counter = 0;
            this.finished++;
        }
    }
}
