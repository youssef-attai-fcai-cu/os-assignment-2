import java.util.LinkedList;
import java.util.Queue;

public abstract class CPUScheduler {
    protected final Queue<Process> processes = new LinkedList<>();

    public abstract void start();
}
