package com.winning.hmap.portal.auth.dto.auth.req.query;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginParam {

    @NotBlank
    private String loginName;

    @NotBlank
    private String password;

}
