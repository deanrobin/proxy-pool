package com.dean.proxy.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.dean.proxy.bean.Proxy;
import com.dean.proxy.constant.AnonymityEnum;
import com.dean.proxy.service.AbstractProxyService;
import com.dean.proxy.util.IPUtil;
import com.google.common.collect.Maps;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import static sun.jvm.hotspot.debugger.win32.coff.DebugVC50X86RegisterEnums.TAG;

/**
 * @author dean
 */
@Service
public class DaiLi89Service extends AbstractProxyService {

    final static String HTTP = "http";

    @Override
    public String getURL() {
        return "http://www.89ip.cn/";
    }

    @Override
    public List<Proxy> reptileProxy(String html) {
        List<Proxy> list = new ArrayList<>();

        Document doc = Jsoup.parse(html);

        Element table = doc.select("table.layui-table").first();
        Element body = table.child(1);
        for (Element element : body.children()) {
            String ip = element.child(0).text();
            Integer port = Integer.valueOf(element.child(1).text());
            String location = element.child(2).text();
            String source = element.child(3).text();

            String internal = getProxyId() + IPUtil.deletePoint(ip);
            Proxy proxy = new Proxy(ip, port, AnonymityEnum.NORMAL.getCode(), HTTP,
                source, COUNTRY, location, 0L, internal);
            list.add(proxy);
        }

        return list;
    }

    @Override
    public String getProxyId() {
        return "89D";
    }

    public static String proxyUrl = "http://innerproxyhk.okcoin.org";

    public static String nodeUrl = "https://api.zilliqa.com/";

    public static void main(String[] args) throws IOException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", "1");
        jsonObject.put("jsonrpc", "2.0");
        jsonObject.put("method", "GetNetworkId");

        JSONArray array = new JSONArray();
        //array.add("518452");
        jsonObject.put("params", array);

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), jsonObject.toJSONString());

        Request.Builder requestBuilder = new Request.Builder().url(proxyUrl).post(requestBody);

        Request request = requestBuilder.build();

        Call call = new OkHttpClient.Builder().addInterceptor(chain -> {
            Request request1 = chain.request();
            Request.Builder builder1 = request1.newBuilder();
            builder1.addHeader("Referer", nodeUrl);
            builder1.addHeader("Content-Type", "application/json");
            return chain.proceed(builder1.build());
        }).build().newCall(request);
        Response response = call.execute();
        String result = response.body().string();

        System.out.println(result);
    }
}
