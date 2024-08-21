package com.winning.hmap.portal.auth.entity;

import lombok.Data;

import java.util.Date;

@Data
public class SysMedGrpLog {

    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 操作人
     */
    private String opter;

    /**
     * 操作时间
     */
    private Date oprtTime;

    /**
     * 医疗组，名称+代码
     */
    private String medGrp;

    /**
     * 操作内容
     */
    private String oprtCont;

}
