package com.winning.hmap.portal.auth.dto.auth.req.put;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UpdateResourceParam {

    @NotNull
    private Long resourceId;

    private Long parentId;

    @NotBlank
    private String name;

    private String code;

    private String icon;

    /**
     * 1、菜单    2、菜单下面的page    3、page 不作为菜单
     */
    private Integer type;

    /**
     * 访问url地址
     */
    private String url;

    private Integer sortBy;


}
