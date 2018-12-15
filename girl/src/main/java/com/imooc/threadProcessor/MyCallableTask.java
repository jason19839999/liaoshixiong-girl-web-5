package com.imooc.threadProcessor;

import java.util.concurrent.Callable;

/**
 * @描述
 * @创建人 shicong.zhang
 * @创建时间 $date$
 * @修改人和其它信息
 */
public class MyCallableTask implements Callable<String> {
    String str;

    public MyCallableTask(String str) {
        this.str = str;
    }
    public String call() throws Exception {
//        int num = 0;
//        for (int i = 0; i < 10; i++) {
//            Thread.sleep(50);
//            num++;
//            System.out.println("num=" + num);
//        }
//        return String.valueOf(num);
        return str;
    }
}
