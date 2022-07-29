package com.atguigu.guli.service.vod.service;

import java.io.InputStream;

public interface VodService {
    /**
     * 视频上传
     * @param title
     * @param fileName
     * @param inputStream
     * @return
     */
    String upload(String title, String fileName, InputStream inputStream);

    String GetVideoPlayAuth(String videoSourceId);
}
