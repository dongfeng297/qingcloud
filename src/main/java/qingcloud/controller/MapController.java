package qingcloud.controller;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import qingcloud.config.CourseConfig;
import qingcloud.config.MapConfig;
import qingcloud.dto.Result;
import qingcloud.service.MapService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/map")
public class MapController {


    @Autowired
    private MapService mapService;


    /**
     * 计算交通价格
     * @param address 用户地址
     * @return Result
     */
    @GetMapping("/calculate")
    @ApiOperation(value = "计算交通价格")
    public Result getTrafficFee(@RequestParam String address) {
        return mapService.getTrafficFee(address);
    }
}

