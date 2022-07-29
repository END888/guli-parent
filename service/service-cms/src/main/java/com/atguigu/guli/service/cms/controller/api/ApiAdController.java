package com.atguigu.guli.service.cms.controller.api;


import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.cms.service.AdService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 广告推荐 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2022-07-30
 */
@RestController
@RequestMapping("/api/cms/ad")
@CrossOrigin
@Api(tags = "广告推荐")
public class ApiAdController {

    @Autowired
    AdService adService;



    @ApiOperation("查询首页热门数据")
    @GetMapping("getAds")
    public R getAds(){
        Map<String, List> map = adService.getHosts();
        return R.ok().data("map",map);
    }
}

