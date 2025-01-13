package qingcloud.service.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import qingcloud.dto.Result;
import qingcloud.entity.Course;
import qingcloud.entity.CourseRanking;
import qingcloud.entity.TeacherRanking;
import qingcloud.entity.User;
import qingcloud.mapper.CourseMapper;
import qingcloud.mapper.CourseRankingMapper;
import qingcloud.mapper.TeacherRankingMapper;
import qingcloud.mapper.UserMapper;
import qingcloud.service.RankingService;
import qingcloud.vo.CourseRankingVO;
import qingcloud.vo.TeacherRankingVO;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static qingcloud.constant.RedisConstant.*;


@Service
public class RankingServiceImpl implements RankingService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private CourseRankingMapper courseRankingMapper;
    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private TeacherRankingMapper teacherRankingMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public Result getCourseRanking() {
        // 1. 获取课程购买排行
        Set<ZSetOperations.TypedTuple<String>> topCourses =
                stringRedisTemplate.opsForZSet().reverseRangeWithScores(
                        COURSE_PURCHASE,
                        0,
                        9  // 获取前10名
                );

        List<CourseRanking> rankingList = new ArrayList<>();
        List<CourseRankingVO> rankingVOList = new ArrayList<>();
        if (topCourses != null && !topCourses.isEmpty()) {
            // 处理课程排名
            int position = 1;
            for (ZSetOperations.TypedTuple<String> tuple : topCourses) {
                Long courseId = Long.parseLong(tuple.getValue());

                // 保存ranking信息
                CourseRanking ranking = new CourseRanking();
                ranking.setCourseId(courseId);
                ranking.setRankingPosition(position);
                rankingList.add(ranking);



                //保存VO
                Course course = courseMapper.getById(courseId);
                if(course==null){
                    return Result.fail("课程不存在");
                }
                CourseRankingVO courseRankingVO = new CourseRankingVO();
                courseRankingVO.setTitle(course.getTitle());
                courseRankingVO.setCoverUrl(course.getCoverUrl());
                courseRankingVO.setSaleNum(tuple.getScore().intValue());
                courseRankingVO.setRankingPosition(position);
                rankingVOList.add(courseRankingVO);

                position++;
            }

        }
        if (rankingList != null && !rankingList.isEmpty()) {
            // 批量保存ranking信息（用作历史记录）
            courseRankingMapper.insertBatch(rankingList);
        }
        return Result.ok(rankingVOList);

    }

    @Override
    public Result getTeacherRanking() {
        // 2. 获取教师粉丝排行
        Set<ZSetOperations.TypedTuple<String>> topTeachers =
                stringRedisTemplate.opsForZSet().reverseRangeWithScores(
                        TEACHER_FANS,
                        0,
                        9
                );

        List<TeacherRanking> rankingList = new ArrayList<>();
        List<TeacherRankingVO> rankingVOList = new ArrayList<>();
        if (topTeachers != null && !topTeachers.isEmpty()) {
            // 处理课程排名
            int position = 1;
            for (ZSetOperations.TypedTuple<String> tuple : topTeachers) {
                Long teacherId = Long.parseLong(tuple.getValue());

                // 保存ranking信息
                TeacherRanking teacherRanking = new TeacherRanking();
                teacherRanking.setTeacherId(teacherId);
                teacherRanking.setRankingPosition(position);
                rankingList.add(teacherRanking);


                //保存VO
                User user = userMapper.getById(teacherId);
                if(user==null){
                    return Result.fail("老师不存在");
                }
                TeacherRankingVO teacherRankingVO = new TeacherRankingVO();
                teacherRankingVO.setName(user.getName());
                teacherRankingVO.setAvatar(user.getAvatar());
                teacherRankingVO.setFansNum(tuple.getScore().intValue());
                teacherRankingVO.setRankingPosition(position);
                rankingVOList.add(teacherRankingVO);

                position++;
            }

        }
        if (rankingList != null && !rankingList.isEmpty()) {
            // 批量保存ranking信息
            teacherRankingMapper.insertBatch(rankingList);
        }
        return Result.ok(rankingVOList);


    }
}
