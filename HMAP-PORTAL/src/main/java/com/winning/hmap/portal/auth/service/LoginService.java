package com.winning.hmap.portal.auth.service;

import com.winning.hmap.portal.auth.dto.auth.req.query.LoginParam;
import com.winning.hmap.portal.auth.dto.auth.resp.LoginUser;

public interface LoginService {

    /**
     * 用户登录
     * @param loginParam
     * @return
     */
    LoginUser login(LoginParam loginParam) throws Exception;

    /**
     * 切换角色
     * @param roleId
     * @param loginUser
     * @return
     */
    LoginUser switchUserRole(Long roleId,LoginUser loginUser);

    /**
     * 切换科室
     * @param deptId
     * @param loginUser
     * @return
     */
    LoginUser switchUserDept(Long deptId,LoginUser loginUser);

}
