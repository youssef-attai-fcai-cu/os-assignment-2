import java.util.List;

public class FirstComeFirstServeScheduler extends CPUScheduler{
    @Override
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
//        printReadyQueue();

//        Get the current running process
        Process currentRunningProcess = this.readyQueue.peek();
        assert currentRunningProcess != null;

//        If the current process has just started at this point in time
        if (
                this.executionOrder.size() == 0 ||
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
//        System.out.println("Process " + currentRunningProcess.name + " remaining time: " + currentRunningProcess.remainingTime);

        if (currentRunningProcess.remainingTime == 0) {
            currentRunningProcess.turnAroundTime = currentTime - currentRunningProcess.arrivalTime + 1;
            currentRunningProcess.waitingTime = currentRunningProcess.turnAroundTime - currentRunningProcess.burstTime;
            this.readyQueue.poll();
            this.finished++;
        }
    }
}
