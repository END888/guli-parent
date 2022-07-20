package com.atguigu.guli.service.edu.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.atguigu.guli.service.edu.entity.ExcelSubjectData;
import com.atguigu.guli.service.edu.entity.Subject;
import com.atguigu.guli.service.edu.mapper.SubjectMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class ExcelSubjectDataListener extends AnalysisEventListener<ExcelSubjectData> {

    // 假设这个是一个DAO，当然有业务逻辑这个也可以是一个service。当然如果不用存储这个对象没用。
    private SubjectMapper subjectMapper;

    /**
     * 遍历每一行的记录
     * @param excelSubjectData
     * @param analysisContext
     */
    @Override
    public void invoke(ExcelSubjectData excelSubjectData, AnalysisContext analysisContext) {
        log.info("解析到一条记录：{}",excelSubjectData);
        // 处理读取出来的数据
        String levelOneTitle = excelSubjectData.getLevelOneTitle(); // 一级标题
        String levelTwoTitle = excelSubjectData.getLevelTwoTitle(); // 二级标题

        // 判断一级分类是否重复
        Subject subjectLevelOne = this.getByTitle(levelOneTitle);
        String parentId = null;
        if (subjectLevelOne == null){
            // 将一级分类存入数据库
            Subject subject = new Subject();
            subject.setParentId("0");
            subject.setTitle(levelOneTitle);// 一级分类名称
            subjectMapper.insert(subject);
            parentId = subject.getId();
        }else {
            parentId = subjectLevelOne.getId();
        }
        // 判断二级分类是否重复
        Subject subjectLevelTwo = this.getSubByTitle(levelTwoTitle, parentId);
        if (subjectLevelTwo == null){
            // 将二级分类存入数据库
            Subject subject = new Subject();
            subject.setTitle(levelTwoTitle);
            subject.setParentId(parentId);
            subjectMapper.insert(subject); // 添加
        }
    }

    /**
     * 所有数据解析完成了，都会来调用
     * @param analysisContext
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        log.info("所有数据解析完成！");
    }

    /**
     * 根据分类名称查询这个一级分类是否存在
     * @param title
     * @return
     */
    private Subject getByTitle(String title){
        QueryWrapper<Subject> wrapper = new QueryWrapper<>();
        wrapper.eq("title",title).eq("parent_id","0"); // 一级分类
        return subjectMapper.selectOne(wrapper);
    }

    /**
     * 根据分类名称和父id查询这个二级分类是否存在
     * @param title
     * @param parentId
     * @return
     */
    private Subject getSubByTitle(String title,String parentId){
        QueryWrapper<Subject> wrapper = new QueryWrapper<>();
        wrapper.eq("title",title).eq("parent_id",parentId);
        return subjectMapper.selectOne(wrapper);
    }
}
