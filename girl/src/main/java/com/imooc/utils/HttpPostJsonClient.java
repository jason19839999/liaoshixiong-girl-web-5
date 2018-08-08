package com.imooc.utils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;

public class HttpPostJsonClient {
    private static final Logger logger = LoggerFactory
            .getLogger(HttpPostJsonClient.class);

    final static ObjectMapper objectMapper = getJacksonMapper();

    static String url;
    static String postJson;

    public static String server_ip;
    public static String server_port;

    static {
//		server_ip="106.2.174.5";
//		server_port="23501";
        server_ip = "app.investassistant.com";
        server_port = "80";
//		server_ip="192.168.2.218";
//		server_port="8080";
    }

    public HttpPostJsonClient(String url, String postJson) {
        this.url = url;
        this.postJson = postJson;
    }

    HttpPostJsonClient() {
    }

    static HttpPost getHttpPost() throws UnsupportedEncodingException {
        HttpPost httppost = new HttpPost(url);
        StringEntity entity = new StringEntity(postJson, "utf-8");
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        httppost.setEntity(entity);
        return httppost;
    }

    public String HttpPost() throws UnsupportedEncodingException {
        HttpPost httppost = getHttpPost();
        CloseableHttpClient httpClient = HttpClients.createDefault();

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(100000).setConnectionRequestTimeout(5000)
                .setSocketTimeout(100000).build();
        httppost.setConfig(requestConfig);

        CloseableHttpResponse reponse = null;
        String resData = null;
        try {
            reponse = httpClient.execute(httppost);
            resData = EntityUtils.toString(reponse.getEntity(), "UTF-8");
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            logger.error("HttpPost==" + e);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            logger.error("HttpPost==" + e);
        } finally {
            try {
                if (reponse != null)
                    reponse.close();
                if (httpClient != null)
                    httpClient.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                logger.error("HttpPost==" + e);
            }
        }
        return resData;
    }

    public static String HttpGzipPost() throws IOException {
        for (int i = 0; i < 6; i++) {
            Result r = getResponse(url, postJson);
            if (r != null && r.success) {
                return r.data;
            }
        }
        return null;
    }

    private static Result getResponse(String u, String json) throws IOException {
        HttpURLConnection httpConn = null;
        InflaterInputStream is = null;
        ByteArrayOutputStream bo = null;
        try {
            URL url = new URL(u);
            httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setConnectTimeout(100000);
            httpConn.setReadTimeout(100000);    //--TODO 超时设多少？
            httpConn.setDoOutput(true); // 需要输出
            httpConn.setDoInput(true); // 需要输入
            httpConn.setUseCaches(false); // 不允许缓存
            httpConn.setRequestMethod("POST"); // 设置POST方式连接
            httpConn.setRequestProperty("Content-Type", "application/x-gzip");
            // 连接,也可以不用明文connect，使用下面的httpConn.getOutputStream()会自动connect
            httpConn.connect();
            DeflaterOutputStream dos = new DeflaterOutputStream(httpConn.getOutputStream());
            dos.write(json.getBytes());
            dos.finish();
            dos.flush();
            dos.close();
            // 获得响应状态
            int resultCode = httpConn.getResponseCode();
            if (HttpURLConnection.HTTP_OK == resultCode) {
                System.out.println(httpConn.getContentType());
                is = new InflaterInputStream(httpConn.getInputStream());
                byte[] stringBuffer = new byte[1024 * 1024 * 20];    //-TODO 会有超过这个大小的文件么？
                bo = new ByteArrayOutputStream();
                int size = 0;
                while (true) {
                    size = is.read(stringBuffer);
                    if (size < 0)
                        break;
                    bo.write(stringBuffer, 0, size);
                }
                bo.flush();

                byte[] last = bo.toByteArray();
                return new Result(new String(last), true);
            }
        } catch (Exception e) {
            e.printStackTrace();
//			if (e.toString().equals("java.io.EOFException: Unexpected end of ZLIB input stream")){
            return new Result(null, false);
//			}
        } finally {
            try {
                if (bo != null) {
                    bo.close();
                }
                if (is != null) {
                    is.close();
                }
                if (httpConn != null) {
                    httpConn.disconnect();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                logger.error("HttpGzipPost==" + e);
            }
        }
        return new Result(null, false);
    }

    static class Result {
        String data;
        Boolean success;

        public Result(String data, Boolean success) {
            this.data = data;
            this.success = success;
        }
    }



    public static void main(String[] args) throws Exception {
//		testData(args);
//		getAllRealTimeStockPrice();
//		logger.info(Boolean.toString(isTradingDay(new Date())));
//		logger.info(DateUtil.getDate(2015, 3, 31).toString());
//		double index=HttpPostJsonClient.getHistStockPrice(DateUtil.getDate(2015, 3, 31), "SESH", "000300");
//		System.out.println(index);
//		System.out.println(getAllRealtimeStockPriceStr());
//		System.out.println(getHistStockPrice(DateUtil.getDate(2015,5,7), "SESH", "601766"));
//		System.out.println(getAllHistStockPrice(DateUtil.getDate(2015,5,7)).get("SESH_601766").getClose());
//		System.out.println(HttpPostJsonClient.getStockIdByExchCode("SESZ", "000001"));
        logger.info(getStockTimerStr("SESH", "000300"));
    }



    /*    "header" : {
            "version" : 1,
                    "imei" : "046097B8690EA0D2DDFC76CA05D957C8",
                    "user_type" : 1,
                    "user_name" : "15699885506",
                    "system_time" : 1508985271271,
                    "response_code" : 0,
                    "ua" : {
                "platform" : "android",
                        "model" : "HM NOTE 1LTE",
                        "os_version" : "19",
                        "width" : 720,
                        "height" : 1280,
                        "app_version" : "1.6.4",
                        "trader" : "mining_t",
                        "language" : "zh_HK"
            }
        }*/
    static Map getInitialMap() {
        Map map = new HashMap();
        /** RequestHeader header = new RequestHeader();
        header.setVersion(1);
        header.setImei("046097B8690EA0D2DDFC76CA05D957C8");
        header.setKeyCode("1B1D9D39F50EE4302D65A3438FD43067");
        header.setUserType(1);
        header.setUserName("15699885506");
        header.setAuthCode("345C51A23487E33CC0E72601B855C1F2");
        header.setSystemTime(DateUtil.getNow().getTime());
        map.put("header", header); **/
        return map;
    }


    static JSONObject getInitialJson() {
        //Map map = new HashMap();
        /*RequestHeader header = new RequestHeader();
        header.setVersion(1);
        header.setImei("046097B8690EA0D2DDFC76CA05D957C8");
        header.setKeyCode("1B1D9D39F50EE4302D65A3438FD43067");
        header.setUserType(1);
        header.setUserName("15699885506");
        header.setAuthCode("345C51A23487E33CC0E72601B855C1F2");
        header.setSystemTime(1422263479031L);
        //map.put("header", header);
        */

        JSONObject header = new JSONObject();
        header.put("version", 1);
        header.put("imei", "046097B8690EA0D2DDFC76CA05D957C8");
        header.put("key_code", "1B1D9D39F50EE4302D65A3438FD43067");
        header.put("user_type", 1);
        header.put("user_name", "15699885506");
        header.put("auth_code", "345C51A23487E33CC0E72601B855C1F2");
        header.put("system_time", DateUtil.getNow().getTime());

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("header", header);
        return jsonObject;
    }

    /**
     * @param date     :
     * @param exchange
     * @param code
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public static double getHistStockPrice(Date date, String exchange, String code) throws Exception {

        Map map = getInitialMap();
        Map request_data = new HashMap<>();
        request_data.put("date", DateUtil.format(date, "yyyy/MM/dd"));
        request_data.put("exchange", exchange);
        request_data.put("code", code);

        map.put("request_data", request_data);

        String json = null;
        try {
            json = objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String url = "http://" + server_ip + ":" + server_port + "/MiningStock/hot/getSpecialStockInfo";

        try {
            String res = new HttpPostJsonClient(url, json).HttpPost();
//			logger.info(res);
            JSONObject jsonObject = JSONObject.parseObject(res);
            String response_data = jsonObject.getString("response_data");
            JSONObject jsonObject2 = JSONObject.parseObject(response_data);
            double close_px = jsonObject2.getDoubleValue("close_px");
            if (close_px <= 0) {
                logger.error("Price of " + exchange + "_" + code + " " + DateUtil.format(date, "yyyy/MM/dd") + "<=0");
                return 1d;
            }
            return close_px;
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new Exception("Server Error.");
        }

    }

    public static String getAllRealtimeStockPriceStr(String... market) throws Exception {
        return getAllStockPrice(new Date(), RequestType.RealTime, market);
    }

    private static String getAllHistStockPriceStr(Date date) throws Exception {
        return getAllStockPrice(date, RequestType.Hist);

    }

    public static String getAllStockPrice(Date date, RequestType type, String... market) throws Exception {
        Map map = getInitialMap();

        HashMap<Object, Object> request_data = new HashMap<>();
        request_data.put("type", type.getValue());
        request_data.put("date", DateUtil.format(date, "yyyy/MM/dd"));

        //TODO market可变参数传市场
        if (market != null && market.length != 0 && market[0].equals("HK")) {
            request_data.put("market", "HK");
        } else if (market != null && market.length != 0 && market[0].equals("US")) {
            request_data.put("market", "US");
        }

        map.put("request_data", request_data);

        String json = null;
        try {
            json = objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        /*MiningStock*/
        String url = "http://" + server_ip + ":" + server_port + "/MiningStock/hot/getStockInfo";

        try {
            String res = new HttpPostJsonClient(url, json).HttpGzipPost();
            //logger.info("HttpPostJsonClient_getAllStockPrice_res:" + res);
            return res;

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new Exception("Server Error");
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception("Server Error");
        }

    }


    public static String getStockProfile(String exchage, String code) throws Exception {

        Map map = getInitialMap();

        HashMap<Object, Object> request_data = new HashMap<>();
        request_data.put("exchange", exchage);
        request_data.put("code", code);

        map.put("request_data", request_data);

        String json = null;
        try {
            json = objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        /*MiningStock*/
        String url = "http://" + server_ip + ":" + server_port + "/MiningStock/stock/getStockProfile";

        try {
            String res = new HttpPostJsonClient(url, json).HttpPost();
            return res;
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new Exception("Server Error");
        }
    }


    public static String getIndexInfoList(ArrayList<String> listExchangeCode) throws Exception {
        JSONObject json_request = getInitialJson();

        JSONArray index_array = new JSONArray();

        for (String excode : listExchangeCode) {
            String strs[] = excode.split("_");
            String exchange = strs[0];
            String code = strs[1];
            JSONObject jsonIndexSingle = new JSONObject();
            jsonIndexSingle.put("exchange", exchange);
            jsonIndexSingle.put("code", code);
            index_array.add(jsonIndexSingle);
        }
        JSONObject request_data = new JSONObject();
        request_data.put("index", index_array);
        request_data.put("show_type", 1);


        json_request.put("request_data", request_data);


        String strJson = json_request.toJSONString();
        String url = "http://" + server_ip + ":" + server_port + "/MiningStock/layout/getIndexInfo";

        try {
            String res = new HttpPostJsonClient(url, strJson).HttpPost();

            return res;
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new Exception("Server Error");
        }

    }

    //--TODO 去掉stockId
    public static String getStockIdByExchCode(String exchange, String code) throws Exception {
        Map map = getInitialMap();

        HashMap<Object, Object> request_data = new HashMap<>();
        request_data.put("code", code);
        request_data.put("exchange", exchange);

        map.put("request_data", request_data);

        String json = null;
        try {
            json = objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String url = "http://" + server_ip + ":" + server_port + "/MiningStock/stock/getStockId";
        try {
            String res = new HttpPostJsonClient(url, json).HttpPost();
            //int stock_id=JSONObject.parseObject(res).getJSONObject("response_data").getJSONObject("stock_info").getIntValue("stock_id");
            return res;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new Exception("Server Error");
        }
    }

/*	public static String getStockTimerStr(String exchange, String code) throws Exception{
        return getStockTimerStr(getStockIdByExchCode(exchange, code));
	}*/


    public static boolean isTradingDay(Date date, String... market) throws Exception {
        Map map = getInitialMap();

        HashMap<Object, Object> request_data = new HashMap<>();
        request_data.put("time_info", DateUtil.format(date, "yyyyMMddHHmmss"));

        //market可变参数传市场，不传默认A股
        if (market != null && market.length != 0 && market[0].equals("HK")) {
            request_data.put("market", "HK");
        } else if (market != null && market.length != 0 && market[0].equals("US")) {
            request_data.put("market", "US");
        }

        map.put("request_data", request_data);

        String json = null;
        try {
            json = objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String url = "http://" + server_ip + ":" + server_port + "/MiningStock/stock/isStockTime";

        try {
            String res = new HttpPostJsonClient(url, json).HttpPost();
            int result = JSONObject.parseObject(res).getJSONObject("response_data").getInteger("is_stockdate"); //is_stocktime
            return result == 1;
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new Exception("Server Error");
        }

    }

    /**
     * 判断是否是交易时间，按app的交易时间走
     *
     * @param date
     * @param market
     * @return
     * @throws Exception
     */
    public static boolean isTradingTime(Date date, String... market) throws Exception {
        Map map = getInitialMap();

        HashMap<Object, Object> request_data = new HashMap<>();
        request_data.put("time_info", DateUtil.format(date, "yyyyMMddHHmmss"));

        //market可变参数传市场，不传默认A股
        if (market != null && market.length != 0 && market[0].equals("HK")) {
            request_data.put("market", "HK");
        } else if (market != null && market.length != 0 && market[0].equals("US")) {
            request_data.put("market", "US");
        }

        map.put("request_data", request_data);

        String json = null;
        try {
            json = objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String url = "http://" + server_ip + ":" + server_port + "/MiningStock/stock/isStockTime";

        try {
            String res = new HttpPostJsonClient(url, json).HttpPost();
            int result = JSONObject.parseObject(res).getJSONObject("response_data").getInteger("is_stocktime"); //is_stocktime
            return result == 1; //1是，0否
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new Exception("Server Error");
        }

    }

    /**
     * 判断是否是开盘时间，按行情时间走
     *
     * @param date
     * @param market
     * @return
     * @throws Exception
     */
    public static boolean isStockInfoTime(Date date, String... market) throws Exception {
        try {
            boolean isTradingTime = isTradingTime(date, market);

            String yyyyMMddHHmmss = DateUtil.format(date, "yyyyMMddHHmmss");    //20171102092346
            String hhmm = yyyyMMddHHmmss.substring(8, 12);  //0923
            boolean isStockInfo = true;

            //market可变参数传市场，不传默认A股
            if (market == null || market.length == 0 || market[0].equals("A")) {
                if (Integer.parseInt(hhmm) >= 914 && Integer.parseInt(hhmm) <= 931) {
                    isStockInfo = false;
                }
            } else if (market.length != 0 && market[0].equals("HK")) {
                if (Integer.parseInt(hhmm) >= 859 && Integer.parseInt(hhmm) <= 931) {
                    isStockInfo = false;
                }
            }
            return isTradingTime && isStockInfo; //1是，0否
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new Exception("Server Error");
        }

    }

/*    public static boolean isTradingDay(Date date) throws Exception {
        Map map = getInitialMap();

        HashMap<Object, Object> request_data = new HashMap<>();
        request_data.put("date", DateUtil.format(date, "yyyy/MM/dd"));

        map.put("request_data", request_data);

        String json = null;
        try {
            json = objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String url = "http://" + server_ip + ":" + server_port + "/MiningStock/hot/isTradingDay";

        try {
            String res = new HttpPostJsonClient(url, json).HttpPost();
            int result = JSONObject.parseObject(res).getJSONObject("response_data").getInteger("result");
            if (result == 1) return true;
            else return false;
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new Exception("Server Error");
        }

    }*/


    public static String getStockTimerStr(String exchange, String code) throws Exception {

        Map map = getInitialMap();

        HashMap<Object, Object> request_data = new HashMap<>();
        request_data.put("exchange", exchange);
        request_data.put("code", code);

        map.put("request_data", request_data);

        String json = null;
        try {
            json = objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

//		String url =  "http://"+server_ip+":"+server_port+"/MiningStock/stock/getStockTimer";
        //分时行情新接口
        String url = "http://" + server_ip + ":" + server_port + "/MiningStock/stock/getStockTimerNew";

        try {
            String res = new HttpPostJsonClient(url, json).HttpGzipPost();
            return res;

        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new Exception("Server Error");
        }
    }

    protected static ObjectMapper getJacksonMapper() {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        objectMapper
                .setPropertyNamingStrategy(new PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy());
        objectMapper.setSerializationInclusion(Include.NON_NULL);
        return objectMapper;
    }
}

enum RequestType {
    RealTime(0), Hist(1);

    public int getValue() {
        return value;
    }

    int value;

    private RequestType(int value) {
        this.value = value;
    }
}
