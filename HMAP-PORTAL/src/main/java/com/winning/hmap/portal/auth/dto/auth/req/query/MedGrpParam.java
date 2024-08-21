package com.winning.hmap.portal.auth.dto.auth.req.query;

import lombok.Data;

import java.util.List;

/**
 * @author cpj
 * @date 2024/3/21 10:47
 * @desciption: 医疗组入参
 */
@Data
public class MedGrpParam {

    /**
     * 医疗组名称或编码
     */
    private String medGrpNameOrCode;

    /**
     * 医疗组组长姓名或医生编码
     */
    private String grpLeaderNameOrCode;

    /**
     * 科室编码集合
     */
    private List<Long> deptIds;

    /**
     * 医疗机构集合
     */
    private List<Long> medinIds;

}
