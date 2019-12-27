package com.lengsword.java.os.dispatch.util;

import com.lengsword.java.os.dispatch.model.Process;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * @author LengSword
 */
public class ProcessUtils {
    public static List<Process> readProcessesFromFile(String filepath) {
        List<Process> processList = new ArrayList<>();
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(filepath));
            while (scanner.hasNext()) {
                char processId = scanner.next().charAt(0);
                int arrivalTime = scanner.nextInt();
                int serviceTime = scanner.nextInt();
                int priority = scanner.nextInt();
                Process process = new Process(processId, arrivalTime, serviceTime, priority);
                process.setRemainingServiceTime(serviceTime);
                processList.add(process);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (scanner != null) {
            scanner.close();
        }
        return processList;
    }

    /**
     * 计算周转时间
     *
     * @param process process
     */
    public static void calculateWholeTime(Process process) {
        process.setTurnaroundTime(process.getFinishedTime() - process.getArrivalTime());
    }

    /**
     * 计算带权周转时间
     *
     * @param process process
     */
    public static void calculateWeightWholeTime(Process process) {
        process.setTurnaroundTimeWithWeight(process.getTurnaroundTime() / (double) process.getServiceTime());
    }

    public static void sortByProcessId(List<Process> processList) {
        processList.sort(new Comparator<Process>() {
            @Override
            public int compare(Process proc1, Process proc2) {
                return Character.compare(proc1.getProcessId(), proc2.getProcessId());
            }
        });
    }

    public static void sortByArrivalTime(List<Process> processList) {
        processList.sort(new Comparator<Process>() {
            @Override
            public int compare(Process proc1, Process proc2) {
                if (proc1.getArrivalTime() > proc2.getArrivalTime()) {
                    return 1;
                } else if (proc1.getArrivalTime() == proc2.getArrivalTime()) {
                    // 到达时间相同时, 按服务时间排序
                    return Integer.compare(proc1.getServiceTime(), proc2.getServiceTime());
                } else {
                    return -1;
                }
            }
        });
    }

    public static void sortByServiceTime(List<Process> processList) {
        processList.sort(new Comparator<Process>() {
            @Override
            public int compare(Process proc1, Process proc2) {
                return Integer.compare(proc1.getServiceTime(), proc2.getServiceTime());
            }
        });
    }

    public static void sortByRemainingServiceTime(List<Process> processList) {
        processList.sort(new Comparator<Process>() {
            @Override
            public int compare(Process proc1, Process proc2) {
                return Integer.compare(proc1.getRemainingServiceTime(), proc2.getRemainingServiceTime());
            }
        });
    }

    public static void sortByPriority(List<Process> processList) {
        processList.sort(new Comparator<Process>() {
            @Override
            public int compare(Process proc1, Process proc2) {
                return Integer.compare(proc1.getPriority(), proc2.getPriority());
            }
        });
    }
}
