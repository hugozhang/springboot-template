package com.winning.hmap.portal.auth.dto.auth.resp;

import lombok.Data;

import java.io.Serializable;

@Data
public class SimpleMedinsDept implements Serializable {

    private Long deptId;

    private String deptName;

    private String deptCode;

    private String hiDeptCode;

    private String hiDeptName;

}
