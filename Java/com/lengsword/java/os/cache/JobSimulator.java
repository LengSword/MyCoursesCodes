package com.lengsword.java.os.cache;

/**
 * 某采用页式存储管理的系统,接收了一共7页的作业,作业执行时依次访问的页为1,2,3,4,2,1,5,6,2,1,2,3,7.
 * 若把开始4页先装入主存,若分别用FIFO和LRU调度算法,作业执行时会产生多少次缺页中断?
 * 写出依次尝试缺页中断后应淘汰的页
 */
public class JobSimulator {
    public static void main(String[] args) {
        runLRU();
        runFIFO();
    }

    public static void runFIFO() {
        FIFO<Integer, Integer> fifo = new FIFO<>(4);
        fifo.put(1, 1);
        fifo.put(2, 2);
        fifo.put(3, 3);
        fifo.put(4, 4);
        fifo.endInit();
        System.out.println("=====FIFO=====");
        System.out.println(fifo);

        System.out.println("装入2, 1后:");
        fifo.put(2, 2);
        fifo.put(1, 1);
        System.out.println(fifo);

        System.out.println("装入5, 6后:");
        fifo.put(5, 5);
        fifo.put(6, 6);
        System.out.println(fifo);

        System.out.println("装入2, 1后:");
        fifo.put(2, 2);
        fifo.put(1, 1);
        System.out.println(fifo);

        System.out.println("装入2, 3后:");
        fifo.put(2, 2);
        fifo.put(3, 3);
        System.out.println(fifo);

        System.out.println("装入7后:");
        fifo.put(7, 7);
        System.out.println(fifo);
        fifo.showEliminationList();
    }

    public static void runLRU() {
        LRU<Integer, Integer> lru = new LRU<>(4);
        lru.put(1, 1);
        lru.put(2, 2);
        lru.put(3, 3);
        lru.put(4, 4);
        lru.endInit();
        System.out.println("=====LRU=====");
        System.out.println(lru);

        System.out.println("装入2, 1后:");
        lru.put(2, 2);
        lru.put(1, 1);
        System.out.println(lru);

        System.out.println("装入5, 6后:");
        lru.put(5, 5);
        lru.put(6, 6);
        System.out.println(lru);

        System.out.println("装入2, 1后:");
        lru.put(2, 2);
        lru.put(1, 1);
        System.out.println(lru);

        System.out.println("装入2, 3后:");
        lru.put(2, 2);
        lru.put(3, 3);
        System.out.println(lru);

        System.out.println("装入7后:");
        lru.put(7, 7);
        System.out.println(lru);
        lru.showEliminationList();
    }

}
