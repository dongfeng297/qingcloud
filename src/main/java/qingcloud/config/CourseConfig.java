package qingcloud.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Data
@Configuration
@ConfigurationProperties(prefix = "course")
public class CourseConfig {

    private BigDecimal perKmFee;
    private Integer maxDistance;
    private Integer freeDistance;
    private String orgAddress;
    private BigDecimal orgLatitude;
    private BigDecimal orgLongitude;

}

