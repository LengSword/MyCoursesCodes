package com.lengsword.java.os.state;

import com.lengsword.java.os.state.model.PCB;
import com.lengsword.java.os.state.util.PCBUtils;

import java.util.List;

public class ProcessStateTransitionTest {
    public static void main(String[] args) {
        List<PCB> pcbList = PCBUtils.readProcessesFromFile("test.txt");

        ProcessStateTransition processStateTransition = new ProcessStateTransition(pcbList);
        PCBUtils.sortByArrivalTime(processStateTransition.getProcessList());
        processStateTransition.setTimeSlice(1);
        processStateTransition.execute();
    }
}
