package com.atguigu.guli.service.edu.service;

import com.atguigu.guli.service.edu.bo.TeacherQuery;
import com.atguigu.guli.service.edu.entity.Teacher;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author atguigu
 * @since 2022-07-16
 */
public interface TeacherService extends IService<Teacher> {
    IPage<Teacher> selectPage(Long pageNum, Long limit, TeacherQuery teacherQuery);
}
