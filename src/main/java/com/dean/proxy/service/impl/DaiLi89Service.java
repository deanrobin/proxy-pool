package com.dean.proxy.service.impl;

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
}
