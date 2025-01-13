package qingcloud.mapper;

import org.apache.ibatis.annotations.*;
import qingcloud.entity.CourseChapter;

@Mapper
public interface CourseChapterMapper {


    @Insert("insert into course_chapter (course_id, title, sort,video_id) VALUES (#{courseId},#{title},#{sort},#{videoId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void addCourseChapter(CourseChapter courseChapter);

    @Select("select * from course_chapter where id = #{id}")
    CourseChapter getById(Long id);
}
