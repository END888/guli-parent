package com.atguigu.guli.service.edu.service.impl;

import com.atguigu.guli.service.edu.entity.Comment;
import com.atguigu.guli.service.edu.entity.Course;
import com.atguigu.guli.service.edu.mapper.CommentMapper;
import com.atguigu.guli.service.edu.service.CommentService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 评论 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2022-07-16
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {



    public void test1(Course course){
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();

    }


}
@ToString
@Accessors
class A{
    private String name;
    private String password;

    public A setName(String name){
        this.name = name;
        return this;
    }
    public A setPassword(String password){
        this.password = password;
        return this;
    }

    public static void main(String[] args) {
        A a = new A();
        a.setName("张三").setPassword("李四");
        System.out.println(a);
    }
}
