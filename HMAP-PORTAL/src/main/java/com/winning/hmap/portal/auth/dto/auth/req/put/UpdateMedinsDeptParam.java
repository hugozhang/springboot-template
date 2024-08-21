package com.winning.hmap.portal.auth.dto.auth.req.put;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UpdateMedinsDeptParam {

    @NotNull
    private Long deptId;

    @NotBlank
    @Length(min =2)
    private String deptName;

    @NotBlank
    private String deptCode;

    private String hiDeptName;

    private String hiDeptCode;

    /**
     * 父节点id
     */
    @NotNull
    @Min(0)
    private Long parentId;

}
