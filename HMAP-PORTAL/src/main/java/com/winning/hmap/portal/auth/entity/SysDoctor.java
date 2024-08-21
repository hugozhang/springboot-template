package com.winning.hmap.portal.auth.entity;

import com.winning.hmap.portal.auth.dto.auth.req.query.DoctorMedGrpParam;

import java.util.Date;
import java.util.List;

/**
 *
 * @author hugo.zxh
 * @date 2024-02-27
 */
public class SysDoctor {
    private Long id;

    private String drName;

    private String drCode;

    private String hiDrCode;

    private String hiDrName;

    private Long medGrpId;

    private String dutyCode;

    private String dutyName;

    /**
     * 电话
     */
    private String tel;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 是否可用,1：可用，0不可用
     */
    private String delFlag;

    private Date crteTime;

    private Long crterId;

    private Date updtTime;

    private Long updtrId;

    private List<SysMedinsDeptDoctor> doctorRelatedDept;

    private List<DoctorMedGrpParam> doctorRelatedMedGroup;

    public List<DoctorMedGrpParam> getDoctorRelatedMedGroup() {
        return doctorRelatedMedGroup;
    }

    public void setDoctorRelatedMedGroup(List<DoctorMedGrpParam> doctorRelatedMedGroup) {
        this.doctorRelatedMedGroup = doctorRelatedMedGroup;
    }

    public List<SysMedinsDeptDoctor> getDoctorRelatedDept() {
        return doctorRelatedDept;
    }

    public void setDoctorRelatedDept(List<SysMedinsDeptDoctor> doctorRelatedDept) {
        this.doctorRelatedDept = doctorRelatedDept;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDrName() {
        return drName;
    }

    public void setDrName(String drName) {
        this.drName = drName == null ? null : drName.trim();
    }

    public String getDrCode() {
        return drCode;
    }

    public void setDrCode(String drCode) {
        this.drCode = drCode == null ? null : drCode.trim();
    }

    public String getHiDrCode() {
        return hiDrCode;
    }

    public void setHiDrCode(String hiDrCode) {
        this.hiDrCode = hiDrCode;
    }

    public String getHiDrName() {
        return hiDrName;
    }

    public void setHiDrName(String hiDrName) {
        this.hiDrName = hiDrName;
    }

    public Long getMedGrpId() {
        return medGrpId;
    }

    public void setMedGrpId(Long medGrpId) {
        this.medGrpId = medGrpId;
    }

    public String getDutyCode() {
        return dutyCode;
    }

    public void setDutyCode(String dutyCode) {
        this.dutyCode = dutyCode == null ? null : dutyCode.trim();
    }

    public String getDutyName() {
        return dutyName;
    }

    public void setDutyName(String dutyName) {
        this.dutyName = dutyName == null ? null : dutyName.trim();
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel == null ? null : tel.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
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