package com.imooc.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.imooc.domain.Girl;
import com.imooc.domain.Result;
import com.imooc.enums.ResultEnum;
import com.imooc.exception.GirlException;
import com.imooc.repository.GirlRepository;
import com.imooc.service.GirlService;
import com.imooc.threadProcessor.myRunable;
import com.imooc.threadProcessor.myThread;
import com.imooc.utils.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by 廖师兄
 * 2016-11-03 23:15
 */
@RestController
public class GirlController {

    private final static Logger logger = LoggerFactory.getLogger(GirlController.class);

    @Autowired
    private GirlRepository girlRepository;

    @Autowired
    private GirlService girlService;

    private static int i = 0;

    final static ObjectMapper objectMapper = getJacksonMapper();

    /**
     * 查询所有女生列表
     *
     * @return
     */
    @GetMapping(value = "/girls")
    public List<Girl> girlList() {
        JSONObject result = new JSONObject();
        //JSONObject类型的数据
        List<JSONObject> name = new ArrayList<>();
        JSONObject tmp = new JSONObject();
        tmp.put("name", "zhangsan");
        name.add(tmp);
        tmp = new JSONObject();
        tmp.put("name", "lisi");
        name.add(tmp);
        result.put("name", name);


        //Integer类型的数组
        List<Integer> age = new ArrayList<Integer>();
        age.add(18);
        age.add(19);
        result.put("age", age);

        JSONArray jarry = result.getJSONArray("name");
        JSONArray jarryAge = result.getJSONArray("age");
        if (jarry != null && jarry.size() > 0) {
            for (int i = 0; i < jarry.size(); i++) {
                System.out.println(jarry.getJSONObject(i).getString("name"));
            }
        }
        if (jarry != null && jarryAge.size() > 0) {
            for (int i = 0; i < jarryAge.size(); i++) {
                System.out.println(jarryAge.getInteger(i));
            }
        }

        //JSONObject序列化成json
        String jsonResult = "";
        try {
            jsonResult = objectMapper.writeValueAsString(result);
        } catch (Exception ex) {
            logger.info(ex.getStackTrace().toString());
            //正常抛出系统异常  记录错误日志信息
            throw new GirlException(ResultEnum.UNKONW_ERROR);
        }

        //自定义实体类型序列化与反序列化
        //List<Entity>list=new ArrayList<Entity>();
        //String listString = JSON.toJSONString(list, true);
        //List<Entity> list2 = JSON.parseArray(listString2, Entity.class);

        //自定义线程类
        myThread objThread = new myThread();
        objThread.run();
        myRunable objRunable = new myRunable();
        objRunable.run();

        //异步执行任务，不影响前台相应时间
        new Thread(new Runnable() {
            @Override
            public void run() {
                sleep(5000);
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                sleep(10000);
            }
        }).start();
        logger.info("girlList");
        return girlRepository.findAll();
    }

    //线程排队执行
    public synchronized static void sleep(long count) {
        try {
            i++;
            logger.info("进入异步执行模式");
            logger.info("sleep的当前线程名称是： " + Thread.currentThread().getName());
            if (i == 3) {
                //Thread.interrupted();
                logger.info("关闭线程");
                //Thread.yield();
            }
            Thread.sleep(count);
            logger.info("结束异步执行模式");
        } catch (Exception ex) {
            logger.error(ex.toString());
        }
    }

    /**
     * 添加一个女生
     *
     * @return
     */
    @PostMapping(value = "/girls")
    public Result<Girl> girlAdd(@Valid Girl girl, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResultUtil.error(1, bindingResult.getFieldError().getDefaultMessage());
        }

        girl.setCupSize(girl.getCupSize());
        girl.setAge(girl.getAge());

        return ResultUtil.success(girlRepository.save(girl));
    }

    //查询一个女生
    @GetMapping(value = "/girls/{id}")
    public Girl girlFindOne(@PathVariable("id") Integer id) {
        return girlRepository.findOne(id);
    }

    //更新
    @PutMapping(value = "/girls/{id}")
    public Girl girlUpdate(@PathVariable("id") Integer id,
                           @RequestParam("cupSize") String cupSize,
                           @RequestParam("age") Integer age) {
        Girl girl = new Girl();
        girl.setId(id);
        girl.setCupSize(cupSize);
        girl.setAge(age);

        return girlRepository.save(girl);
    }

    //删除
    @DeleteMapping(value = "/girls/{id}")
    public void girlDelete(@PathVariable("id") Integer id) {
        girlRepository.delete(id);
    }

    //通过年龄查询女生列表
    @GetMapping(value = "/girls/age/{age}")
    public List<Girl> girlListByAge(@PathVariable("age") Integer age) {
        return girlRepository.findByAge(age);
    }

    @PostMapping(value = "/girls/two")
    public void girlTwo() {
        girlService.insertTwo();
    }

    @GetMapping(value = "girls/getAge/{id}")
    public void getAge(@PathVariable("id") Integer id) throws Exception {
        girlService.getAge(id);
    }

    @GetMapping(value = "girls/noAutority")
    public void noAutority() {
        logger.info("非常抱歉，您没有权限访问！");
    }

    protected static ObjectMapper getJacksonMapper() {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        objectMapper
                .setPropertyNamingStrategy(new PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy());
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return objectMapper;
    }


}
