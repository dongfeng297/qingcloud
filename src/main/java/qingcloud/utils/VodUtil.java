package qingcloud.utils;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
@Slf4j
@Data
@Component
public class VodUtil {

    @Value("${aliyun.access-key-id}")
    private String accessKeyId;
    @Value("${aliyun.access-key-secret}")
    private String accessKeySecret;


    /**
     * 初始化视频点播客户端
     */
    public DefaultAcsClient initVodClient() {
        DefaultProfile profile = DefaultProfile.getProfile(
                "cn-shanghai",
                accessKeyId,
                accessKeySecret);
        return new DefaultAcsClient(profile);
    }
}