import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public abstract class CPUScheduler {
    protected final Deque<Process> readyQueue = new LinkedList<>();

    protected int currentTime = 0;
    protected int finished = 0;
    protected final List<Interval> executionOrder = new ArrayList<>();

    public void start(List<Process> processes) {
//        System.out.println("Scheduling started");
        do {
//            System.out.println("=======================");
//            System.out.println("time: " + this.currentTime + ":");
//            System.out.println("=======================");
            this.step(processes);
            this.currentTime++;
        } while (processes.size() != this.finished);
    }

    protected abstract void step(List<Process> processes);

    public void printReadyQueue() {
        System.out.print("Ready queue: ");
        for (Process pp : this.readyQueue) System.out.print(pp.name + " ");
        System.out.println();
    }

    public void addContextSwitching(int contextSwitchingTime) {
        for (int i = 1; i < this.executionOrder.size(); i++) {
            this.executionOrder.get(i).setStart(this.executionOrder.get(i).getStart() + contextSwitchingTime * i);
            this.executionOrder.get(i).setEnd(this.executionOrder.get(i).getEnd() + contextSwitchingTime * i);
        }
    }
}
