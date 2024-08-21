package com.winning.hmap.portal.auth.service;

import com.winning.hmap.portal.auth.dto.auth.req.put.AddResourceParam;
import com.winning.hmap.portal.auth.dto.auth.req.put.UpdateResourceParam;
import com.winning.hmap.portal.auth.dto.auth.resp.FuncPermission;
import com.winning.hmap.portal.auth.dto.auth.resp.LoginUser;
import com.winning.hmap.portal.auth.dto.auth.resp.Menu;

import java.util.List;

public interface ResourceService {

    /**
     * 添加菜单
     * @param addResourceParam
     */
    void addMenu(AddResourceParam addResourceParam, LoginUser loginUser);

    /**
     * 更新菜单
     * @param updateResourceParam
     */
    void updateMenu(UpdateResourceParam updateResourceParam,LoginUser loginUser);

    /**
     * 根据父id查询菜单
     * @param parentId
     * @return
     */
    List<Menu> findMenuByParentId(Long parentId);

    /**
     * 查询所有菜单
     * @return
     */
    List<Menu> findAllMenu();

    /**
     * 查询所有功能权限页面
     * @return
     */
    List<Menu> findAllFuncPermissionPage();

    /**
     * 查询所有功能权限
     * @return
     */
    List<FuncPermission> findAllFuncPermission();

    /**
     * 禁用菜单
     * @param resourceId
     */
    void disable(Long resourceId, LoginUser loginUser);

    /**
     * 启用菜单
     * @param resourceId
     */
    void enable(Long resourceId,LoginUser loginUser);
}
