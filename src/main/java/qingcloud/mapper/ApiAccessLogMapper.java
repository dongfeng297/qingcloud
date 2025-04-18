package qingcloud.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import qingcloud.entity.ApiAccessLog;

@Mapper
public interface ApiAccessLogMapper {
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("INSERT INTO api_access_log (" +
            " username, ip_address, start_time, execution_time, request_url, " +
            " method_name, request_method,  status_code, error_message,operation_desc) " +
            "VALUES (" +
            " #{username}, #{ipAddress}, #{startTime}, #{executionTime}, #{requestUrl}, " +
            " #{methodName}, #{requestMethod},  #{statusCode}, #{errorMessage},#{operationDesc})")
    void insert(ApiAccessLog apiAccessLog);

}
