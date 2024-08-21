package com.winning.hmap.portal.auth.dto.license.req;

import lombok.Data;

import java.util.Date;

/**
 * 证书存放额外内容
 *
 * @author: hugo.zxh
 * @date: 2022/10/18 15:44
 * @description:
 */
@Data
public class LicenseCheckModel {

    private String medinsName;

    private String clientName;

    private String license;

    private String macId;

    private Date expiryDate;

}
