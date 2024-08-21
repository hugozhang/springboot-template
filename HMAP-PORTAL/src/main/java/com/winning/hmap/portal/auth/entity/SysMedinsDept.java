package com.winning.hmap.portal.auth.entity;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author hugo.zxh
 * @date 2024-02-26
 */
public class SysMedinsDept implements Serializable {

    private static final long serialVersionUID = 1596954516740600512L;

    private Long id;

    private String deptName;

    private String deptCode;

    private String hiDeptName;

    private String hiDeptCode;

    /**
     * 医疗机构id
     */
    private Long medinsId;

    /**
     * 医疗机构编码
     */
    private String medinsNo;

    /**
     * 父节点id
     */
    private Long parentId;

    /**
     * 是否可用,1：可用，0不可用
     */
    private String delFlag;

    private Date crteTime;

    private Long crterId;

    private Date updtTime;

    private Long updtrId;

    private String label;

    private String code;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName == null ? null : deptName.trim();
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode == null ? null : deptCode.trim();
    }

    public String getHiDeptName() {
        return hiDeptName;
    }

    public void setHiDeptName(String hiDeptName) {
        this.hiDeptName = hiDeptName;
    }

    public String getHiDeptCode() {
        return hiDeptCode;
    }

    public void setHiDeptCode(String hiDeptCode) {
        this.hiDeptCode = hiDeptCode;
    }

    public Long getMedinsId() {
        return medinsId;
    }

    public void setMedinsId(Long medinsId) {
        this.medinsId = medinsId;
    }

    public String getMedinsNo() {
        return medinsNo;
    }

    public void setMedinsNo(String medinsNo) {
        this.medinsNo = medinsNo;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
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