package com.winning.hmap.portal.auth.service.impl;

import com.winning.hmap.portal.auth.entity.SysResource;
import com.winning.hmap.portal.auth.dto.auth.req.put.AddResourceParam;
import com.winning.hmap.portal.auth.dto.auth.req.put.UpdateResourceParam;
import com.winning.hmap.portal.auth.dto.auth.resp.LoginUser;
import com.winning.hmap.portal.auth.mapper.SysFuncPermissionMapper;
import com.winning.hmap.portal.auth.mapper.SysResourceMapper;
import com.winning.hmap.portal.auth.dto.auth.resp.FuncPermission;
import com.winning.hmap.portal.auth.dto.auth.resp.Menu;
import com.winning.hmap.portal.auth.service.ResourceService;
import com.winning.hmap.portal.util.BizConstant;
import me.about.widget.spring.mvc.exception.BizException;
import org.springframework.stereotype.Service;

import com.google.common.collect.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResourceServiceImpl implements ResourceService {

    @Resource
    private SysResourceMapper sysResourceMapper;

    @Resource
    private SysFuncPermissionMapper sysFuncPermissionMapper;

    @Override
    public void addMenu(AddResourceParam addResourceParam, LoginUser loginUser) {
        SysResource resourceByName = sysResourceMapper.getResourceByName(addResourceParam.getName());
        if (resourceByName != null) {
            throw new BizException(400,addResourceParam.getName() + " , 该资源已存在");
        }
        SysResource sysResource = new SysResource();
        sysResource.setParentId(addResourceParam.getParentId());
        sysResource.setName(addResourceParam.getName());
        sysResource.setCode(addResourceParam.getCode());
        //当前仅使用菜单权限资源
        sysResource.setType(1);
        sysResource.setIcon(addResourceParam.getIcon());
        sysResource.setUrl(addResourceParam.getUrl());
        sysResource.setSortBy(addResourceParam.getSortBy());
        sysResource.setDelFlag(BizConstant.DEL_FLAG_DEFAULT);
        sysResource.setCrteTime(new Date());
        sysResource.setCrterId(loginUser.getUserId());
        sysResourceMapper.insert(sysResource);
    }

    @Override
    public void updateMenu(UpdateResourceParam updateResourceParam,LoginUser loginUser) {
        SysResource resourceByName = sysResourceMapper.getResourceByName(updateResourceParam.getName());
        if (resourceByName != null && !resourceByName.getId().equals(updateResourceParam.getResourceId())) {
            throw new BizException(400,updateResourceParam.getName() + " , 该资源已存在");
        }
        SysResource sysResource = new SysResource();
        sysResource.setId(updateResourceParam.getResourceId());
        sysResource.setParentId(updateResourceParam.getParentId());
        sysResource.setName(updateResourceParam.getName());
        sysResource.setCode(updateResourceParam.getCode());
        sysResource.setType(updateResourceParam.getType());
        sysResource.setIcon(updateResourceParam.getIcon());
        sysResource.setUrl(updateResourceParam.getUrl());
        sysResource.setSortBy(updateResourceParam.getSortBy());
        sysResource.setUpdtTime(new Date());
        sysResource.setUpdtrId(loginUser.getUserId());
        sysResourceMapper.updateByPrimaryKey(sysResource);
    }


    private Menu convertToMenu(SysResource sysResource) {
        Menu menu = new Menu();
        menu.setMenuId(sysResource.getId());
        menu.setParentId(sysResource.getParentId());
        menu.setName(sysResource.getName());
        menu.setCode(sysResource.getCode());
        menu.setIcon(sysResource.getIcon());
        menu.setUrl(sysResource.getUrl());
        menu.setSortBy(sysResource.getSortBy());
        menu.setChecked(true);
        return menu;
    }
    @Override
    public List<Menu> findMenuByParentId(Long parentId) {
        return sysResourceMapper.findMenuByParentId(parentId)
                .stream()
                .map(this::convertToMenu).collect(Collectors.toList());
    }

    @Override
    public List<Menu> findAllMenu() {
        List<Menu> allMenu = sysResourceMapper.findAllMenu().stream()
                .map(this::convertToMenu).collect(Collectors.toList());
        return recursion(0L,allMenu);
    }

    @Override
    public List<Menu> findAllFuncPermissionPage() {
        List<Menu> allMenu = sysResourceMapper.findAllFuncPermissionPage().stream()
                .map(this::convertToMenu).collect(Collectors.toList());
        return recursion(0L,allMenu);
    }

    @Override
    public List<FuncPermission> findAllFuncPermission() {
        return sysFuncPermissionMapper.findAllFuncPermission();
    }


    private List<Long> ids(Long resourceId) {
        List<Long> ids = Lists.newArrayList(resourceId);
        List<Long> childrenIds = sysResourceMapper.findMenuByParentId(resourceId)
                .stream().map(SysResource::getId).collect(Collectors.toList());
        if (!childrenIds.isEmpty()) {
            ids.addAll(childrenIds);
        }
        return ids;
    }

    @Override
    public void disable(Long resourceId,LoginUser loginUser) {
        sysResourceMapper.disable(ids(resourceId));
    }

    @Override
    public void enable(Long resourceId, LoginUser loginUser) {
        sysResourceMapper.enable(ids(resourceId));
    }

    private List<Menu> recursion(Long parentId,List<Menu> allMenu) {
       return allMenu.stream().filter(item -> item.getParentId().equals(parentId))
                .peek((m) -> m.setChildren(recursion(m.getMenuId(),allMenu)))
               .collect(Collectors.toList());
    }
}
