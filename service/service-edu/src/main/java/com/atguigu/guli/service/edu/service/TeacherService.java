package com.atguigu.guli.service.edu.service;

import com.atguigu.guli.service.edu.bo.TeacherQuery;
import com.atguigu.guli.service.edu.entity.Teacher;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

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

    List<Map<String, Object>> selectNameListByKey(String key);

    /**
     * 根据讲师头像地址删除OSS中的头像
     * @param avatar
     * @return
     */
    boolean removeAvatarById(String avatar);


    /**
     * 根据讲师id删除，并且将数据库中的头像置为空
     * @param id
     * @return
     */
    boolean removeAndAvatarById(String id);
}
