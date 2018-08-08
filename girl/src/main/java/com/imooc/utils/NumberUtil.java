package com.imooc.utils;

import java.text.DecimalFormat;

/**
 * Created by yang.gao on 2017/6/27.
 */
public class NumberUtil {

    public static String defaultNumPatten = "0.00";
    public static String defaultFourNumPatten = "0.0000";
    public static String defaultPercentNumPatten = "0.00%";

    /**
     * 取四舍五入保留2位小数，且为0则保留4位
     * null则结果为0
     * @param num
     * @return
     */
    public static String getDecimalApproxFormatNumber(Double num){
        if (num == null){
            return "0";
        }
        String format = String.format("%.2f", num);
        if (format.equals("0.00")){
//            format = "0.01";  //进位
            format = String.format("%.4f",num); //保留四位
//            format = String.valueOf(num);   //原数转化为Str
        }
        return format;
    }

    public static String getFormatNumberString(String num) {
        String ret = num;
        double dNum = Double.parseDouble(num);
        if (Math.abs(dNum) > 10000) {
            ret = format(dNum / 10000) + "万";
        }
        if (Math.abs(dNum) > 100000000) {
            ret = format(dNum / 100000000) + "亿";
        }
        return ret;
    }

    public static String getFormatNumberStringByD(double num) {
        String ret = new String();
        if (Math.abs(num) > 10000) {
            ret = format(num / 10000) + "万";
        }
        if (Math.abs(num) > 100000000) {
            ret = format(num / 100000000) + "亿";
        }
        return ret;
    }

    /**
     * 格式化数字，以万为单位，默认"#.00"
     * @param num
     * @return
     * @author yang.gao
     */
    public static String getWanFormatNumberStr(String num) {
        String ret = num;
        double dNum = Double.parseDouble(num);
        ret = format(dNum / 10000);
        return ret;
    }

    /**
     * 格式化数字为亿单位，默认"#.00"
     * @param num
     * @return
     * @author yang.gao
     */
    public static String getYiFormatNumberStr(String num) {
        String ret = num;
        double dNum = Double.parseDouble(num);
        ret = format(dNum / 100000000);
        return ret;
    }

    /**
     * 格式化数字，以万为单位，默认"#.00"
     * @param num
     * @return
     * @author yang.gao
     */
    public static String getWanFormatNumberByD(double num) {
        String ret = format(num / 10000);
        return ret;
    }

    /**
     * 格式化数字为亿单位，默认"#.00"
     * @param num
     * @return
     * @author yang.gao
     */
    public static String getYiFormatNumberByD(double num) {
        String ret = format(num / 100000000);
        return ret;
    }

    /**
     * 格式化数字，默认"#.00"
     *
     * @param value
     * @return
     */
    public static String format(double value) {
        return format(value, defaultNumPatten);
    }

    public static String format(String value) {
        return format(value, defaultNumPatten);
    }

    /**
     * 格式化数字，返回double，默认"#.00"
     *
     * @param value
     * @return
     * @author yang.gao
     */
    public static double parse(double value) {
        return Double.parseDouble(format(value, defaultNumPatten));
    }

    /**
     * 格式化数字，化为百分数，取小数点后两位数四舍五入
     *
     * @param value
     * @return
     * @author yang.gao
     */
    public static String formatPercent(double value) {
        return format(value, defaultPercentNumPatten);
    }

    /**
     * 格式化数字，化为百分数，取小数点后两位数四舍五入
     *
     * @param value
     * @return
     * @author yang.gao
     */
    public static String formatPercent(String value) {
        return format(value, defaultPercentNumPatten);
    }

    /**
     * 格式化数字
     *
     * @param value
     * @param pattern
     * @return
     */
    public static String format(double value, String pattern) {
        DecimalFormat df = new DecimalFormat(pattern);
        return df.format(value);
    }

    public static String format(String value, String pattern) {
        DecimalFormat df = new DecimalFormat(pattern);
        return df.format(Double.parseDouble(value));
    }

    public NumberUtil() {
        // TODO Auto-generated constructor stub
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }
}
