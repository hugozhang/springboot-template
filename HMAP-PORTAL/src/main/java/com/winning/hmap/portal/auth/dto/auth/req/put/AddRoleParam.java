package com.winning.hmap.portal.auth.dto.auth.req.put;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AddRoleParam {

    @NotBlank
    private String name;
}
