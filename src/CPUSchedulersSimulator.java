import java.util.List;

public class CPUSchedulersSimulator {
    public static void main(String[] args) {
        CPUScheduler rrScheduler = new RoundRobinScheduler(10, 1);

        rrScheduler.start(List.of(
                new Process("P1", 0, 10, 0),
                new Process("P2", 0, 29, 0),
                new Process("P3", 0, 3, 0),
                new Process("P4", 0, 7, 0),
                new Process("P5", 0, 12, 0)
        ));

        for (Interval interval : rrScheduler.executionOrder) {
            System.out.println(interval.getStart() + "|" + interval.getProcessName() + "|" + interval.getEnd());
        }
    }
}
