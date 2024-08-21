package com.winning.hmap.portal.dict.entity;

import java.util.Date;

/**
 *
 * @author hugo.zxh
 * @date 2024-03-07
 */
public class SysLog {
    private Long id;

    private String opnType;

    private Long userId;

    /**
     * 姓名
     */
    private String username;

    /**
     * 账号
     */
    private String loginName;

    private String ip;

    /**
     * 操作时间
     */
    private Date opnTime;

    /**
     * 内容
     */
    private String content;

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

    public String getOpnType() {
        return opnType;
    }

    public void setOpnType(String opnType) {
        this.opnType = opnType == null ? null : opnType.trim();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName == null ? null : loginName.trim();
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    public Date getOpnTime() {
        return opnTime;
    }

    public void setOpnTime(Date opnTime) {
        this.opnTime = opnTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
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