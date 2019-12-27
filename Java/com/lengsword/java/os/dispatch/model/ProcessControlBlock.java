package com.lengsword.java.os.dispatch.model;

import com.lengsword.java.os.dispatch.util.ProcessUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * PCB
 *
 * @author LengSword
 */
public class ProcessControlBlock {
    /**
     * 进程数
     */
    private int processCount;
    /**
     * 进程列表
     */
    private List<Process> processList;
    /**
     * 未到达进程队列
     */
    private Queue<Process> unreachQueue;
    /**
     * 等待执行队列
     */
    private Queue<Process> readyQueue;
    /**
     * 执行完成队列
     */
    private Queue<Process> finishedQueue;
    /**
     * 总周转时间
     */
    private int totalTurnaroundTime;
    /**
     * 总带权周转时间
     */
    private double totalTurnaroundTimeWithWeight;
    /**
     * 时间片
     */
    private int timeSlice;
    /**
     * CPU时间(全局时间)
     */
    private int cpuTime;

    public ProcessControlBlock(List<Process> processList) {
        this.processCount = processList.size();
        this.processList = processList;
        this.unreachQueue = new LinkedList<>(processList);
        this.readyQueue = new LinkedList<>();
        this.finishedQueue = new LinkedList<>();
        this.cpuTime = 0;
    }

    public List<Process> getProcessList() {
        return processList;
    }

    public Queue<Process> getUnreachQueue() {
        return unreachQueue;
    }

    public Queue<Process> getReadyQueue() {
        return readyQueue;
    }

    public Queue<Process> getFinishedQueue() {
        return finishedQueue;
    }

    public int getTimeSlice() {
        return timeSlice;
    }

    public void setTimeSlice(int timeSlice) {
        this.timeSlice = timeSlice;
    }

    /**
     * TODO 重构 + currentCPUTime
     *
     * @param type dispatching algorithms type
     */
    public void run(DispatchingAlgorithmTypeEnum type) {
        readyQueue.offer(unreachQueue.poll());
        Process currentProcess = readyQueue.poll();
        if (currentProcess == null) {
            return;
        }
        // 执行第一个进程
        int currentTime = 0;
        currentProcess.setStartingTime(currentTime);
        if (type == DispatchingAlgorithmTypeEnum.RR) {
            currentTime = executeProcessByTimeSlice(currentProcess, 0);
        } else {
            currentTime = executeProcess(currentProcess, 0);
        }

        while (!readyQueue.isEmpty() || !unreachQueue.isEmpty() || currentProcess.getRemainingServiceTime() > 0) {
            // 把所有到达时间达到的进程加入运行队列头部
            while (!unreachQueue.isEmpty()) {
                if (unreachQueue.peek().getArrivalTime() <= currentTime) {
                    readyQueue.offer(unreachQueue.poll());
                } else {
                    break;
                }
            }
            if (currentProcess.getRemainingServiceTime() > 0) {
                readyQueue.offer(currentProcess);
            }
            // 运行队列不为空时
            if (!readyQueue.isEmpty()) {
                if (type == DispatchingAlgorithmTypeEnum.SJF) {
                    ProcessUtils.sortByServiceTime((LinkedList<Process>) readyQueue);
                }
                currentProcess = readyQueue.poll();
                // 若未设置开始时间则进行赋值
                if (currentProcess.getStartingTime() == -1) {
                    currentProcess.setStartingTime(currentTime);
                }
                if (type == DispatchingAlgorithmTypeEnum.RR) {
                    currentTime = executeProcessByTimeSlice(currentProcess, currentTime);
                } else {
                    currentTime = executeProcess(currentProcess, currentTime);
                }
            } else {
                // 当前没有进程执行,但还有进程未到达,时间直接跳转到到达时间
                currentTime = unreachQueue.peek().getArrivalTime();
            }
        }
        ProcessUtils.sortByProcessId((LinkedList<Process>) finishedQueue);
    }

    /**
     * @param currentProcess current process
     * @param currentTime    current time
     *
     * @return the current time after executed the process
     */
    private int executeProcess(Process currentProcess, int currentTime) {
        currentProcess.setState(ProcessStateEnum.RUNNING);
        // 当前进程在这个时间片内能执行完毕
        int nextTime = currentTime + currentProcess.getRemainingServiceTime();
        showProcessRunningMessage(currentTime, nextTime, currentProcess.getProcessId());
        currentProcess.setFinishedTime(nextTime);
        currentProcess.setRemainingServiceTime(0);
        // 对周转时间等进行计算
        ProcessUtils.calculateWholeTime(currentProcess);
        ProcessUtils.calculateWeightWholeTime(currentProcess);
        totalTurnaroundTime += currentProcess.getTurnaroundTime();
        totalTurnaroundTimeWithWeight += currentProcess.getTurnaroundTimeWithWeight();
        finishedQueue.add(currentProcess);
        currentProcess.setState(ProcessStateEnum.FINISHED);
        return nextTime;
    }

    /**
     * @param currentProcess current process
     * @param currentTime    current time
     *
     * @return the current time after executed the process
     */
    private int executeProcessByTimeSlice(Process currentProcess, int currentTime) {
        int nextTime;
        if (currentProcess.getRemainingServiceTime() - timeSlice <= 0) {
            nextTime = executeProcess(currentProcess, currentTime);
        } else {
            currentProcess.setState(ProcessStateEnum.RUNNING);
            // 不能执行完毕
            nextTime = currentTime + timeSlice;
            showProcessRunningMessage(currentTime, nextTime, currentProcess.getProcessId());
            currentProcess.setRemainingServiceTime(currentProcess.getRemainingServiceTime() - timeSlice);
        }
        return nextTime;
    }

    private void showProcessRunningMessage(int startTime, int endTime, char name) {
        System.out.println(startTime + "~" + endTime + ":[进程" + name + "]运行");
    }

    public void showResult() {
        System.out.println("===========================================");
        System.out.println(String.format("CPU时间: %d", cpuTime + 1));
        System.out.print("进程名\t");
        System.out.print("优先权\t");
        System.out.print("到达时间\t");
        System.out.print("服务时间\t");
        System.out.print("开始时间\t");
        System.out.print("等待时间\t");
        System.out.print("完成时间\t");
        System.out.print("周转时间\t");
        System.out.print("带权周转时间\t");
        System.out.println("响应比\t");
        for (Process process : finishedQueue) {
            System.out.print("进程" + process.getProcessId() + "\t");
            System.out.print("\t" + process.getPriority() + "\t");
            System.out.print("\t" + process.getArrivalTime() + "\t");
            System.out.print("\t" + process.getServiceTime() + "\t");
            System.out.print("\t" + process.getStartingTime() + "\t");
            System.out.print("\t" + process.getWaitingTime() + "\t");
            System.out.print("\t" + process.getFinishedTime() + "\t");
            System.out.print("\t" + process.getTurnaroundTime() + "\t");
            System.out.print(String.format("\t%4.2f\t", process.getTurnaroundTimeWithWeight()));
            System.out.println(process.getResponseRatio() + "\t");
        }
        System.out.println(String.format("平均周转时间：%4.2f", totalTurnaroundTime / (double) processCount));
        System.out.println(String.format("平均带权周转时间：%4.2f", totalTurnaroundTimeWithWeight / (double) processCount));
    }
}
