package qingcloud.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import qingcloud.event.CourseStaticEvent;

import static qingcloud.constant.RedisConstant.*;



@Component
@Slf4j
public class CourseStaticEventListener {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @EventListener(classes = CourseStaticEvent.class)
    @Async
    public void handleCourseEvent(CourseStaticEvent event) {
        try {
            switch (event.getEventType()) {
                case PURCHASE:
                    // 课程购买
                    handlePurchase(event.getCourseId());
                    break;
                case LIKE:
                    // 课程点赞
                    handleCourseLike(event.getCourseId());
                    break;
                case CANCEL_LIKE:
                    // 取消点赞
                    handleCancelLike(event.getCourseId());
                    break;
                case FOLLOW_TEACHER:
                    // 关注老师
                    handleTeacherFollow(event.getTeacherId());
                    break;
                case UNFOLLOW_TEACHER:
                    // 取消关注
                    handleTeacherUnfollow(event.getTeacherId());
                    break;
                default:
                    log.warn("Unknown event type: {}", event.getEventType());
            }
        } catch (Exception e) {
            log.error("Handle course event error: {}", e.getMessage(), e);
        }
    }

    /**
     * 处理课程购买
     */
    private void handlePurchase(Long courseId) {
        try {
            // 更新课程购买数量（ZSet方式存储）
            stringRedisTemplate.opsForZSet().incrementScore(
                    COURSE_PURCHASE,
                    courseId.toString(),
                    1
            );
        } catch (Exception e) {
            log.error("Handle course purchase error, courseId: {}", courseId, e);
        }
    }

    /**
     * 处理课程点赞
     */
    private void handleCourseLike(Long courseId) {
        try {
            // 更新课程点赞数（String方式存储）
            stringRedisTemplate.opsForValue().increment(
                    COURSE_LIKE + courseId
            );
        } catch (Exception e) {
            log.error("Handle course like error, courseId: {}", courseId, e);
        }
    }

    /**
     * 处理取消点赞
     */
    private void handleCancelLike(Long courseId) {
        try {
            stringRedisTemplate.opsForValue().decrement(
                    COURSE_LIKE + courseId
            );
        } catch (Exception e) {
            log.error("Handle cancel like error, courseId: {}", courseId, e);
        }
    }

    /**
     * 处理教师关注
     */
    private void handleTeacherFollow(Long teacherId) {
        try {
            // 更新教师粉丝数（ZSet方式存储）
            stringRedisTemplate.opsForZSet().incrementScore(
                    TEACHER_FANS,
                    teacherId.toString(),
                    1
            );
        } catch (Exception e) {
            log.error("Handle teacher follow error, teacherId: {}", teacherId, e);
        }
    }

    /**
     * 处理取消关注
     */
    private void handleTeacherUnfollow(Long teacherId) {
        try {
            stringRedisTemplate.opsForZSet().incrementScore(
                    TEACHER_FANS,
                    teacherId.toString(),
                    -1
            );
        } catch (Exception e) {
            log.error("Handle teacher unfollow error, teacherId: {}", teacherId, e);
        }
    }
}




