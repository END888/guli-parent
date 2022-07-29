package com.atguigu.guli.service.edu.service;

import com.atguigu.guli.service.edu.entity.Course;
import com.atguigu.guli.service.edu.vo.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
public interface CourseService extends IService<Course> {

    Page<AdminCourseItemVo> queryCourseItemVoPage(Integer pageNum, Integer pageSize, CourseQueryVo courseQueryVo);

    /**
     * 保存课程基本信息，返回生成的id
     * @param adminCourseInfoBo
     * @return
     */
    String saveCourseInfo(AdminCourseInfoVo adminCourseInfoBo);

    /**
     * 根据课程id查询课程基本信息
     * @param courseId
     * @return
     */
    AdminCourseInfoVo getCourseInfo(String courseId);

    /**
     * 修改course相关表数据
     * @param adminCourseInfoBo
     */
    void updateCourseInfo(AdminCourseInfoVo adminCourseInfoBo);

    /**
     * 根据课程id删除课程基本信息
     * @param courseId
     */
    void deleteCourseInfo(String courseId);

    /**
     * 根据课程id查询课程的发布信息
     * @param id
     * @return
     */
    AdminCourseItemVo getPublishInfoById(String id);

    /**
     * 修改该课程为已发布
     * @param id
     */
    Boolean publishCourse(String id);

    List<Course> getCourseByCondition(ApiCourseQueryVo apiCourseQueryVo);

    CourseDetailInfo getCourseDetailPageById(String id);
}
