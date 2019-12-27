package com.lengsword.java.os.dispatch;

import com.lengsword.java.os.dispatch.model.DispatchingAlgorithmTypeEnum;
import com.lengsword.java.os.dispatch.model.Process;
import com.lengsword.java.os.dispatch.model.ProcessControlBlock;
import com.lengsword.java.os.dispatch.util.ProcessUtils;

import java.util.List;

public class RR {
    private ProcessControlBlock processControlBlock;
    private int timeSlice;

    public RR(List<Process> processList, int timeSlice) {
        this.processControlBlock = new ProcessControlBlock(processList);
        this.timeSlice = timeSlice;
    }

    public void start() {
        ProcessUtils.sortByArrivalTime(processControlBlock.getProcessList());
        System.out.println("====================RR====================");
        processControlBlock.setTimeSlice(timeSlice);
        processControlBlock.run(DispatchingAlgorithmTypeEnum.RR);
        processControlBlock.showResult();
    }
}
