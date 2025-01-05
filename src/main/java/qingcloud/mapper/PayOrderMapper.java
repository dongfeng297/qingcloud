package qingcloud.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import qingcloud.entity.PayOrder;

@Mapper
public interface PayOrderMapper {
    @Select("select * from pay_order where order_id = #{orderId}")
    PayOrder getByOrderId(Long orderId);
}
