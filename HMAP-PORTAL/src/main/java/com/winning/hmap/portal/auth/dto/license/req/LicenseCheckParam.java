package com.winning.hmap.portal.auth.dto.license.req;

import lombok.Data;

@Data
public class LicenseCheckParam {

    /**
     * 医疗机构名称
     */
    private  String medinsName;

    /**
     * 客户名称
     */
    private String clientName;

    /**
     * MAC地址
     */
    private String macId;

    /**
     * 序列号
     */
    private String license;

}
