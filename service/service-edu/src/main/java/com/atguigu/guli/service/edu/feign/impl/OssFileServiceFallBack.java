package com.atguigu.guli.service.edu.feign.impl;

import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.edu.feign.OssFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OssFileServiceFallBack implements OssFileService {
    @Override
    public R test() {
        return R.error();
    }

    @Override
    public R remove(String url) {
        log.warn("熔断保护");
        return null;
    }

    @Override
    public R test2(String str) {
        log.warn("熔断保护");
        return null;
    }

    @Override
    public R test3(R r) {
        return null;
    }
}
