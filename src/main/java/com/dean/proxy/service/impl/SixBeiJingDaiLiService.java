package com.dean.proxy.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.dean.proxy.bean.Proxy;
import com.dean.proxy.constant.AnonymityEnum;
import com.dean.proxy.service.AbstractProxyService;
import com.dean.proxy.util.IPUtil;
import com.dean.proxy.util.IntegerUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

@Service
public class SixBeiJingDaiLiService extends AbstractProxyService {

    protected final static int COUNT = 20;
    protected final static String HTTP = "http";

    @Override
    public String getURL() {
        return "http://www.66ip.cn/areaindex_1/1.html";
    }

    @Override
    public List<Proxy> reptileProxy(String html) {
        List<Proxy> list = new ArrayList<>();

        Document doc = Jsoup.parse(html);

        Element table = doc.select("div.footer").first().child(0).child(5);
        Element tbody = table.child(0);

        int count = IntegerUtil.getBigger(COUNT, tbody.children().size());

        // 跳过0
        for (int i = 1; i < count; ++i) {
            Element tr = tbody.child(i);
            String ip  = tr.child(0).text();
            Integer port = Integer.valueOf(tr.child(1).text());
            String location = tr.child(2).text();
            String anonymity = tr.child(3).text();
            String source = "";
            String internal = getProxyId() + IPUtil.deletePoint(ip);

            Proxy proxy = new Proxy(
                ip, port, AnonymityEnum.getCode(anonymity), HTTP, "",
                COUNTRY, location, 0L, internal);
            list.add(proxy);
        }
        return list;
    }

    @Override
    public String getProxyId() {
        return "6BJ";
    }
}
