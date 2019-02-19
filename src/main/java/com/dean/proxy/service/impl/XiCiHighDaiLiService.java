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
import org.springframework.stereotype.Service;

/**
 * @author dean
 */
@Service
public class XiCiHighDaiLiService extends AbstractProxyService {

    @Override
    public String getURL() {
        return "https://www.xicidaili.com/nn";
    }

    @Override
    public List<Proxy> reptileProxy(String html) {
        List<Proxy> list = new ArrayList<>();

        Document doc = Jsoup.parse(html);
        Element table = doc.select("div.clearfix.proxies").first();
        Element body = table.child(3).child(0);
        // 跳过0
        for (int i = 1; i < body.childNodeSize() / 2; ++i) {
            Element element = body.child(i);
            String ip = element.child(1).text();
            Integer port = Integer.valueOf(element.child(2).text());
            Element locationTd = element.child(3);
            if (locationTd.children().size() ==0) {
                continue;
            }
            String location = locationTd.child(0).text();
            String anonymity = element.child(4).text();
            String type = element.child(5).text();

            String time =
                element.child(6).child(0).attr("title").replace("秒", "");
            Long response = new BigDecimal(time).multiply(new BigDecimal(1000)).longValue();
            String internal = getProxyId() + IPUtil.deletePoint(ip);

            Proxy proxy = new Proxy(ip, port, AnonymityEnum.getCode(anonymity), type, "",
                COUNTRY, location, response, internal);
            list.add(proxy);
        }

        return list;
    }

    @Override
    public String getProxyId() {
        return "XCH";
    }
}
