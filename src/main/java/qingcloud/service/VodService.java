package qingcloud.service;

import org.springframework.web.multipart.MultipartFile;
import qingcloud.dto.Result;

public interface VodService {
    /**
     * 视频上传
     */
    Result uploadVideo(MultipartFile file);

    /**
     * 获取视频播放凭证
     */
    Result getPlayAuth(String videoId);

    /**
     * 获取视频播放地址
     */
    Result getPlayUrl(String videoId);

    /**
     * 删除视频
     */
    Result removeVideo(String videoId);


}
