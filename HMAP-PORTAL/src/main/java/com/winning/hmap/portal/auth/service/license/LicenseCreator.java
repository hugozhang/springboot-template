package com.winning.hmap.portal.auth.service.license;

import com.winning.hmap.portal.auth.dto.license.req.LicenseCheckModel;
import com.winning.hmap.portal.util.LicenseUtils;
import de.schlichtherle.license.*;
import lombok.extern.slf4j.Slf4j;
import me.about.widget.spring.mvc.exception.BizException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.security.auth.x500.X500Principal;
import java.io.File;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.prefs.Preferences;

/**
 * 证书创建
 *
 * @author: hugo.zxh
 * @date: 2022/10/18 15:46
 * @description:
 */
@Slf4j
public class LicenseCreator {

    private final static X500Principal DEFAULT_HOLDER_AND_ISSUER = new X500Principal("CN=localhost, OU=localhost, O=localhost, L=SH, ST=SH, C=CN");

    private final LicenseCreatorConfig param;

    public LicenseCreator(LicenseCreatorConfig param) {
        this.param = param;
    }

    private LicenseManager getLicenseManager() {
        return LicenseManagerHolder.getLicenseManager(initLicenseParam());
    }

    public void uninstall() {
        try {
            LicenseManager licenseManager = getLicenseManager();
            licenseManager.uninstall();
            log.info("证书已卸载");
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
    }

    public LicenseCheckModel getLicenseCheckModel() {
        LicenseManager licenseManager = getLicenseManager();
        try {
            LicenseContent verify = licenseManager.verify();
            Object extra = verify.getExtra();
            if (extra instanceof LicenseCheckModel) {
                return (LicenseCheckModel) extra;
            } else {
                log.info(extra.toString());
            }
        } catch (NoLicenseInstalledException e) {
            log.error(e.getMessage() + " 没有授权认证，请联系管理员");
//            throw new BizException(400,"没有找到应用授权证书，请联系管理员");
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        return null;
    }

    public boolean verify() {
        LicenseCheckModel licenseCheckModel = getLicenseCheckModel();
        if (licenseCheckModel == null) {
            return false;
        }
        Date expiryDate = licenseCheckModel.getExpiryDate();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        String dateFormat = sdf1.format(expiryDate);
        log.debug("client_name:{},license:{},expiry_date:{}",new Object[]{licenseCheckModel.getClientName(),licenseCheckModel.getLicense(), dateFormat});
        return LicenseUtils.checkLicense(licenseCheckModel.getLicense(), licenseCheckModel.getClientName(),licenseCheckModel.getMacId());
    }

    /**
     * 生成License证书
     * @return boolean
     */
    public boolean generateLicense() {
        try {
            LicenseManager licenseManager = getLicenseManager();
            licenseManager.uninstall();
            LicenseContent licenseContent = initLicenseContent();


            File f = new File(param.getLicensePath());
            if (!f.exists()) {
                f.mkdirs();
            }
            String filename = "license.lic";
            File file = new File(f, filename);
            if (!file.exists()) {
                file.createNewFile();
            }
            log.debug("证书生成地址:" + file.getAbsolutePath());

//            Resource resource = new ClassPathResource(param.getLicensePath());
//            File file = resource.getFile();
            licenseManager.store(licenseContent,file);
//            log.debug("证书生成地址:" + file.getAbsolutePath());
            licenseManager.install(file);
            return true;
        }catch (Exception e) {
            log.error(MessageFormat.format("证书生成失败：{0}",param),e);
            return false;
        }
    }

    /**
     * 初始化证书生成参数
     * @return de.schlichtherle.license.LicenseParam
     */
    private LicenseParam initLicenseParam() {
        Preferences preferences = Preferences.userNodeForPackage(LicenseCreator.class);
        //设置对证书内容加密的秘钥
        CipherParam cipherParam = new DefaultCipherParam(param.getStorePass());

        KeyStoreParam privateStoreParam = new DefaultKeyStoreParam(LicenseCreator.class
                , param.getPrivateKeysStorePath()
                , param.getPrivateAlias()
                , param.getKeyPass()
                , param.getKeyPass());

        LicenseParam licenseParam = new DefaultLicenseParam(param.getSubject()
                ,preferences
                ,privateStoreParam
                ,cipherParam);
        return licenseParam;
    }

    /**
     * 设置证书生成正文信息
     * @return de.schlichtherle.license.LicenseContent
     */
    private LicenseContent initLicenseContent() {
        LicenseContent licenseContent = new LicenseContent();
        licenseContent.setHolder(DEFAULT_HOLDER_AND_ISSUER);
        licenseContent.setIssuer(DEFAULT_HOLDER_AND_ISSUER);

        licenseContent.setSubject(param.getSubject());
        licenseContent.setIssued(param.getIssuedTime());
        licenseContent.setNotBefore(param.getIssuedTime());
        licenseContent.setNotAfter(param.getExpiryDate());
        licenseContent.setConsumerType(param.getConsumerType());
        licenseContent.setConsumerAmount(param.getConsumerAmount());
        licenseContent.setInfo(param.getDescription());
        //扩展校验服务器硬件信息
        licenseContent.setExtra(param.getLicenseCheckModel());
        return licenseContent;
    }

}
