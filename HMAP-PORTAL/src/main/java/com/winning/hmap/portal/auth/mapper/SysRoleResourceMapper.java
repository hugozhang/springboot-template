package com.winning.hmap.portal.auth.mapper;


import com.winning.hmap.portal.auth.entity.SysResource;
import com.winning.hmap.portal.auth.entity.SysRoleResource;

import java.util.List;

public interface SysRoleResourceMapper  {

    void insertBatch(List<SysRoleResource> rows);

    void deleteByRoleId(Long roleId);

    List<SysResource> findResourceByRoleId(Long roleId);

}