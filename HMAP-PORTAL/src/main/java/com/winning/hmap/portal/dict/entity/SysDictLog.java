package com.winning.hmap.portal.dict.entity;


public class SysDictLog{

    /**
     * 日志ID
     */
    private Long id;
    /**
     * 操作类型  1:新增 2:编辑 3:删除
     */
    private String optType;
    /**
     * 操作人
     */
    private String opter;
    /**
     * 操作内容
     */
    private String optContent;
    /**
     * 操作时间
     */
    private String optTime;

    /**
     * 操作时间开始
     */
    private String startDate;
    /**
     * 操作时间结束
     */
    private String endDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOptType() {
        return optType;
    }

    public void setOptType(String optType) {
        this.optType = optType;
    }

    public String getOpter() {
        return opter;
    }

    public void setOpter(String opter) {
        this.opter = opter;
    }

    public String getOptContent() {
        return optContent;
    }

    public void setOptContent(String optContent) {
        this.optContent = optContent;
    }

    public String getOptTime() {
        return optTime;
    }

    public void setOptTime(String optTime) {
        this.optTime = optTime;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
