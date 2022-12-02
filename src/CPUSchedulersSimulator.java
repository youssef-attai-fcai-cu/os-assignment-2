public class CPUSchedulersSimulator {
    public static void main(String[] args) {
        CPUScheduler sjfScheduler = new SJFScheduler();
        CPUScheduler rrScheduler = new RoundRobinScheduler();
        CPUScheduler pScheduler = new PriorityScheduler();
        CPUScheduler agScheduler = new AGScheduler();
    }
}
