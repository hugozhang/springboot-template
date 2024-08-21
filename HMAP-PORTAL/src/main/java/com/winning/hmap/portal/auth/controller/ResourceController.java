package com.winning.hmap.portal.auth.controller;


import com.winning.hmap.portal.auth.dto.auth.req.put.AddResourceParam;
import com.winning.hmap.portal.auth.dto.auth.req.put.UpdateResourceParam;
import com.winning.hmap.portal.auth.dto.auth.resp.FuncPermission;
import com.winning.hmap.portal.auth.dto.auth.resp.LoginUser;
import com.winning.hmap.portal.auth.dto.auth.resp.Menu;
import com.winning.hmap.portal.auth.service.ResourceService;
import com.winning.hmap.portal.dict.aop.WriteLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@Api(tags = "资源接口")
@RestController
@RequestMapping("/hmap/resouce")
public class ResourceController {

    @Resource
    private ResourceService resourceService;

    @ApiOperation(value = "添加菜单")
    @WriteLog(type = "3", description = "添加菜单")
    @PutMapping("/menu/add")
    public void add(@RequestBody @Valid AddResourceParam addResourceParam, @ApiIgnore LoginUser loginUser) {
        resourceService.addMenu(addResourceParam,loginUser);
    }

//    @ApiOperation(value = "添加菜单")
//    @PutMapping("/menu/add")
//    public void add(@RequestBody @Valid AddResourceParam addResourceParam, @ApiIgnore LoginUser loginUser) {
//        resourceService.addMenu(addResourceParam,loginUser);
//    }

    @ApiOperation(value = "更新菜单")
    @WriteLog(type = "5", description = "更新菜单")
    @PutMapping("/menu/{menuId}/update")
    public void update(@RequestBody @Valid UpdateResourceParam updateResourceParam, @ApiIgnore LoginUser loginUser) {
        resourceService.updateMenu(updateResourceParam,loginUser);
    }

    @ApiOperation(value = "启用菜单")
    @WriteLog(type = "5", description = "启用菜单")
    @PutMapping("/menu/{menuId}/enable")
    public void update(@PathVariable("menuId") Long menuId, @ApiIgnore LoginUser loginUser) {
        resourceService.enable(menuId,loginUser);
    }

    @ApiOperation(value = "禁用菜单")
    @WriteLog(type = "5", description = "禁用菜单")
    @PutMapping("/menu/{menuId}/disable")
    public void disable(@PathVariable("menuId") Long menuId, @ApiIgnore LoginUser loginUser) {
        resourceService.disable(menuId,loginUser);
    }

    @ApiOperation(value = "子菜单列表")
    @PostMapping("/menu/{menuId}/children")
    public List<Menu> selectByPage(@PathVariable("menuId") Long menuId) {
        return resourceService.findMenuByParentId(menuId);
    }

    @ApiOperation(value = "功能权限页面")
    @PostMapping("/func/page")
    public List<Menu> findAllFuncPermissionPage() {
        return resourceService.findAllFuncPermissionPage();
    }

    @ApiOperation(value = "功能权限列表")
    @WriteLog(type = "6", description = "功能权限列表")
    @PostMapping("/func/permission")
    public List<FuncPermission> findAllFuncPermission() {
        return resourceService.findAllFuncPermission();
    }

}
