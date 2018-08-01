package com.imooc.threadProcessor;


//runable可以处理共享资源，比如买票 5张
public class myRunable implements Runnable {
    //定义火车票数
    private int i = 5;


    @Override  //如果加synchronized,那么就被一个线程占用执行了，不加的话三个线程同时处理，卖出五张票
    public void run() {
        while(i >0){
            i--;
            System.out.println("myRunable 的当前线程名称是： " + Thread.currentThread().getName() + "  剩余票数:" + i);
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}
