package com.winning.hmap.portal.auth.mapper;


import com.winning.hmap.portal.auth.dto.auth.req.query.RoleParam;
import com.winning.hmap.portal.auth.dto.auth.resp.Role;
import com.winning.hmap.portal.auth.dto.auth.resp.RoleResource;
import com.winning.hmap.portal.auth.entity.SysRole;
import me.about.widget.mybatis.plugin.page.model.PageParam;
import me.about.widget.mybatis.plugin.page.model.PageResult;

import java.util.List;

public interface SysRoleMapper {

    void insert(SysRole sysRole);

    void updateByPrimaryKey(SysRole sysRole);

    PageResult<Role> selectByPage(PageParam<RoleParam> pageParam);
    List<RoleResource> findResourceByRoleId(Long roleId);
    Role getRoleById(Long roleId);

    Role getRoleByName(String roleName);

    void disable(Long id);

    void enable(Long id);

}