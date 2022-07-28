package com.atguigu.guli.service.edu.service.impl;

import com.atguigu.guli.service.edu.entity.Chapter;
import com.atguigu.guli.service.edu.mapper.ChapterMapper;
import com.atguigu.guli.service.edu.service.ChapterService;
import com.atguigu.guli.service.edu.service.VideoService;
import com.atguigu.guli.service.edu.vo.ChapterVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class ChapterServiceImpl extends ServiceImpl<ChapterMapper, Chapter> implements ChapterService {

    @Autowired
    VideoService videoService;
    /**
     * 方式1：使用SQL查询
     *
     * @param courseId
     * @return
     */
    @Override
    public List<ChapterVo> getChapterListByCourseId(String courseId) {
        // 方式1：在查询语句后添加 where 条件
//        return baseMapper.getChapterListByCourseId(courseId);
        // 方式2：使用 QueryWrapper 自动生成 where 条件
        QueryWrapper<ChapterVo> wrapper = new QueryWrapper<>();
        wrapper.eq("c1.course_id",courseId).orderByAsc("c1_sort").orderByAsc("v1_sort");
        List<ChapterVo> voList = baseMapper.getChapterListByCourseIdPassQueryWrapper(wrapper);
        voList.forEach(System.out::println);
        return voList;
    }

    @Override
    public void deleteById(String id) {
        // TODO 根据章节id删除课时列表
        baseMapper.deleteById(id);
    }


//    /**
//     * 方式2：使用通过Java操作
//     *
//     * @param courseId
//     * @return
//     */
//    @Override
//    public List<ChapterVo> getChapterListByCourseId(String courseId) {
//        // 方式一：先根据课程id查询章节列表，然后遍历章节列表，拿每一个章节去数据库查找对应的课程列表，生成对应的ChapterVo集合
////        List<Chapter> chapterList = baseMapper.selectList(new LambdaQueryWrapper<Chapter>().eq(Chapter::getCourseId, courseId));
////        List<ChapterVo> chapterVoList = chapterList.stream().map(chapter -> {
////            ChapterVo chapterVo = new ChapterVo();
////            List<Video> videoList = videoService.getVideoListByChapterId(chapter.getId());
////            BeanUtils.copyProperties(chapter, chapterVo);
////            chapterVo.setVideoList(videoList);
////            return chapterVo;
////        }).collect(Collectors.toList());
//        // 方式二：先根据课程id查询章节列表，然后再查询所有的课时列表，对章节列表进行遍历，拿到每一个课程列表，判断该课时对应的章节id
//        // 是否是当前章节id，如果是，说明找到了，然后生成对应的ChapterVo集合
//        List<Chapter> chapterList = baseMapper.selectList(new LambdaQueryWrapper<Chapter>().eq(Chapter::getCourseId, courseId));
//        List<Video> videos = videoService.list(null);
//        List<ChapterVo> chapterVoList = chapterList.stream().map(chapter -> {
//            List<Video> videoList = videos.stream().filter(video -> {
//                return video.getChapterId().equals(chapter.getId());
//            }).collect(Collectors.toList());
//            ChapterVo chapterVo = new ChapterVo();
//            BeanUtils.copyProperties(chapter, chapterVo);
//            chapterVo.setVideoList(videoList);
//            return chapterVo;
//        }).collect(Collectors.toList());
//        return chapterVoList;
//    }
}
