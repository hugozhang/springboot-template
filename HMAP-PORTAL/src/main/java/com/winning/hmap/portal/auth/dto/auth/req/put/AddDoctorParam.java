package com.winning.hmap.portal.auth.dto.auth.req.put;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import me.about.widget.spring.validation.annotation.PositiveNumberList;

import javax.validation.constraints.NotBlank;
import java.util.List;


@Data
public class AddDoctorParam {

    @NotBlank
    private String drName;

    @NotBlank
    private String drCode;

    @ApiModelProperty("职务代码")
    private String dutyCode;

    @ApiModelProperty("职务名称")
    private String dutyName;

    private String tel;

    private String email;

    @ApiModelProperty("医护人员编码")
    private String hiDrCode;

    @ApiModelProperty("医护人员名称")
    private String hiDrName;

    @ApiModelProperty("医疗组id")
    private Long medGrpId;

//    @NotEmpty
    @PositiveNumberList
    @ApiModelProperty("科室id数组")
    private List<Long> deptIds;

}
