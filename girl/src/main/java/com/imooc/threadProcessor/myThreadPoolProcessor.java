package com.imooc.threadProcessor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class myThreadPoolProcessor {
    //我创建了一个包含2条线程的线程池，但执行3个任务，从结果可以看出第三个任务使用的线程名称与第一个任务相同，即任务3与任务1使用同一条线程。
    // 还可以看出，任务3实在前两个任务完成后再执行的。
    public static void myPool() {
        ExecutorService service = Executors.newFixedThreadPool(2);
        service.execute(new PrintStr("A"));// AB同时执行
        service.execute(new PrintStr("B"));
        service.execute(new PrintStr("C"));// 在AB完成后执行
        service.shutdown();
    }


    //分析结果，newCachedThreadPool（）创建的线程池，线程数量根据需要创建。即如果池中没有空闲线程，则创建一条新线程（3个任务创建了3个线程）。
    // 若有有空闲线程，则复用（任务D、E复用了线程2和2）。
    public static void myPool2() {
        ExecutorService service = Executors.newCachedThreadPool();
        service.execute(new PrintStr("A"));
        service.execute(new PrintStr("B"));
        service.execute(new PrintStr("C"));
        // 等待以上任务执行完毕
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        service.execute(new PrintStr("D"));// 会复用空闲的Thread
        service.execute(new PrintStr("E"));// 会复用空闲的Thread
        service.shutdown();
    }


}

class PrintStr implements Runnable {
    String str;

    public PrintStr(String str) {
        this.str = str;
    }

    public void run() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(str + " : " + Thread.currentThread().getName());
    }
}

//线程池的意义在于：
// 1、减少在创建和销毁线程上所花的时间以及系统资源的开销，提升任务执行性能。
// 2、控制进程中线程数量的峰值，避免系统开销过大。
//通过线程池，可创建一定数量的线程，并由线程池管理。在需要执行任务时，直接使用其中的线程。任务执行完成后，线程保留，并可用于执行下一个任务。如果任务比线程多，则等待线程空闲。

//线程池使用中的几个关键对象关系：
//Executor：是一个执行接口，只包含 void execute(Runnable command)声明。
//ExecutorService：继承Executor接口，提供管理、终止线程的方法，可以跟踪任务执行状况生成 Future。
//Executors：定义 Executor、ExecutorService、ScheduledExecutorService、ThreadFactory 和 Callable 类的工厂和实用方法。用于创建ExecutorService 或ThreadFactory 等。
//可以看出Executors充当的是一个工具角色，主要作用是创建和处理。

//下面详细介绍一下线程池的几个常用方法。
//1、创建线程池
//创建线程池常用如下两个方法：
//ExecutorService service1 = Executors.newFixedThreadPool(2);
//ExecutorService service2 = Executors.newCachedThreadPool();
//Executors.newFixedThreadPool(2) 初始化指定数量的线程，如果线程意外终止，将重建并替换。
//Executors.newCachedThreadPool() 根据需要创建新线程的线程池，但是在以前构造的线程可用时将重用它们。若创建的线程空闲60秒以上则将其销毁并移除。
//2、关闭线程池
//关闭线程池有两种方法
//void shutdown()：
//启动一次顺序关闭，正在执行的任务会继续执行，但不接受新任务。所有任务完成后，关闭线程池。
//List < Runnable > shutdownNow():
//试图停止所有执行中的任务，暂停等待中的任务，并返回等待执行的任务列表，立即关闭线程池。
//注意：无法保证能够停止正在处理的活动执行任务，但是会尽力尝试。无法响应中断的任务可能无法终止。







