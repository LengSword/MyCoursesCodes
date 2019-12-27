package com.lengsword.java.os.dispatch.test;

import com.lengsword.java.os.dispatch.RR;
import com.lengsword.java.os.dispatch.model.Process;
import com.lengsword.java.os.dispatch.util.ProcessUtils;

import java.util.List;

public class RRTest {
    private static List<Process> processList = ProcessUtils.readProcessesFromFile("test.txt");

    public static void main(String[] args) {
        RR rr = new RR(processList, 1);
        rr.start();
    }
}