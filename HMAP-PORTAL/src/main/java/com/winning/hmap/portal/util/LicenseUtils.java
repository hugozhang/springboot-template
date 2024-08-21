package com.winning.hmap.portal.util;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 工具类  和医院端一致  相同加密过程  代码来自polaris和医健   因为依赖比较多  单独拿下来
 *
 * @author: hugo.zxh
 * @date: 2022/10/18 11:24
 * @description:
 */
@Slf4j
public class LicenseUtils {

    private static final long WARNING_DAYS = 10L;

    public static final String SPLIT = "-polaris-";

    private LicenseUtils() {
    }

    @Setter
    @Getter
    @Builder
    @ToString
    static class LicenseEntity {

        private String licenseSource;

        private String medinsName;

        private String clientName;

        private String macId; //序列号

        private String oriMacId; //mac地址

        private String expiryDate;
    }


    public static LicenseEntity decryptLicenseEntity(String license) {
        LicenseEntity.LicenseEntityBuilder builder = LicenseEntity.builder();
        try {
            String licenseSource = EncryptUtils.getInstance().decrypt(license);
            String[] split = licenseSource.split(SPLIT);
            String clientName = split[0];
            String expiryDate = split[1];
            //加密的，需要解密得到原始的序列号
            String encryptMacId = split[2];
            String medinsName = split[3];
            //原始序列号
            String decrypt = EncryptUtils.getInstance().decrypt(encryptMacId);
            String[] parts = decrypt.split("-");
            String oriMacId = new EncryptUtils(parts[1]).decrypt(parts[0]);
            //构建对象
            builder.licenseSource(licenseSource)
                    .medinsName(medinsName)
                    .clientName(clientName)
                    .macId(encryptMacId)
                    .oriMacId(oriMacId)
                    .expiryDate(expiryDate);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        LicenseEntity build = builder.build();
        return build;
    }

    public static boolean checkLicense(String license, String client_name,String macIdSn) {
        return checkLicense(license, false, client_name,macIdSn);
    }

    public static boolean checkLicense(String license , boolean isWarning, String clientName,String macId) {

        if (StringUtils.isBlank(license) || StringUtils.isBlank(clientName)) {
            return false;
        }

        LicenseEntity licenseEntity = decryptLicenseEntity(license);

        //是否含有机构名称
        if (StringUtils.isBlank(licenseEntity.getMedinsName())) {
            return false;
        }

        //检查macId macId不等于null才检查
        if (macId != null && !macId.equals(licenseEntity.getMacId())){
            return false;
        }

        //判断是否存在mac地址
        if (StringUtils.isBlank(licenseEntity.getOriMacId()) && !NetUtils.getLocalMac().toUpperCase().contains(licenseEntity.getOriMacId())) {
            return false;
        }

        //是否含有客户名称
        if (StringUtils.isBlank(clientName) || !clientName.equals(licenseEntity.getClientName())) {
            return false;
        }

        //判断日期是否过期
        String ymd = licenseEntity.getExpiryDate();
        if (StringUtils.isBlank(ymd)) {
            return false;
        }

        //获取日期差
        long day = getDaySub(ymd);
        if (isWarning) {
            if (day <= WARNING_DAYS) {
                log.info("授权到期不足10天");
            }
        }
        if(day <= -1L) {
            return false;
        }
        return true;
    }

    public static String getExpiryDate(String license) {
        if (StringUtils.isBlank(license)) {
            return "";
        }
        LicenseEntity licenseEntity = decryptLicenseEntity(license);
        return licenseEntity.getExpiryDate();
    }

    //获取医院名称
    public static String getMedinsName(String license) {
        if (StringUtils.isBlank(license)) {
            return "";
        }
        LicenseEntity licenseEntity = decryptLicenseEntity(license);
        return licenseEntity.getMedinsName();
    }

    /**
     * 功能描述：时间相减得到天数
     * @param
     * @param endDateStr
     * @return
     * long
     * @author Administrator
     */
    public static long getDaySub(String endDateStr) {
        try{
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
            Date endDate= format.parse(endDateStr);
            long day=(endDate.getTime() - System.currentTimeMillis())/(24*60*60*1000);
            return day;
        } catch (Exception e1) {
            return -1;
        }
    }
}
