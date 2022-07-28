package com.atguigu.guli.service.edu.service;

import com.atguigu.guli.service.edu.entity.Chapter;
import com.atguigu.guli.service.edu.vo.ChapterVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author atguigu
 * @since 2022-07-16
 */
public interface ChapterService extends IService<Chapter> {

    /**
     * 根据课程id，查询章节列表
     * @param courseId
     * @return
     */
    List<ChapterVo> getChapterListByCourseId(String courseId);

    /**
     * 根据章节id删除
     * @param id
     */
    void deleteById(String id);
}
