package com.winning.hmap.portal.auth.dto.auth.req.put;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UpdateMedinsParam {

    @NotNull
    private Long medinsId;

    /**
     * 机构名称
     */
    @NotBlank
    @Length(min = 2)
    private String medinsName;

    /**
     * 机构编码
     */
    @NotBlank
    private String medinsNo;

    /**
     * 医保区划
     */
    private String admdvs;

    /**
     * 机构等级  比如三级甲等
     */
    private String medinsLv;

    /**
     * 机构类型
     */
    private String medinsType;

    /**
     * 机构类别
     */
    private String medinsCat;

    /**
     * 启用状态 0-启用 1-停用
     */
    private String delFlag;

}
