package com.lengsword.java.os.state.model;

/**
 * @author LengSword
 */

public enum ProcessStateEnum {
    /**
     * 就绪态
     */
    READY,
    /**
     * 阻塞态
     */
    BLOCKED,
    /**
     * 运行态
     */
    RUNNING,
    /**
     * 完成态
     */
    FINISHED;
}
