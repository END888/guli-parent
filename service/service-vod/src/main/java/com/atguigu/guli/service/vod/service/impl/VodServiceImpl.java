package com.atguigu.guli.service.vod.service.impl;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.atguigu.guli.service.vod.properties.VodProperties;
import com.atguigu.guli.service.vod.service.VodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.InputStream;


@Service
public class VodServiceImpl implements VodService {

    @Autowired
    private VodProperties vodProperties;
    private String accessKeyId;//: LTAI5tFKSrqJT9NpwFrNjAeM
    private String accessKeySecret;//: HULmexek99zeNE2RbEsRjEdEVqtWeK
    private String templateGroupId;// 转码模板组id
    private String workflowId;// 工作流id

    @PostConstruct
    public void init() {
        accessKeyId = vodProperties.getAccessKeyId();
        accessKeySecret = vodProperties.getAccessKeySecret();
        templateGroupId = vodProperties.getTemplateGroupId();
        workflowId = vodProperties.getWorkflowId();
    }
    @Override
    public String upload(String title, String fileName, InputStream inputStream) {
        return testUploadStream(accessKeyId,accessKeySecret,title,fileName,inputStream,workflowId);
    }

    /**
     * 流式上传接口,返回视频id
     */
    private String testUploadStream(String accessKeyId, String accessKeySecret, String title, String fileName, InputStream inputStream,String workflowId) {
        UploadStreamRequest request = new UploadStreamRequest(accessKeyId, accessKeySecret, title, fileName, inputStream);
        request.setWorkflowId(workflowId);
        UploadVideoImpl uploader = new UploadVideoImpl();
        UploadStreamResponse response = uploader.uploadStream(request);
        System.out.print("RequestId=" + response.getRequestId() + "\n");  //请求视频点播服务的请求ID
        if (response.isSuccess()) {
            System.out.print("VideoId=" + response.getVideoId() + "\n");
            return response.getVideoId();
        } else { //如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因
            System.out.print("VideoId=" + response.getVideoId() + "\n");
            System.out.print("ErrorCode=" + response.getCode() + "\n");
            System.out.print("ErrorMessage=" + response.getMessage() + "\n");
            return null;
        }
    }

}
