package com.winning.hmap.portal.auth.controller;

import com.winning.hmap.portal.auth.dto.auth.req.put.AddUserParam;
import com.winning.hmap.portal.auth.dto.auth.req.put.UpdateUserParam;
import com.winning.hmap.portal.auth.dto.auth.req.query.UserParam;
import com.winning.hmap.portal.auth.dto.auth.resp.LoginUser;
import com.winning.hmap.portal.auth.dto.auth.resp.User;
import com.winning.hmap.portal.auth.service.UserService;
import com.winning.hmap.portal.dict.aop.WriteLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.about.widget.mybatis.plugin.page.model.PageParam;
import me.about.widget.mybatis.plugin.page.model.PageResult;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@Api(tags = "用户接口")
@RestController
@RequestMapping("/hmap/user")
public class UserController {

    @Resource
    private UserService userService;

    @ApiOperation(value = "停用用户")
    @WriteLog(type = "5", description = "停用用户")
    @PutMapping("/{userId}/disable")
    public void disable(@PathVariable("userId") Long userId,@ApiIgnore LoginUser loginUser) {
        userService.disable(userId,loginUser);
    }

    @ApiOperation(value = "启用用户")
    @WriteLog(type = "5", description = "启用用户")
    @PutMapping("/{userId}/enable")
    public void enable(@PathVariable("userId") Long userId,@ApiIgnore LoginUser loginUser) {
        userService.enable(userId,loginUser);
    }

    @ApiOperation(value = "添加用户")
    @WriteLog(type = "3", description = "添加用户")
    @PutMapping("/add")
    public void add(@RequestBody AddUserParam addUserParam, @ApiIgnore LoginUser loginUser) throws Exception {
        userService.add(addUserParam,loginUser);
    }

    /**
     * 一键生成用户
     * @return
     */
    @ApiOperation(value = "一键生成用户")
    @WriteLog(type = "3", description = "一键生成用户")
    @PostMapping("/addUserBatch")
    public void addUserBatch(@ApiIgnore LoginUser loginUser) throws Exception {
        userService.addUserBatch(loginUser);
    }

    @ApiOperation(value = "更新用户")
    @WriteLog(type = "5", description = "更新用户")
    @PutMapping("/{userId}/update")
    public void update(@RequestBody @Valid UpdateUserParam updateUserParam, @ApiIgnore LoginUser loginUser) throws Exception {
        userService.update(updateUserParam,loginUser);
    }

    @ApiOperation(value = "更新密码")
    @WriteLog(type = "5", description = "更新密码")
    @PostMapping("/password/update")
    public void updatePassword(@RequestBody @Valid UpdateUserParam updateUserParam, @ApiIgnore LoginUser loginUser) throws Exception {
        userService.updatePassword(updateUserParam,loginUser);
    }

    @ApiOperation(value = "用户列表")
    @WriteLog(type = "6", description = "用户列表")
    @PostMapping("/list")
    public PageResult<User> selectByPage(@RequestBody @Valid PageParam<UserParam> pageParam) {
        return userService.selectByPage(pageParam);
    }

    @ApiOperation(value = "用户详情")
    @PostMapping("/{userId}/info")
    public User selectByUserId(@PathVariable("userId") Long userId) throws Exception{
        return userService.selectByUserId(userId);
    }

    @ApiOperation(value = "用户详情")
    @PostMapping("/name")
    public List<User> selectByUserId(@RequestBody UserParam userParam) throws Exception{
        return userService.selectByUserName(userParam.getUsername());
    }

}
