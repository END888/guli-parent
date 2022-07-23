package com.atguigu.guli.service.edu.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.atguigu.guli.service.edu.entity.ExcelSubjectData;
import com.atguigu.guli.service.edu.entity.Subject;
import com.atguigu.guli.service.edu.listener.ExcelSubjectDataListener;
import com.atguigu.guli.service.edu.mapper.SubjectMapper;
import com.atguigu.guli.service.edu.service.SubjectService;
import com.atguigu.guli.service.edu.vo.SubjectVo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2022-07-16
 */
@Service
public class SubjectServiceImpl extends ServiceImpl<SubjectMapper, Subject> implements SubjectService {

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void batchImport(InputStream inputStream) {
        // 这里需要指定读用哪个class去读，然后读取第一个sheet，文件流会自动关闭
        EasyExcel.read(inputStream, ExcelSubjectData.class, new ExcelSubjectDataListener(baseMapper))
                .excelType(ExcelTypeEnum.XLS)
                .sheet()
                .doRead();
    }

    @Override
    public List<SubjectVo> nestedList() {
        return baseMapper.selectNestedList();
    }

    /**
     * 不写SQL，使用mybatisPlus进行获取
     *
     * @return
     */
    @Override
    public List<SubjectVo> nestedList2() {
        // 1、获取所有数据
        List<Subject> selectList = baseMapper.selectList(null);
        // 2、通过 parent_id（父分类id），先找出所有的一级分类
        List<SubjectVo> subjectVoList = selectList.stream()
                .filter(subject -> {    // 过滤出所有的一级分类
                    String parentId = subject.getParentId();
                    return StringUtils.isEmpty(parentId) || parentId.equals("0");
                }).map(subject -> {
                    SubjectVo subjectVo = new SubjectVo();
                    BeanUtils.copyProperties(subject,subjectVo);
                    subjectVo.setChildren(getChildren(subject, selectList));
                    return subjectVo;
                }).sorted((s1, s2) -> {
                    return (s1.getSort() == null ? 0 : s1.getSort()) - (s2.getSort() == null ? 0 : s2.getSort());
                }).collect(Collectors.toList());
        return subjectVoList;
    }

    private List<SubjectVo> getChildren(Subject subject, List<Subject> selectList) {
        List<SubjectVo> children = selectList.stream().filter(sub -> {  // 过滤出当前sub的父分类id为要找子分类的id
            return sub.getParentId().equals(subject.getId());
        }).map(sub -> {
            SubjectVo subjectVo = new SubjectVo();
            BeanUtils.copyProperties(sub,subjectVo);
            subjectVo.setChildren(getChildren(sub, selectList));
            return subjectVo;
        }).sorted((s1, s2) -> {
            return (s1.getSort() == null ? 0 : s1.getSort()) - (s2.getSort() == null ? 0 : s2.getSort());
        }).collect(Collectors.toList());
        return children;
    }
}
