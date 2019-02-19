package com.dean.proxy.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.dean.proxy.bean.Proxy;
import com.dean.proxy.constant.AnonymityEnum;
import com.dean.proxy.service.AbstractProxyService;
import com.dean.proxy.service.ProxyService;
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
public class KuaiDaiLiService extends AbstractProxyService {

    //String country = "中国";

    @Override
    public String getURL() {
        return "https://www.kuaidaili.com/free/inha/";
    }

    //table table-bordered table-striped
    @Override
    public List<Proxy> reptileProxy(String html) {
        List<Proxy> list = new ArrayList<>();

        Document doc = Jsoup.parse(html);
        Element table = doc.select("table.table-bordered.table-striped").first();
        Element tbody = table.child(1);
        for(Element tr : tbody.children()) {
            Elements td = tr.children();
            String ip = td.get(0).text();
            Integer port = Integer.valueOf(td.get(1).text());
            String anonymity = td.get(2).text();
            String http = td.get(3).text();

            String[] strs = td.get(4).text().split(" ");
            String source = strs[strs.length - 1];
            String location = "";
            if (strs.length == 2 || strs.length == 3) {
                location = strs[0];
            } else if (strs.length == 4) {
                location = strs[1] + strs[2];
            }

            Long responseTime = new BigDecimal(
                td.get(5).text().replace("秒", ""))
                .multiply(new BigDecimal(1000)).longValue();
            String internal = getProxyId() + IPUtil.deletePoint(ip);

            Proxy proxy = new Proxy();
            proxy.setIp(ip);
            proxy.setPort(port);
            proxy.setAnonymity(AnonymityEnum.getCode(anonymity));
            proxy.setType(http);
            proxy.setSource(source);
            proxy.setCountry(COUNTRY);
            proxy.setLocation(location);
            proxy.setResponseTime(responseTime);
            proxy.setInternalId(internal);
            list.add(proxy);
        }
        return list;
    }

    @Override
    public String getProxyId() {
        return "KDL";
    }
}
