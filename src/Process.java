public class Process {
    public final String name;
    public final int arrivalTime;
    public final int burstTime;
    public final int priority;
    public int remainingTime;
    public int waitingTime;
    public int turnAroundTime;

    public Process(String name, int arrivalTime, int burstTime, int priority) {
        this.name = name;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;

        this.remainingTime = this.burstTime;
    }
}
