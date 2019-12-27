package com.lengsword.java.os.state;

import com.lengsword.java.os.state.model.PCB;
import com.lengsword.java.os.state.model.ProcessStateEnum;
import com.lengsword.java.os.state.util.PCBUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

/**
 * @author LengSword
 */
public class ProcessStateTransition {
    /**
     * 进程列表
     */
    private List<PCB> processList;
    /**
     * 就绪队列
     */
    private Queue<PCB> readyQueue;
    /**
     * 运行队列
     */
    private Queue<PCB> runningQueue;
    /**
     * 阻塞队列
     */
    private Queue<PCB> blockedQueue;
    /**
     * 完成队列
     */
    private Queue<PCB> finishedQueue;
    /**
     * 时间片
     */
    private int timeSlice;

    public ProcessStateTransition(List<PCB> processList) {
        this.processList = processList;
        this.readyQueue = new LinkedList<>(processList);
        this.blockedQueue = new LinkedList<>();
        this.runningQueue = new LinkedList<>();
        this.finishedQueue = new LinkedList<>();
    }

    public List<PCB> getProcessList() {
        return processList;
    }

    public void setTimeSlice(int timeSlice) {
        this.timeSlice = timeSlice;
    }

    public void execute() {
        runningQueue.offer(readyQueue.poll());
        PCB currentProcess = runningQueue.poll();
        if (currentProcess == null) {
            return;
        }
        // 执行第一个进程
        int currentTime = 0;
        currentTime = executeProcessByTimeSlice(currentProcess, 0);

        while (!readyQueue.isEmpty() || !runningQueue.isEmpty() || currentProcess.getRemainingServiceTime() > 0) {
            // 把所有到达时间达到的进程加入运行队列
            while (!readyQueue.isEmpty()) {
                if (readyQueue.peek().getArrivalTime() <= currentTime) {
                    runningQueue.offer(readyQueue.poll());
                } else {
                    break;
                }
            }
            if (currentProcess.getRemainingServiceTime() > 0) {
                runningQueue.offer(currentProcess);
            }
            // 按照优先权降序排序
            PCBUtils.sortByPriority((LinkedList<PCB>) runningQueue);
            // 若运行队列中最高优先级的进程与当前进程不一致 (运行态->就绪态)
            if (((LinkedList<PCB>) runningQueue).getFirst() != currentProcess
                    && currentProcess.getState() != ProcessStateEnum.FINISHED) {
                currentProcess.setState(ProcessStateEnum.READY);
            }
            // 运行队列不为空时
            if (!runningQueue.isEmpty()) {
                currentProcess = runningQueue.poll();
                currentTime = executeProcessByTimeSlice(currentProcess, currentTime);
            } else {
                // 当前没有进程执行,但还有进程未到达,时间直接跳转到到达时间
                currentTime = readyQueue.peek().getArrivalTime();
            }
        }
        PCBUtils.sortByProcessId((LinkedList<PCB>) finishedQueue);
    }

    /**
     * @param currentProcess current process
     * @param currentTime    current time
     *
     * @return the current time after executed the process
     */
    private int executeProcess(PCB currentProcess, int currentTime) {
        currentProcess.setState(ProcessStateEnum.RUNNING);
        // 当前进程在这个时间片内能执行完毕
        int nextTime = currentTime + currentProcess.getRemainingServiceTime();
        currentProcess.setFinishedTime(nextTime);
        currentProcess.setRemainingServiceTime(0);
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
    private int executeProcessByTimeSlice(PCB currentProcess, int currentTime) {
        int nextTime;
        boolean isIoEventOccur = false;
        Random randomInputOutputEvent = new Random();
        // 概率为20%的伪随机I/O事件
        if (randomInputOutputEvent.nextInt(5) == 0) {
            isIoEventOccur = true;
            currentProcess.setState(ProcessStateEnum.BLOCKED);
            nextTime = currentTime + timeSlice;
        } else {
            if (currentProcess.getRemainingServiceTime() - timeSlice <= 0) {
                nextTime = executeProcess(currentProcess, currentTime);
            } else {
                currentProcess.setState(ProcessStateEnum.RUNNING);
                // 不能执行完毕
                nextTime = currentTime + timeSlice;
                currentProcess.setRemainingServiceTime(currentProcess.getRemainingServiceTime() - timeSlice);
            }
        }
        showResult(processList, "processList", currentTime, isIoEventOccur);
        return nextTime;
    }

    public void showResult(List<PCB> list, String name, int currentTime, boolean isIoEventOccur) {
        System.out.println(String.format("====================%s=======================", name));
        System.out.println(String.format("CPU时间: %d", currentTime + 1));
        if (isIoEventOccur) {
            System.out.println("当前进程发生了I/O操作->被阻塞");
        }
        System.out.print("进程名  ");
        System.out.print("优先权   ");
        System.out.print("到达时间   ");
        System.out.print("服务时间   ");
        System.out.print("剩余服务时间   ");
        System.out.print("完成时间   ");
        System.out.println("状态   ");
        for (PCB pcb : list) {
            System.out.print(String.format("进程%-6s", pcb.getProcessId()));
            System.out.print(String.format("%-8d", pcb.getPriority()));
            System.out.print(String.format("%-8d", pcb.getArrivalTime()));
            System.out.print(String.format("%-12d", pcb.getServiceTime()));
            System.out.print(String.format("%-10d", pcb.getRemainingServiceTime()));
            System.out.print(String.format("%-8d", pcb.getFinishedTime()));
            System.out.println(String.format("%-8s", pcb.getState()));
        }
    }
}
