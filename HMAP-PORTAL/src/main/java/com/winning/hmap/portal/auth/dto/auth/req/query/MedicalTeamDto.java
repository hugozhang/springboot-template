package com.winning.hmap.portal.auth.dto.auth.req.query;

import lombok.Data;

@Data
public class MedicalTeamDto {

    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 医疗组编码
     */
    private String medicalTeamCode;

    /**
     * 医疗组名称
     */
    private String medicalTeamName;

}
