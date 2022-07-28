package com.atguigu.guli.service.edu.service;

import com.atguigu.guli.service.edu.entity.Video;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author atguigu
 * @since 2022-07-16
 */
public interface VideoService extends IService<Video> {

    List<Video> getVideoListByChapterId(String chapterId);
}
