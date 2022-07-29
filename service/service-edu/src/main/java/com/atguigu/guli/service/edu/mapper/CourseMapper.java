package com.atguigu.guli.service.edu.mapper;

import com.atguigu.guli.service.edu.entity.Course;
import com.atguigu.guli.service.edu.vo.AdminCourseItemVo;
import com.atguigu.guli.service.edu.vo.CourseDetailInfo;
import com.atguigu.guli.service.edu.vo.CourseQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
public interface CourseMapper extends BaseMapper<Course> {

    List<AdminCourseItemVo> selectCourseItemVoPage(@Param("page") Page<AdminCourseItemVo> page, @Param("ew") QueryWrapper<CourseQueryVo> wrapper);

    /**
     * 根据课程id查询课程的发布信息
     * @param id
     * @return
     */
    AdminCourseItemVo getPublishInfoById(@Param("id") String id);

    List<AdminCourseItemVo> getCourseItems();

    CourseDetailInfo getCourseDetailPageById(@Param("id") String id);
}
