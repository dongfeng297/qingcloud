package qingcloud.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import qingcloud.annotation.Log;
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
    @ApiOperation(value = "添加课程")
    @Log(value = "添加课程")
    public Result addCourse(@RequestBody Course course){
        return courseService.addCourse(course);
    }
    @GetMapping("/{id}")
    @ApiOperation(value = "查询课程")
    public Result getCourse(@PathVariable("id") Long id){
        return courseService.getCourse(id);
    }

    @DeleteMapping
    @ApiOperation(value = "删除课程")
    @Log(value = "删除课程")
    public Result deleteCourse(@RequestParam("id") Long id){
        return courseService.deleteCourse(id);
    }

    @PutMapping
    @ApiOperation(value = "更新课程")
    @Log(value = "更新课程")
    public Result updateCourse(@RequestBody Course course){
        return courseService.updateCourse(course);
    }

    @GetMapping
    @ApiOperation(value = "多条件分页查询")
    public Result getByCondition(ConditonCoursePageQueryDTO queryDTO){
        return courseService.getCourseByContiditon(queryDTO);
    }
}
