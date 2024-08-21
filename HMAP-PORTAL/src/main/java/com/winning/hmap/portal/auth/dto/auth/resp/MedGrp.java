package com.winning.hmap.portal.auth.dto.auth.resp;

import lombok.Data;

/**
 * @author cpj
 * @date 2024/4/10 14:05
 * @desciption: 医疗组
 */
@Data
public class MedGrp {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 科室名称
     */
    private String deptName;

    /**
     * 科室ID
     */
    private Long deptId;

    /**
     * 科室代码
     */
    private String deptCode;

    /**
     * 医疗组名称
     */
    private String medGrpName;

    /**
     * 医疗组代码
     */
    private String medGrpCode;

    /**
     * 组长名称
     */
    private String grpLeaderName;

    /**
     * 组长工号
     */
    private String grpLeaderCode;

    /**
     * 有效标志 0:可用 1:不可用
     */
    private String valiFlag;

}
