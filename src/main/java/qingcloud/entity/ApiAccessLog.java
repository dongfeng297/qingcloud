package qingcloud.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@ApiModel(value = "接口访问日志")
public class ApiAccessLog {
    @ApiModelProperty(value = "主键ID", example = "1")
    private Long id;
    
    @ApiModelProperty(value = "用户名", example = "dongfeng297")
    private String username;
    
    @ApiModelProperty(value = "IP地址", required = true, example = "192.168.1.1")
    private String ipAddress;
    
    @ApiModelProperty(value = "开始操作时间", required = true, example = "2025-01-16 03:50:21")
    private LocalDateTime startTime;
    
    @ApiModelProperty(value = "方法执行时长(毫秒)", required = true, example = "100")
    private Long executionTime;
    
    @ApiModelProperty(value = "请求URL", required = true, example = "/api/users")
    private String requestUrl;

    @ApiModelProperty(value = "方法名", required = true, example = "getUserInfo")
    private String methodName;
    
    @ApiModelProperty(value = "请求方法", required = true, example = "GET", 
            allowableValues = "GET,POST,PUT,DELETE")
    private String requestMethod;

    @ApiModelProperty(value = "操作描述", example = "查询用户信息/新增订单/删除商品")
    private String operationDesc;
    
    @ApiModelProperty(value = "HTTP状态码", example = "200")
    private Integer statusCode;
    
    @ApiModelProperty(value = "错误信息", example = "用户未找到")
    private String errorMessage;
}