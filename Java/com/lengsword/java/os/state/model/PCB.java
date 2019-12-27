package com.lengsword.java.os.state.model;

import com.lengsword.java.os.state.model.ProcessStateEnum;

/**
 * @author LengSword
 */
public class PCB {
    /**
     * 进程ID
     */
    private char processId;
    /**
     * 优先权 -数字越大, 优先权越高
     */
    private int priority;
    /**
     * 到达时间
     */
    private int arrivalTime;
    /**
     * 服务时间
     */
    private int serviceTime;
    /**
     * 剩余服务时间
     */
    private int remainingServiceTime;
    /**
     * 完成时间
     */
    private int finishedTime;
    /**
     * 进程状态
     */
    private ProcessStateEnum state = ProcessStateEnum.READY;

    public PCB(char processId, int arrivalTime, int serviceTime) {
        super();
        this.processId = processId;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
    }

    public PCB(char processId, int arrivalTime, int serviceTime, int priority) {
        super();
        this.processId = processId;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
        this.priority = priority;
    }

    public char getProcessId() {
        return processId;
    }

    public void setProcessId(char processId) {
        this.processId = processId;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(int serviceTime) {
        this.serviceTime = serviceTime;
    }

    public int getRemainingServiceTime() {
        return remainingServiceTime;
    }

    public void setRemainingServiceTime(int remainingServiceTime) {
        this.remainingServiceTime = remainingServiceTime;
    }

    public int getFinishedTime() {
        return finishedTime;
    }

    public void setFinishedTime(int finishedTime) {
        this.finishedTime = finishedTime;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public ProcessStateEnum getState() {
        return state;
    }

    public void setState(ProcessStateEnum state) {
        this.state = state;
    }

    private String stateToString(ProcessStateEnum state) {
        switch (state) {
            case READY:
                return "READY";
            case RUNNING:
                return "RUNNING";
            case BLOCKED:
                return "BLOCKED";
            case FINISHED:
                return "FINISHED";
            default:
                return "UNKNOWN";
        }
    }

    @Override
    public String toString() {
        return "进程{" +
                "进程ID='" + processId + '\'' +
                ", 优先权=" + priority +
                ", 到达时间=" + arrivalTime +
                ", 服务时间=" + serviceTime +
                ", 剩余服务时间=" + remainingServiceTime +
                ", 状态=" + stateToString(state) +
                '}';
    }
}
