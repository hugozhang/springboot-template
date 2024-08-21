package com.winning.hmap.portal.auth.service.license;

import com.winning.hmap.portal.auth.dto.license.req.LicenseCheckModel;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 证书信息
 *
 * @author: hugo.zxh
 * @date: 2022/10/18 15:43
 * @description:
 */
@Component
@Data
public class LicenseCreatorConfig {

    /**
     * 证书subject
     */
    @Value("${subject}")
    private String subject;

    /**
     * 密钥别称
     */
    @Value("${private.key.alias}")
    private String privateAlias;

    /**
     * 密钥密码（需要妥善保管，不能让使用者知道）
     */
    @Value("${private.key.pwd}")
    private String keyPass;

    /**
     * 访问秘钥库的密码
     */
    @Value("${key.store.pwd}")
    private String storePass;
    /**
     * 证书生成路径  在外部配置文件
     */
    @Value("${license.licPath:config/license/}")
    private String licensePath;

    /**
     * 密钥库存储路径
     */
    @Value("${priPath}")
    private String privateKeysStorePath;

    /**
     * 证书生效时间
     */
    private Date issuedTime = new Date();

    /**
     * 证书失效时间
     */
    private Date expiryDate;

    /**
     * 用户类型
     */
    private String consumerType = "user";

    /**
     * 用户数量
     */
    private Integer consumerAmount = 1;

    /**
     * 描述信息
     */
    private String description = "";

    /**
     * 额外的服务器硬件校验信息
     */
    private LicenseCheckModel licenseCheckModel;

}
