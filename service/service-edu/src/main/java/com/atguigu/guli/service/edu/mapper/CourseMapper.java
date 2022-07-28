package com.atguigu.guli.service.edu.mapper;

import com.atguigu.guli.service.edu.entity.Course;
import com.atguigu.guli.service.edu.vo.AdminCourseItemVo;
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

    /**
     * 进行分页条件查询
     * @param page  分页对象
     * @return  查询结果
     */
    List<AdminCourseItemVo> selectCourseItemVoPage(Page<AdminCourseItemVo> page);

    /**
     * 根据课程id查询课程的发布信息
     * @param id
     * @return
     */
    AdminCourseItemVo getPublishInfoById(@Param("id") String id);
}
