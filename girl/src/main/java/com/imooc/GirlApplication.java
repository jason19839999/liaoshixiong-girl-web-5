package com.imooc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.imooc.threadProcessor.myRunable;
import com.imooc.threadProcessor.myThreadPoolProcessor;
import com.imooc.utils.DateUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;

@SpringBootApplication
public class GirlApplication {

    public static void main(String[] args) throws Exception {

        //将特殊格式的日期时间型字符串转换为Date类型
        String strDate = "Wed Aug 08 16:28:44 +0800 2018";
        Date date = DateUtil.parse(strDate, "EEE MMM dd HH:mm:ss Z yyyy");


        myThreadPoolProcessor.myPool();
        //myThreadPoolProcessor.myPool2();

        //myCallablePorcessor.myCallable();
        //JsonObjectDeal();
        dealMyRunable();
        SpringApplication.run(GirlApplication.class, args);
    }

    //jsonArray处理
    private static void JsonObjectDeal() {
        String stocks = "[{\"code\":\"SESZ_002405\",\"tag_type\":\"A\",\"name\":\"四维图新\"},{\"code\":\"SESZ_002355\",\"tag_type\":\"A\",\"name\":\"兴民智通\"}]";
        JSONArray arrys = JSON.parseArray(stocks);
        //可以将json串转换为JSONObject，这里需要json格式的，stocks不行。
        //JSONObject jsonobj = JSONObject.parseObject("{" + stocks +"}");

        for (int i = 0; i < arrys.size(); i++) {
            JSONObject rep = arrys.getJSONObject(i);
            //自定义map，继续添加属性
            Map<String, String> mapRep = rep.toJavaObject(Map.class);
            mapRep.put("exchange", "exchange");
            mapRep.put("quotation", "quotation");
            System.out.println(rep.getString("code"));
        }

    }

    //runable可以处理共享资源，比如买票 5张
    private static void dealMyRunable(){
        myRunable obj = new myRunable();
        //定义三个线程，相当于三个窗口同时卖票
        Thread thread1= new Thread(obj,"线程1");
        Thread thread2 = new Thread(obj,"线程2");
        Thread thread3 = new Thread(obj,"线程3");
        thread1.start();
        thread2.start();
        thread3.start();

    }
}
