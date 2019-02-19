package com.dean.proxy.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.dean.proxy.bean.Proxy;
import com.dean.proxy.constant.AnonymityEnum;
import com.dean.proxy.service.AbstractProxyService;
import com.dean.proxy.util.IPUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

/**
 * @author dean
 */
@Service
public class WuYouDaiLiService extends AbstractProxyService {

    final static String KEY = "ABCDEFGHIZ";

    @Override
    public String getURL() {
        return "http://www.data5u.com/free/gngn/index.shtml";
    }

    @Override
    public List<Proxy> reptileProxy(String html) {
        List<Proxy> list = new ArrayList<>();

        Document doc = Jsoup.parse(html);

        Elements table = doc.select("ul.l2");
        for (Element tr : table) {
            Elements td = tr.children();
            String ip = td.get(0).text();

            String portTag = td.get(1).child(0).className().split(" ")[1];

            Integer port = converPort(portTag);
            String anonymity = td.get(2).child(0).text();
            String http = td.get(3).child(0).text();
            //
            String location = td.get(5).text();
            String country = td.get(4).text();
            String source = td.get(6).text();

            String time = td.get(7).text().replace("秒", "").trim();
            Long responseTime = new BigDecimal(
                time)
                .multiply(new BigDecimal(1000)).longValue();
            String internal = getProxyId() + IPUtil.deletePoint(ip);
            //
            Proxy proxy = new Proxy(
                ip, port, AnonymityEnum.getCode(anonymity), http,
                source, country, location, responseTime, internal);
            list.add(proxy);
        }
        return list;
    }

    @Override
    public String getProxyId() {
        return "5UH";
    }

    public int converPort(String str) {
        String result = "";
        for (byte b : str.getBytes()) {
            result = result + KEY.indexOf(b);
        }
        return Integer.valueOf(result) / 8;
    }
}
