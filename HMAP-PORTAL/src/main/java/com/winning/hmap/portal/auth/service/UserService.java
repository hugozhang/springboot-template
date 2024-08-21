package com.winning.hmap.portal.auth.service;

import com.winning.hmap.portal.auth.dto.auth.req.put.AddUserParam;
import com.winning.hmap.portal.auth.dto.auth.req.put.UpdateUserParam;
import com.winning.hmap.portal.auth.dto.auth.req.query.UserParam;
import com.winning.hmap.portal.auth.entity.SysUser;
import com.winning.hmap.portal.auth.dto.auth.resp.LoginUser;
import com.winning.hmap.portal.auth.dto.auth.resp.User;
import com.winning.hmap.portal.auth.dto.auth.resp.UserRole;
import me.about.widget.mybatis.plugin.page.model.PageParam;
import me.about.widget.mybatis.plugin.page.model.PageResult;

import java.util.List;

public interface UserService {

    /**
     * 根据用户id获取用户角色列表
     * @param userId
     * @return
     */
    List<UserRole> getUserRoleList(Long userId);

    /**
     * 根据登录名获取用户
     * @param loginName
     * @return
     */
    SysUser getUserByLoginName(String loginName);

    /**
     * 根据drId获取用户
     * @param drId
     * @return
     */
    SysUser getUserByDrId(Long drId);

    /**
     * 新增用户
     * @param addUserParam
     */
    void add(AddUserParam addUserParam, LoginUser loginUser) throws Exception;

    /**
     * 批量新增用户
     */
    void addUserBatch(LoginUser loginUser) throws Exception;

    /**
     *  更新用户
     * @param updateUserParam
     */
    void update(UpdateUserParam updateUserParam, LoginUser loginUser) throws Exception;

    /**
     *  更新用户密码
     * @param updateUserParam
     */
    void updatePassword(UpdateUserParam updateUserParam, LoginUser loginUser) throws Exception;

    /**
     * 停用用户
     * @param userId
     */
    void disable(Long userId,LoginUser loginUser);

    /**
     * 启用用户
     * @param userId
     */
    void enable(Long userId, LoginUser loginUser);

    /**
     * 科室下的用户列表
     */
    List<User> findUserList(UserParam userParam);

    /**
     * 用户分页列表
     * @param pageParam
     * @return
     */
    PageResult<User> selectByPage(PageParam<UserParam> pageParam);

    User selectByUserId(Long userId) throws Exception;

    List<User> selectByUserName(String userId) throws Exception;
}
