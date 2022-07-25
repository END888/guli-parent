package com.atguigu.guli.service.edu.service.impl;

import com.atguigu.guli.service.edu.entity.Course;
import com.atguigu.guli.service.edu.entity.CourseDescription;
import com.atguigu.guli.service.edu.mapper.CourseMapper;
import com.atguigu.guli.service.edu.service.CourseDescriptionService;
import com.atguigu.guli.service.edu.service.CourseService;
import com.atguigu.guli.service.edu.vo.AdminCourseInfoVo;
import com.atguigu.guli.service.edu.vo.AdminCourseItemVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2022-07-16
 */
@Service
@Slf4j
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {
    @Autowired
    CourseService courseService;
    @Autowired
    CourseDescriptionService courseDescriptionService;

    /**
     *  MybatisPlus 自定义分页查询时，如果配置了分页拦截器，只需要将分页的页码和 pageSize 设置给一个 page 对象并
     *  传入到自定义的 mapper 方法中；
     *  MybatisPlus 的分页拦截器会自动使用分页条件生成 limit 条件；
     *
     *  自定义分页查询返回的结果是查询的数据集合，需要手动设置到 page 对象的分页数据集合中
     */
    @Override
    public Page<AdminCourseItemVo> queryCourseItemVoPage(Integer pageNum, Integer pageSize, Map<String, Object> query) {
        log.info("查询条件：{}",query);
        Page<AdminCourseItemVo> page = new Page<>(pageNum, pageSize);
        List<AdminCourseItemVo> data = baseMapper.selectCourseItemVoPage(page);
        page.setRecords(data);
        return page;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String saveCourseInfo(AdminCourseInfoVo adminCourseInfoBo) {
        // 1、先往 edu_course 中添加
        Course course = new Course();
        BeanUtils.copyProperties(adminCourseInfoBo,course);
        courseService.save(course);
        // 2、根据添加后，同步的 course 的id，往 edu_course_description 中添加
        // 【注意：】 course 表中的每条记录，它的 id 正好与 edu_course_description 中的 id 对应
        CourseDescription courseDescription = new CourseDescription();
        BeanUtils.copyProperties(adminCourseInfoBo,courseDescription);
        courseDescription.setId(course.getId());
        courseDescriptionService.save(courseDescription);
        return course.getId();
    }

    @Override
    public AdminCourseInfoVo getCourseInfo(String courseId) {
        // 根据课程 id 查询 course
        Course course = courseService.getById(courseId);
        // 根据课程 id 查询 courseDescription
        CourseDescription courseDescription = courseDescriptionService.getById(courseId);
        // 将查询到的信息复制到 adminCourseInfoVo 中
        AdminCourseInfoVo adminCourseInfoVo = new AdminCourseInfoVo();
        BeanUtils.copyProperties(course,adminCourseInfoVo);
        BeanUtils.copyProperties(courseDescription,adminCourseInfoVo);
        return adminCourseInfoVo;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateCourseInfo(AdminCourseInfoVo adminCourseInfoBo) {
        // 1、先往 edu_course 中添加
        Course course = new Course();
        BeanUtils.copyProperties(adminCourseInfoBo,course);
        courseService.updateById(course);
        // 2、根据添加后，同步的 course 的id，往 edu_course_description 中添加
        // 【注意：】 course 表中的每条记录，它的 id 正好与 edu_course_description 中的 id 对应
        CourseDescription courseDescription = new CourseDescription();
        BeanUtils.copyProperties(adminCourseInfoBo,courseDescription);
        courseDescription.setId(course.getId());
        courseDescriptionService.updateById(courseDescription);
    }
}
