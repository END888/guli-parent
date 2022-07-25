package com.atguigu.guli.service.edu.service.impl;

import com.atguigu.guli.service.edu.vo.TeacherQuery;
import com.atguigu.guli.service.edu.entity.Teacher;
import com.atguigu.guli.service.edu.feign.OssClient;
import com.atguigu.guli.service.edu.mapper.TeacherMapper;
import com.atguigu.guli.service.edu.service.TeacherService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2022-07-16
 */
@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements TeacherService {


    @Autowired
    private OssClient ossClient;

    @Override
    public IPage<Teacher> selectPage(Long pageNum, Long limit, TeacherQuery teacherQuery) {
        // 1、创建page对象
        Page<Teacher> page = new Page<>(pageNum, limit);
        // 2、构建查询语句（排序）
        LambdaQueryWrapper<Teacher> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(Teacher::getSort);
        // 3、判断teacherQuery是否为空
        if (StringUtils.isEmpty(teacherQuery)) {
            return baseMapper.selectPage(page, wrapper);
        }
        // 4、对查询条件分别进行非空判断，并添加查询条件
        String name = teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String joinDateBegin = teacherQuery.getJoinDateBegin();
        String joinDateEnd = teacherQuery.getJoinDateEnd();

        if (!StringUtils.isEmpty(name)) {
            wrapper.likeRight(Teacher::getName, name);
        }
        if (level != null) {
            wrapper.eq(Teacher::getLevel, level);
        }
        if (!StringUtils.isEmpty(joinDateBegin)) {
            wrapper.ge(Teacher::getJoinDate, joinDateBegin);
        }
        if (!StringUtils.isEmpty(joinDateEnd)) {
            wrapper.le(Teacher::getJoinDate, joinDateEnd);
        }
        // 5、查询返回
        return baseMapper.selectPage(page, wrapper);
    }

    @Override
    public List<Map<String, Object>> selectNameListByKey(String key) {
        QueryWrapper<Teacher> wrapper = new QueryWrapper<>();
        wrapper.select("name").likeRight("name", key);

        return baseMapper.selectMaps(wrapper);
    }

    @Override
    public boolean removeAndAvatarById(String id) {
        // 根据id查询,然后把数据库中的头像修改为null
        Teacher teacher = baseMapper.selectById(id);
        teacher.setAvatar("");
        baseMapper.updateById(teacher);
        // 执行删除操作
        int result = baseMapper.deleteById(teacher);
        return result > 0;
    }

    @Transactional
    @Override
    public boolean deleteById(String id) {
        // 先根据讲师id查询，然后将头像设置为空字符串，进行更新
        Teacher teacher = baseMapper.selectById(id);
        teacher.setAvatar("");
        baseMapper.updateById(teacher);
        int i = baseMapper.deleteById(id);
        return i > 0;
    }
}
