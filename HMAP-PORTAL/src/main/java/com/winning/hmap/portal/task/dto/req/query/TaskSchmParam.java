package com.winning.hmap.portal.task.dto.req.query;

import lombok.Data;

/**
 * @author cpj
 * @date 2024/3/19 10:09
 */
@Data
public class TaskSchmParam {

    /**
     * 任务ID
     */
    private Long taskId;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 任务类型
     */
    private String taskType;

    /**
     * 执行方案名称
     */
    private String schmName;

    /**
     * 执行方案类型
     */
    private String schmType;

    /**
     * 有效状态
     */
    private String valiFlag;

}
