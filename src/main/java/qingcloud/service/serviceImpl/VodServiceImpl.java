package qingcloud.service.serviceImpl;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import qingcloud.dto.Result;
import qingcloud.service.VodService;
import qingcloud.utils.VodUtil;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VodServiceImpl implements VodService {

    @Autowired
    private VodUtil vodUtil;

    @Override
    public Result uploadVideo(MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();
            String title = fileName.substring(0, fileName.lastIndexOf("."));

            InputStream inputStream = file.getInputStream();

            UploadStreamRequest request = new UploadStreamRequest(
                    vodUtil.getAccessKeyId(),
                    vodUtil.getAccessKeySecret(),
                    title,
                    fileName,
                    inputStream);
            // 指定加密模板ID
            request.setTemplateGroupId("5ee20a46283ce0a7d4bcec962c5e2558");

            UploadVideoImpl uploader = new UploadVideoImpl();
            UploadStreamResponse response = uploader.uploadStream(request);

            if (response.isSuccess()) {
                return Result.ok(response.getVideoId());
            } else {
                return Result.fail("上传视频失败：" + response.getMessage());
            }
        } catch (Exception e) {
            return Result.fail("上传视频失败：" + e.getMessage());
        }
    }

    @Override
    public Result getPlayAuth(String videoId) {
        try {
            DefaultAcsClient client = vodUtil.initVodClient();

            GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
            request.setVideoId(videoId);

            GetVideoPlayAuthResponse response = client.getAcsResponse(request);
            String playAuth = response.getPlayAuth();

            return Result.ok(playAuth);
        } catch (Exception e) {
            return Result.fail("获取播放凭证失败：" + e.getMessage());
        }
    }

    @Override
    public Result getPlayUrl(String videoId) {
        try {
            DefaultAcsClient client = vodUtil.initVodClient();

            GetPlayInfoRequest request = new GetPlayInfoRequest();
            request.setVideoId(videoId);

            GetPlayInfoResponse response = client.getAcsResponse(request);
            List<GetPlayInfoResponse.PlayInfo> playInfoList = response.getPlayInfoList();

            // 播放地址信息
            List<String> playUrls = new ArrayList<>();
            for (GetPlayInfoResponse.PlayInfo playInfo : playInfoList) {
                if (playInfo != null && playInfo.getPlayURL() != null) {
                    playUrls.add(playInfo.getPlayURL());
                }
            }
            return Result.ok(playUrls);
        } catch (Exception e) {
            return Result.fail("获取播放地址失败：" + e.getMessage());
        }
    }
    @Override
    public Result removeVideo(String videoId) {
        try {
            DefaultAcsClient client = vodUtil.initVodClient();

            DeleteVideoRequest request = new DeleteVideoRequest();
            request.setVideoIds(videoId);

            client.getAcsResponse(request);
            return Result.ok();
        } catch (Exception e) {
            return Result.fail("删除视频失败：" + e.getMessage());
        }
    }
}