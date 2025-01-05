package qingcloud.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import qingcloud.dto.Result;
import qingcloud.service.MapService;

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

