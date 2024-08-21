package com.winning.hmap.portal.auth.dto.auth.resp;

import lombok.Data;

import java.io.Serializable;

@Data
public class MedinsDept implements Serializable {

    /**
     * Excel 序号
     */
    private Integer sn;

    private Long deptId;

    private String deptName;

    private String deptCode;

    private String hiDeptName;

    private String hiDeptCode;

    private Medins medins;

    private MedinsDept parentMedinsDept;

    private Long parentId;

    private String parentName;

    private String delFlag;

}
