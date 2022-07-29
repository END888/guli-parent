package com.atguigu.guli.service.cms.controller.admin;


import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.cms.entity.Ad;
import com.atguigu.guli.service.cms.service.AdService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 广告推荐 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2022-07-30
 */
@RestController
@RequestMapping("/admin/cms/ad")
@CrossOrigin
@Api(tags = "广告推荐管理")
public class AdminAdController {

    @Autowired
    AdService adService;

    //1、查询所有
    @ApiOperation("查询所有")
    @GetMapping
    public R list(){
        return R.ok().data("items",adService.list(new LambdaQueryWrapper<Ad>()
            .orderByDesc(Ad::getSort)));
    }

    //2、根据id查询
    @ApiOperation("根据id查询")
    @GetMapping("{id}")
    public R getById(@ApiParam("id") @PathVariable String id){
        return R.ok().data("item",adService.getById(id));
    }
    //3、更新ad
    @ApiOperation("更新ad")
    @PutMapping("update")
    public R update(@RequestBody Ad ad){
        adService.updateById(ad);
        return R.ok();
    }
    //4、新增ad
    @ApiOperation("新增ad")
    @PostMapping("save")
    public R save(@RequestBody Ad ad){
        adService.save(ad);
        return R.ok();
    }
    //5、删除ad
    @ApiOperation("根据id删除")
    @DeleteMapping("deleteById/{id}")
    public R deleteById(@ApiParam("id")@PathVariable String id){
        adService.removeById(id);
        return R.ok();
    }


}

