package qingcloud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import qingcloud.dto.CourseOrderDTO;
import qingcloud.dto.Result;
import qingcloud.service.CourseOrderService;

@RestController
@RequestMapping("/course-order")
public class CourseOrderController {
    @Autowired
    private CourseOrderService courseOrderService;

    @PostMapping
    public Result orderCourse(CourseOrderDTO courseOrderDTO) {
        return courseOrderService.orderCourse(courseOrderDTO);
    }
}
