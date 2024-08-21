package com.winning.hmap.portal.auth.controller;

import com.winning.hmap.portal.auth.dto.auth.req.put.AddRoleParam;
import com.winning.hmap.portal.auth.dto.auth.req.put.UpdateRoleParam;
import com.winning.hmap.portal.auth.dto.auth.req.query.BindingRoleResourceFuncPermissionParam;
import com.winning.hmap.portal.auth.dto.auth.req.query.BindingRoleResourceParam;
import com.winning.hmap.portal.auth.dto.auth.req.query.RoleParam;
import com.winning.hmap.portal.auth.dto.auth.resp.*;
import com.winning.hmap.portal.auth.service.RoleService;
import com.winning.hmap.portal.dict.aop.WriteLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.about.widget.mybatis.plugin.page.model.PageParam;
import me.about.widget.mybatis.plugin.page.model.PageResult;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Api(tags = "角色接口")
@RestController
@RequestMapping("/hmap/role")
public class RoleController {

    @Resource
    private RoleService roleService;

    @ApiOperation(value = "添加角色")
    @WriteLog(type = "3", description = "添加角色")
    @PutMapping("/add")
    public void add(@RequestBody @Valid AddRoleParam addRoleParam, @ApiIgnore LoginUser loginUser) {
        roleService.add(addRoleParam,loginUser);
    }

    @ApiOperation(value = "更新角色")
    @WriteLog(type = "5", description = "更新角色")
    @PutMapping("/{roleId}/update")
    public void update(@RequestBody @Valid UpdateRoleParam updateRoleParam, @ApiIgnore LoginUser loginUser) {
        roleService.update(updateRoleParam,loginUser);
    }

    @ApiOperation(value = "角色列表")
    @WriteLog(type = "6", description = "角色列表")
    @PostMapping("/list")
    public PageResult<Role> selectByPage(@RequestBody PageParam<RoleParam> pageParam) {
        return roleService.selectByPage(pageParam);
    }

    @ApiOperation(value = "角色资源列表")
    @WriteLog(type = "6", description = "角色资源列表")
    @GetMapping("/{roleId}/resource")
    public MenuVO roleResourceOpt(@PathVariable("roleId") Long roleId) {
       return roleService.roleResourceList(roleId);
    }

    @ApiOperation(value = "只显示角色关联资源列表")
    @GetMapping("/{roleId}/link/resource")
    public List<Menu> roleLinkResource(@PathVariable("roleId") Long roleId) {
        return roleService.getRoleMenuList(roleId);
    }

    @ApiOperation(value = "绑定角色资源")
    @PutMapping("/{roleId}/resource")
    public void bindingRoleResource(@RequestBody @Valid BindingRoleResourceParam bindingRoleResource
            ,@ApiIgnore LoginUser loginUser) {
        roleService.bindingRoleResource(bindingRoleResource,loginUser);
    }

    @ApiOperation(value = "角色功能权限列表")
    @WriteLog(type = "6", description = "角色功能权限列表")
    @GetMapping("/{roleId}/func")
    public List<FuncPage> roleResourceFuncPermission(@PathVariable("roleId") Long roleId) {
        return roleService.roleResourceFuncPermission(roleId);
    }

    @ApiOperation(value = "绑定角色功能权限")
    @WriteLog(type = "5", description = "绑定角色功能权限")
    @PutMapping("/{roleId}/func")
    public void bindingRoleResource(@RequestBody @Valid BindingRoleResourceFuncPermissionParam bindingRoleResourceFuncPermissionParam
            ,@ApiIgnore LoginUser loginUser) {
        roleService.bindingRoleResourceFuncPermission(bindingRoleResourceFuncPermissionParam,loginUser);
    }

    @ApiOperation(value = "停用角色")
    @WriteLog(type = "5", description = "停用角色")
    @PutMapping("/{roleId}/disable")
    public void disable(@PathVariable("roleId") Long roleId,@ApiIgnore LoginUser loginUser) {
        roleService.disable(roleId,loginUser);
    }

    @ApiOperation(value = "启用角色")
    @WriteLog(type = "5", description = "启用角色")
    @PutMapping("/{roleId}/enable")
    public void enable(@PathVariable("roleId") Long roleId,@ApiIgnore LoginUser loginUser) {
        roleService.enable(roleId,loginUser);
    }

    @ApiOperation(value = "变更角色")
    @PostMapping("/change_role")
    public void login(String js_id, HttpSession session, HttpServletRequest request){
        session.setAttribute("session_js_id", js_id);
    }


}
