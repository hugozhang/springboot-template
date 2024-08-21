package com.winning.hmap.portal.auth.dto.auth.req.put;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import me.about.widget.spring.validation.annotation.PositiveNumberList;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class UpdateDoctorParam {

    @NotNull
    private Long drId;

    private String drName;

    @NotBlank
    private String drCode;

    @ApiModelProperty("医护人员编码")
    private String hiDrCode;

    @ApiModelProperty("医护人员名称")
    private String hiDrName;

    @ApiModelProperty("医疗组id")
    private List<Long> medGrpIds;

    private String dutyCode;

    private String dutyName;

    private String tel;

    private String email;

    @PositiveNumberList
    private List<Long> deptIds;

}
