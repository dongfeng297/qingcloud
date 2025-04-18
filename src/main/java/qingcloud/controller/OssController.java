package qingcloud.controller;

import cn.hutool.core.lang.UUID;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import qingcloud.annotation.Log;
import qingcloud.constant.OssProperties;
import qingcloud.dto.Result;


@RestController
@RequestMapping("/oss")
public class OssController {

    @Autowired
    private OssProperties ossProperties;

    @PostMapping("/upload")
    @ApiOperation(value = "上传文件")
    @Log(value = "上传文件")
    public Result upload(@RequestParam("file") MultipartFile file) {
        String url = "";
        try {
            // 创建OSSClient实例
            OSS ossClient = new OSSClientBuilder().build(
                    ossProperties.getEndpoint(),
                    ossProperties.getAccessKeyId(),
                    ossProperties.getAccessKeySecret());

            // 生成文件名
            String fileName = UUID.randomUUID().toString() +
                    file.getOriginalFilename().substring(
                            file.getOriginalFilename().lastIndexOf("."));

            // 上传文件
            ossClient.putObject(
                    ossProperties.getBucketName(),
                    fileName,
                    file.getInputStream());

            // 获取文件访问路径
            url = "https://" + ossProperties.getBucketName() + "." +
                    ossProperties.getEndpoint() + "/" + fileName;

            // 关闭OSSClient
            ossClient.shutdown();

            return Result.ok(url);

        } catch (Exception e) {
            return Result.fail("上传失败");
        }
    }
}
