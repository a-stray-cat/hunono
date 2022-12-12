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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: thek
 * @date: 2022/11/17 下午4:22
 */
public class EpicUtil {
    static Map<String, Epic> epicMap = new HashMap<>();

    public static Map<String, Epic> getEpicFreeMap() {
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
                Date now = new Date();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                AtomicInteger i = new AtomicInteger();
                arrayList.forEach((item) -> {
                    JSONObject element = (JSONObject) item;
                    String title = (String) element.get("title");
                    String description = (String) element.get("description");
                    JSONObject imgUrl = (JSONObject) JSON.parseArray(String.valueOf(element.get("keyImages"))).get(0);
                    String imgUrlString = (String) imgUrl.get("url");
                    JSONObject object = (JSONObject) ((JSONObject) item).get("promotions");
                    if (object != null) {
                        JSONArray objectArray = (JSONArray) object.get("promotionalOffers");
                        if (objectArray.size() > 0) {
                            object = (JSONObject) objectArray.get(0);
                            objectArray = (JSONArray) object.get("promotionalOffers");
                            object = (JSONObject) objectArray.get(0);
                            String endDate = (String) object.get("endDate");
                            try {
                                Date date = simpleDateFormat.parse(endDate);
                                if (now.before(date)) {
                                    Epic epic = new Epic();
                                    epic.setTitle(title);
                                    epic.setDes(description);
                                    epic.setDate(endDate);
                                    epic.setImgUrl(imgUrlString);
                                    epicMap.put(String.valueOf(i.get()), epic);
                                    i.getAndIncrement();
                                }
                            } catch (ParseException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                });
            }
        } catch (Exception e) {
            return epicMap;
        }
        return epicMap;
    }
}
