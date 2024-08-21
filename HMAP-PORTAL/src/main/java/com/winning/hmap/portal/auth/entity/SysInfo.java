package com.winning.hmap.portal.auth.entity;

import java.util.Date;

/**
 * @author cpj
 * @date 2024/5/6 14:35
 * @desciption: 系统信息
 */
public class SysInfo {

    /**
     * 系统名称
     */
    private String sysName;

    /**
     * 机构名称
     */
    private String medinsName;

    /**
     * 机构名称2（可选）
     */
    private String medinsName2;

    /**
     * 系统版本
     */
    private String sysVer;

    /**
     * 系统备注
     */
    private String memo;

    /**
     * 创建时间
     */
    private Date crteTime;

    /**
     * 更新时间
     */
    private Date updtTime;

    public String getSysName() {
        return sysName;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    public String getMedinsName() {
        return medinsName;
    }

    public void setMedinsName(String medinsName) {
        this.medinsName = medinsName;
    }

    public String getMedinsName2() {
        return medinsName2;
    }

    public void setMedinsName2(String medinsName2) {
        this.medinsName2 = medinsName2;
    }

    public String getSysVer() {
        return sysVer;
    }

    public void setSysVer(String sysVer) {
        this.sysVer = sysVer;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Date getCrteTime() {
        return crteTime;
    }

    public void setCrteTime(Date crteTime) {
        this.crteTime = crteTime;
    }

    public Date getUpdtTime() {
        return updtTime;
    }

    public void setUpdtTime(Date updtTime) {
        this.updtTime = updtTime;
    }
}
