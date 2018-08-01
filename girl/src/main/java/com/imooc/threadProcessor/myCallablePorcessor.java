package com.imooc.threadProcessor;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class myCallablePorcessor {
    public static void myCallable() {
        FutureTask<String> task = new FutureTask<String>(new MyCallableTask());
        new Thread(task).start();
        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        task.cancel(true); // 中断线程，ture表示不必等待执行完成（会打断线程）。
        System.out.println(getValueFormFuture(task));
        System.out.println("这句将在task.get()阻塞结束后执行！");
    }

    private static String getValueFormFuture(FutureTask<String> task) {
        String str = "defaultValue";
        try {
            str = task.get();// task.get()会阻塞当前线程，等待子线程结束。
        } catch (Exception e) {
            System.out.println("任务已经被取消！");
        }
        return str;
    }


}


class MyCallableTask implements Callable<String> {
    public String call() throws Exception {
        int num = 0;
        for (int i = 0; i < 10; i++) {
            Thread.sleep(50);
            num++;
            System.out.println("num=" + num);
        }
        return String.valueOf(num);
    }
}