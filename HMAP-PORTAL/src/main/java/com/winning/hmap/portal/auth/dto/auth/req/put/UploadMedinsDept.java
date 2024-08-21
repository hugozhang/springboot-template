package com.winning.hmap.portal.auth.dto.auth.req.put;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author cpj
 * @date 2024-04-19
 */
public class UploadMedinsDept implements Serializable {

    private static final long serialVersionUID = 1596954516740600512L;

    /**
     * Excel 序号
     */
    private Integer sn;

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
     * 父节点 name
     */
    private String parentName;

    /**
     * 上级科室编码
     */
    private String parentCode;

    private Date crteTime;

    private Long crterId;

    private Date updtTime;

    private Long updtrId;

    public Integer getSn() {
        return sn;
    }

    public void setSn(Integer sn) {
        this.sn = sn;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
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