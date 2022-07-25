package com.atguigu.guli.service.edu.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.atguigu.guli.service.edu.entity.ExcelSubjectData;
import com.atguigu.guli.service.edu.entity.Subject;
import com.atguigu.guli.service.edu.mapper.SubjectMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class ExcelSubjectDataListener extends AnalysisEventListener<ExcelSubjectData> {

    // 假设这个是一个DAO，当然有业务逻辑这个也可以是一个service。当然如果不用存储这个对象没用。
    private SubjectMapper subjectMapper;

    /**
     * 遍历每一行的记录
     * @param data
     * @param analysisContext
     */
    @Override
    public void invoke(ExcelSubjectData data, AnalysisContext analysisContext) {
        String levelOneTitle = data.getLevelOneTitle();// 获取一级分类标题
        String levelTwoTitle = data.getLevelTwoTitle();// 获取二级分类标题
        // 先根据一级分类标题、parent_id=0，去数据库中查，如果能查到，说明该标题已经存在了
        // 则略过，判断二级标题；如果从数据库中查不到，则创建Subject对象，添加到数据库
        LambdaQueryWrapper<Subject> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Subject::getTitle,levelOneTitle).eq(Subject::getParentId,"0");
        Subject selectOne = subjectMapper.selectOne(wrapper);
        if (StringUtils.isEmpty(selectOne)){
            selectOne = new Subject();
            selectOne.setTitle(levelOneTitle);
            selectOne.setParentId("0");
            selectOne.setSort(0);
            subjectMapper.insert(selectOne);
        }
        // 需要通过父分类id、title来确定二级分类是否已经存在
        // 如果前面查询结果为null，则会添加新的，并把添加后生成的id同步给selectOne
        // 如果前面查询不为null，则刚好是我们需要查找的父分类id
        String parentId = selectOne.getId();
        // 此时进行子分类查询，可以对上面的lambda查询条件进行复用，不用需要先对原来的查询条件清空
        wrapper.clear();
        wrapper.eq(Subject::getTitle,levelTwoTitle).eq(Subject::getParentId,parentId);
        Long count = subjectMapper.selectCount(wrapper);
        if (count == 0){
            // 查不到，则新增
            Subject subject = new Subject();
            subject.setTitle(levelTwoTitle);
            subject.setParentId(parentId);
            subject.setSort(0);
            subjectMapper.insert(subject);
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

}
