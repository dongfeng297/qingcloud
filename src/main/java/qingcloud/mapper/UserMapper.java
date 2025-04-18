package qingcloud.mapper;

import org.apache.ibatis.annotations.*;
import qingcloud.entity.User;

import java.math.BigDecimal;

@Mapper
public interface UserMapper {
    @Select("select * from user where email=#{email}")
    public User getByEmali(String email);

    @Insert("insert into user (username,email) values(#{username},#{email})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void save(User user);

    @Update("update user set address=#{address} where id=#{userId}")
    void updateAdderss(Long userId, String address);

    @Update("update user set traffic_fee=#{extraFee} where id=#{userId}")
    void updateTrafficFee(Long userId, BigDecimal extraFee);

    @Select("select traffic_fee from user where id=#{userId}")
    BigDecimal getTrafficFee(Long userId);

    @Select("select balance from user where id=#{userId}")
    BigDecimal getBalance(Long userId);

    @Update("update user set balance=balance-#{payAmount} where id=#{userId}")
    void updateBalance(Long userId, BigDecimal payAmount);



    @Select("select * from user where id=#{userId}")
    User getById(Long userId);



}
