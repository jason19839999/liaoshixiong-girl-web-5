package com.imooc.utils;


import java.util.UUID;


/**
 * hbase rowkey tool
 * @author weishi
 *
 */
public class RowKeyUtil {

    //define the length of Byte for each field in rowkey, the total length of rowkey is 16Byte
    public static int timestamp_length = 8;
    public static int website_length = 2;
    public static int channel_length = 2;
    public static int juniorchina_channel_length = 2;
    public static int app_user_id_length = 8;
    public static int hash_id_length = 4;

    public static int search_item_id_length = 4;
    public static int star_user_weibo_userid_length = 4;
    public static int star_user_weixin_userid_length = 4;

    public static int rec_score_length = 4;
    public static int tag_length = 4;
    public static int weight_length = 4;
    public static int item_length = 8;

    /**
     *
     * @param timestamp time milliseconds since January 1, 1970, 00:00:00
     * @return the reverse timestamp in bytes
     */
    public static byte[] reverseTimestamp(long timestamp) {
        long reverseTimestamp = Long.MAX_VALUE - timestamp;
        return ByteUtil.toBytes(reverseTimestamp);
    }

    /**
     *
     * @param RecScore int
     * @return the reverse RecScore in bytes
     */
    public static byte[] reverseRecScore(int recScore) {
        int reverseRecScore = Integer.MAX_VALUE - recScore;
        return ByteUtil.toBytes(reverseRecScore);
    }

    /**
     * generate rowKey for table news_raw
     * @param website
     * @param channel
     * @param timestamp the timestamp in milliseconds.
     * @param url to generate hashId
     * @return byte[16] rowkey: website+channel+timestamp+hashId(url)
     */
    public static byte[] getRowKeyForNewsRaw(String website, String channel, long timestamp, String url) {
        byte[] rowKey =  new byte[16];

        int i = 0;

        byte[] websiteBytes = MD5.getSubMD5Bytes(website, 0, website_length);
        for(int j = 0; j < website_length; j++,i++){
            rowKey[i] = websiteBytes[j];
        }

        byte[] channelBytes = MD5.getSubMD5Bytes(channel, 0, channel_length);
        for(int j = 0; j < channel_length; j++,i++){

            rowKey[i] = channelBytes[j];
        }

        byte[] timestampBytes = reverseTimestamp(timestamp);
        for(int j = 0; j < timestamp_length; j++,i++){
            rowKey[i] = timestampBytes[j];
        }

        byte[] hashIdBytes = MD5.getSubMD5Bytes(url, 0, hash_id_length);
        for(int j = 0; j < hash_id_length; j++,i++){
            rowKey[i] = hashIdBytes[j];
        }

        return rowKey;
    }

    /**
     * 获取公告key
     * @param source_onweb
     * @param secucode
     * @param timestamp
     * @param title
     * @return
     */
    public static byte[] getRowKeyForAnnouncementRaw(String source_onweb, String secucode, long timestamp, String title) {
        byte[] rowKey =  new byte[16];

        int i = 0;

        byte[] websiteBytes = MD5.getSubMD5Bytes(source_onweb, 0, website_length);
        for(int j = 0; j < website_length; j++,i++){
            rowKey[i] = websiteBytes[j];
        }

        byte[] channelBytes = MD5.getSubMD5Bytes(secucode, 0, hash_id_length);
        for(int j = 0; j < hash_id_length; j++,i++){

            rowKey[i] = channelBytes[j];
        }

        byte[] timestampBytes = reverseTimestamp(timestamp);
        for(int j = 0; j < timestamp_length; j++,i++){
            rowKey[i] = timestampBytes[j];
        }

        if(title == null || title.isEmpty()) {
            title = "null";
        }
        byte[] hashIdBytes = MD5.getSubMD5Bytes(title, 0, channel_length);
        for(int j = 0; j < channel_length; j++,i++){
            rowKey[i] = hashIdBytes[j];
        }

        return rowKey;
    }

    /**
     * 获取公告key
     * @param source_onweb
     * @param secucode
     * @param timestamp
     * @param title
     * @return
     */
    public static byte[] getPrefixRowKeyForAnnouncement(String source_onweb, String secucode, long timestamp) {
        byte[] rowKey =  new byte[16];

        int i = 0;

        byte[] websiteBytes = MD5.getSubMD5Bytes(source_onweb, 0, website_length);
        for(int j = 0; j < website_length; j++,i++){
            rowKey[i] = websiteBytes[j];
        }

        byte[] channelBytes = MD5.getSubMD5Bytes(secucode, 0, hash_id_length);
        for(int j = 0; j < hash_id_length; j++,i++){

            rowKey[i] = channelBytes[j];
        }

        byte[] timestampBytes = reverseTimestamp(timestamp);
        for(int j = 0; j < timestamp_length; j++,i++){
            rowKey[i] = timestampBytes[j];
        }

        return rowKey;
    }

    /**
     * get the prefix of rowkey using website, channel, timestamp
     * @param website
     * @param channel if null only website to be the prefix
     * @param timestamp the timestamp in milliseconds, if = -1 only website and channel to be the prefix
     * @return byte[16] rowkey: website+channel+timestamp+ 0x00 0x00 0x00 0x00
     */
    public static byte[] getPrefixRowKeyForNewsRaw(String website, String channel, long timestamp) {
        byte[] rowKey =  new byte[16];

        int i = 0;

        byte[] websiteBytes = MD5.getSubMD5Bytes(website, 0, website_length);
        for(int j = 0; j < website_length; j++,i++){
            rowKey[i] = websiteBytes[j];
        }

        if(channel != null){
            byte[] channelBytes = MD5.getSubMD5Bytes(channel, 0, channel_length);
            for(int j = 0; j < channel_length; j++,i++){

                rowKey[i] = channelBytes[j];
            }
        }else{
            return rowKey;
        }

        if(timestamp != -1){
            byte[] timestampBytes = reverseTimestamp(timestamp);
            for(int j = 0; j < timestamp_length; j++,i++){
                rowKey[i] = timestampBytes[j];
            }
        }else{
            return rowKey;
        }


        return rowKey;
    }


    /**
     * generate rowKey for table news
     * @param juniorChinaChannel
     * @param timestamp the timestamp in milliseconds.
     * @param url to generate hashId
     * @return byte[16] rowkey: juniorChinaChannel+timestamp+uuidBytes(4B)
     */
    public static byte[] getRowKeyForNews(String juniorChinaChannel, long timestamp) {
        byte[] rowKey =  new byte[16];

        byte[] channelBytes = MD5.getSubMD5Bytes(juniorChinaChannel, 0, juniorchina_channel_length);

        int i = 0;

        for(int j = 0; j < juniorchina_channel_length; j++,i++){
            rowKey[i] = channelBytes[j];
        }

        byte[] timestampBytes = reverseTimestamp(timestamp);
        for(int j = 0; j < timestamp_length; j++,i++){
            rowKey[i] = timestampBytes[j];
        }

        UUID uuid = UUID.randomUUID();
        byte[] hashIdBytes = ByteUtil.subBytes(ByteUtil.toBytes(uuid.getMostSignificantBits()),0, hash_id_length);
        for(int j = 0; j < hash_id_length; j++,i++){

            rowKey[i] = hashIdBytes[j];
        }

        return rowKey;
    }

    /**
     * generate rowKey for hk bills
     * @param code,bill_type,bill_date
     * @return byte[16] rowkey: code+bill_type+bill_date
     */
    public static byte[] getRowKeyForHKBill(String code,String bill_type,String bill_date,String file_name) {
        byte[] rowKey =  new byte[16];

        byte[] codeBytes = MD5.getSubMD5Bytes(code, 0, 4);
        byte[] billTypeBytes = MD5.getSubMD5Bytes(bill_type, 0, 4);
        byte[] billDateBytes = MD5.getSubMD5Bytes(bill_date, 0, 4);
        byte[] billNameBytes = MD5.getSubMD5Bytes(file_name, 0, 4);
        int i = 0;

        for(int j = 0; j < 4; j++,i++){
            rowKey[i] = codeBytes[j];
        }

        for(int j = 0; j < 4; j++,i++){
            rowKey[i] = billTypeBytes[j];
        }

        for(int j = 0; j < 4; j++,i++){
            rowKey[i] = billDateBytes[j];
        }

        for(int j = 0; j < 4; j++,i++){
            rowKey[i] = billNameBytes[j];
        }


        return rowKey;
    }


    /**
     * generate rowKey for table search_weibo
     * @param search_item_id
     * @param timestamp the timestamp in milliseconds.
     * @param idStr to generate hashId
     * @return byte[16] rowkey: search_item_id+timestamp+hashId(idStr)
     */
    public static byte[] getRowKeyForSearchWeibo(String search_item_id, long timestamp, String idStr) {
        byte[] rowKey =  new byte[16];

        byte[] keywordBytes = MD5.getSubMD5Bytes(search_item_id, 0, search_item_id_length);

        int i = 0;

        for(int j = 0; j < search_item_id_length; j++,i++){
            rowKey[i] = keywordBytes[j];
        }

        byte[] timestampBytes = reverseTimestamp(timestamp);
        for(int j = 0; j < timestamp_length; j++,i++){
            rowKey[i] = timestampBytes[j];
        }

        byte[] hashIdBytes = MD5.getSubMD5Bytes(idStr, 0, hash_id_length);
        for(int j = 0; j < hash_id_length; j++,i++){
            rowKey[i] = hashIdBytes[j];
        }

        return rowKey;
    }

    /**
     * get the prefix of rowkey for table search_weibo using search_item_id, timestamp
     * @param search_item_id
     * @param timestamp the timestamp in milliseconds, if = -1 only keyword to be the prefix
     * @return byte[16] rowkey: search_item_id+timestamp+ 0x00 0x00 0x00 0x00
     */
    public static byte[] getPrefixRowKeyForSearchWeibo(String search_item_id, long timestamp) {
        byte[] rowKey =  new byte[16];

        byte[] keywordBytes = MD5.getSubMD5Bytes(search_item_id, 0, search_item_id_length);

        int i = 0;

        for(int j = 0; j < search_item_id_length; j++,i++){
            rowKey[i] = keywordBytes[j];
        }

        if(timestamp != -1){
            byte[] timestampBytes = reverseTimestamp(timestamp);
            for(int j = 0; j < timestamp_length; j++,i++){
                rowKey[i] = timestampBytes[j];
            }
        }else{
            return rowKey;
        }


        return rowKey;
    }

    /**
     * generate rowKey for table star_user_weibo
     * @param userIdStr
     * @param timestamp the timestamp in milliseconds.
     * @param idStr to generate hashId
     * @return byte[16] rowkey: userIdStr+timestamp+hashId(idStr)
     */
    public static byte[] getRowKeyForStarUserWeibo(String userIdStr, long timestamp, String idStr) {
        byte[] rowKey =  new byte[16];

        byte[] starUserIdBytes = MD5.getSubMD5Bytes(userIdStr, 0, star_user_weibo_userid_length);

        int i = 0;

        for(int j = 0; j < star_user_weibo_userid_length; j++,i++){
            rowKey[i] = starUserIdBytes[j];
        }

        byte[] timestampBytes = reverseTimestamp(timestamp);
        for(int j = 0; j < timestamp_length; j++,i++){
            rowKey[i] = timestampBytes[j];
        }

        byte[] hashIdBytes = MD5.getSubMD5Bytes(idStr, 0, hash_id_length);
        for(int j = 0; j < hash_id_length; j++,i++){
            rowKey[i] = hashIdBytes[j];
        }

        return rowKey;
    }

    /**
     * get the prefix of rowkey for table star_user_weibo using userIdStr, timestamp
     * @param userIdStr
     * @param timestamp the timestamp in milliseconds, if = -1 only keyword to be the prefix
     * @return byte[16] rowkey: userIdStr+timestamp+ 0x00 0x00 0x00 0x00
     */
    public static byte[] getPrefixRowKeyForStarUserWeibo(String userIdStr, long timestamp) {
        byte[] rowKey =  new byte[16];

        byte[] starUserIdBytes = MD5.getSubMD5Bytes(userIdStr, 0, star_user_weibo_userid_length);

        int i = 0;

        for(int j = 0; j < star_user_weibo_userid_length; j++,i++){
            rowKey[i] = starUserIdBytes[j];
        }

        if(timestamp != -1){
            byte[] timestampBytes = reverseTimestamp(timestamp);
            for(int j = 0; j < timestamp_length; j++,i++){
                rowKey[i] = timestampBytes[j];
            }
        }else{
            return rowKey;
        }


        return rowKey;
    }

    /**
     * generate rowKey for table star_user_xueqiu
     * @param userIdStr
     * @param timestamp the timestamp in milliseconds.
     * @param idStr to generate hashId
     * @return byte[16] rowkey: userIdStr+timestamp+hashId(idStr)
     */
    public static byte[] getRowKeyForStarUserXueqiu(String userIdStr, long timestamp, String idStr) {
        byte[] rowKey =  new byte[16];

        byte[] starUserIdBytes = MD5.getSubMD5Bytes(userIdStr, 0, star_user_weibo_userid_length);

        int i = 0;

        for(int j = 0; j < star_user_weibo_userid_length; j++,i++){
            rowKey[i] = starUserIdBytes[j];
        }

        byte[] timestampBytes = reverseTimestamp(timestamp);
        for(int j = 0; j < timestamp_length; j++,i++){
            rowKey[i] = timestampBytes[j];
        }

        byte[] hashIdBytes = MD5.getSubMD5Bytes(idStr, 0, hash_id_length);
        for(int j = 0; j < hash_id_length; j++,i++){
            rowKey[i] = hashIdBytes[j];
        }

        return rowKey;
    }

    /**
     * get the prefix of rowkey for table star_user_xueqiu using userIdStr, timestamp
     * @param userIdStr
     * @param timestamp the timestamp in milliseconds, if = -1 only keyword to be the prefix
     * @return byte[16] rowkey: userIdStr+timestamp+ 0x00 0x00 0x00 0x00
     */
    public static byte[] getPrefixRowKeyForStarUserXueqiu(String userIdStr, long timestamp) {
        byte[] rowKey =  new byte[16];

        byte[] starUserIdBytes = MD5.getSubMD5Bytes(userIdStr, 0, star_user_weibo_userid_length);

        int i = 0;

        for(int j = 0; j < star_user_weibo_userid_length; j++,i++){
            rowKey[i] = starUserIdBytes[j];
        }

        if(timestamp != -1){
            byte[] timestampBytes = reverseTimestamp(timestamp);
            for(int j = 0; j < timestamp_length; j++,i++){
                rowKey[i] = timestampBytes[j];
            }
        }else{
            return rowKey;
        }


        return rowKey;
    }


    /**
     * generate rowKey for table star_user_blog
     * @param userUrl
     * @param timestamp the timestamp in milliseconds.
     * @param url to generate hashId
     * @return byte[16] rowkey: userUrl+timestamp+hashId(url)
     */
    public static byte[] getRowKeyForStarUserBlog(String userUrl, long timestamp, String url) {
        byte[] rowKey =  new byte[16];

        byte[] starUserUrlBytes = MD5.getSubMD5Bytes(userUrl, 0, star_user_weibo_userid_length);

        int i = 0;

        for(int j = 0; j < star_user_weibo_userid_length; j++,i++){
            rowKey[i] = starUserUrlBytes[j];
        }

        byte[] timestampBytes = reverseTimestamp(timestamp);
        for(int j = 0; j < timestamp_length; j++,i++){
            rowKey[i] = timestampBytes[j];
        }

        byte[] hashIdBytes = MD5.getSubMD5Bytes(url, 0, hash_id_length);
        for(int j = 0; j < hash_id_length; j++,i++){
            rowKey[i] = hashIdBytes[j];
        }

        return rowKey;
    }

    /**
     * get the prefix of rowkey for table star_user_blog using userUrl, timestamp
     * @param userUrl
     * @param timestamp the timestamp in milliseconds, if = -1 only keyword to be the prefix
     * @return byte[16] rowkey: userUrl+timestamp+ 0x00 0x00 0x00 0x00
     */
    public static byte[] getPrefixRowKeyForStarUserBlog(String userUrl, long timestamp) {
        byte[] rowKey =  new byte[16];

        byte[] starUserUrlBytes = MD5.getSubMD5Bytes(userUrl, 0, star_user_weibo_userid_length);

        int i = 0;

        for(int j = 0; j < star_user_weibo_userid_length; j++,i++){
            rowKey[i] = starUserUrlBytes[j];
        }

        if(timestamp != -1){
            byte[] timestampBytes = reverseTimestamp(timestamp);
            for(int j = 0; j < timestamp_length; j++,i++){
                rowKey[i] = timestampBytes[j];
            }
        }else{
            return rowKey;
        }


        return rowKey;
    }



    /**
     * generate rowKey for table star_user_weibo
     * @param userIdStr
     * @param timestamp the timestamp in milliseconds.
     * @param idStr to generate hashId
     * @return byte[16] rowkey: userIdStr+timestamp+hashId(idStr)
     */
    public static byte[] getRowKeyForStarUserWeixin(String userIdStr, long timestamp, String idStr) {
        byte[] rowKey =  new byte[16];

        byte[] starUserIdBytes = MD5.getSubMD5Bytes(userIdStr, 0, star_user_weixin_userid_length);

        int i = 0;

        for(int j = 0; j < star_user_weixin_userid_length; j++,i++){
            rowKey[i] = starUserIdBytes[j];
        }

        byte[] timestampBytes = reverseTimestamp(timestamp);
        for(int j = 0; j < timestamp_length; j++,i++){
            rowKey[i] = timestampBytes[j];
        }

        byte[] hashIdBytes = MD5.getSubMD5Bytes(idStr, 0, hash_id_length);
        for(int j = 0; j < hash_id_length; j++,i++){
            rowKey[i] = hashIdBytes[j];
        }

        return rowKey;
    }

    /**
     * get the prefix of rowkey for table search_weibo using keyword, timestamp
     * @param userIdStr
     * @param timestamp the timestamp in milliseconds, if = -1 only keyword to be the prefix
     * @return byte[16] rowkey: userIdStr+timestamp+ 0x00 0x00 0x00 0x00
     */
    public static byte[] getPrefixRowKeyForStarUserWeixin(String userIdStr, long timestamp) {
        byte[] rowKey =  new byte[16];

        byte[] starUserIdBytes = MD5.getSubMD5Bytes(userIdStr, 0, star_user_weixin_userid_length);

        int i = 0;

        for(int j = 0; j < star_user_weixin_userid_length; j++,i++){
            rowKey[i] = starUserIdBytes[j];
        }

        if(timestamp != -1){
            byte[] timestampBytes = reverseTimestamp(timestamp);
            for(int j = 0; j < timestamp_length; j++,i++){
                rowKey[i] = timestampBytes[j];
            }
        }else{
            return rowKey;
        }


        return rowKey;
    }

    /**
     * generate rowKey for table user_rec_realtime
     * @param userIdStr
     * @param recScore = f(timestamp in minutes + score)
     * @param itemId the unique item_id string
     * @return byte[24] rowkey: userIdStr+recScore+item_rowKey
     */
    public static byte[] getRowKeyForUserRecRealtime(String userIdStr, int recScore, String itemId) {
        byte[] rowKey =  new byte[24];

        byte[] userIdBytes = MD5.getSubMD5Bytes(userIdStr, 0, app_user_id_length);

        int i = 0;

        for(int j = 0; j < app_user_id_length; j++,i++){
            rowKey[i] = userIdBytes[j];
        }

        byte[] recScoreBytes = reverseRecScore(recScore);
        for(int j = 0; j < rec_score_length; j++,i++){
            rowKey[i] = recScoreBytes[j];
        }

        byte[] itemBytes = MD5.getSubMD5Bytes(itemId, 0, item_length);
        for(int j = 0; j < item_length; j++,i++){
            rowKey[i] = itemBytes[j];
        }

        return rowKey;
    }

    /**
     * get the prefix of rowkey for table user_rec_realtime using recScore, recScore
     * @param userIdStr
     * @param recScore = f(timestamp in minutes + score), if = -1 only userIdStr to be the prefix
     * @return byte[24] rowkey: userIdStr+recScore+ 0x00 0x00 0x00 0x00
     */
    public static byte[] getPrefixRowKeyForUserRecRealtime(String userIdStr, int recScore) {
        byte[] rowKey =  new byte[24];

        byte[] userIdBytes = MD5.getSubMD5Bytes(userIdStr, 0, app_user_id_length);

        int i = 0;

        for(int j = 0; j < app_user_id_length; j++,i++){
            rowKey[i] = userIdBytes[j];
        }

        if(recScore != -1){
            byte[] recScoreBytes = reverseRecScore(recScore);
            for(int j = 0; j < rec_score_length; j++,i++){
                rowKey[i] = recScoreBytes[j];
            }
        }else{
            return rowKey;
        }


        return rowKey;
    }

    /**
     * generate rowKey for table tag_item
     * @param tag the sting of the tag
     * @param score = the score bewteen tag and item
     * @param itemId
     * @return byte[16] rowkey: tag+score+itemId
     */
    public static byte[] getRowKeyForTagItem(String tag, int score, String itemId) {
        byte[] rowKey =  new byte[16];

        int i = 0;

        byte[] tagBytes = MD5.getSubMD5Bytes(tag, 0, tag_length);

        for(int j = 0; j < tag_length; j++,i++){
            rowKey[i] = tagBytes[j];
        }

        byte[] scoreBytes = reverseRecScore(score);
        for(int j = 0; j < rec_score_length; j++,i++){
            rowKey[i] = scoreBytes[j];
        }

        byte[] itemBytes = MD5.getSubMD5Bytes(itemId, 0, item_length);
        for(int j = 0; j < item_length; j++,i++){
            rowKey[i] = itemBytes[j];
        }

        return rowKey;
    }

    /**
     * get the prefix of rowkey for table tag_item using tag, score
     * @param tag the sting of the tag
     * @param score = the score bewteen tag and user, if = -1 only tag to be the prefix
     * @return byte[16] rowkey: tag+score+ 0x00 0x00 0x00 0x00 0x00 0x00 0x00 0x00
     */
    public static byte[] getPrefixRowKeyForTagItem(String tag, int score) {
        byte[] rowKey =  new byte[16];

        int i = 0;

        byte[] tagBytes = MD5.getSubMD5Bytes(tag, 0, tag_length);

        for(int j = 0; j < tag_length; j++,i++){
            rowKey[i] = tagBytes[j];
        }

        if(score != -1){
            byte[] scoreBytes = reverseRecScore(score);
            for(int j = 0; j < rec_score_length; j++,i++){
                rowKey[i] = scoreBytes[j];
            }
        }else{
            return rowKey;
        }


        return rowKey;
    }



    /**
     * generate rowKey for table tag_user
     * @param tag the sting of the tag
     * @return byte[4] rowkey: tag
     */
    public static byte[] getRowKeyForTagUser(String tag) {
        byte[] rowKey =  new byte[4];

        int i = 0;

        byte[] tagBytes = MD5.getSubMD5Bytes(tag, 0, tag_length);

        for(int j = 0; j < tag_length; j++,i++){
            rowKey[i] = tagBytes[j];
        }

        return rowKey;
    }


    /**
     * get the rowkey for table user_tag using userIdStr
     * @param userIdStr
     * @return byte[8] rowkey: userIdStr
     */
    public static byte[] getRowKeyForUserTag(String userIdStr) {
        byte[] rowKey =  new byte[8];

        byte[] userIdBytes = MD5.getSubMD5Bytes(userIdStr, 0, app_user_id_length);

        int i = 0;

        for(int j = 0; j < app_user_id_length; j++,i++){
            rowKey[i] = userIdBytes[j];
        }

        return rowKey;
    }



    /**
     * generate rowKey for table user_rec_history
     * @param userIdStr
     * @param itemId the unique item_id string
     * @return byte[16] rowkey: itemId+userIdStr
     */
    public static byte[] getRowKeyForUserRecHistory(String itemId, String userIdStr) {
        byte[] rowKey =  new byte[16];

        int i = 0;

        byte[] itemBytes = MD5.getSubMD5Bytes(itemId, 0, item_length);
        for(int j = 0; j < item_length; j++,i++){
            rowKey[i] = itemBytes[j];
        }


        byte[] userIdBytes = MD5.getSubMD5Bytes(userIdStr, 0, app_user_id_length);
        for(int j = 0; j < app_user_id_length; j++,i++){
            rowKey[i] = userIdBytes[j];
        }


        return rowKey;
    }

    /**
     * get the prefix of rowkey for table user_rec_history using itemId
     * @param userIdStr
     * @return byte[16] rowkey: itemId+ 0x00 0x00 0x00 0x00 0x00 0x00 0x00 0x00
     */
    public static byte[] getPrefixRowKeyForUserRecHistory(String itemId) {
        byte[] rowKey =  new byte[16];

        int i = 0;

        byte[] itemBytes = MD5.getSubMD5Bytes(itemId, 0, item_length);
        for(int j = 0; j < item_length; j++,i++){
            rowKey[i] = itemBytes[j];
        }


        return rowKey;
    }

    /**
     * get the postfix of rowkey for table user_rec_history using itemId
     * @param userIdStr
     * @return byte[16] rowkey: itemId+ 0xff 0xff 0xff 0xff 0xff 0xff 0xff 0xff
     */
    public static byte[] getPostfixRowKeyForUserRecHistory(String itemId) {
        byte[] rowKey = getPrefixRowKeyForUserRecHistory(itemId);

        for(int i = item_length; i < rowKey.length; i++){
            rowKey[i] = (byte) 0xff;
        }

        return rowKey;
    }

    /**
     * generate rowKey for table weibo_star_user
     * @param userIdStr
     * @param idStr to generate hashId
     * @return md5 byte[16]
     */
    public static byte[] getRowKeyForWeiboStarUser(String userIdStr) {
        return MD5.getMD5Bytes(userIdStr);
    }

    /**
     * generate rowKey for table blog_star_user
     * @param userUrl
     * @param idStr to generate hashId
     * @return md5 byte[16]
     */
    public static byte[] getRowKeyForBlogStarUser(String userUrl) {
        return MD5.getMD5Bytes(userUrl);
    }

    /**
     * generate rowKey for table xueqiu_star_user
     * @param userIdStr
     * @param idStr to generate hashId
     * @return md5 byte[16]
     */
    public static byte[] getRowKeyForXueqiuStarUser(String userIdStr) {
        return MD5.getMD5Bytes(userIdStr);
    }

    /**
     * generate rowKey for table announcement
     * @param uniqueKey
     * @param uniqueKey to generate hashId
     * @return md5 byte[16]
     */
    public static byte[] getRowKeyForAnnouncement(String uniqueKey) {
        return MD5.getMD5Bytes(uniqueKey);
    }

    /**
     * generate rowKey for table report
     * @param uniqueKey
     * @param uniqueKey to generate hashId
     * @return md5 byte[16]
     */
    public static byte[] getRowKeyForReport(String uniqueKey) {
        return MD5.getMD5Bytes(uniqueKey);
    }

    /**
     * generate rowKey for table news of 独家新闻
     * @param url
     * @return md5 byte[16]
     */
    public static byte[] getRowKeyForDujiaNews(String url) {
        return MD5.getMD5Bytes(url);
    }

    /**
     * generate rowKey for table deep_read
     * @param url to generate hashId
     * @return md5 byte[16]
     */
    public static byte[] getRowKeyForDeepRead(String url) {
        return MD5.getMD5Bytes(url);
    }

    /**
     * generate rowKey for table chuanwen
     * @param url
     * @param url to generate hashId
     * @return md5 byte[16]
     */
    public static byte[] getRowKeyForChuanWen(String url) {
        return MD5.getMD5Bytes(url);
    }

    /**
     * generate rowKey for table bin_file
     * @param url
     * @param url to generate hashId
     * @return md5 byte[16]
     */
    public static byte[] getRowKeyForBinFile(String url) {
        return MD5.getMD5Bytes(url);
    }


    /**
     * get the rowkey for table user_subscribe using userIdStr tag
     * @param userIdStr
     * @param tag
     * @return byte[16] rowkey: userIdStr+tag+0x00 0x00 0x00 0x00
     */
    public static byte[] getRowKeyForUserSubscribe(String userIdStr, String tag) {
        byte[] rowKey =  new byte[16];

        byte[] userIdBytes = MD5.getSubMD5Bytes(userIdStr, 0, app_user_id_length);

        int i = 0;

        for(int j = 0; j < app_user_id_length; j++,i++){
            rowKey[i] = userIdBytes[j];
        }

        byte[] tagBytes = MD5.getSubMD5Bytes(tag, 0, tag_length);

        for(int j = 0; j < tag_length; j++,i++){
            rowKey[i] = tagBytes[j];
        }

        return rowKey;
    }

    /**
     * get the prefix of rowkey for table user_subscribe using userIdStr
     * @param userIdStr
     * @return byte[16] rowkey: userIdStr+0x00 0x00 0x00 0x00 0x00 0x00 0x00 0x00
     */
    public static byte[] getPrefixRowKeyForUserSubscribe(String userIdStr) {
        byte[] rowKey =  new byte[16];

        byte[] userIdBytes = MD5.getSubMD5Bytes(userIdStr, 0, app_user_id_length);

        int i = 0;

        for(int j = 0; j < app_user_id_length; j++,i++){
            rowKey[i] = userIdBytes[j];
        }


        return rowKey;
    }

    /**
     * get the start rowkey for table user_subscribe to get all the tag subscribed for the given userIdStr
     * @param userIdStr
     * @return byte[16] rowkey: userIdStr+0x00 0x00 0x00 0x00 0x00 0x00 0x00 0x00
     */
    public static byte[] getStartRowKeyForUserSubscribe(String userIdStr) {
        return getPrefixRowKeyForUserSubscribe(userIdStr);
    }


    /**
     * get the stop rowkey for table user_subscribe to get all the tag subscribed for the given userIdStr
     * @param userIdStr
     * @return byte[16] rowkey: userIdStr+0xff 0xff 0xff 0xff 0xff 0xff 0xff 0xff
     */
    public static byte[] getStopRowKeyForUserSubscribe(String userIdStr) {
        byte[] rowKey = getPrefixRowKeyForUserSubscribe(userIdStr);

        for(int i = app_user_id_length; i < rowKey.length; i++){
            rowKey[i] = (byte) 0xff;
        }

        return rowKey;
    }

    /**
     * get the rowkey for table user_channel_history using userIdStr tag
     * @param userIdStr
     * @param channel
     * @return byte[16] rowkey: userIdStr+channel+0x00 0x00 0x00 0x00 0x00 0x00
     */
    public static byte[] getRowKeyForUserChannelHistory(String userIdStr, String channel) {
        byte[] rowKey =  new byte[16];

        byte[] userIdBytes = MD5.getSubMD5Bytes(userIdStr, 0, app_user_id_length);

        int i = 0;

        for(int j = 0; j < app_user_id_length; j++,i++){
            rowKey[i] = userIdBytes[j];
        }

        byte[] tagBytes = MD5.getSubMD5Bytes(channel, 0, juniorchina_channel_length);

        for(int j = 0; j < juniorchina_channel_length; j++,i++){
            rowKey[i] = tagBytes[j];
        }

        return rowKey;
    }

    /**
     * get rowkey for table user_favorite_news using userIdStr, timestamp
     * @param userIdStr
     * @param timestamp the timestamp in milliseconds, if = -1 only keyword to be the prefix
     * @return byte[16] rowkey: userIdStr+timestamp
     */
    public static byte[] getRowKeyForUserFavoriteNews(String userIdStr, long timestamp) {
        byte[] rowKey =  new byte[16];

        byte[] userIdBytes = MD5.getSubMD5Bytes(userIdStr, 0, app_user_id_length);

        int i = 0;

        for(int j = 0; j < app_user_id_length; j++,i++){
            rowKey[i] = userIdBytes[j];
        }

        byte[] timestampBytes = reverseTimestamp(timestamp);
        for(int j = 0; j < timestamp_length; j++,i++){
            rowKey[i] = timestampBytes[j];
        }

        return rowKey;
    }


    public static void main(String[] args) throws Exception {


        int cnt = 1;

        long start;
        long end;

        start = System.currentTimeMillis();
        for (int i = 0; i < cnt; i++) {
            getRowKeyForNewsRaw("金融街","股票",System.currentTimeMillis(),"http://stock.jrj.com.cn/2015/01/09141718673449.shtml");
        }
        end = System.currentTimeMillis();
        System.out.println("time:\t" + (end - start));

        start = System.currentTimeMillis();
        for (int i = 0; i < cnt; i++) {
            getPrefixRowKeyForNewsRaw("金融街","股票",-1);
        }
        end = System.currentTimeMillis();
        System.out.println("time:\t" + (end - start));

        start = System.currentTimeMillis();
        for (int i = 0; i < cnt; i++) {
            getRowKeyForNews("股票",System.currentTimeMillis());
        }
        end = System.currentTimeMillis();
        System.out.println("time:\t" + (end - start));


        start = System.currentTimeMillis();
        for (int i = 0; i < cnt; i++) {
            getRowKeyForSearchWeibo("华发股份",System.currentTimeMillis(),"3800754470572118");
        }
        end = System.currentTimeMillis();
        System.out.println("time:\t" + (end - start));


        start = System.currentTimeMillis();
        for (int i = 0; i < cnt; i++) {
            getPrefixRowKeyForSearchWeibo("华发股份",System.currentTimeMillis());
        }
        end = System.currentTimeMillis();
        System.out.println("time:\t" + (end - start));


        start = System.currentTimeMillis();
        for (int i = 0; i < cnt; i++) {
            getRowKeyForStarUserWeibo("2419711945",System.currentTimeMillis(),"3800754470572118");
        }
        end = System.currentTimeMillis();
        System.out.println("time:\t" + (end - start));


        start = System.currentTimeMillis();
        for (int i = 0; i < cnt; i++) {
            getPrefixRowKeyForStarUserWeibo("2419711945",-1);
        }
        end = System.currentTimeMillis();
        System.out.println("time:\t" + (end - start));




        start = System.currentTimeMillis();
        for (int i = 0; i < cnt; i++) {
            getRowKeyForUserRecRealtime("2419711945", 23770304,"e624901d7ffffeb3eed1893f47d0fb03");
        }
        end = System.currentTimeMillis();
        System.out.println("time:\t" + (end - start));

        start = System.currentTimeMillis();
        for (int i = 0; i < cnt; i++) {
            getPrefixRowKeyForUserRecRealtime("2419711945",23770304);
        }
        end = System.currentTimeMillis();
        System.out.println("time:\t" + (end - start));


        start = System.currentTimeMillis();
        for (int i = 0; i < cnt; i++) {
            getRowKeyForUserRecHistory("2419711945", "e624901d7ffffeb3eed1893f47d0fb03");
        }
        end = System.currentTimeMillis();
        System.out.println("time:\t" + (end - start));

        start = System.currentTimeMillis();
        for (int i = 0; i < cnt; i++) {
            getPrefixRowKeyForUserRecHistory("2419711945");
        }
        end = System.currentTimeMillis();
        System.out.println("time:\t" + (end - start));


        start = System.currentTimeMillis();
        for (int i = 0; i < cnt; i++) {
            getRowKeyForTagItem("机器人", 23770304,"2419711945");
        }
        end = System.currentTimeMillis();
        System.out.println("time:\t" + (end - start));

        start = System.currentTimeMillis();
        for (int i = 0; i < cnt; i++) {
            getPrefixRowKeyForTagItem("机器人", 23770304);
        }
        end = System.currentTimeMillis();
        System.out.println("time:\t" + (end - start));

        start = System.currentTimeMillis();
        for (int i = 0; i < cnt; i++) {
            getRowKeyForUserTag("e624901d7ffffeb3eed1893f47d0fb03");
        }
        end = System.currentTimeMillis();
        System.out.println("time:\t" + (end - start));

        start = System.currentTimeMillis();
        for (int i = 0; i < cnt; i++) {
            getRowKeyForTagUser("机器人");
        }
        end = System.currentTimeMillis();
        System.out.println("time:\t" + (end - start));



        start = System.currentTimeMillis();
        for (int i = 0; i < cnt; i++) {
            getRowKeyForUserSubscribe("2419711945", "机器人");
        }
        for (int i = 0; i < cnt; i++) {
            getStartRowKeyForUserSubscribe("2419711945");
        }
        for (int i = 0; i < cnt; i++) {
            getStopRowKeyForUserSubscribe("2419711945");
        }
        end = System.currentTimeMillis();
        System.out.println("time:\t" + (end - start));



//		UUID uuid = UUID.randomUUID();
//		System.out.println (uuid);
//		System.out.println (ByteUtil.encodeHex(ByteUtil.subBytes(ByteUtil.toBytes(uuid.getMostSignificantBits()),0, 2)));
//		System.out.println (Bytes.toHex(ByteUtil.subBytes(Bytes.toBytes(uuid.getMostSignificantBits()),0, 2)));
//		System.out.println ((Long.toHexString(uuid.getMostSignificantBits()).substring(0, 4)));
//		System.currentTimeMillis();

//		long timeStamp = DateUtil.parse("2015-01-08 13:41:43","yyyy-MM-dd HH:mm:ss").getTime();
//		long timeStamp1 = DateUtil.parse("2015-01-08 13:41:44","yyyy-MM-dd HH:mm:ss").getTime();
//		System.out.println(ByteUtil.encodeHex(reverseTimestamp(timeStamp)));
//		System.out.println(ByteUtil.encodeHex(reverseTimestamp(timeStamp1)));

//
//		System.out.println(DateUtil.parse(timeStamp));
//		System.out.println(timeStamp);
//		System.out.println(DateUtil.getNow().getTime());
//		System.out.println(System.currentTimeMillis());





    }
}
