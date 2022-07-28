package com.atguigu.guli.service.edu.service.impl;

import com.atguigu.guli.service.edu.entity.Course;
import com.atguigu.guli.service.edu.entity.CourseDescription;
import com.atguigu.guli.service.edu.mapper.CourseMapper;
import com.atguigu.guli.service.edu.service.CourseDescriptionService;
import com.atguigu.guli.service.edu.service.CourseService;
import com.atguigu.guli.service.edu.service.SubjectService;
import com.atguigu.guli.service.edu.service.TeacherService;
import com.atguigu.guli.service.edu.vo.AdminCourseInfoVo;
import com.atguigu.guli.service.edu.vo.AdminCourseItemVo;
import com.atguigu.guli.service.edu.vo.CourseQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

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
    SubjectService subjectService;
    @Autowired
    CourseService courseService;
    @Autowired
    CourseDescriptionService courseDescriptionService;
    @Autowired
    TeacherService teacherService;

    /**
     *  MybatisPlus 自定义分页查询时，如果配置了分页拦截器，只需要将分页的页码和 pageSize 设置给一个 page 对象并
     *  传入到自定义的 mapper 方法中；
     *  MybatisPlus 的分页拦截器会自动使用分页条件生成 limit 条件；
     *
     *  自定义分页查询返回的结果是查询的数据集合，需要手动设置到 page 对象的分页数据集合中
     */
    @Override
    public Page<AdminCourseItemVo> queryCourseItemVoPage(Integer pageNum, Integer pageSize, CourseQueryVo courseQueryVo){
        QueryWrapper<CourseQueryVo> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(courseQueryVo.getTitle())){
            wrapper.eq("c1.title",courseQueryVo.getTitle());
        }
        if (!StringUtils.isEmpty(courseQueryVo.getSubjectId())){
            wrapper.eq("c1.subject_id",courseQueryVo.getSubjectId());
        }
        if (!StringUtils.isEmpty(courseQueryVo.getSubjectParentId())){
            wrapper.eq("c1.subject_parent_id",courseQueryVo.getSubjectParentId());
        }
        if (!StringUtils.isEmpty(courseQueryVo.getTeacherId())){
            wrapper.eq("c1.teacher_id",courseQueryVo.getTeacherId());
        }
        Page<AdminCourseItemVo> page = new Page<>(pageNum, pageSize);
        List<AdminCourseItemVo> data = baseMapper.selectCourseItemVoPage(page,wrapper);
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

    /**
     *  根据课程id，删除课程基本信息：edu_course、edu_course_description
     * @param courseId
     */
    @Transactional
    @Override
    public void deleteCourseInfo(String courseId) {
        // TODO 删除课程还需要考虑到其他表：
        // edu_chapter、edu_comment、edu_course、edu_course_collect、
        //edu_course_description、edu_subject、edu_video
        courseService.removeById(courseId);
        courseDescriptionService.removeById(courseId);
    }

    /**
     * 方式一：使用SQL查询
     * @param id
     * @return
     */
    @Override
    public AdminCourseItemVo getPublishInfoById(String id) {
        return baseMapper.getPublishInfoById(id);
    }

    @Override
    public Boolean publishCourse(String id) {
        Course course = new Course();
        int count = baseMapper.update(course, new LambdaUpdateWrapper<Course>().set(Course::getStatus, "Normal").eq(Course::getId, id));
        return count > 0;
    }
}
