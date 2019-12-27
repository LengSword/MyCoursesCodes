package com.lengsword.java.os.dispatch;

import com.lengsword.java.os.dispatch.model.DispatchingAlgorithmTypeEnum;
import com.lengsword.java.os.dispatch.model.Process;
import com.lengsword.java.os.dispatch.model.ProcessControlBlock;
import com.lengsword.java.os.dispatch.util.ProcessUtils;

import java.util.List;

/**
 * @author LengSword
 */
public class FCFS {
    private ProcessControlBlock processControlBlock;

    public FCFS(List<Process> processList) {
        this.processControlBlock = new ProcessControlBlock(processList);
    }

    public void start() {
        ProcessUtils.sortByArrivalTime(processControlBlock.getProcessList());
        System.out.println("====================FCFS====================");
        processControlBlock.run(DispatchingAlgorithmTypeEnum.FCFS);
        processControlBlock.showResult();
    }

}
