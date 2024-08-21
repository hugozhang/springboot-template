package com.winning.hmap.portal.auth.service;

import com.winning.hmap.portal.auth.dto.auth.req.query.BindingRoleResourceFuncPermissionParam;
import com.winning.hmap.portal.auth.dto.auth.req.query.BindingRoleResourceParam;
import com.winning.hmap.portal.auth.dto.auth.req.put.AddRoleParam;
import com.winning.hmap.portal.auth.dto.auth.req.put.UpdateRoleParam;
import com.winning.hmap.portal.auth.dto.auth.req.query.RoleParam;
import com.winning.hmap.portal.auth.dto.auth.resp.*;
import me.about.widget.mybatis.plugin.page.model.PageParam;
import me.about.widget.mybatis.plugin.page.model.PageResult;

import java.util.List;

public interface RoleService {

    /**
     * 角色关联资源列表
     * @param roleId
     * @return
     */
    List<Menu> getRoleMenuList(Long roleId);

    /**
     * 根据id获取角色
     * @param roleId
     * @return
     */
    Role getRoleById(Long roleId);

    /**
     * 添加角色
     * @param addRoleParam
     */
    void add(AddRoleParam addRoleParam, LoginUser loginUser);

    /**
     * 更新角色
     * @param updateRoleParam
     */
    void update(UpdateRoleParam updateRoleParam, LoginUser loginUser);

    /**
     * 角色分页列表
     * @param pageParam
     * @return
     */
    PageResult<Role> selectByPage(PageParam<RoleParam> pageParam);


    /**
     * 角色->资源列表
     * @param roleId
     * @return
     */
    MenuVO roleResourceList(Long roleId);

    /**
     * 角色绑定资源
     * @param bindingRoleResource
     */
    void bindingRoleResource(BindingRoleResourceParam bindingRoleResource, LoginUser loginUser);


    /**
     * 角色->资源功能权限列表
     * @param roleId
     */
    List<FuncPage> roleResourceFuncPermission(Long roleId);

    /**
     * 角色绑定资源功能权限
     * @param bindingRoleResourceFuncPermissionParam
     */
    void bindingRoleResourceFuncPermission(BindingRoleResourceFuncPermissionParam bindingRoleResourceFuncPermissionParam, LoginUser loginUser);

    /**
     * 停用用户
     * @param roleId
     */
    void disable(Long roleId,LoginUser loginUser);

    /**
     * 启用用户
     * @param roleId
     */
    void enable(Long roleId, LoginUser loginUser);



}
