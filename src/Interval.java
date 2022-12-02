public class Interval {
    private final Process process;
    private int start;
    private int end;

    public Interval(Process process) {
        this.process = process;
    }

    String getProcessName() {
        return this.process.name;
    }

    public void setEnd(int endTime) {
        this.end = endTime;
    }

    public int getEnd() {
        return end;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public boolean within(int time) {
        return time >= start && time <= end;
    }
}
