package com.winning.hmap.portal.auth.controller;

import com.winning.hmap.portal.auth.dto.license.req.LicenseCheckModel;
import com.winning.hmap.portal.auth.entity.SysInfo;
import com.winning.hmap.portal.auth.service.SysService;
import com.winning.hmap.portal.auth.service.license.LicenseCreator;
import com.winning.hmap.portal.auth.service.license.LicenseCreatorConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 基础数据模块
 * @return
 */
@RestController
@RequestMapping("/hmap/sys")
public class SysController {

    @Autowired
    private SysService sysService;

    @Resource
    private LicenseCreatorConfig licenseCreatorConfig;

    /**
     * 系统信息
     * @return
     */
    @PostMapping("/info")
    public SysInfo info() {
        SysInfo sysInfo = sysService.querySysInfo();
        LicenseCreator licenseCreator = new LicenseCreator(licenseCreatorConfig);
        LicenseCheckModel licenseCheckModel = licenseCreator.getLicenseCheckModel();
        if(licenseCheckModel != null){
            sysInfo.setMedinsName(licenseCheckModel.getMedinsName());
        }
    	return sysInfo;
    }

}
