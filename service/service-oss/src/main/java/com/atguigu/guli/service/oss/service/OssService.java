package com.atguigu.guli.service.oss.service;

import org.springframework.web.multipart.MultipartFile;

public interface OssService {

    /**
     * 文件上传至阿里云
     */
    String upload(MultipartFile file, String module);


    void delete(String path, String module);
}
