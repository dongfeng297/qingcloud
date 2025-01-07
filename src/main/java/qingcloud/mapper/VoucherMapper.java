package qingcloud.mapper;

import org.apache.ibatis.annotations.*;
import qingcloud.entity.Voucher;

import java.util.List;

@Mapper
public interface VoucherMapper {
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into voucher (title, sub_title, rules, pay_value, actual_value, stock, status, begin_time, end_time) VALUES (" +
            " #{title}, #{subTitle}, #{rules}, #{payValue}, #{actualValue}, #{stock}, #{status}, #{beginTime}, #{endTime})")
    void addVoucher(Voucher voucher);

    @Select("select * from voucher where id = #{id}")
    Voucher getById(Long id);

    @Update("update voucher set stock = stock - 1 where id = #{id} and stock > 0")
    int updateStock(Long id);



    List<Voucher> getVouchers(List<Long> ids);

    @Update("update voucher set stock = stock + 1 where id = #{voucherId}")
    void rollbackStock(Long voucherId);
}
