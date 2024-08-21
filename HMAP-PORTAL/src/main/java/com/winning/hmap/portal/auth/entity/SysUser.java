package com.winning.hmap.portal.auth.entity;

import java.util.Date;

/**
 *
 * @author hugo.zxh
 * @date 2024-02-27
 */
public class SysUser {
    private Long id;

    /**
     * 账号
     */
    private String loginName;

    /**
     * 密码
     */
    private String password;

    /**
     * 姓名
     */
    private String username;

    /**
     * 关联医生id
     */
    private Long drId;

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

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName == null ? null : loginName.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public Long getDrId() {
        return drId;
    }

    public void setDrId(Long drId) {
        this.drId = drId;
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