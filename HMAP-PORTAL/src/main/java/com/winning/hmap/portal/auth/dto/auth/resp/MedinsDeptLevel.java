package com.winning.hmap.portal.auth.dto.auth.resp;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
public class MedinsDeptLevel {

    private Long deptId;

    private String deptName;

    private String label;

    private String deptCode;

    private String code;

    private String hiDeptName;

    private String hiDeptCode;

    private Long parentId;

    private String value;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<MedinsDeptLevel> children;

}
