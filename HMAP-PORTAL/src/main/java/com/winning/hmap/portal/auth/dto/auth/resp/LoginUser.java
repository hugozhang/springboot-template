package com.winning.hmap.portal.auth.dto.auth.resp;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import me.about.widget.spring.mvc.security.SessionUser;

import java.util.List;

@Data
public class LoginUser implements SessionUser {

    private Long userId;

    private String loginName;

    private String username;

    private Doctor doctor;

    private List<UserRole> roleList;

    @ApiModelProperty("登录角色")
    private UserRole loginRole;

    private SimpleDoctorMedinsDept loginDept;

    @Override
    public String getIndexName() {
        return loginName;
    }
}
