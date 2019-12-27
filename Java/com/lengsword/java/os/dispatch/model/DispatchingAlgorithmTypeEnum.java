package com.lengsword.java.os.dispatch.model;

/**
 * @author LengSword
 */
public enum DispatchingAlgorithmTypeEnum {
    /**
     * 先来先服务
     * First Come First Served, FCFS
     */
    FCFS,
    /**
     * 最短作业(进程)优先
     * Shortest Job First, SJF
     */
    SJF,
    /**
     * 最短剩余时间优先
     * Shortest Remaining Time First, SRTF
     */
    SRTF,
    /**
     * 最高优先权优先
     * Highest Priority First, HPF
     */
    HPF,
    /**
     * 最高响应比优先
     * Highest Response Ratio First, HRRF
     */
    HRRF,
    /**
     * 轮转
     * Round-Robin, RR
     */
    RR;
}
