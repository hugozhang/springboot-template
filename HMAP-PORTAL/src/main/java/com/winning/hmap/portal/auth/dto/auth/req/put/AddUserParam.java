package com.winning.hmap.portal.auth.dto.auth.req.put;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class AddUserParam {

    /**
     * 账号
     */
    @NotBlank
    private String loginName;

    /**
     * 密码
     */
    @NotBlank
    private String password;

    /**
     * 姓名
     */
    @NotBlank
    private String username;

    /**
     * 关联医生id
     */
    @NotNull
    private Long drId;


    /**
     * 角色id列表
     */
    @NotEmpty
    private List<Long> roleIds;


    private String drName;



}
