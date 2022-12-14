package com.atguigu.guli.service.edu.service;

import com.atguigu.guli.service.edu.entity.Subject;
import com.atguigu.guli.service.edu.vo.SubjectVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.InputStream;
import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author atguigu
 * @since 2022-07-16
 */
public interface SubjectService extends IService<Subject> {

    void  batchImport(InputStream inputStream);

    List<SubjectVo> nestedList();

    /**
     * 使用mybatisPlus进行处理
     * @return
     */
    List<SubjectVo> nestedList2();
}
