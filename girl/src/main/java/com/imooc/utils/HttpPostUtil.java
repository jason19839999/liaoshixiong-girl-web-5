package com.imooc.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;

import com.alibaba.fastjson.JSONArray;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.http.message.BasicNameValuePair;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class HttpPostUtil {
    private static final Logger logger = LoggerFactory
            .getLogger(HttpPostUtil.class);

    static class miTM implements javax.net.ssl.TrustManager,
            javax.net.ssl.X509TrustManager {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public boolean isServerTrusted(
                java.security.cert.X509Certificate[] certs) {
            return true;
        }

        public boolean isClientTrusted(
                java.security.cert.X509Certificate[] certs) {
            return true;
        }

        public void checkServerTrusted(
                java.security.cert.X509Certificate[] certs, String authType)
                throws java.security.cert.CertificateException {
            return;
        }

        public void checkClientTrusted(
                java.security.cert.X509Certificate[] certs, String authType)
                throws java.security.cert.CertificateException {
            return;
        }
    }

    /**
     * get response data from post request trust all the host
     *
     * @param url
     * @param postJson
     * @return
     * @throws Exception
     */
    public static String getResponseTrust(String url, String postJson) throws Exception {
        HttpPost httpPost = null;
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        try {


            SSLContext sslcontext = SSLContext.getInstance("SSLv3");  //建立证书实体
            javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];
            javax.net.ssl.TrustManager tm = new miTM();
            trustAllCerts[0] = tm;
            sslcontext.init(null, trustAllCerts, null);
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            httpClient = HttpClients.custom().setSSLSocketFactory(sslsf).build();

            httpPost = new HttpPost(url);

            StringEntity entity = new StringEntity(postJson, "UTF-8");
            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/json;charset=utf-8");

            httpPost.setEntity(entity);

        } catch (Exception e1) {
            e1.printStackTrace();
        }

        String resData = null;
        try {
            response = httpClient.execute(httpPost);
            resData = EntityUtils.toString(response.getEntity(), "UTF-8");
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            logger.error("HttpPost==" + e);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("HttpPost==" + e);
        } finally {
            try {
                if (response != null)
                    response.close();
                if (httpClient != null)
                    httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("HttpPost==" + e);
            }
        }
        return resData;
    }


    /**
     * get response data from post request
     *
     * @param url
     * @param postJson
     * @return
     */
    public static String getResponse(String url, String postJson) {
        HttpPost httpPost = null;
        try {
            httpPost = new HttpPost(url);
            StringEntity entity = new StringEntity(postJson, "utf-8");
            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/json");
            httpPost.setEntity(entity);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resData = null;
        try {
            response = httpClient.execute(httpPost);
            resData = EntityUtils.toString(response.getEntity(), "UTF-8");
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            logger.error("HttpPost==" + e);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("HttpPost==" + e);
        } finally {
            try {
                if (response != null)
                    response.close();
                if (httpClient != null)
                    httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("HttpPost==" + e);
            }
        }
        return resData;
    }

    /**
     * get short url with baidu api
     *
     * @param longUrl
     * @return
     */
    public static String getShortUrl(String longUrl) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        String shortUrl = null;
        try {
            if (longUrl.contains("192.168.2.232")) {
                shortUrl = longUrl;
            } else {
                httpClient = HttpClients.createDefault();
                List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
                params.add(new BasicNameValuePair("url", longUrl));
                HttpPost httpPost = new HttpPost("http://dwz.cn/create.php");
                StringEntity entity = new UrlEncodedFormEntity(params, "utf-8");
                httpPost.setEntity(entity);
                response = httpClient.execute(httpPost);
                String jsonStr = EntityUtils    //测试环境域名解析不了
                        .toString(response.getEntity(), "utf-8");
                //{"status":-1,"err_msg":"暂时不支持ip域名，请重新输入","longurl":"http://192.168.2.232:8083/news/1/36d47ffffea0374f791651a3a1490000"}
                JSONObject object = JSON.parseObject(jsonStr);
                shortUrl = object.getString("tinyurl");
            }
        } catch (Exception e) {
//			shortUrl = longUrl;
            e.printStackTrace();
            logger.error("HttpPost==" + e);
        } finally {
            try {
                if (response != null)
                    response.close();
                if (httpClient != null)
                    httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("HttpPost==" + e);
            }
        }
        return shortUrl;
    }

    /**
     * 调新浪api获取短链接
     *
     * @param long_url
     * @return
     */
    public static String getShortUrl_sina(String long_url) {

        String urlString = "http://api.t.sina.com.cn/short_url/shorten.json?source=492200439&url_long=" + long_url;

        String res = "";
        String url_short = null;
        try {
            URL url = new URL(urlString);
            java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("GET");
            java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                res += line + "\n";
            }
            JSONArray jsonArray = JSONArray.parseArray(res);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            url_short = jsonObject.getString("url_short");
            in.close();
        } catch (Exception e) {
            logger.error("error in wapaction,and e is " + e.getMessage());
        }
        return url_short;
    }

    /**
     * 调用新浪api获取短链接
     * 替换appKey
     * 2849184197,202088835,211160679
     * @param url_long
     * @return
     */
    public static String getShortUrlBySina(String url_long) {
        CloseableHttpClient client = null;
        CloseableHttpResponse response = null;
        HttpGet httpGet = null;
        String url_short = null;
        String get_url = null;
        try {
            if (url_long.contains("192.168.2.232")) {
                url_short = url_long;
            } else {
                client = HttpClients.createDefault();
                get_url = "http://api.t.sina.com.cn/short_url/shorten.json?source=2849184197&url_long=" + url_long;
                httpGet = new HttpGet(get_url);
                // 设置请求和传输超时时间
                RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(2000).setConnectTimeout(2000).build();
                httpGet.setConfig(requestConfig);

                response = client.execute(httpGet);
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    HttpEntity entity = response.getEntity();
                    String strResult = EntityUtils.toString(entity, "utf-8");

                    JSONArray jsonArray = JSONArray.parseArray(strResult);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    url_short = jsonObject.getString("url_short");
                } else {
                    logger.error("短链接get失败:" + get_url);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("短链接get失败:" + get_url);
        } finally {
            try {
                if (httpGet != null) {
                    httpGet.releaseConnection();
                }
                if (response != null) {
                    response.close();
                }
                if (client != null) {
                    client.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("HttpPost==" + e);
            }
        }
        return url_short;
    }

    public static void main(String[] args) throws Exception {
/*        logger.info(HttpPostUtil
                .getShortUrlBySina("http://t.financialdatamining.com/news/1/sentiment_result_morning_20151126.html"));*/

/*        String data = "{\"segText\":\"本/r 周/nr 股市/n 三大猜想/NNT 及/c 应对/SENTIMENT 策略/n ：/w 大盘/n 再度/d 崛起/v ？/w\",\"filterNoise\":true}";
        String url = "https://localhost:8000/queries.json";
        String response = HttpPostUtil.getResponseTrust(url, data);
        System.err.println(response);*/

        String shortUrl = HttpPostUtil.getShortUrlBySina("http://t.financialdatamining.com/news/1/481c7ffffe9fad98a1c6cba837510000");
        logger.info(shortUrl);
    }
}
