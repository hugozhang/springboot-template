package com.winning.hmap.container.spring;

import com.winning.hmap.portal.auth.dto.auth.resp.*;

import com.google.common.collect.Lists;

public class SessionUserUtils {

    public static LoginUser mockLoginUser()  {

        LoginUser loginUser = new LoginUser();
        loginUser.setUserId(1L);
        loginUser.setLoginName("admin");
        loginUser.setUsername("admin");

        Doctor doctor = new Doctor();
        doctor.setDrId(1L);
        doctor.setDrCode("123456");
        doctor.setDrName("张三");

        SimpleDoctorMedinsDept medins = new SimpleDoctorMedinsDept();
        medins.setMedinsId(1L);
        medins.setMedinsName("机构A");
        medins.setMedinsNo("123456");

        SimpleMedinsDept medinsDept = new SimpleMedinsDept();
        medinsDept.setDeptId(1L);
        medinsDept.setDeptName("外科");
        medinsDept.setDeptCode("123456");
        medins.setDeptList(Lists.newArrayList(medinsDept));

        doctor.setMedinsList(Lists.newArrayList(medins));

        loginUser.setDoctor(doctor);

        UserRole userRole = new UserRole();
        userRole.setRoleId(1L);
        userRole.setRoleName("超级管理员");
        loginUser.setRoleList(Lists.newArrayList(userRole));

        loginUser.setLoginRole(userRole);

        loginUser.setLoginDept(medins);

        return loginUser;
    }

}
