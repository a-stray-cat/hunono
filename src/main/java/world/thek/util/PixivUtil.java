package world.thek.util;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import world.thek.config.ConfigData;

import java.io.IOException;
import java.util.HashMap;

/**
 * @author: thek
 * @date: 2022/9/27 下午3:11
 */
public class PixivUtil {
    private static String token = ConfigData.INSTANCE.getApiKey();
    static CloseableHttpClient httpClient = HttpClients.createDefault();
    static CloseableHttpResponse response = null;

    public static HashMap<String,String> getImageById(int id) throws IOException {
        String url ="https://api.acgmx.com/illusts/detail?illustId="+id+"&reduction=true";
        HashMap<String,String> map = new HashMap<>();
        String imgUrl = "";
        HttpGet request = new HttpGet(url);
        request.setHeader(
                "User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.60 Safari/537.36");
        request.setHeader("token",token);
        try {
            response = httpClient.execute(request);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_CREATED) {
                String data = EntityUtils.toString(response.getEntity());
                int uindex = data.indexOf("large")+8;
                int uend = data.indexOf("square_medium")-3;
                imgUrl = data.substring(uindex,uend).replace("i.pximg.net", "i.acgmx.com");
                imgUrl = imgUrl.replace("600x1200_90_webp", "540x540_70");
                map.put("url",imgUrl);
                int tindex = data.indexOf("title")+8;
                int tend = data.indexOf("type")-3;
                String title = data.substring(tindex,tend);
                map.put("title",title);
                int iindex = data.lastIndexOf("id")+4;
                int iend = data.lastIndexOf("account")-2;
                String userId = data.substring(iindex,iend);
                map.put("id",userId);
            }
        } catch (IOException e) {
            return map;
        }
//        HttpClientUtils.closeQuietly(response);
//        HttpClientUtils.closeQuietly(httpClient);
        return map;
    }
}
