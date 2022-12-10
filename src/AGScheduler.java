import java.util.ArrayList;
import java.util.List;

public class AGScheduler {
    List<Process> readyQueue = new ArrayList<>();
    List<Interval> executionOrder = new ArrayList<>();
    int currentTime = 0;
    Process running = null;
    int finished = 0;
    AGMode mode = AGMode.FCFS;

    public void start(List<Process> processes) {
        while (finished != processes.size()) {
//            System.out.println("====================");
//            System.out.println("current time = " + currentTime);
            step(processes);
            currentTime++;
        }
    }

    private void step(List<Process> processes) {
        for (Process p : processes) {
//        Did any process arrive?
            if (p.arrivalTime == currentTime) {
//            If so, add it to the ready queue
                readyQueue.add(p);
//                LOG
//                System.out.println(p.name + " has arrived");
            }
        }
//        LOG
//        printReadyQueue();

//    If there is no running process
        if (running == null) {
//            LOG
//            System.out.println("No processes are running");

//        Run the next process in ready queue
            running = readyQueue.get(0);
            readyQueue.remove(running);

            if (executionOrder.size() == 0) {
                executionOrder.add(new Interval(running));
                executionOrder.get(0).setStart(currentTime);
            } else {
                if (running != executionOrder.get(executionOrder.size() - 1).process) {
                    executionOrder.add(new Interval(running));
                    executionOrder.get(executionOrder.size() - 1).setStart(currentTime - 1);
                }
            }

//            LOG
//            System.out.println(running.name + " started running");
//            printReadyQueue();

//        Reset AG mode to First come, first served
            mode = AGMode.FCFS;

//            LOG
//            System.out.println("AG Mode switched to " + mode);

//        Update remaining time and varQuantum if time is past 0
            if (currentTime > 0) {
                running.remainingTime--;

//                LOG
//                System.out.println(running.name + "'s remaining time is now " + running.remainingTime);

                running.varQuantum++;

//                LOG
//                System.out.println(running.name + "'s Quantum is " + running.quantum);
//                System.out.println(running.name + "'s varQuantum is now " + running.varQuantum);

//            If running process finished, skip AG mode switching and go to next iteration
                if (running.remainingTime == 0) {
                    running.turnAroundTime = currentTime - running.arrivalTime;
                    running.waitingTime = running.turnAroundTime - running.burstTime;

                    running.quantum = 0;
                    finished++;

//                LOG
//                    System.out.println(running.name + " finished");

//                Reset running
                    running = null;

                    return;
                }
            }
        }
//    Otherwise, if there is an already running process
        else {
            if (executionOrder.size() > 0 && running != executionOrder.get(executionOrder.size() - 1).process) {
                executionOrder.add(new Interval(running));
                executionOrder.get(executionOrder.size() - 1).setStart(currentTime - 1);
            }

//            LOG
//            System.out.println(running.name + " is running");

//        Update remaining time and varQuantum if time is past 0
            if (currentTime > 0) {
                running.remainingTime--;

//                LOG
//                System.out.println(running.name + "'s remaining time is now " + running.remainingTime);

                running.varQuantum++;

//                LOG
//                System.out.println(running.name + "'s Quantum is " + running.quantum);
//                System.out.println(running.name + "'s varQuantum is now " + running.varQuantum);

//            If running process finished
                if (running.remainingTime == 0) {
                    running.turnAroundTime = currentTime - running.arrivalTime;
                    running.waitingTime = running.turnAroundTime - running.burstTime;

//                Skip AG mode switching and go to next iteration
                    running.quantum = 0;
                    finished++;

//                    LOG
//                    System.out.println(running.name + " finished");

//                Reset running
                    running = null;
                    executionOrder.get(executionOrder.size() - 1).setEnd(currentTime);

                    return;
                }
            }

//        Did varQuantum reach 25% of quantum?
            if (running.varQuantum == (int) Math.ceil(running.quantum / 4.0f)) {
//                LOG
//                System.out.println(running.name + " varQuantum reached " + running.varQuantum + " which is ceil(25% of " + running.quantum + ")");

//            Switch AG mode to Non-preemptive priority
                mode = AGMode.NPP;

//                LOG
//                System.out.println("AG Mode switched to " + mode);

//            Push running process to back of ready queue
                readyQueue.add(running);

//                LOG
//                printReadyQueue();

//            Get next process (with the highest priority from ready queue)
                Process next = readyQueue.get(getHighestPriorityIndex());

//                LOG
//                System.out.println("Next is " + next.name);

//            If current process does NOT have the highest priority
                if (next != running) {

//                Adjust quantum of running process
                    running.quantum += (int) Math.ceil((running.quantum - running.varQuantum) / 2.0f);
                    running.varQuantum = 0;

                    running = next;

//                LOG
//                    printReadyQueue();

                    executionOrder.get(executionOrder.size() - 1).setEnd(currentTime);
                }

//            Remove running process from ready queue
                readyQueue.remove(running);
            }
//        Or did varQuantum reach 50% of quantum?
            else if (running.varQuantum == (int) Math.ceil(running.quantum / 2.0f)) {
//                LOG
//                System.out.println(running.name + " varQuantum reached " + running.varQuantum + " which is ceil(50% of " + running.quantum + ")");

//            Switch AG mode to Preemptive shortest job first
                mode = AGMode.PSJF;

//                LOG
//                System.out.println("AG Mode switched to " + mode);

//                Push running process to back of ready queue
                readyQueue.add(running);

//                LOG
//                printReadyQueue();

//            Get next process (with the highest priority from ready queue)
                Process next = readyQueue.get(getShortestJobIndex());

//                LOG
//                System.out.println("Next is " + next.name);

//            If current process does NOT have the highest priority
                if (next != running) {

//                Adjust quantum of running process
                    running.quantum += (running.quantum - running.varQuantum);
                    running.varQuantum = 0;

                    running = next;

//                LOG
//                    printReadyQueue();

                    executionOrder.get(executionOrder.size() - 1).setEnd(currentTime);
                }

//            Remove running process from ready queue
                readyQueue.remove(running);
            }
        }
    }

    private void printReadyQueue() {
        System.out.print("Ready queue: ");
        for (Process p : readyQueue)
            System.out.print(p.name + " ");
        System.out.println();
    }

    private int getHighestPriorityIndex() {
        int highestPriorityIdx = 0;
        for (int i = 0; i < readyQueue.size(); i++)
            if (readyQueue.get(i).priority < readyQueue.get(highestPriorityIdx).priority)
                highestPriorityIdx = i;
        return highestPriorityIdx;
    }

    private int getShortestJobIndex() {
        int shortestJobIdx = 0;
        for (int i = 0; i < readyQueue.size(); i++)
            if (readyQueue.get(i).remainingTime < readyQueue.get(shortestJobIdx).remainingTime)
                shortestJobIdx = i;
        return shortestJobIdx;
    }
}
