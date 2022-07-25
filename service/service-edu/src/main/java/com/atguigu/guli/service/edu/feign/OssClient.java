package com.atguigu.guli.service.edu.feign;

import com.atguigu.guli.service.base.model.BaseEntity;
import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.edu.feign.fallback.OssClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Service
@FeignClient(value = "service-oss",fallback = OssClientFallback.class)
public interface OssClient {

    @GetMapping("/admin/oss/file/entity")
    String entity(@SpringQueryMap BaseEntity baseEntity);

    @DeleteMapping("admin/oss/file/delete")
    R delete(@RequestParam String path , @RequestParam String module);
}
