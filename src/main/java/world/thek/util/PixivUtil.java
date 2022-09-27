package world.thek.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import world.thek.config.ConfigData;

import java.io.IOException;

/**
 * @author: thek
 * @date: 2022/9/27 下午3:11
 */
public class PixivUtil {
    private static String token = ConfigData.INSTANCE.getApiKey();

    // 1.生成httpclient，相当于打开一个浏览器
    static CloseableHttpClient httpClient = HttpClients.createDefault();
    static CloseableHttpResponse response = null;

    public static String getImageById(int id) throws IOException {
        String url ="https://api.acgmx.com/illusts/detail?illustId="+id+"&reduction=true";
        String imgUrl = "";
//        URL url=new URL(urlStr);
//        StringBuffer document = new StringBuffer();
//        URLConnection conn = url.openConnection();
        HttpGet request = new HttpGet(url);
        request.setHeader(
                "User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.60 Safari/537.36");
        request.setHeader("token",token);
        try {
            response = httpClient.execute(request);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity httpEntity = response.getEntity();
                String html = EntityUtils.toString(httpEntity, "utf-8");
                Document document = Jsoup.parse(html);
                Elements hoverableList = document.getElementsByTag("div").select("hoverable");
                for (int i = 0; i < hoverableList.size(); i++) {
                    Elements content = hoverableList.get(i).getElementsByTag("span");
                    if (content.text() == "large") {
                        Elements a = hoverableList.get(i).getElementsByTag("a");
                        imgUrl = a.text();
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            HttpClientUtils.closeQuietly(response);
            HttpClientUtils.closeQuietly(httpClient);
        }
        return imgUrl;
    }
}
