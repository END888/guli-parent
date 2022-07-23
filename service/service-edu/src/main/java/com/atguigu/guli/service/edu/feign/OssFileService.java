package com.atguigu.guli.service.edu.feign;

import com.atguigu.guli.service.base.model.BaseEntity;
import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.edu.feign.impl.OssFileServiceFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@Service
@FeignClient(value = "service-oss",fallback = OssFileServiceFallBack.class)
public interface OssFileService {
    @GetMapping("/admin/oss/file/test")
    R test();

    @DeleteMapping("/admin/oss/file/remove")
    public R remove(@RequestBody String url);

    @GetMapping("/admin/oss/file/test2")
    R test2(@RequestParam String str);

    @GetMapping("/admin/oss/file/test3")
    R test3(@SpringQueryMap R r);

    @GetMapping("/admin/oss/file/entity")
    String entity(@SpringQueryMap BaseEntity baseEntity);
}
