package com.winning.hmap.portal.auth.dto.auth.req.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class MedinsDeptParam {

    //@NotNull
    private Long medinsId;

    @ApiModelProperty("分页列表没有这个条件按医生的科室")
    private Long drId;

    @ApiModelProperty("分页列表查询的时候使用")
    private String medinsDeptNameOrCode;

    private String deptName;

    private String hiDeptName;

    private String delFlag;

    private String hiDeptCode;

}
