import java.util.List;

public class Utils {
    public static int maxArrival(List<Process> processes) {
        int max = 0;
        for (Process p : processes)
            if (p.arrivalTime > max)
                max = p.arrivalTime;
        return max;
    }

    public static int clamp(int burstTime, int q) {
        return Math.min(burstTime, q);
    }
}
