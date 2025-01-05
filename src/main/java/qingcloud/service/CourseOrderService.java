package qingcloud.service;

import qingcloud.dto.CourseOrderDTO;
import qingcloud.dto.Result;

public interface CourseOrderService {
    Result orderCourse(CourseOrderDTO courseOrderDTO);
}
