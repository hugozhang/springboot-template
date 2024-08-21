package com.winning.hmap.portal.auth.controller;

import com.winning.hmap.portal.auth.dto.license.req.LicenseCheckParam;
import com.winning.hmap.portal.auth.dto.license.req.LicenseCheckModel;
import com.winning.hmap.portal.auth.service.license.LicenseCreator;
import com.winning.hmap.portal.auth.service.license.LicenseCreatorConfig;
import com.winning.hmap.portal.dict.aop.WriteLog;
import com.winning.hmap.portal.util.EncryptUtils;
import com.winning.hmap.portal.util.LicenseUtils;
import com.winning.hmap.portal.util.NetUtils;
import com.winning.hmap.portal.util.NumberUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.about.widget.spring.mvc.exception.BizException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Api(tags = "证书接口")
@RestController
@RequestMapping("/hmap/license")
public class LicenseController {

    @Resource
    private LicenseCreatorConfig licenseCreatorConfig;

    @ApiOperation(value = "解析序列过期时间")
    @WriteLog(type = "6", description = "解析序列过期时间")
    @PostMapping("/expiry_date")
    public String parse(@RequestBody LicenseCheckParam licenseCheckParam) {
        return LicenseUtils.getExpiryDate(licenseCheckParam.getLicense());
    }

    @ApiOperation(value = "生成macId")
    @WriteLog(type = "3", description = "生成macId")
    @GetMapping("/gen_mac_id")
    public String genMacId() throws Exception {

        String localMac = NetUtils.getLocalMac();
        int gen4thNumber = NumberUtils.gen4thNumber();

        //用随机数作为秘钥二次加密
        EncryptUtils encryptUtil = new EncryptUtils(gen4thNumber + "");
        String encryptMac = encryptUtil.encrypt(localMac);

        return EncryptUtils.getInstance().encrypt(encryptMac + "-" + gen4thNumber);
    }

    @ApiOperation(value = "展示证书信息")
    @WriteLog(type = "6", description = "展示证书信息")
    @GetMapping("/view")
    public LicenseCheckModel view() {
        LicenseCreator licenseCreator = new LicenseCreator(licenseCreatorConfig);
        return licenseCreator.getLicenseCheckModel();
    }

    @ApiOperation(value = "检查并安装证书")
    @WriteLog(type = "3", description = "检查并安装证书")
    @PostMapping("/install")
    public void licenseCheckParam(@RequestBody LicenseCheckParam licenseCheckParam) {
        boolean checkResult = LicenseUtils.checkLicense(licenseCheckParam.getLicense(), licenseCheckParam.getClientName(),licenseCheckParam.getMacId());
        if (!checkResult) {
            throw new BizException(400,"证书校验失败");
        }
        installLicense(licenseCheckParam);
    }

    @ApiOperation(value = "卸载证书")
    @WriteLog(type = "4", description = "卸载证书")
    @GetMapping("/uninstall")
    public void uninstall()  {
        new LicenseCreator(licenseCreatorConfig).uninstall();
    }

    private void installLicense(LicenseCheckParam licenseCheckParam) {
        String expiryDate = LicenseUtils.getExpiryDate(licenseCheckParam.getLicense());
        String medinsName = LicenseUtils.getMedinsName(licenseCheckParam.getLicense());
        if(StringUtils.isBlank(expiryDate)) {
            throw new RuntimeException("证书解析失败");
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = dateFormat.parse(expiryDate);
        } catch (ParseException e) {
            log.error(e.getMessage(),e);
        }

        // 证书存放的业务信息
        LicenseCheckModel licenseCheckModel = new LicenseCheckModel();
        licenseCheckModel.setMedinsName(medinsName);
        licenseCheckModel.setClientName(licenseCheckParam.getClientName());
        licenseCheckModel.setExpiryDate(date);
        licenseCheckModel.setLicense(licenseCheckParam.getLicense());
        licenseCheckModel.setMacId(licenseCheckParam.getMacId());

        // 证书配置
        licenseCreatorConfig.setLicenseCheckModel(licenseCheckModel);
        licenseCreatorConfig.setExpiryDate(date);
        LicenseCreator creator = new LicenseCreator(licenseCreatorConfig);
        boolean isSuccess = creator.generateLicense();
        if (!isSuccess) {
            throw new BizException(500,"证书生成失败");
        }
    }
}
