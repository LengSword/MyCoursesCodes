# Java

## 导航

- [os/cache](https://github.com/LengSword/MyCoursesCodes/tree/master/Java/com/lengsword/java/os/cache)

> LRU/FIFO页面置换算法的题目实现

题目如下:

```java
/**
 * 某采用页式存储管理的系统,接收了一共7页的作业,作业执行时依次访问的页为1,2,3,4,2,1,5,6,2,1,2,3,7.
 * 若把开始4页先装入主存,若分别用FIFO和LRU调度算法,作业执行时会产生多少次缺页中断?
 * 写出依次尝试缺页中断后应淘汰的页
 */
```

- [os/deadlock](https://github.com/LengSword/MyCoursesCodes/tree/master/Java/com/lengsword/java/os/deadlock)

> 死锁避免 银行家算法模拟

- [os/dispatch](https://github.com/LengSword/MyCoursesCodes/tree/master/Java/com/lengsword/java/os/dispatch)

> 进程调度模拟

参考了网上的例子,模拟FCFS/RR/SJF三种调度方式,但并没有对进程的状态进行模拟

暂未完善对响应比的计算,估计仍有许多bug,但是可以使用

- [os/state](https://github.com/LengSword/MyCoursesCodes/tree/master/Java/com/lengsword/java/os/state)

> 模拟进程状态转换

复用了**进程调度模拟**中的核心代码,对状态进行了修改(变为: 就绪态 阻塞态 运行态 完成态)

示例使用RR的调度方式,且为了方便展示模拟过程,设置时间片为1