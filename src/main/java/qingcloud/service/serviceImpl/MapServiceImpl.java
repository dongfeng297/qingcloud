package qingcloud.service.serviceImpl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qingcloud.config.CourseConfig;
import qingcloud.dto.Result;
import qingcloud.mapper.UserMapper;
import qingcloud.service.MapService;
import qingcloud.utils.MapUtil;
import qingcloud.utils.UserHolder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
@Slf4j
@Service
public class MapServiceImpl implements MapService {


    @Autowired
    private CourseConfig courseConfig;
    @Autowired
    private UserMapper userMapper;

    private static final String AMAP_DISTANCE_URL = "https://apis.map.qq.com/ws/distance/v1/matrix?mode=driving";


    @Override
    public Result getTrafficFee(String address) {
        if (StrUtil.isBlank(address)) {
            return Result.fail("地址不能为空");
        }


        try {
            // 1. 调用腾讯地图API进行地理编码获取经纬度
            Map<String, Object> map = MapUtil.getURLContent(address);
            if (map == null || !map.containsKey("lng") || !map.containsKey("lat")) {
                return Result.fail("获取用户地址经纬度失败");
            }

            String lng = (String) map.get("lng");
            String lat = (String) map.get("lat");

            // 2. 计算距离
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("from", courseConfig.getOrgLatitude() + "," + courseConfig.getOrgLongitude());
            paramMap.put("to", lat + "," + lng);
            paramMap.put("key",
                    "YDHBZ-CMGKC-7QP2A-AP5YU-KZ6OV-PDBA5");

            String distanceResponse = HttpUtil.post(AMAP_DISTANCE_URL, paramMap);
            JSONObject distanceJson = JSONUtil.parseObj(distanceResponse);

            if (!"0".equals(distanceJson.getStr("status"))) {
                return Result.fail("距离计算失败");
            }

            Long userId= UserHolder.getUser().getId();
            userMapper.updateAdderss(userId,address);

            // 获取距离（米）
            Object result = distanceJson.getObj("result");

            // 假设 distanceJson 是一个 JSONObject
            int distance = distanceJson.getJSONObject("result")
                    .getJSONArray("rows")
                    .getJSONObject(0)
                    .getJSONArray("elements")
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
