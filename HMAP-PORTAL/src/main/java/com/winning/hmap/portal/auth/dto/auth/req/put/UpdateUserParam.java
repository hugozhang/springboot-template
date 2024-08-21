package com.winning.hmap.portal.auth.dto.auth.req.put;

import lombok.Data;
import me.about.widget.spring.validation.annotation.PositiveNumberList;

import java.util.List;

@Data
public class UpdateUserParam {

    /**
     * 用户id
     */
    //@NotNull
    private Long userId;

    /**
     * 新密码
     */
    private String password;

    /**
     * 原密码
     */
    private String oldPassword;

    /**
     * 姓名
     */
    private String username;

    /**
     * 关联医生id
     */
    private Long drId;


    /**
     * 角色id列表
     */
    @PositiveNumberList
    private List<Long> roleIds;


    private String drName;

    private String loginName;


}
