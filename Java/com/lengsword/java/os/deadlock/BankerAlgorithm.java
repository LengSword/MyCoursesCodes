package com.lengsword.java.os.deadlock;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class BankerAlgorithm {
    /**
     * 数据输入模式
     * true: 标准输入
     * false: 从文件输入
     */
    private boolean isStandardInput;
    /**
     * 输入
     */
    private Scanner dataScanner;
    /**
     * 进程数
     */
    private int processCount;
    /**
     * 资源种类数
     */
    private int resourceTypeCount;
    /**
     * 需求矩阵
     */
    private int[][] need;
    /**
     * 最大需求矩阵
     */
    private int[][] maxNeed;
    /**
     * 已分配矩阵
     */
    private int[][] allocation;
    /**
     * 可用资源向量
     */
    private int[] available;
    /**
     * 工作向量
     * 用于安全性检查
     * 初始值=available
     */
    private int[] work;
    /**
     * 记录进程是否已完成的向量
     * 用于安全性检查
     */
    private boolean[] finish;
    /**
     * 请求资源矩阵
     */
    private int[][] request;
    /**
     * 需请求资源的进程ID
     */
    private int requestProcessId;

    public Scanner getDataScanner() {
        return dataScanner;
    }

    public BankerAlgorithm() {
    }

    public BankerAlgorithm(boolean isStandardInput) {
        this.isStandardInput = isStandardInput;
    }

    public static void main(String[] args) {
        BankerAlgorithm banker = new BankerAlgorithm(false);
        String command;
        banker.init("banker.txt");
        do {
            banker.bankerAlgorithm();
            System.out.print("你是否还要进行请求资源?(y/n)");
            command = banker.getDataScanner().next();
            if ("n".equals(command)) {
                break;
            }
        } while ("y".equals(command));
    }

    public void init(String filename) {
        try {
            if (isStandardInput) {
                dataScanner = new Scanner(System.in);
            } else {
                dataScanner = new Scanner(new File(filename));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (isStandardInput) {
            System.out.print("请输入进程总数: ");
        }
        processCount = dataScanner.nextInt();
        if (isStandardInput) {
            System.out.print("请输入资源种类数: ");
        }
        resourceTypeCount = dataScanner.nextInt();
        // 初始化数组
        available = new int[resourceTypeCount];
        maxNeed = new int[processCount][resourceTypeCount];
        allocation = new int[processCount][resourceTypeCount];
        need = new int[processCount][resourceTypeCount];

        if (isStandardInput) {
            System.out.printf("请输入当前可用资源数向量Available(%d):\n", resourceTypeCount);
        }
        for (int i = 0; i < resourceTypeCount; i++) {
            available[i] = dataScanner.nextInt();
        }
        if (isStandardInput) {
            System.out.printf("请输入最大需求矩阵Max(%d*%d):\n", processCount, resourceTypeCount);
        }
        for (int i = 0; i < processCount; i++) {
            for (int j = 0; j < resourceTypeCount; j++) {
                maxNeed[i][j] = dataScanner.nextInt();
            }
        }
        if (isStandardInput) {
            System.out.printf("请输入分配矩阵Allocation(%d*%d):\n", processCount, resourceTypeCount);
        }
        for (int i = 0; i < processCount; i++) {
            for (int j = 0; j < resourceTypeCount; j++) {
                allocation[i][j] = dataScanner.nextInt();
                need[i][j] = maxNeed[i][j] - allocation[i][j];
                if (need[i][j] < 0) {
                    System.out.printf("你输入的第%d个进程申请的第%d个资源大于最大需求量,请重新输入:\n", i + 1, j + 1);
                    if (!isStandardInput) {
                        return;
                    }
                    j--;
                }
            }
        }
        showCurrentSystemMessage();
    }

    private boolean checkSafety() {
        work = new int[resourceTypeCount];
        int processCanFinishCount;
        int processFinishCount = 0;
        List<Integer> safetyList = new ArrayList<>(processCount);

        if (resourceTypeCount > 0) {
            System.arraycopy(available, 0, work, 0, resourceTypeCount);
        }
        for (int i = 0; i < processCount; i++) {
            finish[i] = false;
        }
        for (int i = 0; i < processCount; i++) {
            processCanFinishCount = 0;
            for (int j = 0; j < resourceTypeCount; j++) {
                if (!finish[i] && need[i][j] <= work[j]) {
                    processCanFinishCount++;
                }
                // 进程符合要求
                if (processCanFinishCount == resourceTypeCount) {
                    for (int k = 0; k < resourceTypeCount; k++) {
                        work[k] += allocation[i][k];
                    }
                    finish[i] = true;
                    safetyList.add(i);
                    i = -1;
                    processFinishCount++;
                }
            }
        }
        if (processFinishCount != processCount) {
            System.out.println("系统是不安全的");
            return false;
        }
        System.out.print("找到一个安全序列: ");
        for (int i = 0; i < processFinishCount; i++) {
            System.out.printf("p%d", safetyList.get(i));
            if (i != processFinishCount - 1) {
                System.out.print("->");
            }
        }
        System.out.print("\n");

        return true;
    }

    public void bankerAlgorithm() {
        dataScanner = new Scanner(System.in);
        finish = new boolean[processCount];
        request = new int[processCount][resourceTypeCount];

        System.out.printf("请输入需请求资源的进程号(0-%d): ", processCount - 1);
        requestProcessId = dataScanner.nextInt();
        System.out.printf("你输入的是 p[%d] 进程号:\n", requestProcessId);
        System.out.printf("该进程需求量为: %s\n", Arrays.toString(need[requestProcessId]));
        System.out.print("请输入该进程请求各资源的数量: ");
        for (int i = 0; i < resourceTypeCount; i++) {
            request[requestProcessId][i] = dataScanner.nextInt();
            if (request[requestProcessId][i] > need[requestProcessId][i]) {
                System.out.println("你输入的请求数超过进程的需求量!请重新输入!");
                i--;
                continue;
            }
            if (request[requestProcessId][i] > available[i]) {
                System.out.println("你输入的请求数超过进程可用的资源数!请重新输入!");
                i--;
            }
        }
        System.out.println("开始执行银行家算法,下面进行试分配...");
        tryAllocation();
        System.out.println("试分配完成!");
        System.out.println("进入安全性检测...");
        if (checkSafety()) {
            System.out.println("已通过安全性检测!");
            System.out.printf("开始给 p[%d] 进程分配资源...\n", requestProcessId);
            System.out.println("分配完成!打印输出...");
            showCurrentSystemMessage();
        } else {
            System.out.println("你的请求被拒绝!");
            rollbackAllocation();
        }
    }

    /**
     * 试分配
     */
    private void tryAllocation() {
        for (int i = 0; i < resourceTypeCount; i++) {
            available[i] -= request[requestProcessId][i];
            allocation[requestProcessId][i] += request[requestProcessId][i];
            need[requestProcessId][i] -= request[requestProcessId][i];
        }
    }

    /**
     * 试分配回退
     */
    private void rollbackAllocation() {
        for (int i = 0; i < resourceTypeCount; i++) {
            available[i] += request[requestProcessId][i];
            allocation[requestProcessId][i] -= request[requestProcessId][i];
            need[requestProcessId][i] += request[requestProcessId][i];
        }
    }

    private void showCurrentSystemMessage() {
        StringBuilder result = new StringBuilder();
        result.append("=======================================================\n");
        result.append(String.format("进程总数: %d\t资源种类数: %d\n", processCount, resourceTypeCount));
        result.append("=======================================================\n");
        result.append("| 进程 |    Max    |Allocation |   Need    | Avaliable |\n");
        for (int currentProcessId = 0; currentProcessId < processCount; currentProcessId++) {
            result.append(String.format("|  p%d |  ", currentProcessId));
            result.append(getMatrixRowDataString(maxNeed, currentProcessId));
            result.append("  |  ");
            result.append(getMatrixRowDataString(allocation, currentProcessId));
            result.append("  |  ");
            result.append(getMatrixRowDataString(need, currentProcessId));
            result.append("  |");
            if (currentProcessId == 0) {
                result.append("  ");
                result.append(getVectorDataString(available));
                result.append("  |\n");
            } else {
                result.append("\n");
            }
        }
        System.out.println(result.toString());
    }

    private String getMatrixRowDataString(int[][] matrix, int currentProcessId) {
        return getVectorDataString(matrix[currentProcessId]);
    }

    private String getVectorDataString(int[] vector) {
        StringBuilder result = new StringBuilder();
        for (int currentResourceId = 0; currentResourceId < resourceTypeCount; currentResourceId++) {
            result.append(vector[currentResourceId]);
            if (currentResourceId != resourceTypeCount - 1) {
                result.append("  ");
            }
        }
        return result.toString();
    }

}
