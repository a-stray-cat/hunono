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
import world.thek.entity.Pixiv;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: thek
 * @date: 2022/9/22 上午10:01
 */
public class HttpClientUtil {
    public List<Pixiv> getDiscountInfo(int page) {
        // 1.生成httpclient，相当于打开一个浏览器
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        // 2.创建get请求，相当于在浏览器地址栏输入 网址
        String url = "https://www.getitfree.cn/page/"+page;
        System.out.println(url);
        HttpGet request = new HttpGet(url);
        //HttpGet request = new HttpGet("http://cloudreve.whanp.ltd/");
        // 设置请求头，将爬虫伪装成浏览器
        request.setHeader(
                "User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.60 Safari/537.36");
        ArrayList<Pixiv> disList = new ArrayList<>();
        try {
            // 3.执行get请求，相当于在输入地址栏后敲回车键
            response = httpClient.execute(request);
            // 4.判断响应状态为200，进行处理
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                // 5.获取响应内容
                HttpEntity httpEntity = response.getEntity();
                String html = EntityUtils.toString(httpEntity, "utf-8");

                // 6.Jsoup解析html
                Document document = Jsoup.parse(html);
                // 像js一样，通过标签获取title
                Elements articleList = document.getElementsByTag("article");


                for (int i = 0; i < articleList.size(); i++) {
                    Pixiv dis = new Pixiv();
                    Elements a = articleList.get(i).getElementsByTag("a").select("[class=post-title]");
                    String text = a.text();
//                    dis.setTitle(text);
                    String href = a.attr("href");
//                    dis.setLink(href);
                    Elements excerpt = articleList.get(i).getElementsByTag("a").select("[class=post-excerpt]");
                    String description = excerpt.text();
                    description = description.replace("摘要： ","");
                    description = description.replace("摘要: ","");
//                    dis.setDescription(description);
                    Elements image = articleList.get(i).getElementsByTag("img");
                    String img = image.attr("src");
//                    dis.setImg(img);

                    disList.add(dis);

                    //System.out.println(disList);

                }
            } else {
                // 如果返回状态不是200，比如404（页面不存在）等，根据情况做处理
                disList = null;
                System.out.println(EntityUtils.toString(response.getEntity(), "utf-8"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 6.关闭
            HttpClientUtils.closeQuietly(response);
            HttpClientUtils.closeQuietly(httpClient);
        }
        return disList;
    }
}
