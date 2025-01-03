package qingcloud.service.serviceImpl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import qingcloud.constant.RedisConstant;
import qingcloud.dto.ConditonCoursePageQueryDTO;
import qingcloud.dto.PageResult;
import qingcloud.dto.Result;
import qingcloud.entity.Course;
import qingcloud.mapper.CourseMapper;
import qingcloud.service.CourseService;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static qingcloud.constant.RedisConstant.*;

@Service
public class CourseServiceImpl implements CourseService {
    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public Result addCourse(Course course) {
        courseMapper.insert(course);
        return Result.ok();
    }

    @Override
    public Result getCourse(Long id) {
        //1.先查缓存，有则返回
        String courseJson = stringRedisTemplate.opsForValue().get(CACHE_COURSE_KEY + id);
        if(StrUtil.isNotBlank(courseJson)){
            Course course = BeanUtil.toBean(courseJson, Course.class);
            return Result.ok(course);
        }
        //2.如果没有实际数据，看是否为空字符串(防止缓存穿透打到数据库上）
        if("".equals(courseJson)){
            return Result.fail("课程不存在");
        }



        //3.获取互斥锁
        String lockKey=LOCK_COURSE_KEY+id;
        //4.获得锁失败则休眠+重试
        for (int i=0;i<30;i++) {//设置最大重置次数
            boolean isLock = tryLock(lockKey);
            if (!isLock) {
                try {
                    Thread.sleep(20);
                    continue;
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            try {
                //获得锁后二次检查缓存
                String courseJson1 = stringRedisTemplate.opsForValue().get(CACHE_COURSE_KEY + id);
                if(StrUtil.isNotBlank(courseJson1)){
                    Course course = BeanUtil.toBean(courseJson1, Course.class);
                    return Result.ok(course);
                }
                //2.如果没有实际数据，看是否为空字符串(防止缓存穿透打到数据库上）
                if("".equals(courseJson1)){
                    return Result.fail("课程不存在");
                }


                //5.获取锁成功查数据库重建缓存
                Course course = courseMapper.getById(id);
                if (course == null) {
                    //在缓存中写入一个空值（防止缓存穿透）
                    stringRedisTemplate.opsForValue().set(CACHE_COURSE_KEY + id, "", CACHE_NULL_TTL, TimeUnit.MINUTES);
                    return Result.fail("课程不存在");
                }
                //6.写缓存
                String json = JSONUtil.toJsonStr(course);
                stringRedisTemplate.opsForValue().set(CACHE_COURSE_KEY + id, json, CACHE_COURSE_TTL + new Random().nextInt(10), TimeUnit.MINUTES);

                return Result.ok(course);
            } finally {
                unLock(lockKey);
            }
        }
        return Result.fail("系统繁忙，请稍后再试");
    }

    @Override
    public Result deleteCourse(Long id) {
        courseMapper.deleteById(id);
        return Result.ok();
    }
    @Override
    public Result updateCourse(Course course) {
        if(course.getId()==null){
            return Result.fail("课程id不能为空");
        }
        //先操作数据库
        courseMapper.update(course);
        //再删除缓存
        stringRedisTemplate.delete(CACHE_COURSE_KEY+course.getId());
        return Result.ok();
    }


    @Override
    public Result getCourseByContiditon(ConditonCoursePageQueryDTO queryDTO) {
        PageHelper.startPage(queryDTO.getPage(),queryDTO.getPageSize());
        Page<Course> pages=courseMapper.pageQuery(queryDTO);
        return Result.ok(new PageResult(pages.getTotal(),pages.getResult()));
    }


    Boolean tryLock(String key){
        Boolean flag=stringRedisTemplate.opsForValue().setIfAbsent(key,"1",10,TimeUnit.SECONDS);
        return Boolean.TRUE.equals(flag);
    }
    void unLock(String key){
        stringRedisTemplate.delete(key);
    }
}
