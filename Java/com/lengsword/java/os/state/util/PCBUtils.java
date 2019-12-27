package com.lengsword.java.os.state.util;

import com.lengsword.java.os.state.model.PCB;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

/**
 * @author LengSword
 */
public class PCBUtils {
    public static List<PCB> readProcessesFromFile(String filepath) {
        List<PCB> processList = new ArrayList<>();
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(filepath));
            while (scanner.hasNext()) {
                char processId = scanner.next().charAt(0);
                int arrivalTime = scanner.nextInt();
                int serviceTime = scanner.nextInt();
                int priority = scanner.nextInt();
                PCB process = new PCB(processId, arrivalTime, serviceTime, priority);
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

    public static void sortByProcessId(List<PCB> processList) {
        processList.sort(new Comparator<PCB>() {
            @Override
            public int compare(PCB proc1, PCB proc2) {
                return Character.compare(proc1.getProcessId(), proc2.getProcessId());
            }
        });
    }

    public static void sortByArrivalTime(List<PCB> processList) {
        processList.sort(new Comparator<PCB>() {
            @Override
            public int compare(PCB proc1, PCB proc2) {
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

    public static void sortByServiceTime(List<PCB> processList) {
        processList.sort(new Comparator<PCB>() {
            @Override
            public int compare(PCB proc1, PCB proc2) {
                return Integer.compare(proc1.getServiceTime(), proc2.getServiceTime());
            }
        });
    }

    public static void sortByRemainingServiceTime(List<PCB> processList) {
        processList.sort(new Comparator<PCB>() {
            @Override
            public int compare(PCB proc1, PCB proc2) {
                return Integer.compare(proc1.getRemainingServiceTime(), proc2.getRemainingServiceTime());
            }
        });
    }

    public static void sortByPriority(List<PCB> processList) {
        processList.sort(new Comparator<PCB>() {
            @Override
            public int compare(PCB proc1, PCB proc2) {
                return Integer.compare(proc2.getPriority(), proc1.getPriority());
            }
        });
    }
}
