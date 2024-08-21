package com.winning.hmap.portal.auth.mapper;


import com.winning.hmap.portal.auth.entity.SysResource;

import java.util.List;

public interface SysResourceMapper  {

    void insert(SysResource sysResource);

    void updateByPrimaryKey(SysResource sysResource);
    List<SysResource> findMenuByParentId(Long parentId);
    List<SysResource> findAllMenu();
    List<SysResource> findAllFuncPermissionPage();
    SysResource getResourceByName(String resourceName);

    void disable(List<Long> ids);

    void enable(List<Long> ids);

}