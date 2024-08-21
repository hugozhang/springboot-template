package com.winning.hmap.portal.task.entity;

import lombok.Data;

import java.util.Date;

/**
 * 计划任务配置表
 * @TableName TASK
 */
@Data
public class Task {
    /**
     * ID
     */
    private Long id;

    /**
     * 任务名称
     */
    private String name;

    /**
     * 计划类型
     */
    private String type;

    /**
     * 执行表达式
     */
    private String exeExpr;

    /**
     * 任务描述
     */
    private String dscr;

    /**
     * 有效标识 0-无效 1-有效
     */
    private String valiFlag;

    /**
     * 任务标识
     */
    private String taskFlag;

    /**
     * 创建人ID
     */
    private Long crterId;

    /**
     * 创建时间
     */
    private Date crteTime;

    /**
     * 修改人ID
     */
    private Long updtrId;

    /**
     * 修改人姓名
     */
    private String modifyName;

    /**
     * 修改时间
     */
    private Date updtTime;

    /**
     * 删除标识
     */
    private String delFlag;

}