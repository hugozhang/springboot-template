package com.winning.hmap.portal.auth.controller;


import com.winning.hmap.portal.auth.dto.auth.resp.LoginUser;
import com.winning.hmap.portal.dict.aop.WriteLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags = "测试接口")
@RestController
@RequestMapping("/hmap/test")
public class TestController {


    @WriteLog(type = "test", description = "测试获取用户信息")
    @ApiOperation(value = "测试获取用户信息")
    @GetMapping("/current/user")
    public LoginUser getLoginUser(@ApiIgnore LoginUser loginUser) {
        System.out.println(loginUser);
        return loginUser;
    }

}
