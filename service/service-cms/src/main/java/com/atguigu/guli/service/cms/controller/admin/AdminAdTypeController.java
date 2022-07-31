package com.atguigu.guli.service.cms.controller.admin;


import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.cms.entity.AdType;
import com.atguigu.guli.service.cms.service.AdTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 推荐位 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2022-07-30
 */
@RestController
@RequestMapping("/admin/cms/ad-type")
@Api(tags = "推荐管理")
public class AdminAdTypeController {
    @Autowired
    AdTypeService adTypeService;

    //1、查询所有
    @ApiOperation("查询所有")
    @GetMapping
    public R list(){
        return R.ok().data("items",adTypeService.list());
    }

    //2、根据id查询
    @ApiOperation("根据id查询")
    @GetMapping("{id}")
    public R getById(@ApiParam("id") @PathVariable String id){
        return R.ok().data("item",adTypeService.getById(id));
    }
    //3、更新ad
    @ApiOperation("更新ad")
    @PutMapping("update")
    public R update(@RequestBody AdType adType){
        adTypeService.updateById(adType);
        return R.ok();
    }
    //4、新增ad
    @ApiOperation("新增ad")
    @PostMapping("save")
    public R save(@RequestBody AdType adType){
        adTypeService.save(adType);
        return R.ok();
    }
    //5、删除ad
    @ApiOperation("根据id删除")
    @DeleteMapping("deleteById/{id}")
    public R deleteById(@ApiParam("id")@PathVariable String id){
        adTypeService.removeById(id);
        return R.ok();
    }
}

