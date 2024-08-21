package com.winning.hmap.portal.auth.entity;

import java.util.Date;

/**
 *
 * @author hugo.zxh
 * @date 2024-02-26
 */
public class SysMedins {
    private Long id;

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
     * 机构类型
     */
    private String medinsType;

    /**
     * 机构类别
     */
    private String medinsCat;


    private String delFlag;

    private Date crteTime;

    private Long crterId;

    private Date updtTime;

    private Long updtrId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMedinsName() {
        return medinsName;
    }

    public void setMedinsName(String medinsName) {
        this.medinsName = medinsName == null ? null : medinsName.trim();
    }

    public String getMedinsNo() {
        return medinsNo;
    }

    public void setMedinsNo(String medinsNo) {
        this.medinsNo = medinsNo == null ? null : medinsNo.trim();
    }

    public String getMedinsLv() {
        return medinsLv;
    }

    public void setMedinsLv(String medinsLv) {
        this.medinsLv = medinsLv == null ? null : medinsLv.trim();
    }

    public String getMedinsType() {
        return medinsType;
    }

    public void setMedinsType(String medinsType) {
        this.medinsType = medinsType == null ? null : medinsType.trim();
    }

    public String getMedinsCat() {
        return medinsCat;
    }

    public void setMedinsCat(String medinsCat) {
        this.medinsCat = medinsCat == null ? null : medinsCat.trim();
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag == null ? null : delFlag.trim();
    }

    public Date getCrteTime() {
        return crteTime;
    }

    public void setCrteTime(Date crteTime) {
        this.crteTime = crteTime;
    }

    public Long getCrterId() {
        return crterId;
    }

    public void setCrterId(Long crterId) {
        this.crterId = crterId;
    }

    public Date getUpdtTime() {
        return updtTime;
    }

    public void setUpdtTime(Date updtTime) {
        this.updtTime = updtTime;
    }

    public Long getUpdtrId() {
        return updtrId;
    }

    public void setUpdtrId(Long updtrId) {
        this.updtrId = updtrId;
    }
}