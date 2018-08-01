package com.imooc.threadProcessor;

public class myThread extends Thread {

    @Override
    public void run() {
        System.out.println("myThread 的当前线程名称是：" + Thread.currentThread().getName());
    }


}
