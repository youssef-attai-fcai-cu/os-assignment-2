import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;

public class AGScheduler extends CPUScheduler {
    private AGMode mode = AGMode.FCFS;

    @Override
    protected void step(List<Process> processes) {
//        Check if a process arrived
        for (Process p : processes) {
            if (p.arrivalTime == this.currentTime) {
                this.readyQueue.add(p);
                System.out.println(p.name + " has arrived");
            }
        }

        if (mode == AGMode.FCFS) {
            System.out.println("Current mode: FCFS");
        } else if (mode == AGMode.NPP) {
            System.out.println("Current mode: NPP");
            sortByPriority(this.readyQueue);
        } else if (mode == AGMode.PSJF) {
            System.out.println("Current mode: PSJF");
            sortByRemainingTime(this.readyQueue);
        }

        printReadyQueue();

//        Get the current running process
        Process currentRunningProcess = this.readyQueue.peek();
        assert currentRunningProcess != null;
        System.out.println("Current: " + currentRunningProcess.name);

//        If the current process has just started at this point in time
        if (this.executionOrder.size() == 0 || !currentRunningProcess.name.equals(this.executionOrder.get(this.executionOrder.size() - 1).getProcessName())) {
            currentRunningProcess.varQuantum = 0;
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
        currentRunningProcess.varQuantum++;
        System.out.println(currentRunningProcess.name + "'s var quantum: " + currentRunningProcess.varQuantum);

        if (currentRunningProcess.varQuantum == (int) Math.ceil(currentRunningProcess.quantum / (double) 4)) {
            this.mode = AGMode.NPP;
            System.out.println("Switched to NPP");
        } else if (currentRunningProcess.varQuantum == (int) Math.ceil(currentRunningProcess.quantum / (double) 2)) {
            this.mode = AGMode.PSJF;
            System.out.println("Switched to PSJF");
        } else if (currentRunningProcess.varQuantum == currentRunningProcess.quantum) {
            this.mode = AGMode.FCFS;
            System.out.println("Switched to FCFS");

        }

//        if (currentRunningProcess.varQuantum == currentRunningProcess.quantum && currentRunningProcess.remainingTime != 0 && currentRunningProcess.remainingTime != currentRunningProcess.burstTime) {
//            currentRunningProcess.quantum += 2;
//            currentRunningProcess.varQuantum = 0;
//            this.readyQueue.poll();
//            this.readyQueue.add(currentRunningProcess);
//        }
//        else if (mode == AGMode.NPP && currentRunningProcess.varQuantum != currentRunningProcess.quantum) {
//            currentRunningProcess.quantum += (currentRunningProcess.quantum - currentRunningProcess.varQuantum) / 2;
//            currentRunningProcess.varQuantum = 0;
//            this.readyQueue.poll();
//            this.readyQueue.add(currentRunningProcess);
//        }
//        else if (mode == AGMode.PSJF && currentRunningProcess.varQuantum != currentRunningProcess.quantum) {
//            currentRunningProcess.quantum += currentRunningProcess.quantum - currentRunningProcess.varQuantum;
//            currentRunningProcess.varQuantum = 0;
//            this.readyQueue.poll();
//            this.readyQueue.add(currentRunningProcess);
//        }
//        else if (currentRunningProcess.varQuantum != currentRunningProcess.quantum && currentRunningProcess.remainingTime == 0){
//            currentRunningProcess.quantum = 0;
//        }


//        System.out.println("Process " + currentRunningProcess.name + " remaining time: " + currentRunningProcess.remainingTime);

        if (currentRunningProcess.remainingTime == 0) {
            currentRunningProcess.turnAroundTime = currentTime - currentRunningProcess.arrivalTime + 1;
            currentRunningProcess.waitingTime = currentRunningProcess.turnAroundTime - currentRunningProcess.burstTime;
            this.readyQueue.poll();
            this.finished++;
        }

    }

    private void sortByRemainingTime(Deque<Process> processes) {
        List<Process> ps = new ArrayList<>(processes.stream().sorted(Comparator.comparingInt(o -> o.remainingTime)).toList());
        processes.clear();
//        Collections.reverse(ps);
        processes.addAll(ps);
    }

    private void sortByPriority(Deque<Process> processes) {
        List<Process> ps = new ArrayList<>(processes.stream().sorted(Comparator.comparingInt(o -> o.priority)).toList());
        processes.clear();
//        Collections.reverse(ps);
        processes.addAll(ps);
    }
}
