package com.imooc.utils;

import org.apache.commons.codec.binary.Base64;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * Byte操作工具类
 *
 * @author weishi
 */
public class ByteUtil {


    public static String defaultCharSet = "utf-8";

    /**
     * @方法功能 InputStream 转为 byte
     * @param InputStream
     * @return 字节数组
     * @throws Exception
     */
    public static byte[] inputStream2Byte(InputStream inStream)
            throws Exception {
        // ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        // byte[] buffer = new byte[1024];
        // int len = -1;
        // while ((len = inStream.read(buffer)) != -1) {
        // outSteam.write(buffer, 0, len);
        // }
        // outSteam.close();
        // inStream.close();
        // return outSteam.toByteArray();
        int count = 0;
        while (count == 0) {
            count = inStream.available();
        }
        byte[] b = new byte[count];
        inStream.read(b);
        return b;
    }

    /**
     * @方法功能 byte 转为 InputStream
     * @param 字节数组
     * @return InputStream
     * @throws Exception
     */
    public static InputStream byte2InputStream(byte[] b) throws Exception {
        InputStream is = new ByteArrayInputStream(b);
        return is;
    }


    /**
     * @功能 String与字节的转换,使用默认编码
     * @param str 输入字符串
     * @return 字节数组
     * @throws UnsupportedEncodingException
     */
    public static byte[] toBytes(String str) throws UnsupportedEncodingException {
        return str.getBytes(defaultCharSet);
    }

    /**
     * @功能 String与字节的转换
     * @param str 输入字符串
     * @param charSet 编码
     * @return 字节数组
     * @throws UnsupportedEncodingException
     */
    public static byte[] toBytes(String str, String charSet) throws UnsupportedEncodingException {
        return str.getBytes(charSet);
    }

    /**
     * @功能 字节与String的转换,使用默认编码
     * @param str 输入字节数组
     * @return 字符串
     * @throws UnsupportedEncodingException
     */
    public static String toString(byte[] b) throws UnsupportedEncodingException {
        return new String(b,defaultCharSet);
    }
    /**
     * @功能 字节与String的转换
     * @param str 输入字节数组
     * @param charSet 编码
     * @return 字符串
     * @throws UnsupportedEncodingException
     */
    public static String toString(byte[] b, String charSet) throws UnsupportedEncodingException {
        return new String(b,charSet);
    }

    /**
     * @功能 字节与十六机制String的转换
     * @param str 输入字节数组
     * @return 十六机制表示字符串
     */
    public static String encodeHex(byte[] b) {
        String s = null;
        int length = b.length;
        char hexDigits[] = { // 用来将字节转换成 16 进制表示的字符
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd','e', 'f' };
        // 用字节表示就是 16 个字节
        char str[] = new char[length * 2]; // 每个字节用 16 进制表示的话，使用两个字符，
        // 所以表示成 16 进制需要 32 个字符
        int k = 0; // 表示转换结果中对应的字符位置
        for (int i = 0; i < length; i++) { // 从第一个字节开始，对每一个字节
            // 转换成 16 进制字符的转换
            byte byte0 = b[i]; // 取第 i 个字节
            str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换,
            // >>> 为逻辑右移，将符号位一起右移
            str[k++] = hexDigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换
        }
        s = new String(str);

        return s;
    }

    /**
     * @功能 字节与十六机制String的转换
     * @param str 输入字符串
     * @return 字节数组
     */
    public static byte[] decodeHex(String str) throws Exception {
        char[] data = str.toCharArray();
        int len = data.length;

        if ((len & 0x01) != 0) {
            throw new Exception("Odd number of characters.");
        }

        byte[] out = new byte[len >> 1];

        // two characters form the hex value.
        for (int i = 0, j = 0; j < len; i++) {
            int f = toDigit(data[j], j) << 4;
            j++;
            f = f | toDigit(data[j], j);
            j++;
            out[i] = (byte) (f & 0xFF);
        }

        return out;
    }

    /**
     * Converts a hexadecimal character to an integer.
     *
     * @param ch
     *            A character to convert to an integer digit
     * @param index
     *            The index of the character in the source str, single character index can be 0
     * @return An integer
     * @throws DecoderException
     *             Thrown if ch is an illegal hex character
     */
    protected static int toDigit(char ch, int index) throws Exception {
        int digit = Character.digit(ch, 16);
        if (digit == -1) {
            throw new Exception("Illegal hexadecimal charcter " + ch + " at index " + index);
        }
        return digit;
    }

    /**
     * @功能 短整型与字节的转换
     * @param 短整型
     * @return 两位的字节数组
     */
    public static byte[] toBytes(short num) {
        int temp = num;
        byte[] b = new byte[2];
        for (int i = b.length - 1; i >= 0 ; i--) {
            b[i] = new Integer(temp & 0xff).byteValue();// 将最低位保存在最低位
            temp = temp >> 8; // 向右移8位
        }
        return b;
    }

    /**
     * @功能 字节的转换与短整型
     * @param 两位的字节数组
     * @return 短整型
     */
    public static short toShort(byte[] b) {
        short s = 0;
        short s0 = (short) (b[1] & 0xff);// 最低位
        short s1 = (short) (b[0] & 0xff);
        s1 <<= 8;
        s = (short) (s0 | s1);
        return s;
    }

    /**
     * @方法功能 整型与字节数组的转换
     * @param 整型
     * @return 四位的字节数组
     */
    public static byte[] toBytes(int num) {
        byte[] bytes = new byte[4];
        for (int ix = 0; ix < 4; ++ix) {
            int offset = 32 - (ix + 1) * 8;
            bytes[ix] = (byte) ((num >> offset) & 0xff);
        }
        return bytes;
    }

    /**
     * @方法功能 字节数组和整型的转换
     * @param 字节数组
     * @return 整型
     */
    public static int toInt(byte[] bytes) {
        int num = 0;
        for (int ix = 0; ix < 4; ++ix) {
            num <<= 8;
            num |= (bytes[ix] & 0xff);
        }
        return num;
    }

    /**
     * @方法功能 字节数组和长整型的转换
     * @param 字节数组
     * @return 长整型
     */
    public static byte[] toBytes(long num) {
        byte[] bytes = new byte[8];
        for (int ix = 0; ix < 8; ++ix) {
            int offset = 64 - (ix + 1) * 8;
            bytes[ix] = (byte) ((num >> offset) & 0xff);
        }
        return bytes;
    }

    /**
     * @方法功能 字节数组和长整型的转换
     * @param 字节数组
     * @return 长整型
     */
    public static long toLong(byte[] b) {
        long num = 0;
        for (int ix = 0; ix < 8; ++ix) {
            num <<= 8;
            num |= (b[ix] & 0xff);
        }
        return num;
    }


    /**
     * return a sub-part of byte[] for byte[] src
     *
     * @param src the source byte[]
     * @param begin index begin (include)
     * @param count the cnt of sub bytes
     * @return
     */
    public static byte[] subBytes(byte[] src, int begin, int count) {

        int length = src.length;
        if (begin < 0 || begin >= length || count == 0) {
            return null;
        }

        if (count < 0 || count > (length - begin)) {
            count = (length - begin);
        }

        byte[] bs = new byte[count];
        for (int i = begin; (i < begin + count); i++)
            bs[i - begin] = src[i];
        return bs;
    }

    /**
     * 字符串base 64编码
     * @param s
     * @return
     */
    public static String getBASE64(String s){
        if (s == null)
            return null;
        try {
            Base64 base64 =  new Base64(true);
            return new String( base64.encode(s.getBytes()));
        }
        catch (Exception ex){
            ex.printStackTrace();
            return null;
        }

    }

    /**
     * 字符串Base 64 解码
     * @param s
     * @return
     */
    public static String getFromBASE64(String s){
        if (s == null)
            return null;
        Base64 base64 = new Base64(true);
        try {
            byte[] b = base64.decode(s.getBytes());
            return new String(b);
        }
        catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) throws Exception {

        byte[] b = {(byte) 0x00,(byte) 0x00,(byte) 0x00,(byte)0x00,(byte) 0x00,(byte) 0x00,(byte) 0xf0,(byte)0x00};
        //byte[] b = {(byte) 0xa1,(byte) 0x00,(byte) 0x00,(byte)0xff};
        //byte[] b = {(byte) 0x00,(byte)0xa0};
        String hex = "1a4d4f78fd34abc8f98c3dd0bef27df4";//ByteUtil.encodeHex(b);
        long num = ByteUtil.toLong(b);
        byte[] hexNumBytes = ByteUtil.toBytes(num);
        byte[] hexStrBytes = ByteUtil.decodeHex(hex);


        System.out.println(hex);
        System.out.println(num);
        System.out.println(ByteUtil.encodeHex(hexNumBytes));
        System.out.println(ByteUtil.encodeHex(hexStrBytes));


    }
}