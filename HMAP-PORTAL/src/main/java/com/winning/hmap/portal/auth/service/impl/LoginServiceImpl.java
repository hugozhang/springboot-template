package com.winning.hmap.portal.auth.service.impl;

import cn.hutool.core.codec.Base64;
import com.winning.hmap.portal.auth.dto.auth.req.query.LoginParam;
import com.winning.hmap.portal.auth.dto.auth.resp.Doctor;
import com.winning.hmap.portal.auth.entity.SysUser;
import com.winning.hmap.portal.auth.service.DoctorService;
import com.winning.hmap.portal.auth.service.UserService;
import com.winning.hmap.portal.auth.dto.auth.resp.LoginUser;
import com.winning.hmap.portal.auth.dto.auth.resp.UserRole;
import com.winning.hmap.portal.auth.service.LoginService;
import com.winning.hmap.portal.util.BizConstant;
import com.winning.hmap.portal.util.EncryptUtils;
import me.about.widget.spring.mvc.exception.BizException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service
public class LoginServiceImpl implements LoginService {

    @Resource
    private UserService userService;

    @Resource
    private DoctorService doctorService;

    @Override
    public LoginUser login(LoginParam loginParam) throws Exception {

        SysUser userByLoginName = userService.getUserByLoginName(loginParam.getLoginName());

        if(userByLoginName == null){
            throw new BizException(400,"您输入的账号不存在！");
        }

        if (!BizConstant.DEL_FLAG_DEFAULT.equals(userByLoginName.getDelFlag())) {
            throw new BizException(400,"您的账号已经停用，请联系管理员！");
        }


        if (userByLoginName == null || !userByLoginName.getPassword().equals(EncryptUtils.getInstance().encrypt(Base64.decodeStr(loginParam.getPassword())))) {
            throw new BizException(400,"用户名或密码错误");
        }

        Doctor doctor = doctorService.getDoctorById(userByLoginName.getDrId());
        if (userByLoginName.getDrId()!=-1 && userByLoginName.getPassword()
                .equals(EncryptUtils.getInstance().encrypt(doctor.getDrCode()))) {
            throw new BizException(403,"密码不能与初始密码相同！");
        }

        LoginUser loginUser = new LoginUser();
        loginUser.setUserId(userByLoginName.getId());
        loginUser.setUsername(userByLoginName.getUsername());
        loginUser.setLoginName(userByLoginName.getLoginName());

        // 账号绑定的医生
        loginUser.setDoctor(doctor);

        // 账号角色
        List<UserRole> userRoleList = userService.getUserRoleList(userByLoginName.getId());
        loginUser.setRoleList(userRoleList);

        // 设置默认角色
        loginUser.setLoginRole(userRoleList.isEmpty() ? null : userRoleList.get(0));

        // 设置默认机构
//        if (doctor != null && !doctor.getMedinsList().isEmpty()) {
//            loginUser.setLoginDept(doctor.getMedinsList().get(0));
//        }

        return loginUser;
    }

    @Override
    public LoginUser switchUserRole(Long roleId,LoginUser loginUser) {
        loginUser.getRoleList().forEach(userRole -> {
            if (userRole.getRoleId().equals(roleId)) {
                loginUser.setLoginRole(userRole);
            }
        });
        return loginUser;
    }

    @Override
    public LoginUser switchUserDept(Long deptId, LoginUser loginUser) {
//        loginUser.getDoctor().getDeptList().forEach(dept -> {
//            if (dept.getDeptId().equals(deptId)) {
//                loginUser.setLoginDept(dept);
//            }
//        });
        return loginUser;
    }
}
