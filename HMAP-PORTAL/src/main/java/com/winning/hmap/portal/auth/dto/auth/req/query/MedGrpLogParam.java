package com.winning.hmap.portal.auth.dto.auth.req.query;

import lombok.Data;

@Data
public class MedGrpLogParam {

    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 操作人
     */
    private String opter;

    /**
     * 医疗组，名称+代码
     */
    private String medGrp;


}
