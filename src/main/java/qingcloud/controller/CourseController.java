package qingcloud.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import qingcloud.dto.ConditonCoursePageQueryDTO;
import qingcloud.dto.Result;
import qingcloud.entity.Course;
import qingcloud.service.CourseService;

@RestController
@RequestMapping("/course")
public class CourseController {
    @Autowired
    private CourseService courseService;
    @PostMapping
    public Result addCourse(@RequestBody Course course){
        return courseService.addCourse(course);
    }
    @GetMapping("/{id}")
    public Result getCourse(@PathVariable("id") Long id){
        return courseService.getCourse(id);
    }

    @DeleteMapping
    public Result deleteCourse(@RequestParam("id") Long id){
        return courseService.deleteCourse(id);
    }

    @PutMapping
    public Result updateCourse(@RequestBody Course course){
        return courseService.updateCourse(course);
    }

    @GetMapping
    @ApiOperation("多条件分页查询")
    public Result getByCondition(ConditonCoursePageQueryDTO queryDTO){
        return courseService.getCourseByContiditon(queryDTO);
    }
}
