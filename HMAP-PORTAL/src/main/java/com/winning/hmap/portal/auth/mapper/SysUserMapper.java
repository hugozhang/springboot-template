package com.winning.hmap.portal.auth.mapper;


import com.winning.hmap.portal.auth.dto.auth.req.query.UserParam;
import com.winning.hmap.portal.auth.entity.SysUser;
import me.about.widget.mybatis.plugin.page.model.PageParam;
import me.about.widget.mybatis.plugin.page.model.PageResult;

import java.util.List;

public interface SysUserMapper {

    void insert(SysUser sysUser);

    void updateByPrimaryKey(SysUser sysUser);

    void disable(Long id);

    void enable(Long id);

    PageResult<SysUser> selectByPage(PageParam<UserParam> pageParam);

    List<SysUser> findUserList(UserParam pageParam);

    SysUser getUserByLoginName(String loginName);

    SysUser getUserByDrId(Long drId);

    SysUser getUserByUserId(Long userId);

    int insertBatch(List<SysUser> list);

    SysUser selectByUserId(Long id);

    List<SysUser> selectByUserName(String userName);


}