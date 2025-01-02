package qingcloud.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;
import qingcloud.entity.VoucherOrder;

@Mapper
public interface VoucherOrderMapper {
    @Insert("insert into voucher_order (id,user_id,voucher_id) values (#{id},#{userId},#{voucherId})")
    void addVoucherOrder(VoucherOrder voucherOrder);

    @Select("select count(*) from voucher_order where user_id = #{userId}")
    int getByUserId(Long userId);
}
