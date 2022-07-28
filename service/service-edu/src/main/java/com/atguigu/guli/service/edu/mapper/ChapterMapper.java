package com.atguigu.guli.service.edu.mapper;

import com.atguigu.guli.service.edu.entity.Chapter;
import com.atguigu.guli.service.edu.vo.ChapterVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author atguigu
 * @since 2022-07-16
 */
public interface ChapterMapper extends BaseMapper<Chapter> {

    /**
     * 根据课程id查询 ChapterVo
     * @param courseId
     * @return
     */
    List<ChapterVo> getChapterListByCourseId(@Param("courseId") String courseId);

    /**
     * 根据QueryWrapper动态生成where进行查询
     * @param queryWrapper
     * @return
     */
    List<ChapterVo> getChapterListByCourseIdPassQueryWrapper(@Param("ew") QueryWrapper<ChapterVo> queryWrapper);
}
