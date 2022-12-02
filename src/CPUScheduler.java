import java.util.ArrayList;
import java.util.List;

public abstract class CPUScheduler {
    protected final List<Process> processes = new ArrayList<>();

    public abstract void start();
}
