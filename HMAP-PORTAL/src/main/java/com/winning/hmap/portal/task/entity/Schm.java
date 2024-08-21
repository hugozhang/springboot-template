package com.winning.hmap.portal.task.entity;

import lombok.Data;
import java.util.Date;

/**
 * 计划任务方案配置表
 * @author cpj
 * @TableName task_schm
 */
@Data
public class Schm{

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 方案名称
     */
    private String name;

    /**
     * 方案类型
     */
    private String type;

    /**
     * 执行方案有效标志
     */
    private String valiFlag;

    /**
     * 执行方案附件
     */
    private String attUuid;

    /**
     * 方案实现
     * 0 JavaClass
     * 1 Procedure
     * 2 Kettle
     */
    private String impl;

    /**
     * 方案描述
     */
    private String dscr;

    /**
     * 方案参数
     */
    private String para;

    /**
     * 方案属性
     */
    private String attr;

    /**
     * 创建人ID
     */
    private Long crterId;

    /**
     * 创建人名称
     */
    private Long crterName;

    /**
     * 创建时间
     */
    private Date crteTime;

    /**
     * 修改人ID
     */
    private Long updtrId;

    /**
     * 修改人名称
     */
    private String updtrName;

    /**
     * 修改时间
     */
    private Date updtTime;

    /**
     * 删除标识
     */
    private String delFlag;

}