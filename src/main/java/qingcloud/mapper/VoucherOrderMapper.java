package qingcloud.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Service;
import qingcloud.entity.VoucherOrder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface VoucherOrderMapper {
    @Insert("insert into voucher_order (id,user_id,voucher_id) values (#{id},#{userId},#{voucherId})")
    void addVoucherOrder(VoucherOrder voucherOrder);


    @Select("select count(*) from voucher_order where user_id = #{userId} and id = #{id}")
    int query(Long userId, Long id);

    @Select("select * from voucher_order where user_id = #{userId}")
    List<VoucherOrder> getListByUserId(Long userId);

    @Select("select voucher_id from voucher_order where user_id = #{userId} and status=2")
    List<Long> getVoucherIds(Long userId);

    @Update("update voucher_order set status = #{status} where user_id = #{userId} and voucher_id = #{voucherId}")
    void updateStatus(Long userId,Long voucherId,Integer status);

    @Select("select status from voucher_order where user_id = #{userId} and voucher_id = #{voucherId}")
    Integer getStatus(Long userId, Long voucherId);

    @Select("select * from voucher_order where id = #{orderId}")
    VoucherOrder getById(Long orderId);


    @Update("update voucher_order set status= #{paid},pay_time = #{now},pay_amount = #{payValue} where id = #{orderId}")
    void update(Long orderId, Integer paid, LocalDateTime now, BigDecimal payValue);

    @Update("update voucher_order set status= #{canceled},pay_over_time = #{now} where id = #{orderId}")
    void updateStatusAndPayOverTime(Long orderId, int canceled, LocalDateTime now);

    @Update("update voucher_order set status= #{paid} where id = #{orderId}")
    void rallbackStatus(Long orderId,Integer paid);

    @Select("select * from voucher_order where voucher_id=#{voucherId} and user_id=#{userId}")
    VoucherOrder getByVoucherIdAndUserId(Long voucherId, Long userId);
}
