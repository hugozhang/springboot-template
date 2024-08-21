package com.winning.hmap.portal.auth.dto.auth.req.put;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AddResourceParam {

    private Long parentId;

    @NotBlank
    private String name;

    private String code;

    private String icon;

    /**
     * 1、菜单权限    2、功能权限
     */
    @ApiModelProperty("1、菜单权限    2、功能权限")
    private Integer type;

    /**
     * 访问url地址
     */
    private String url;

    private Integer sortBy;


}
