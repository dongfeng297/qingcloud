package qingcloud.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import qingcloud.dto.Result;
import qingcloud.service.RankingService;

@RestController
@RequestMapping("/top")
public class RankingController {
    @Autowired
    private RankingService rankingService;

    @GetMapping("/courses")
    @ApiOperation(value = "获取课程排行榜")
    public Result getCourseRanking() {
        return rankingService.getCourseRanking();
    }
    @GetMapping("/teachers")
    @ApiOperation(value = "获取教师排行榜")
    public Result getTeacherRanking() {
        return rankingService.getTeacherRanking();
    }

}
