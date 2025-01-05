package qingcloud.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.web.bind.annotation.GetMapping;
import qingcloud.dto.CourseOrderDTO;
import qingcloud.entity.CourseOrder;

import java.time.LocalDateTime;
import java.util.Map;

@Mapper
public interface CourseOrderMapper {
    @Insert("insert into course_order(id,course_id,user_id,pay_amount) values(#{orderId},#{courseId},#{userId},#{price})")
    void addOrder(Map<String ,Object> map);

    @Select("select count(*) from course_order where user_id=#{userId} and course_id=#{courseId}")
    int getByUserIdAndCourseId(Map<String, Object> map);

    @Insert("insert into course_order(id,course_id,user_id,pay_amount,voucher_id) values(#{orderId},#{courseId},#{userId},#{price},#{voucherId})")
    void addOrderWithVoucher(Map<String, Object> map);

    @Select("select * from course_order where id=#{orderId}")
    CourseOrder getById(Long orderId);


    @Update("update course_order set status=#{paid},pay_time=#{now} where id=#{orderId}")
    void updateStatusAndPayTime(Long orderId, int paid, LocalDateTime now);
}
