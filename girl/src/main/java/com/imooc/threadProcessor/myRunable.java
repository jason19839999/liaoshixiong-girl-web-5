package com.imooc.threadProcessor;


//runable可以处理共享资源，比如买票 5张
public class myRunable implements Runnable {
    //定义火车票数
    private int i = 10;


    @Override  //如果加synchronized,那么就被一个线程占用执行了，不加的话三个线程同时处理，卖出五张票
    public void run() {
        while(i >0){
            i--;
            System.out.println("myRunable 的当前线程名称是： " + Thread.currentThread().getName() + "  剩余票数:" + i);
            try {

//                并且它会把当前线程的interrupt状态“复位”，假设当前线程的isInterrupt状态为true，它会返回true，但过后 isInterrupt的状态会复位为false。之后调用(Thread)t.isInterrupt或Thread.interrupted都会返回 false
                Thread.interrupted();  //返回ture
                if(!Thread.currentThread().isInterrupted()) {
                    Thread.sleep(200);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}
