package qingcloud.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.web.bind.annotation.*;
import qingcloud.annotation.Log;
import qingcloud.dto.Result;
import qingcloud.entity.CourseChapter;
import qingcloud.service.CourseChapterService;

@RestController
@RequestMapping("/courseChapter")
public class CourseChapterController {
    @Autowired
    private CourseChapterService courseChapterService;
    @PostMapping("/add")
    @ApiOperation(value = "添加课程章节")
    @Log("添加课程章节")
    public Result addCourseChapter(@RequestBody CourseChapter courseChapter) {
        return courseChapterService.addCourseChapter(courseChapter);
    }
    @GetMapping
    @ApiOperation(value = "获取课程章节")
    public Result getCourseChapter(@RequestParam Long id) {
        return courseChapterService.getById(id);
    }
}
