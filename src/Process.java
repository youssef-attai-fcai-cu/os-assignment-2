public class Process {
    public final String name;
    public final int arrivalTime;
    public final int burstTime;
    public int priority;
    public int remainingTime;
    public int waitingTime;
    public int turnAroundTime;
    public int ageWait = 0;

    public Process(String name, int arrivalTime, int burstTime, int priority) {
        this.name = name;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
        this.waitingTime = 0;
        this.turnAroundTime = 0;
        this.remainingTime = this.burstTime;
    }

    public void age(int agingThreshold) {
        if (this.priority > 0 && this.ageWait >= agingThreshold) {
            this.priority--;
            this.ageWait = 0;
        }
    }
}
