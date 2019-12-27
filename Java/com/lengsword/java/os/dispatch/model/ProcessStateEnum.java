package com.lengsword.java.os.dispatch.model;

/**
 * @author LengSword
 */

public enum ProcessStateEnum {
    /**
     * 就绪态
     */
    READY,
    /**
     * 等待态
     */
    WAITING,
    /**
     * 运行态
     */
    RUNNING,
    /**
     * 完成态
     */
    FINISHED;
}
