package com.winning.hmap.portal.auth.entity;

import java.util.Date;

/**
 *
 * @author hugo.zxh
 * @date 2024-02-27
 */
public class SysMedinsDeptDoctor {
    /**
     * 主键id
     */
    private Long id;

    /**
     * 医生id
     */
    private Long drId;

    /**
     *  科室id
     */
    private Long medinsDeptId;

    /**
     * 创建时间
     */
    private Date crteTime;

    /**
     * 创建人
     */
    private Long crterId;

    /**
     * 更新时间
     */
    private Date updtTime;

    /**
     * 更新人
     */
    private Long updtrId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDrId() {
        return drId;
    }

    public void setDrId(Long drId) {
        this.drId = drId;
    }

    public Long getMedinsDeptId() {
        return medinsDeptId;
    }

    public void setMedinsDeptId(Long medinsDeptId) {
        this.medinsDeptId = medinsDeptId;
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