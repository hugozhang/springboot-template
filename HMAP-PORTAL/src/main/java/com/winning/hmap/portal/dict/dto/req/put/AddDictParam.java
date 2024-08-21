package com.winning.hmap.portal.dict.dto.req.put;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class AddDictParam {

    @NotNull
    private Long parentId;

    @NotBlank
    private String dictName;

    @NotBlank
    private String dictCode;

    private String dictValue;

    @NotNull
    @ApiModelProperty(value = "1=目录   2 = 配置项")
    private Integer dictType;

//    @NotNull
    private Integer sortBy;

    private String parentName;

    private String remark;
}
