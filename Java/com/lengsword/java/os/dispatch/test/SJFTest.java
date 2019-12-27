package com.lengsword.java.os.dispatch.test;

import com.lengsword.java.os.dispatch.SJF;
import com.lengsword.java.os.dispatch.model.Process;
import com.lengsword.java.os.dispatch.util.ProcessUtils;

import java.util.List;

public class SJFTest {
    private static List<Process> processList = ProcessUtils.readProcessesFromFile("test.txt");

    public static void main(String[] args) {
        SJF sjf = new SJF(processList);
        sjf.start();
    }
}