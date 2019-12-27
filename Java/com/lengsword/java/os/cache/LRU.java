package com.lengsword.java.os.cache;

import java.util.*;

/**
 * @author LengSword
 */
public class LRU<V, K> {
    private final int MAX_CACHE_SIZE;
    private final float DEFAULT_LOAD_FACTOR = 0.75f;
    private boolean init;
    private int interruptCount;
    private List<Integer> eliminationList = new ArrayList<>();
    private LinkedHashMap<K, V> map;

    public LRU(int cacheSize) {
        MAX_CACHE_SIZE = cacheSize;
        int capacity = (int) Math.ceil(MAX_CACHE_SIZE / DEFAULT_LOAD_FACTOR) + 1;
        // if accessOrder == true => access-order 按访问顺序排序 -> LRU
        // if accessOrder == false => insertion-order 按插入顺序排序 -> FIFO
        map = new LinkedHashMap<K, V>(capacity, DEFAULT_LOAD_FACTOR, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                return size() > MAX_CACHE_SIZE;
            }
        };
        init = true;
        interruptCount = 0;
    }

    private boolean isInit() {
        return init;
    }

    public synchronized void endInit() {
        this.init = false;
    }

    public synchronized void put(K key, V value) {
        if (!isInit()) {
            checkPageInterrupt(value);
        }
        map.put(key, value);
    }

    public synchronized V get(K key) {
        return map.get(key);
    }

    private void checkPageInterrupt(V value) {
        if (!map.containsValue(value)) {
            if (map.size() + 1 > MAX_CACHE_SIZE) {
                // 会淘汰一个页
                eliminationList.add((Integer) map.values().toArray()[0]);
            }
            interruptCount++;
            System.out.printf("值 %s 发生缺页中断 - 当前缺页中断计数为 %d\n", value, interruptCount);
        }
    }

    public void showEliminationList() {
        System.out.print("依次尝试缺页中断后应淘汰的页: ");
        for (Integer pageValue : eliminationList) {
            System.out.printf("%d ", pageValue);
        }
        System.out.print("\n");
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<K, V> entry : map.entrySet()) {
            sb.append(String.format("[%s]: %s  ", entry.getKey(), entry.getValue()));
        }
        sb.append("\n");
        return sb.toString();
    }
}
