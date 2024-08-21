package com.winning.hmap.portal.auth.dto.auth.req.query;

import lombok.Data;

@Data
public class DeptLabelDto {

    /**
     * 科室ID
     */
    private Integer id;

    /**
     * 机构编码
     */
    private String organCode;

    /**
     * 机构名称
     */
    private String organName;

    /**
     * 科室编码
     */
    private String deptCode;

    /**
     * 科室名称
     */
    private String deptName;
}
