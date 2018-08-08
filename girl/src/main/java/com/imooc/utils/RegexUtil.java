package com.imooc.utils;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegexUtil {
    public static Matcher getMatcher(String content, String regEx) {

        Matcher m = null;

        Pattern p = Pattern.compile(regEx);

        m = p.matcher(content);

        return m;

    }

    public static boolean matches(String content, String regEx) {
        Matcher m = getMatcher(content, regEx);
        return m.find();
    }

    public static void main(String[] args) {
        ArrayList<String> titles = new ArrayList<String>();
        titles.add("5月7日金融界股民内参(附股)");
        titles.add("1月24日金融界早盘股民内参");

        titles.add("5月4日股市早盘内参：一张图读懂五月投资机会");

        titles.add("5月11日投资者早盘必读内参");
        titles.add("操盘必读：5月8日股民内参");
        titles.add("和讯早报(5.4)：京津冀未来6年将投42万亿");
        titles.add("操盘必读：3月24日投资者早盘内参");

        for(String title: titles){
            Matcher m = RegexUtil.getMatcher(title, "RealNewsItemResultProcessor.globalNewsRegEx");
            if(m.find()){
                System.out.println("match: " + title);
            }else{
                System.err.println("dismatch: " + title);
            }
        }

    }
}
