package com.lengsword.java.os.dispatch.test;

import com.lengsword.java.os.dispatch.FCFS;
import com.lengsword.java.os.dispatch.model.Process;
import com.lengsword.java.os.dispatch.util.ProcessUtils;

import java.util.List;

public class FCFSTest {
    private static List<Process> processList = ProcessUtils.readProcessesFromFile("test.txt");

    public static void main(String[] args) {
        FCFS fcfs = new FCFS(processList);
        fcfs.start();
    }
}