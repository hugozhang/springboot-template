package com.winning.hmap.portal.auth.controller;

import com.winning.hmap.portal.auth.dto.auth.req.query.LoginParam;
import com.winning.hmap.portal.auth.dto.auth.resp.LoginUser;
import com.winning.hmap.portal.auth.service.LoginService;
import com.winning.hmap.portal.dict.aop.WriteLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.about.widget.spring.mvc.security.LoginSuccessHandler;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Api(tags = "登录接口")
@RestController
@RequestMapping("/hmap/user/")
public class LoginController {

    @Resource
    private LoginService loginService;

    @ApiOperation(value = "用户登录")
    @WriteLog(type = "1", description = "用户登录")
    @PostMapping("login")
    public LoginUser login(HttpServletRequest request, @RequestBody @Valid LoginParam loginParam) throws Exception {
        LoginUser loginUser = loginService.login(loginParam);
        LoginSuccessHandler.createSessionUser(request.getSession(),loginUser);
        return loginUser;
    }

    @ApiOperation(value = "用户登出")
    @WriteLog(type = "2", description = "用户登出")
    @PostMapping("logout")
    public void logout(HttpServletRequest request) {
        request.getSession().invalidate();
    }

    @ApiOperation(value = "切换角色")
    @PostMapping("role/{roleId}/switch")
    public void switchRole(HttpServletRequest request,
                           @PathVariable("roleId") Long roleId,
                           @ApiIgnore LoginUser loginUser) {
        request.getSession().setAttribute("LoginUser", loginService.switchUserRole(roleId, loginUser));
    }


    @ApiOperation(value = "切换科室")
    @PostMapping("dept/{deptId}/switch")
    public void switchDept(HttpServletRequest request,
                           @PathVariable("deptId") Long deptId,
                           @ApiIgnore LoginUser loginUser) {
        request.getSession().setAttribute("LoginUser", loginService.switchUserDept(deptId, loginUser));
    }

}
