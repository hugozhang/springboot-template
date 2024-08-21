package com.winning.hmap.portal.auth.mapper;


import com.winning.hmap.portal.auth.entity.SysUser;
import com.winning.hmap.portal.auth.entity.SysUserRole;

import java.util.List;

public interface SysUserRoleMapper  {

    void insertBatch(List<SysUserRole> rows);

    void deleteByUserId(Long userId);

    List<SysUserRole> findRoleListByUserId(Long userId);

    int insertUserRoleBatch(List<SysUser> list);

}