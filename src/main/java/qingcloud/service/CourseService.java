package qingcloud.service;

import qingcloud.dto.ConditonCoursePageQueryDTO;
import qingcloud.dto.Result;
import qingcloud.entity.Course;

public interface CourseService {
    Result addCourse(Course course);

    Result getCourse(Long id);

    Result deleteCourse(Long id);

    Result updateCourse(Course course);



    Result getCourseByContiditon(ConditonCoursePageQueryDTO queryDTO);
}
