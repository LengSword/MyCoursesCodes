package com.lengsword.java.os.dispatch.model;

public class Process {
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
     * 开始时间
     */
    private int startingTime = -1;
    /**
     * 等待时间 = 开始时间 - 到达时间
     */
    private int waitingTime;
    /**
     * 完成时间
     */
    private int finishedTime;
    /**
     * 周转时间 = 完成时间 - 到达时间
     */
    private int turnaroundTime;
    /**
     * 带权周转时间 = 周转时间 / 服务时间
     */
    private double turnaroundTimeWithWeight;
    /**
     * 响应比 = 等待时间 / 服务时间 + 1
     */
    private double responseRatio;
    /**
     * 进程状态
     */
    private ProcessStateEnum state = com.lengsword.java.os.dispatch.model.ProcessStateEnum.WAITING;

    public Process(char processId, int arrivalTime, int serviceTime) {
        super();
        this.processId = processId;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
    }

    public Process(char processId, int arrivalTime, int serviceTime, int priority) {
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

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getStartingTime() {
        return startingTime;
    }

    public void setStartingTime(int startingTime) {
        this.startingTime = startingTime;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public int getFinishedTime() {
        return finishedTime;
    }

    public void setFinishedTime(int finishedTime) {
        this.finishedTime = finishedTime;
    }

    public int getTurnaroundTime() {
        return turnaroundTime;
    }

    public void setTurnaroundTime(int turnaroundTime) {
        this.turnaroundTime = turnaroundTime;
    }

    public double getTurnaroundTimeWithWeight() {
        return turnaroundTimeWithWeight;
    }

    public void setTurnaroundTimeWithWeight(double turnaroundTimeWithWeight) {
        this.turnaroundTimeWithWeight = turnaroundTimeWithWeight;
    }

    public double getResponseRatio() {
        return responseRatio;
    }

    public void setResponseRatio(double responseRatio) {
        this.responseRatio = responseRatio;
    }

    public ProcessStateEnum getState() {
        return state;
    }

    public void setState(ProcessStateEnum state) {
        this.state = state;
    }

    private String stateToString(ProcessStateEnum state) {
        switch (state) {
            case WAITING:
                return "WAITING";
            case RUNNING:
                return "RUNNING";
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
                ", 到达时间=" + arrivalTime +
                ", 服务时间=" + serviceTime +
                ", 优先权=" + priority +
                ", 状态=" + stateToString(state) +
                ", 剩余服务时间=" + remainingServiceTime +
                ", 开始时间=" + startingTime +
                ", 等待时间=" + waitingTime +
                ", 完成时间=" + finishedTime +
                ", 周转时间=" + turnaroundTime +
                ", 带权周转时间=" + turnaroundTimeWithWeight +
                ", 响应比=" + responseRatio +
                '}';
    }
}
