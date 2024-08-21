package com.winning.hmap.portal.auth.dto.auth.req.query;

import lombok.Data;

import java.util.List;

@Data
public class DoctorParam {

    private String drNameOrCode;

    private Long medinsDeptId;

    private List<String> drNames;

    private String hiDrName;

    private String deptName;

    private String delFlag;

    private List<String> drCodes;
}
