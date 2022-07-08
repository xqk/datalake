package io.datalake.service.wizard;

import io.datalake.commons.utils.HttpClientConfig;
import io.datalake.commons.utils.HttpClientUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: xqk
 * Date: 2022/1/11
 * Description:
 */
@Service
public class ReptileService {
    String blogUrl = "https://www.icl.site";
    //获取最新的前几条数据
    private static int infoCount=1;

    public List lastActive() {
        List result = new ArrayList();
        try {
            HttpClientConfig config = new HttpClientConfig();
            config.setCocketTimeout(5000);
            //爬取最新数据
            Document doc = Jsoup.parse(HttpClientUtil.get(blogUrl, config));
            Elements elementsContent = doc.getElementsByAttributeValue("rel", "bookmark");
            Elements elementsTime = doc.getElementsByTag("time");
            for(int i = 0;i<infoCount;i++){
                Element info = elementsContent.get(i*3);
                Map<String, String> infoMap = new HashMap();
                infoMap.put("title",info.attr("title"));
                infoMap.put("href",info.attr("href"));
                infoMap.put("time",elementsTime.get(i).childNode(0).outerHtml());
                result.add(infoMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
            //ignore
            Map<String, String> infoMap = new HashMap();
            infoMap.put("title","支持移动端展示，数据源新增对DB2的支持，DataLake开源数据可视化分析平台v1.6.0发布");
            infoMap.put("href","https://www.icl.site");
            infoMap.put("time","2022年1月10日");
            result.add(infoMap);
        }
        return result;
    }


}
