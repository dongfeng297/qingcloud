package qingcloud.mapper;

import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.*;
import qingcloud.dto.ConditonCoursePageQueryDTO;
import qingcloud.entity.Course;

import java.util.List;

@Mapper
public interface CourseMapper {
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Select("insert into course(title,teacher_id,cover_url,description,price,subject,grade,duration,course_type) values(#{title},#{teacherId},#{coverUrl},#{description},#{price},#{subject},#{grade},#{duration},#{courseType})")
    void insert(Course course);

    @Select("select * from course where id=#{id}")
    Course getById(Long id);

    @Delete("delete from course where id=#{id}")
    void deleteById(Long id);

    void update(Course course);


    Page<Course> pageQuery(ConditonCoursePageQueryDTO queryDTO);
}
