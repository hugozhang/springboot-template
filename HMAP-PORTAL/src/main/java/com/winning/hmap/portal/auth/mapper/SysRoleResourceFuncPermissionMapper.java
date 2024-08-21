package com.winning.hmap.portal.auth.mapper;

import com.winning.hmap.portal.auth.entity.SysRoleResourceFuncPermission;

import java.util.List;

public interface SysRoleResourceFuncPermissionMapper {

   void insertBatch(List<SysRoleResourceFuncPermission> rows);

   void deleteByRoleId(Long roleId);

   List<SysRoleResourceFuncPermission> findFuncPermissionByRoleId(Long roleId);


}