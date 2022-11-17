package world.thek.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import world.thek.entity.Epic;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author: thek
 * @date: 2022/11/17 下午4:22
 */
public class EpicUtil {
    static Map<String,Epic> epicMap = new HashMap<>();

    public static Map<String,Epic> getEpicFreeMap() {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String url = "https://store-site-backend-static.ak.epicgames.com/freeGamesPromotions?locale=zh-CN&country=CN&allowCountries=CN";
        HttpGet request = new HttpGet(url);
        request.setHeader(
                "User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.60 Safari/537.36");

        try {
            response = httpClient.execute(request);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity httpEntity = response.getEntity();
                String html1 = EntityUtils.toString(httpEntity, "utf-8");
                JSONObject jsonObject = JSON.parseObject(html1);
                jsonObject = (JSONObject) jsonObject.get("data");
                jsonObject = (JSONObject) jsonObject.get("Catalog");
                jsonObject = (JSONObject) jsonObject.get("searchStore");
                JSONArray arrayList = JSON.parseArray(String.valueOf(jsonObject.get("elements")));
                int i = 0;
                for (Object object : arrayList) {
                    String stringValue = JSON.toJSONString(object);
                    List list1 = new ArrayList<Integer>();
                    int datei = stringValue.indexOf("endDate");
                    while (datei != -1) {
                        list1.add(datei);
                        datei = stringValue.indexOf("endDate", datei + 1);
                    }
                    List list2 = new ArrayList<Integer>();
                    int datee = stringValue.indexOf("startDate");
                    while (datee != -1) {
                        list2.add(datee);
                        datee = stringValue.indexOf("startDate", datee + 1);
                    }
                    if (list1.size() > 1) {
                        int dindex = (int) list1.get(0);
                        int dend = (int) list2.get(0);
                        String dateString = stringValue.substring(dindex + 10, dend - 3);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                        Date date = simpleDateFormat.parse(dateString);
                        Date now = new Date();
                        if (now.before(date)) {
                            int index = stringValue.indexOf("title");
                            int end = stringValue.indexOf("productSlug");
                            if (end < 0) {
                                end = stringValue.indexOf("tags");
                            }
                            String title = stringValue.substring(index + 8, end - 3);
                            int desi = stringValue.indexOf("description");
                            int dese = stringValue.indexOf("title");
                            String des = stringValue.substring(desi + 14, dese - 3);
                            Epic epic = new Epic();
                            epic.setTitle(title);
                            epic.setDes(des);
                            epic.setDate(dateString);
                            epicMap.put(String.valueOf(i),epic);
                            i++;
                        }
                    }
                }
            }
        }catch (Exception e) {
            return epicMap;
        }
        return epicMap;
    }
}
