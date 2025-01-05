package qingcloud.service.serviceImpl;

import cn.hutool.core.util.IdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qingcloud.dto.CourseOrderDTO;
import qingcloud.dto.Result;
import qingcloud.entity.Course;
import qingcloud.entity.Voucher;
import qingcloud.mapper.*;
import qingcloud.service.CourseOrderService;
import qingcloud.utils.UserHolder;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class CourseOrderServiceImpl implements CourseOrderService {
    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private CourseOrderMapper courseOrderMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private VoucherMapper voucherMapper;
    @Autowired
    private VoucherOrderMapper voucherOrderMapper;

    public CourseOrderServiceImpl(CourseMapper courseMapper) {
        this.courseMapper = courseMapper;
    }

    @Override
    @Transactional
    public Result orderCourse(CourseOrderDTO courseOrderDTO) {
        Long courseId = courseOrderDTO.getCourseId();
        Long userId = UserHolder.getUser().getId();
        Long voucherId = courseOrderDTO.getVoucherId();

        Long orderId= IdUtil.getSnowflake().nextId();
        Map<String,Object>map=new HashMap<>();
        map.put("orderId",orderId);
        map.put("userId",userId);
        map.put("courseId",courseId);

        //查询订单表判断是否已经购买过
        int count=courseOrderMapper.getByUserIdAndCourseId(map);
        if(count>0) return Result.fail("您已经购买过该课程");
        //查询数据库获得课程类型
        Course course=courseMapper.getById(courseId);
        if(course.getCourseType()==1){
            //如果是普通课程，直接下单返回
            map.put("price",course.getPrice());
            courseOrderMapper.addOrder(map);
            return Result.ok(orderId);
        }
        //如果是家教课程
        Integer status=voucherOrderMapper.getStatus(userId,voucherId);
        if(status!=2){
            return Result.fail("优惠券状态异常");
        }
        //交通费
        BigDecimal trafficFee=userMapper.getTrafficFee(userId);
        if (trafficFee == null) {
            trafficFee = BigDecimal.ZERO; // 设置默认值
        }
        //课程费用加上交通费
        BigDecimal total=course.getPrice().subtract(trafficFee);
        //优惠卷折扣
        Voucher voucher = voucherMapper.getById(voucherId);
        //修改优惠券订单状态为已核销
        voucherOrderMapper.updateStatus(userId,voucherId,3);
        BigDecimal discount = voucher.getActualValue();

        //实际需要支付的费用
        BigDecimal payValue=total.subtract(discount);

        map.put("price",payValue);
        map.put("voucherId",voucherId);
        courseOrderMapper.addOrderWithVoucher(map);

        return Result.ok(orderId);

    }



}
