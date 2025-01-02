package qingcloud.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import qingcloud.entity.User;

@Mapper
public interface UserMapper {
    @Select("select * from user where email=#{email}")
    public User getByEmali(String email);

    @Insert("insert into user (username,email) values(#{username},#{email})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    public void save(User user);
}
