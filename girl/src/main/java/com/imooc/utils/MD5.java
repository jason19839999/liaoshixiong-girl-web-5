package com.imooc.utils;

import java.io.UnsupportedEncodingException;

/**
 *
 * @author weishi
 *
 */
public class MD5 {


    /**
     * get the md5 hex string for src string
     * @param raw the src string
     * @return the md5 hex string
     */
    public static String getMD5(String raw) {
        String ret = null;
        byte[] source;
        try {
            source = raw.getBytes("utf-8");
            ret = getMD5(source);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return ret;
    }

    /**
     * get the md5 hex string for src byte[]
     * @param raw the src byte[]
     * @return the md5 hex string
     */
    public static String getMD5(byte[] raw) {

        String s = null;
        try {
            java.security.MessageDigest md = java.security.MessageDigest
                    .getInstance("MD5");
            md.update(raw);
            byte tmp[] = md.digest(); // MD5 的计算结果是一个 128 位的长整数，
            // 用字节表示就是 16 个字节
            s = ByteUtil.encodeHex(tmp); // 换后的结果转换为字符串
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }


    /**
     * get the md5 byte[] for src string
     * @param raw the src string
     * @return the md5 byte[]
     */
    public static byte[] getMD5Bytes(String raw) {
        byte ret[] = null;
        byte[] source;
        try {
            source = raw.getBytes("utf-8");
            ret = getMD5Bytes(source);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ret;
    }

    /**
     * get the md5 md5 byte[] for src byte[]
     * @param raw the src byte[]
     * @return the md5 byte[]
     */
    public static byte[] getMD5Bytes(byte[] raw) {
        byte ret[] = null;
        try {
            java.security.MessageDigest md = java.security.MessageDigest
                    .getInstance("MD5");
            md.update(raw);
            ret = md.digest();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    /**
     * get the md5 byte[] for string raw, and return a sub-part of byte[] of the md5 byte[]
     *
     * @param raw
     *            the src string
     * @param begin
     *            the begin index (include) of md5 byte[]
     * @param count
     *            the cnt of sub bytes
     * @return
     */
    public static byte[] getSubMD5Bytes(String raw, int begin, int count) {

        byte md5Bytes[] = getMD5Bytes(raw);
        return ByteUtil.subBytes(md5Bytes, begin, count);
    }

    /**
     * get the md5 byte[] for byte[] raw, and return a sub-part of byte[] of the md5 byte[]
     *
     * @param raw
     *            the src raw byte[]
     * @param begin
     *            the begin index (include) of md5 byte[]
     * @param count
     *            the cnt of sub bytes
     * @return
     */
    public static byte[] getSubMD5Bytes(byte[] raw, int begin, int count) {

        byte md5Bytes[] = getMD5Bytes(raw);
        return ByteUtil.subBytes(md5Bytes, begin, count);
    }



    public static void main(String[] args) throws UnsupportedEncodingException {

        String userid = "金融街";
        // String userid = "中证网";
        long start;
        long end;
        String hash;
        byte[] subByteHash;
        String subHash;


//		byte[] bytes = Bytes.toBytes(userid);
//		start = System.nanoTime();
//		bytes = ByteUtil.toBytes(userid);
//		hash = MD5Hash.getMD5AsHex(bytes);
//		end = System.nanoTime();
//		System.out.println(hash + "\t" + (end - start));


        start = System.nanoTime();
        hash = getMD5(userid);
        subByteHash = getSubMD5Bytes(userid, 1, 1000);
        subHash = ByteUtil.encodeHex(subByteHash);
        end = System.nanoTime();
        System.out.println(hash + "\t" + (end - start));
        System.out.println(subHash + "\t" + (end - start));
    }
}
