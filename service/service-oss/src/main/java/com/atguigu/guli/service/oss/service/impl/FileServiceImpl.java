package com.atguigu.guli.service.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.CannedAccessControlList;
import com.atguigu.guli.service.oss.service.FileService;
import com.atguigu.guli.service.oss.util.OssProperties;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private OssProperties ossProperties;

    @Override
    public String upload(InputStream inputStream, String module, String oFilename) {

        String endpoint = ossProperties.getEndpoint();
        String keyid = ossProperties.getKeyId();
        String keysecret = ossProperties.getKeySecret();
        String bucketname = ossProperties.getBucketName();

        //判断oss实例是否存在：如果不存在则创建，如果存在则获取
        OSS ossClient = new OSSClientBuilder().build(endpoint, keyid, keysecret);
        if (!ossClient.doesBucketExist(bucketname)) {
            //创建bucket
            ossClient.createBucket(bucketname);
            //设置oss实例的访问权限：公共读
            ossClient.setBucketAcl(bucketname, CannedAccessControlList.PublicRead);
        }

        //构建日期路径：avatar/2019/02/26/文件名
        String folder = new DateTime().toString("yyyy/MM/dd");

        //文件名：uuid.扩展名
        String fileName = UUID.randomUUID().toString();
        String fileExtension = oFilename.substring(oFilename.lastIndexOf("."));
        String key = module + "/" + folder + "/" + fileName + fileExtension;

        //文件上传至阿里云
        ossClient.putObject(ossProperties.getBucketName(), key, inputStream);

        // 关闭OSSClient。
//        ossClient.shutdown();
        // 对endpoint进行处理，去掉前面的https://
        //返回url地址
        endpoint = endpoint.replace("https://","");
        String result = "https://" + bucketname + "." + endpoint + "/" + key;
        // 关闭OSSClient。
        ossClient.shutdown();
        return result;
    }

    @Override
    public void removeFile(String url) {
        String endpoint = ossProperties.getEndpoint().replace("https://","");
        String bucketName = ossProperties.getBucketName();
        String keyId = ossProperties.getKeyId();
        String keySecret = ossProperties.getKeySecret();

        // 创建OSSClient实例
        OSS ossClient = new OSSClientBuilder().build(endpoint, keyId, keySecret);

        String host = "https://" + bucketName + "." + endpoint + "/";
        String objectName = url.substring(host.length());

        // 删除文件
        ossClient.deleteObject(bucketName,objectName);

        // 关闭OSSClient
        ossClient.shutdown();
    }
}
