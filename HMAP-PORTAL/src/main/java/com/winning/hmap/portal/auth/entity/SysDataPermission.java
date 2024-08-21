package com.winning.hmap.portal.auth.entity;

import java.util.Date;

/**
 *
 * @author hugo.zxh
 * @date 2024-02-26
 */
public class SysDataPermission {
    private Long id;

    /**
     * 比如  医共体=MEDINS_COMM_SCOPE、医院=MEDINS_SCOPE、科室=MEDINS_DEPT__SCOPE
     */
    private String name;

    /**
     * 比如  医共体=MEDINS_COMM_SCOPE、医院=MEDINS_SCOPE、科室=MEDINS_DEPT__SCOPE
     */
    private String code;

    /**
     * sql 表达式
     */
    private String sqlExpr;

    /**
     * 是否可用,1：可用，0不可用
     */
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getSqlExpr() {
        return sqlExpr;
    }

    public void setSqlExpr(String sqlExpr) {
        this.sqlExpr = sqlExpr == null ? null : sqlExpr.trim();
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