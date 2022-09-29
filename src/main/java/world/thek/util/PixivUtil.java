package world.thek.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import world.thek.config.ConfigData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author: thek
 * @date: 2022/9/27 下午3:11
 */
public class PixivUtil {
    private static String token = ConfigData.INSTANCE.getApiKey();
    static CloseableHttpClient httpClient = HttpClients.createDefault();
    static CloseableHttpResponse response = null;

    //根据作品ID获取图片
    public static HashMap<String, String> getImageById(int id) throws IOException {
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
                List list = new ArrayList<Integer>();
                int index = html.indexOf("large");
                while (index != -1) {
                    list.add(index);
                    index = html.indexOf("large", index + 1);
                }
                int titleIndex = html.indexOf("title") + 8;
                int typeIndex = html.indexOf("type") - 3;
                String title = html.substring(titleIndex, typeIndex);
                int iindex = html.lastIndexOf("id")+4;
                int iend = html.lastIndexOf("account")-2;
                String userId = html.substring(iindex,iend);
                map.put("id",userId);
                map.put("title", title);
                if (list.size() > 1) {
                    for (int i = 1; i < list.size(); i++) {
                        int a = (int) list.get(i);
                        String img = html.substring(a + 8, a + 109);
                        String subId = img.substring(img.length() - 27, img.length() - 15);
                        String repUrl = img.replace("i.pximg.net/c/600x1200_90_webp", "i.acgmx.com/c/540x540_70");
                        map.put(subId, repUrl);
                    }
                } else {
                    index = (int) list.get(0);
                    String img = html.substring(index + 8, index + 109);
                    String subId = img.substring(img.length() - 27, img.length() - 15);
                    String repUrl = img.replace("i.pximg.net/c/600x1200_90_webp", "i.acgmx.com/c/540x540_70");
                    map.put(subId, repUrl);
                }

            }
        } catch (IOException e) {
            return map;
        }
        return map;
    }

    //根据画师ID获取画师作品
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
                List list = new ArrayList<Integer>();
                int index = html.indexOf("large");
                while (index != -1) {
                    list.add(index);
                    index = html.indexOf("large", index + 1);
                }
                int j = 0;
                for (int i = 0; i < list.size(); i++) {
                    int a = (int) list.get(i);
                    String img = html.substring(a + 8, a + 109);
                    String subId = img.substring(img.length() - 27, img.length() - 18);
                    if (!authorMap.containsValue(subId)) {
                        authorMap.put(String.valueOf(j), subId);
                        j++;
                    }
                }
            } else {
                return authorMap;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return authorMap;
    }
}
