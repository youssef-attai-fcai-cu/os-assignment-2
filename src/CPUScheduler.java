import java.util.LinkedList;
import java.util.Queue;

public abstract class CPUScheduler {
    protected final Queue<Process> processes = new LinkedList<>();

    public void addProcess(Process process) {
        this.processes.add(process);
    }

    public abstract void start();
}
