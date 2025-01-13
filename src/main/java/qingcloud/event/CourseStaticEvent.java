package qingcloud.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import qingcloud.constant.CourseStaticEventType;


@Getter
@AllArgsConstructor
public class CourseStaticEvent {
    private CourseStaticEventType eventType;
    private Long courseId;
    private Long teacherId;


    // 构造方法重载，方便使用
    public static CourseStaticEvent purchaseEvent(Long courseId) {
        return new CourseStaticEvent(CourseStaticEventType.PURCHASE, courseId, null);
    }

    public static CourseStaticEvent likeEvent(Long courseId) {
        return new CourseStaticEvent(CourseStaticEventType.LIKE, courseId, null);
    }

    public static CourseStaticEvent followTeacherEvent(Long teacherId) {
        return new CourseStaticEvent(CourseStaticEventType.FOLLOW_TEACHER, null, teacherId);
    }
}