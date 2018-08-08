package com.imooc.utils;

import java.io.BufferedReader;
import java.io.FileReader;



import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


public class JsonConfigReader {
    public static final Logger logger = LoggerFactory.getLogger(JsonConfigReader.class);
    /**
     * parse the json file into a jsonObject
     * @param filename
     * @return
     * @throws Exception
     */
    @SuppressWarnings("finally")
    public static JSONObject readRedisConfig(String filename) throws Exception {
        JSONObject conf = null;
        try {
            // step 1. reader the config file's content
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String value = "";
            String line = "";
            while ((line = reader.readLine()) != null) {
                value += line;
            }
            reader.close();
            // step 2. transform to class;
            conf = JSON.parseObject(value);
        } catch (Exception ex) {
            logger.error("Read Config_file (" + filename + ") error");
            logger.error("Exception Info:" + ex.getMessage());
            throw ex;
        }
        return conf;

    }

    public static void main(String[] args) throws Exception {
        JSONObject conf = JsonConfigReader.readRedisConfig("src/main/resources/config/DoubanBook/tag.json");
        JSONArray data = (JSONArray) conf.get("tagList");
        JSONObject config = (JSONObject) data.get(0);
        String tag = (String) config.get("tag");
        System.out.println(conf.get("tagList"));
    }
}
