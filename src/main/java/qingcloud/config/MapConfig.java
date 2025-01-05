package qingcloud.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "amap")
@Data
public class MapConfig {
    private String key;  // 高德地图API密钥
}