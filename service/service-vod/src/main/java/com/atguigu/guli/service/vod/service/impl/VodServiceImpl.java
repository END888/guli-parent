package com.atguigu.guli.service.vod.service.impl;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
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

    @Override
    public String GetVideoPlayAuth(String videoSourceId) {
        // 2、获取播放地址
        DefaultAcsClient client = initVodClient(accessKeyId, accessKeySecret);
        GetVideoPlayAuthResponse response = new GetVideoPlayAuthResponse();
        String playAuth = "";
        try {
            String id = videoSourceId;
            System.out.println("id = " + id);
            //6f6fc0ff07f14d909e095a685bf8b56b
            response = getVideoPlayAuth(client,id);
            //播放凭证
            playAuth = response.getPlayAuth();
            System.out.print("PlayAuth = " + playAuth + "\n");
            //VideoMeta信息
            System.out.print("VideoMeta.Title = " + response.getVideoMeta().getTitle() + "\n");
        } catch (Exception e) {
            System.out.print("ErrorMessage = " + e.getLocalizedMessage());
        }
        System.out.print("RequestId = " + response.getRequestId() + "\n");
        return playAuth;
    }

    public static DefaultAcsClient initVodClient(String accessKeyId, String accessKeySecret) {
        String regionId = "cn-shanghai";  // 点播服务接入区域
        DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessKeySecret);
        DefaultAcsClient client = new DefaultAcsClient(profile);
        return client;
    }

    /*获取播放凭证函数*/
    public static GetVideoPlayAuthResponse getVideoPlayAuth(DefaultAcsClient client,String videoId) throws Exception {
        GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
        request.setVideoId(videoId);
        request.setAuthInfoTimeout(600L);
        return client.getAcsResponse(request);
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
