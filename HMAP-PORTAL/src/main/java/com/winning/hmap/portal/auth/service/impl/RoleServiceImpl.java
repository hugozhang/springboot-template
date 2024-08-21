package com.winning.hmap.portal.auth.service.impl;

import com.winning.hmap.portal.auth.dto.auth.req.put.AddRoleParam;
import com.winning.hmap.portal.auth.dto.auth.req.put.UpdateRoleParam;
import com.winning.hmap.portal.auth.dto.auth.resp.*;
import com.winning.hmap.portal.auth.mapper.SysRoleMapper;
import com.winning.hmap.portal.auth.mapper.SysRoleResourceFuncPermissionMapper;
import com.winning.hmap.portal.auth.mapper.SysRoleResourceMapper;
import com.winning.hmap.portal.auth.entity.SysRole;
import com.winning.hmap.portal.auth.entity.SysRoleResource;
import com.winning.hmap.portal.auth.entity.SysRoleResourceFuncPermission;
import com.winning.hmap.portal.auth.dto.auth.req.query.BindingRoleResourceFuncPermissionParam;
import com.winning.hmap.portal.auth.dto.auth.req.query.RoleParam;
import com.winning.hmap.portal.auth.dto.auth.req.query.BindingRoleResourceParam;
import com.winning.hmap.portal.auth.service.ResourceService;
import com.winning.hmap.portal.auth.service.RoleService;
import com.winning.hmap.portal.util.BizConstant;
import me.about.widget.mybatis.plugin.page.model.PageParam;
import me.about.widget.mybatis.plugin.page.model.PageResult;
import me.about.widget.spring.mvc.exception.BizException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    @Resource
    private ResourceService resourceService;

    @Resource
    private SysRoleMapper sysRoleMapper;

    @Resource
    private SysRoleResourceMapper sysRoleResourceMapper;

    @Resource
    private SysRoleResourceFuncPermissionMapper sysRoleResourceFuncPermissionMapper;

    @Override
    public List<Menu> getRoleMenuList(Long roleId) {
        List<Menu> menuList = sysRoleResourceMapper.findResourceByRoleId(roleId).stream().map(sysResource -> {
            Menu menu = new Menu();
            menu.setMenuId(sysResource.getId());
            menu.setName(sysResource.getName());
            menu.setParentId(sysResource.getParentId());
            menu.setUrl(sysResource.getUrl());
            menu.setIcon(sysResource.getIcon());
            menu.setSortBy(sysResource.getSortBy());
            menu.setCode(sysResource.getCode());
            menu.setCode(String.valueOf(sysResource.getId()));
            menu.setTitle(sysResource.getName());
            menu.setMkid(String.valueOf(sysResource.getId()));
            menu.setHidden(false);
            menu.setId(sysResource.getCode());
            return menu;
        }).collect(Collectors.toList());
        return recursion(0L,menuList);
    }

    private List<Menu> recursion(Long parentId,List<Menu> allMenu) {
        return allMenu.stream().filter(item -> item.getParentId().equals(parentId))
                .peek((m) -> m.setChildren(recursion(m.getMenuId(),allMenu)))
                .collect(Collectors.toList());
    }

    @Override
    public Role getRoleById(Long roleId) {
        return sysRoleMapper.getRoleById(roleId);
    }

    @Override
    public void add(AddRoleParam addRoleParam, LoginUser loginUser) {

        Role roleByName = sysRoleMapper.getRoleByName(addRoleParam.getName());
        if (roleByName != null) {
            throw new BizException(400,addRoleParam.getName() + ",角色名已存在");
        }

        SysRole sysRole = new SysRole();
        sysRole.setName(addRoleParam.getName());
        sysRole.setDelFlag(BizConstant.DEL_FLAG_DEFAULT);
        sysRole.setCrterId(loginUser.getUserId());
        sysRole.setCrteTime(new Date());
        sysRoleMapper.insert(sysRole);
    }

    @Override
    public void update(UpdateRoleParam updateRoleParam, LoginUser loginUser) {

        Role roleByName = sysRoleMapper.getRoleByName(updateRoleParam.getName());
        if (roleByName != null && !roleByName.getRoleId().equals(updateRoleParam.getRoleId())) {
            throw new BizException(400,updateRoleParam.getName() + ",角色名已存在");
        }

        SysRole sysRole = new SysRole();
        sysRole.setId(updateRoleParam.getRoleId());
        sysRole.setName(updateRoleParam.getName());
        sysRole.setUpdtTime(new Date());
        sysRole.setUpdtrId(loginUser.getUserId());
        sysRoleMapper.updateByPrimaryKey(sysRole);
    }

    @Override
    public PageResult<Role> selectByPage(PageParam<RoleParam> pageParam) {
        return sysRoleMapper.selectByPage(pageParam);
    }

    @Override
    public MenuVO roleResourceList(Long roleId) {
        List<Menu> allMenu = resourceService.findAllMenu();

        // 查询角色资源
        Map<Long, Long> resourceRoleMap = sysRoleMapper.findResourceByRoleId(roleId).stream()
                .collect(Collectors.toMap(RoleResource::getResourceId, RoleResource::getRoleId));

        // 确定哪些菜单被选中
        allMenu.forEach(menu -> {
            if (resourceRoleMap.containsKey(menu.getMenuId())) {
                menu.setChecked(true);
            }
            if (menu.getChildren() != null) {
                menu.getChildren().forEach(childMenu -> {
                    if (resourceRoleMap.containsKey(childMenu.getMenuId())) {
                        childMenu.setChecked(true);
                    }
                });
            }
        });

        MenuVO menuVO = new MenuVO();
        menuVO.setMenus(allMenu);
        menuVO.setResourceIds(new ArrayList<>(resourceRoleMap.keySet()));
        return menuVO;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void bindingRoleResource(BindingRoleResourceParam roleResourceBindingParam,LoginUser loginUser) {
        // 删除角色资源
        sysRoleResourceMapper.deleteByRoleId(roleResourceBindingParam.getRoleId());
        // 添加角色资源
        sysRoleResourceMapper.insertBatch(roleResourceBindingParam.getResourceIds().stream()
                .map(resourceId -> {
                    SysRoleResource sysRoleResource = new SysRoleResource();
                    sysRoleResource.setRoleId(roleResourceBindingParam.getRoleId());
                    sysRoleResource.setResourceId(resourceId);
                    sysRoleResource.setCrteTime(new Date());
                    sysRoleResource.setCrterId(loginUser.getUserId());
                    return sysRoleResource;
                }).collect(Collectors.toList()));
    }

    @Override
    public List<FuncPage> roleResourceFuncPermission(Long roleId) {
        // 页面功能权限列表
        List<FuncPage> funcPageList = resourceService.findAllFuncPermissionPage().stream().map(menu -> {
            FuncPage funcPage = new FuncPage();
            funcPage.setResourceId(menu.getMenuId());
            funcPage.setResourceName(menu.getName());
            funcPage.setFuncPermissions(resourceService.findAllFuncPermission());
            return funcPage;
        }).collect(Collectors.toList());

        // 确认角色下的页面功能权限已经勾选
        Map<Long, List<SysRoleResourceFuncPermission>> checkedRoleFuncPermissionMap = sysRoleResourceFuncPermissionMapper.findFuncPermissionByRoleId(roleId).stream()
                .collect(Collectors.groupingBy(SysRoleResourceFuncPermission::getResourceId));

        // 确认哪些功能权限被选中
        funcPageList.forEach(funcPage -> {
            if (checkedRoleFuncPermissionMap.containsKey(funcPage.getResourceId())) {
                checkedRoleFuncPermissionMap.get(funcPage.getResourceId()).forEach(roleFuncPermission -> {
                    funcPage.getFuncPermissions().forEach(funcPermission -> {
                        if (funcPermission.getId().equals(roleFuncPermission.getFuncPermissionId())) {
                            funcPermission.setChecked(true);
                        }
                    });
                });
            }
        });

        return funcPageList;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void bindingRoleResourceFuncPermission(BindingRoleResourceFuncPermissionParam bindingRoleResourceFuncPermissionParam,LoginUser loginUser) {
        // 删除角色功能权限
        sysRoleResourceFuncPermissionMapper.deleteByRoleId(bindingRoleResourceFuncPermissionParam.getRoleId());

        // 添加角色功能权限
        sysRoleResourceFuncPermissionMapper.insertBatch(bindingRoleResourceFuncPermissionParam.getFuncPermissionIds().stream()
                .map(funcPermissionId -> {
                    SysRoleResourceFuncPermission sysRoleResourceFuncPermission = new SysRoleResourceFuncPermission();
                    sysRoleResourceFuncPermission.setRoleId(bindingRoleResourceFuncPermissionParam.getRoleId());
                    sysRoleResourceFuncPermission.setResourceId(bindingRoleResourceFuncPermissionParam.getResourceId());
                    sysRoleResourceFuncPermission.setFuncPermissionId(funcPermissionId);
                    sysRoleResourceFuncPermission.setCrteTime(new Date());
                    sysRoleResourceFuncPermission.setCrterId(loginUser.getUserId());
                    return sysRoleResourceFuncPermission;
                }).collect(Collectors.toList()));

    }

    @Override
    public void disable(Long roleId, LoginUser loginUser) {
        sysRoleMapper.disable(roleId);
    }

    @Override
    public void enable(Long roleId, LoginUser loginUser) {
        sysRoleMapper.enable(roleId);
    }

}
