package com.zhx.hwtcommon.common.util;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.springframework.util.StringUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.File;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;
import java.util.Map.Entry;

/**
 * gaoxiang by 2018/5/11 18:13
 */


public class HttpsClient {
    /**
     * @Description 处理http请求的post方法
     * @param url
     *            :url
     * @param params
     *            :参数
     * @return 返回的字符串
     */
    public static String post(String url, Hashtable<String, String> params) {
        CloseableHttpClient httpClient = null;
        HttpPost httpPost = null;
        String result = "";
        try {
            httpClient = HttpClients.createDefault();
            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(4000).setConnectTimeout(4000).build();
            httpPost = new HttpPost(url);
            httpPost.setConfig(requestConfig);
            List<NameValuePair> ps = new ArrayList<NameValuePair>();
            for (String pKey : params.keySet()) {
                ps.add(new BasicNameValuePair(pKey, params.get(pKey)));
            }
            httpPost.setEntity(new UrlEncodedFormEntity(ps, "utf-8"));
            CloseableHttpResponse response = httpClient.execute(httpPost);
            HttpEntity httpEntity = response.getEntity();

            result = EntityUtils.toString(httpEntity, "utf-8");

        } catch (ClientProtocolException e) {
            result = "";
        } catch (IOException e) {
            result = "";
        } finally {
            try {
                if (httpPost != null) {
                    httpPost.releaseConnection();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                result = "";
            }
        }
        return result;
    }

    /**
     * @Description 处理http请求的get方法
     * @param url
     *            :url
     * @param params
     *            :参数
     * @return 返回的字符串
     */
    public static String get(String url, Hashtable<String, String> params) {
        CloseableHttpClient httpClient = null;
        HttpGet httpGet = null;

        String result = "";
        try {
            httpClient = HttpClients.createDefault();
            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(4000).setConnectTimeout(4000).build();
            String ps = "";
            for (String pKey : params.keySet()) {
                if (!"".equals(ps)) {
                    ps = ps + "&";
                }
                // 处理特殊字符，%替换成%25，空格替换为%20，#替换为%23
                String pValue = params.get(pKey).replace("%", "%25")
                        .replace(" ", "%20").replace("#", "%23");
                ps += pKey + "=" + pValue;
            }
            if (!"".equals(ps)) {
                url = url + "?" + ps;
            }
            httpGet = new HttpGet(url);
            httpGet.setConfig(requestConfig);
            CloseableHttpResponse response = httpClient.execute(httpGet);
            HttpEntity httpEntity = response.getEntity();
            result = EntityUtils.toString(httpEntity, "utf-8");
        } catch (ClientProtocolException e) {
            result = "";
        } catch (IOException e) {
            result = "";
        } catch (Exception e) {
            result = "";
        } finally {
            try {
                if (httpGet != null) {
                    httpGet.releaseConnection();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                result = "";
            }
        }
        return result;
    }

    /**
     * @Description 处理https请求的post方法
     * @param url
     *            :url
     * @param params
     *            :参数
     * @return 返回的字符串
     */
    public static String postSSL(String url, Hashtable<String, String> params ,String token) {
        CloseableHttpClient httpClient = null;
        HttpPost httpPost = null;
        String result = "";
        try {
            httpClient = (CloseableHttpClient) wrapClient();
            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(4000).setConnectTimeout(4000).build();
            httpPost = new HttpPost(url);
            httpPost.setConfig(requestConfig);
            httpPost.addHeader("Accept","application/vnd.aon.youxuan+json; version=1.0");
            httpPost.addHeader(HTTP.CONTENT_TYPE,"application/json; version=1.0");

//            httpPost.addHeader("Accept-Language","zh-CN,zh;q=0.9");
//            httpPost.addHeader("Accept-Encoding","gzip, deflate, br");
//            httpPost.addHeader("Connection","keep-alive");
//            httpPost.addHeader("Cookie","yikikata=000217fc414cc4cc-f98ee58cd65281943b83d7fffc85ab9b; yikikata=000217fc4cc92a91-d92be81485f8000182620bb1e2d426a6; JSESSIONID=VsfogAxzEESo7InLz-OP0zefb3D1GRPZny8Gak8nscE1-6hb4uds!-642530194");
//            httpPost.addHeader("Host","fpcy.tax.ln.cn");
//            httpPost.addHeader("Referer","https://inv-veri.chinatax.gov.cn/");
//            httpPost.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.26 Safari/537.36 Core/1.63.5702.400 QQBrowser/10.2.1893.400");
            if(!StringUtils.isEmpty(token)){
                httpPost.addHeader("Authorization", "Bearer " + token);
            }

            String message = JsonUtils.toJson(params);
//            message= URLEncoder.encode(message, "UTF-8");
            StringEntity stringEntity = new StringEntity(message,"UTF-8");
            stringEntity.setContentType("application/json");
            httpPost.setEntity(stringEntity);
            CloseableHttpResponse response = httpClient.execute(httpPost);
            HttpEntity httpEntity = response.getEntity();

            result = EntityUtils.toString(httpEntity, "utf-8");

        } catch (ClientProtocolException e) {
            result = "";
        } catch (IOException e) {
            result = "";
        } finally {
            try {
                if (httpPost != null) {
                    httpPost.releaseConnection();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                result = "";
            }
        }
        return result;
    }

    /**
     * @Description 处理https请求的get方法
     * @param url
     *            :url
     * @param params
     *            :参数
     * @return 返回的字符串
     */
    public static String getSSL(String url, Hashtable<String, String> params) {
        CloseableHttpClient httpClient = null;
        HttpGet httpGet = null;

        String result = "";
        try {
            httpClient = (CloseableHttpClient) wrapClient();
            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(4000).setConnectTimeout(4000).build();
            String ps = "";
            for (String pKey : params.keySet()) {
                if (!"".equals(ps)) {
                    ps = ps + "&";
                }
                // 处理特殊字符，%替换成%25，空格替换为%20，#替换为%23
                String pValue = params.get(pKey).replace("%", "%25")
                        .replace(" ", "%20").replace("#", "%23");
                ps += pKey + "=" + pValue;
            }
            if (!"".equals(ps)) {
                url = url + "?" + ps;
            }
            httpGet = new HttpGet(url);
            httpGet.setConfig(requestConfig);
            httpGet.addHeader("Accept","*/*");
            httpGet.addHeader("Accept-Language","zh-CN,zh;q=0.9");
            httpGet.addHeader("Accept-Encoding","gzip, deflate, br");
            httpGet.addHeader("Connection","keep-alive");
            httpGet.addHeader("Cookie","yikikata=000217fc414cc4cc-f98ee58cd65281943b83d7fffc85ab9b; yikikata=000217fc4cc92a91-d92be81485f8000182620bb1e2d426a6; JSESSIONID=VsfogAxzEESo7InLz-OP0zefb3D1GRPZny8Gak8nscE1-6hb4uds!-642530194");
//            httpGet.addHeader("Host","fpcy.tax.ln.cn");
            httpGet.addHeader("Referer","https://inv-veri.chinatax.gov.cn/");
            httpGet.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.26 Safari/537.36 Core/1.63.5702.400 QQBrowser/10.2.1893.400");

            CloseableHttpResponse response = httpClient.execute(httpGet);
            HttpEntity httpEntity = response.getEntity();
            result = EntityUtils.toString(httpEntity, "utf-8");
        } catch (ClientProtocolException e) {
            result = "";
        } catch (IOException e) {
            result = "";
        } catch (Exception e) {
            result = "";
        } finally {
            try {
                if (httpGet != null) {
                    httpGet.releaseConnection();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                result = "";
            }
        }
        return result;
    }




    /**
     * @Description 创建一个不进行正式验证的请求客户端对象 不用导入SSL证书
     * @return HttpClient
     */
    public static HttpClient wrapClient() {
        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] arg0,
                                               String arg1) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] arg0,
                                               String arg1) throws CertificateException {
                }
            };
            ctx.init(null, new TrustManager[] { tm }, null);
            SSLConnectionSocketFactory ssf = new SSLConnectionSocketFactory(
                    ctx, NoopHostnameVerifier.INSTANCE);
            CloseableHttpClient httpclient = HttpClients.custom()
                    .setSSLSocketFactory(ssf).build();
            return httpclient;
        } catch (Exception e) {
            return HttpClients.createDefault();
        }
    }

    /**
     * @Description 创建一个HTTPEntity对象
     * @param params
     *            :字符串的入参
     * @param files
     *            :大数据的入参
     * @return HttpClient
     */
    public static HttpEntity makeMultipartEntity(List<NameValuePair> params,
                                                 Map<String, File> files) {

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE); // 如果有SocketTimeoutException等情况，可修改这个枚举
        if (params != null && params.size() > 0) {
            for (NameValuePair p : params) {
                builder.addTextBody(p.getName(), p.getValue(),
                        ContentType.TEXT_PLAIN.withCharset("UTF-8"));
            }
        }

        if (files != null && files.size() > 0) {
            Set<Entry<String, File>> entries = files.entrySet();
            for (Entry<String, File> entry : entries) {
                builder.addPart(entry.getKey(), new FileBody(entry.getValue()));
            }
        }

        return builder.build();

    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

        String url = "https://uatbizapi.yianyouxuan.com/api/oauth2/token/";
        Hashtable<String, String> params = new Hashtable<String, String>();
        params.put("client_id", "F8PgTllR8AAR9ry3NJ3HJGSUSbrmrWOXXJjFSVCl");
        params.put("client_secret", "lEihGg74JEXWS6OOKPq0lRjAdNlLpxtjzVLx2yWGjE7xRcrzr0f7mSXVPZnsONI8ZqGzqGyib0X5ExiLsBsxJloYVAkKzgKMW6kHu2fXdbRvS3U1nTe2W0FACwatzds8");
        params.put("grant_type", "client_credentials");
        params.put("scope", "");
        String a = postSSL(url, params,"");
        System.out.println("值为：" + a);




    }
}
