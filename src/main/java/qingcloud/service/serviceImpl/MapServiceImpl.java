package qingcloud.service.serviceImpl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qingcloud.config.CourseConfig;
import qingcloud.config.MapConfig;
import qingcloud.dto.Result;
import qingcloud.mapper.UserMapper;
import qingcloud.service.MapService;
import qingcloud.utils.MapUtil;
import qingcloud.utils.UserHolder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
@Slf4j
@Service
public class MapServiceImpl implements MapService {
    @Autowired
    private MapConfig mapConfig;

    @Autowired
    private CourseConfig courseConfig;
    @Autowired
    private UserMapper userMapper;

    private static final String AMAP_DISTANCE_URL = "https://restapi.amap.com/v3/distance";


    @Override
    public Result getTrafficFee(String address) {
        if (StrUtil.isBlank(address)) {
            return Result.fail("地址不能为空");
        }


        try {
            // 1. 调用腾讯地图API进行地理编码获取经纬度
            Map<String, Object> map = MapUtil.getURLContent(address);
            if (map == null || !map.containsKey("lng") || !map.containsKey("lat")) {
                return Result.fail("地理编码失败");
            }

            String lng = (String) map.get("lng");
            String lat = (String) map.get("lat");

            // 2. 计算距离
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("origins", courseConfig.getOrgLatitude() + "," + courseConfig.getOrgLongitude());
            paramMap.put("destination", lat + "," + lng);
            paramMap.put("key", mapConfig.getKey());

            String distanceResponse = HttpUtil.get(AMAP_DISTANCE_URL, paramMap);
            JSONObject distanceJson = JSONUtil.parseObj(distanceResponse);

            if (!"1".equals(distanceJson.getStr("status"))) {
                return Result.fail("距离计算失败");
            }

            Long userId= UserHolder.getUser().getId();
            userMapper.updateAdderss(userId,address);

            // 获取距离（米）
            int distance = distanceJson.getJSONArray("results")
                    .getJSONObject(0)
                    .getInt("distance");

            // 转换为公里
            BigDecimal distanceKm = new BigDecimal(distance).divide(new BigDecimal("1000"), 2, RoundingMode.HALF_UP);

            // 3. 判断距离和计算价格
            if (distanceKm.compareTo(new BigDecimal(courseConfig.getMaxDistance())) > 0) {
                return Result.fail("超出最大服务距离");
            }

            if (distanceKm.compareTo(new BigDecimal(courseConfig.getFreeDistance())) <= 0) {
                // 免费距离内
                Map<String, Object> resultMap = new HashMap<>();

                resultMap.put("distance", distanceKm);
                resultMap.put("extraFee", BigDecimal.ZERO);
                return Result.ok(resultMap);
            } else {
                // 计算额外距离费用
                BigDecimal extraDistance = distanceKm.subtract(new BigDecimal(courseConfig.getFreeDistance()));
                BigDecimal extraFee = extraDistance.multiply(courseConfig.getPerKmFee());

                Map<String, Object> resultMap = new HashMap<>();
                resultMap.put("distance", distanceKm);
                resultMap.put("extraFee", extraFee);

                userMapper.updateTrafficFee(userId,extraFee);
                return Result.ok(resultMap);
            }

        } catch (Exception e) {
            log.error("计算距离和价格失败", e);
            return Result.fail("服务异常");
        }
    }
}
