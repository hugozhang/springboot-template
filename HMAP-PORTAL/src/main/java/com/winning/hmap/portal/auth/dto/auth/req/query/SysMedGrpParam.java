package com.winning.hmap.portal.auth.dto.auth.req.query;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SysMedGrpParam {

    /**
     * Excel 序号
     */
    private Integer sn;

    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 科室Id
     */
    private Long deptId;

    /**
     * 科室名称
     */
    private String deptName;

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
     * 批量删除时的集合
     */
    private List<String> ids;

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

}
