package qingcloud.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author Jsxs
 * @Date 2023/4/4 20:28
 * @PackageName:com.jsxs
 * @ClassName: MapUtil
 * @Description: 地址转经纬度工具类
 * @Version 1.0
 */
public class MapUtil {

    private static final String API_KEY =
            "VNXBZ-HZ5W4-KAVUS-FLLDD-BCGEJ-6DFE2"; // 替换为你的腾讯地图API密钥
    private static final String BASE_URL = "https://apis.map.qq.com/ws/geocoder/v1/";

    public static Map<String, Object> getURLContent(String address) {
        Map<String, Object> map = new HashMap<>();

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            // URL编码处理地址
            String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8.toString());
            String url = BASE_URL + "?address=" + encodedAddress + "&key=" + API_KEY;

            HttpGet httpGet = new HttpGet(url);

            // 设置请求头
            httpGet.setHeader("Content-Type", "application/json");

            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    // 获取响应内容
                    String result = EntityUtils.toString(entity, StandardCharsets.UTF_8);

                    // 解析JSON响应
                    JSONObject jsonObject = JSONObject.parseObject(result);

                    // 检查状态码
                    int status = jsonObject.getInteger("status");
                    if (status == 0) { // 0 表示成功
                        JSONObject resultObj = jsonObject.getJSONObject("result");
                        JSONObject location = resultObj.getJSONObject("location");

                        map.put("lat", location.getString("lat"));
                        map.put("lng", location.getString("lng"));
                    } else {
                        String message = jsonObject.getString("message");
                        System.err.println("API请求失败: " + message);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }

    public static void main(String[] args) {
        Map<String, Object> map = getURLContent("吉林省长春市净月国家高新技术产业开发区");
        System.out.println("lat=" + map.get("lat") + ",lng=" + map.get("lng"));
    }
}
