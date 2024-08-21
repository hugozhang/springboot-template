package com.winning.hmap.portal.task.entity;

import lombok.Data;

/**
 * 计划任务方案关系表
 * @TableName TASK_SCHM_RLTS
 */
@Data
public class TaskSchmRlts {
    /**
     * 任务名称
     */
    private Long taskId;

    /**
     * 执行方案ID
     */
    private Long schmId;

    /**
     * 任务优先级
     */
    private Integer prrt;

    /**
     * 删除标识
     */
    private String delFlag;

}