package qingcloud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import qingcloud.dto.Result;
import qingcloud.entity.CourseChapter;
import qingcloud.service.CourseChapterService;

@RestController
@RequestMapping("/courseChapter")
public class CourseChapterController {
    @Autowired
    private CourseChapterService courseChapterService;
    @PostMapping("/add")
    public Result addCourseChapter(@RequestBody CourseChapter courseChapter) {
        return courseChapterService.addCourseChapter(courseChapter);
    }
}
