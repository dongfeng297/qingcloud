package qingcloud.controller;

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
    public Result getCourseRanking() {
        return rankingService.getCourseRanking();
    }
    @GetMapping("/teachers")
    public Result getTeacherRanking() {
        return rankingService.getTeacherRanking();
    }

}
