package com.winning.hmap.portal.auth.dto.auth.req.put;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UpdateRoleParam {

    @NotNull
    private Long roleId;

    @NotBlank
    private String name;
}
