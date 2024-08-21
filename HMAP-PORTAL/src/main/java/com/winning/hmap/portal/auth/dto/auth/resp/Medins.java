package com.winning.hmap.portal.auth.dto.auth.resp;

import lombok.Data;

import java.io.Serializable;

@Data
public class Medins implements Serializable {

    /**
     * 机构ID
     */
    private Long medinsId;

    /**
     * 机构名称
     */
    private String medinsName;

    /**
     * 机构编码
     */
    private String medinsNo;


    /**
     * 机构等级  比如三级甲等
     */
    private String medinsLv;

    /**
     * 机构类别（分类）    比如：综合医院
     */
    private String medinsType;

    /**
     * 机构性质  比如公立医院
     */
    private String medinsCat;

    private String delFlag;


}
