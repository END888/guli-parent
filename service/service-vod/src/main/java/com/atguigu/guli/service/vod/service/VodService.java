package com.atguigu.guli.service.vod.service;

import java.io.InputStream;

public interface VodService {

    String upload(String title, String fileName, InputStream inputStream);
}
