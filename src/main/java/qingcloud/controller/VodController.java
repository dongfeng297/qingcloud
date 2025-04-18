package qingcloud.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import qingcloud.annotation.Log;
import qingcloud.dto.Result;
import qingcloud.service.VodService;

@RestController
@RequestMapping("/vod")
@CrossOrigin
public class VodController {

    @Autowired
    private VodService vodService;

    /**
     * 上传视频
     */
    @PostMapping("/upload")
    @ApiOperation(value = "上传视频")
    @Log(value = "上传视频")
    public Result uploadVideo(@RequestParam("file") MultipartFile file) {
        return vodService.uploadVideo(file);
    }
    /**
     * 获取视频播放凭证
     */
    @GetMapping("/get-play-auth/{videoId}")
    @ApiOperation(value = "获取视频播放凭证")
    public Result getPlayAuth(@PathVariable String videoId) {
        return vodService.getPlayAuth(videoId);
    }
    /**
     * 获取视频播放地址
     */
    @GetMapping("/get-play-url/{videoId}")
    @ApiOperation(value = "获取视频播放地址")
    public Result getPlayUrl(@PathVariable String videoId) {
        return vodService.getPlayUrl(videoId);
    }

    /**
     * 删除视频
     */
    @DeleteMapping("/remove/{videoId}")
    @ApiOperation(value = "删除视频")
    @Log(value = "删除视频")
    public Result removeVideo(@PathVariable String videoId) {
        return vodService.removeVideo(videoId);
    }

}
