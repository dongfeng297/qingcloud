package qingcloud.service.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qingcloud.dto.Result;
import qingcloud.entity.Course;
import qingcloud.entity.CourseChapter;
import qingcloud.mapper.CourseChapterMapper;
import qingcloud.service.CourseChapterService;

@Service
public class CourseChapterServiceImpl implements CourseChapterService {
    @Autowired
    private CourseChapterMapper courseChapterMapper;
    @Override
    public Result addCourseChapter(CourseChapter courseChapter) {
        courseChapterMapper.addCourseChapter(courseChapter);
        return Result.ok();
    }
}
