package world.thek.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import world.thek.config.ConfigData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * @author: thek
 * @date: 2022/9/27 下午3:11
 */
public class PixivUtil {
    private static final String token = ConfigData.INSTANCE.getApiKey();
    static CloseableHttpClient httpClient = HttpClients.createDefault();
    static CloseableHttpResponse response = null;
    
    /**
     * 根据作品ID获取图片
     */
    public static HashMap<String, String> getImageById(int id) {
        String url = "https://api.acgmx.com/illusts/detail?illustId=" + id + "&reduction=true";
        HashMap<String, String> map = new HashMap<>();
        HttpGet request = new HttpGet(url);
        request.setHeader(
                "User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.60 Safari/537.36");
        request.setHeader("token", token);
        try {
            response = httpClient.execute(request);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_CREATED) {
                HttpEntity httpEntity = response.getEntity();
                String html = EntityUtils.toString(httpEntity, "utf-8");
                List<Integer> list1 = new ArrayList<>();
                int index = html.indexOf("large");
                while (index != -1) {
                    list1.add(index);
                    index = html.indexOf("large", index + 1);
                }
                List<Integer> list2 = new ArrayList<>();
                int end = html.indexOf("square_medium");
                while (end != -1) {
                    list2.add(end);
                    end = html.indexOf("square_medium", end + 1);
                }
                int titleIndex = html.indexOf("title") + 8;
                int typeIndex = html.indexOf("type") - 3;
                String title = html.substring(titleIndex, typeIndex);
                int iindex = html.lastIndexOf("id") + 4;
                int iend = html.lastIndexOf("account") - 2;
                String userId = html.substring(iindex, iend);
                map.put("id", userId);
                map.put("title", title);
                if (list1.size() > 1) {
                    for (int i = 1; i < list1.size(); i++) {
                        int a = list1.get(i);
                        int b = list2.get(i);
                        String img = html.substring(a + 8, b - 3);
                        int m = img.lastIndexOf("/") + 1;
                        String subId = img.substring(m, img.length() - 15);
                        map.put("1", subId);
                        String repUrl = img.replace("i.pximg.net/c/600x1200_90_webp", "i.acgmx.com/c/540x540_70");
                        map.put(subId, repUrl);
                    }
                } else {
                    index = list1.get(0);
                    end = list2.get(0);
                    String img = html.substring(index + 8, end - 3);
                    int m = img.lastIndexOf("/") + 1;
                    String subId = img.substring(m, img.length() - 15);
                    map.put("1", subId);
                    String repUrl = img.replace("i.pximg.net/c/600x1200_90_webp", "i.acgmx.com/c/540x540_70");
                    map.put(subId, repUrl);
                }
            }
        } catch (Exception e) {
            return map;
        }
        return map;
    }

    /**
     * 根据画师ID获取画师作品
     */
    public static HashMap<String, String> getIdByAuthor(int id) {
        String url = "https://api.acgmx.com/public/search/users/illusts?id=" + id + "&offset=10";
        HashMap<String, String> authorMap = new HashMap<>();
        HttpGet request = new HttpGet(url);
        request.setHeader(
                "User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.60 Safari/537.36");
        request.setHeader("token", token);
        try {
            response = httpClient.execute(request);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity httpEntity = response.getEntity();
                String html = EntityUtils.toString(httpEntity, "utf-8");
                List<Integer> list1 = new ArrayList<>();
                int index = html.indexOf("large");
                while (index != -1) {
                    list1.add(index);
                    index = html.indexOf("large", index + 1);
                }
                List<Integer> list2 = new ArrayList<>();
                int end = html.indexOf("square_medium");
                while (end != -1) {
                    list2.add(end);
                    end = html.indexOf("square_medium", end + 1);
                }
                int j = 0;
                for (int i = 0; i < list1.size(); i++) {
                    int a = list1.get(i);
                    int b = list2.get(i);
                    String img = html.substring(a + 8, b - 3);
                    int m = img.lastIndexOf("/") + 1;
                    int n = img.lastIndexOf("_p");
                    String subId = img.substring(m, n);
                    if (!authorMap.containsValue(subId)) {
                        authorMap.put(String.valueOf(j), subId);
                        j++;
                    }
                }
            } else {
                HttpClientUtils.closeQuietly(response);
                return authorMap;
            }
        } catch (Exception e) {
            HttpClientUtils.closeQuietly(response);
            return authorMap;
        }
        return authorMap;
    }

    /**
     * 搜索功能
     */
    public static String searchImage(String value) {
        String url = "https://api.acgmx.com/public/search?q=" + value + "&offset=10";
        String subId = String.valueOf(0);
        HttpGet request = new HttpGet(url);
        request.setHeader(
                "User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.60 Safari/537.36");
        request.setHeader("token", token);
        try {
            response = httpClient.execute(request);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity httpEntity = response.getEntity();
                String html = EntityUtils.toString(httpEntity, "utf-8");
                JSONObject jsonObject = JSON.parseObject(html);
                JSONArray arrayList = JSON.parseArray(String.valueOf(jsonObject.get("illusts")));
                Random r = new Random();
                int indexArray = r.nextInt(arrayList.size());
                String valueString = JSON.toJSONString(arrayList.get(indexArray));
                int index = valueString.indexOf("large");
                int end = valueString.indexOf("square_medium");
                String substringUrl = valueString.substring(index + 8, end - 3);
                int subIndex = substringUrl.lastIndexOf("/");
                int subEnd = substringUrl.indexOf("_p0");
                subId = substringUrl.substring(subIndex + 1, subEnd);
            }
        } catch (Exception e) {
            return subId;
        }
        return subId;
    }
}
