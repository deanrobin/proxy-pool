package com.dean.proxy.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.dean.proxy.bean.Proxy;
import com.dean.proxy.constant.AnonymityEnum;
import com.dean.proxy.util.IPUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

@Service
public class XiCiTransparentService extends XiCiHighDaiLiService {

    @Override
    public String getURL() {
        return "https://www.xicidaili.com/nt";
    }

    @Override
    public String getProxyId() {
        return "XCT";
    }
}
