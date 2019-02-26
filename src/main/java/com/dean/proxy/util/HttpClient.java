package com.dean.proxy.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author dean
 */
@Component
public class HttpClient {

    @Value("${crawl.html.record}")
    private boolean recording;
    @Value("${http.client.proxy}")
    private boolean useProxy;
    @Value("${crawl.https.convert}")
    private boolean convertHttp;

    @Value("${verify.client.timeout}")
    int timeout;

    private static Logger log = LoggerFactory.getLogger(HttpClient.class);

    public static Map<String, String> map = new HashMap<>();

    private static Integer lastRandomNumber = null;

    static {
        // 以后把代理IP配置到数据库
        //map.put("47.96.136.190", "8118");
        //map.put("117.90.5.192", "9000");
        //map.put("112.16.28.103", "8060");
        //map.put("180.104.63.78", "9000");
        //map.put("139.196.137.255", "8118");
        //
        //map.put("183.230.177.118", "8060");
        //map.put("115.231.5.230", "44524");
        //map.put("182.61.59.147", "9999");
        //map.put("119.145.136.126", "8888");
        //map.put("123.56.86.158", "8080");
        //
        //map.put("118.178.227.171", "80");
        //map.put("182.111.64.8", "53364");
        //map.put("47.95.213.117", "80");
        //map.put("183.129.207.84", "33555");
        //map.put("115.218.221.25", "9000");
        //
        //map.put("60.171.111.113", "33069");
        //map.put("115.223.211.4", "9000");
        //map.put("49.51.68.122", "1080");
        //map.put("112.25.129.174", "41323");
        //map.put("140.207.25.114", "50750");
        //
        //map.put("218.198.117.194", "39248");
        //map.put("117.131.75.134", "80");
        //map.put("121.8.98.196", "80");
        //map.put("221.7.255.168", "8080");
        //map.put("120.76.77.152", "9999");
        //
        //map.put("117.87.177.58", "9000");
        //map.put("124.94.196.188", "9999");
        //map.put("110.52.235.76", "9999");
        //map.put("111.177.190.124", "9999");
        //map.put("111.177.175.88", "9999");
        //map.put("115.218.222.77", "9000");
    }

    public String getOkhttpHtml(String url) {
        return getOkhttpHtml(this.convertHttp, url, null, null);
    }

    public String getOkhttpHtml(boolean convertHttp, String url) {
        return getOkhttpHtml(convertHttp, url, null, null);
    }

    public String getOkhttpHtml(boolean convertHttp, String url, String proxyIp, Integer proxyPort) {
        if (convertHttp) {
            url = convertHttp(url);
        }

        //webResult.setCode(200);
        //Map.Entry<String, String> entry = getProxy();
        Response execute = null;
        ResponseBody body = null;
        try {
            OkHttpClient.Builder builder =
                new OkHttpClient.Builder()
                    .addInterceptor(new NetInterceptor());
            //设置连接超时时间  --15 Ms
            builder.connectTimeout(30, TimeUnit.SECONDS);
            builder.readTimeout(30, TimeUnit.SECONDS);
            builder.writeTimeout(30, TimeUnit.SECONDS);

            ////设置代理,需要替换
            if (StringUtils.isNotEmpty(proxyIp) && proxyPort != null) {
                Proxy proxy = new Proxy(Proxy.Type.HTTP,
                    new InetSocketAddress(proxyIp, proxyPort));
                builder.proxy(proxy);
            }
            // header
            Map<String, String> map = new HashMap<>();
            map.put("User-agent","Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");
            map.put("Accept-Language", "zh-CN,zh;q=0.8");
            Headers headers = Headers.of(map);

            Request request = new Request.Builder()
                .headers(headers)
                .url(url)
                .get()
                .build();
            execute =  builder.build().newCall(request).execute();
            body = execute.body();
            return body.string();
            //System.out.println(body.string());
            //webResult.setResult(body.string());
            //webResult.setCode(200);
            //execute.close();
        } catch (java.net.SocketTimeoutException | java.net.SocketException e) {
            //webResult.setCode(-1);
            log.info("this url socket Time out:" + url);
        } catch (java.net.UnknownHostException e) {
            //webResult.setCode(-2);
            log.info("this url unknow host:" + url);
        } catch (java.io.IOException e) {
            log.info("this url have IO exception:" + url);
        } catch (Exception e) {
            log.error("this url error -->" + url);
        } finally {
            try {
                if (body != null) {
                    body.close();
                }
                if (execute != null) {
                    execute.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }

            //
            //String proxy = useProxy ? entry.getKey() : "localhost";
        }
        //return webResult;
        return "";
    }

    public boolean verifyProxy(boolean convertHttp, String url, String proxyIp, Integer proxyPort, Integer timeoutOffset) {
        boolean result = false;
        if (convertHttp) {
            url = convertHttp(url);
        }

        //webResult.setCode(200);
        //Map.Entry<String, String> entry = getProxy();
        Response execute = null;
        ResponseBody body = null;
        try {
            OkHttpClient.Builder builder =
                new OkHttpClient.Builder()
                    // https://www.jianshu.com/p/8753188b315c
                    .addNetworkInterceptor(new NetInterceptor());
            //设置连接超时时间  --15 Ms
            builder.connectTimeout(timeout + timeoutOffset, TimeUnit.SECONDS);
            builder.readTimeout(timeout + timeoutOffset, TimeUnit.SECONDS);
            builder.writeTimeout(timeout + timeoutOffset, TimeUnit.SECONDS);

            ////设置代理,需要替换
            if (StringUtils.isNotEmpty(proxyIp) && proxyPort != null) {
                Proxy proxy = new Proxy(Proxy.Type.HTTP,
                    new InetSocketAddress(proxyIp, proxyPort));
                builder.proxy(proxy);
            }
            // header
            Map<String, String> map = new HashMap<>();
            map.put("User-agent","Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");
            map.put("Accept-Language", "zh-CN,zh;q=0.8");
            map.put("Accept-Encoding", "identity");
            Headers headers = Headers.of(map);

            Request request = new Request.Builder()
                .headers(headers)
                .url(url)
                .get()
                .build();
            execute =  builder.build().newCall(request).execute();

            int code = execute.networkResponse().code();
            body = execute.body();
            result = code >= 200 && code < 300 && body.string().length() > 10;
            //System.out.println(body.string());
            //webResult.setResult(body.string());
            //webResult.setCode(200);
            //execute.close();
        } catch (java.net.SocketTimeoutException | java.net.SocketException e) {
            //webResult.setCode(-1);
            log.info("this url socket Time out:" + url);
        } catch (java.net.UnknownHostException e) {
            //webResult.setCode(-2);
            log.info("this url unknow host:" + url);
        } catch (java.io.IOException e) {
            log.info("this url have IO exception: " + url);
        } catch (Exception e) {
            log.error("this url error -->" + url, e);
        } finally {
            try {
                if (body != null) {
                    body.close();
                }
                if (execute != null) {
                    execute.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    public boolean verifyProxy(boolean convertHttp, String url, String proxyIp, Integer proxyPort) {
        return verifyProxy(convertHttp, url, proxyIp, proxyPort, 0);
    }

    // 此方法日后需要重新设计，需要加锁
    public static Map.Entry<String, String> getProxy() {
        Random random = new Random();

        int size = map.entrySet().size();
        int a = random.nextInt(size);

        if (lastRandomNumber != null && lastRandomNumber.equals(a)) {
            getProxy();
        }
        //System.out.println(a);
        // 赋值
        lastRandomNumber = a;

        int i = 0;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (a == i) {
                return entry;
            }
            ++i;
        }


        return null;
    }

    public static String convertHttp(String url) {
        if (!url.startsWith("https")) {
            return url;
        }

        url = "http" + url.substring(5);
        return url;
    }


    class NetInterceptor implements Interceptor{
        @Override
        public Response intercept(Chain chain)throws IOException {
            Request request = chain.request().newBuilder()
                .addHeader("Connection","close")
                .build();
            return chain.proceed(request);
        }
    }


}
