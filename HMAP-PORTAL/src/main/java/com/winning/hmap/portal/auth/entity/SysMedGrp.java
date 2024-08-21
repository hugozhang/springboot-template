package com.winning.hmap.portal.auth.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class SysMedGrp implements Serializable {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 科室ID
     */
    private Long deptId;

    /**
     * 科室代码
     */
    private String deptCode;

    /**
     * 科室名称
     */
    private String deptName;

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

    /**
     * 创建时间
     */
    private Date crteTime;

    /**
     * 创建人
     */
    private Long crterId;

    /**
     * 更新时间
     */
    private Date updtTime;

    /**
     * 更新人
     */
    private Long updtrId;

    private String code;

    private String label;

}
