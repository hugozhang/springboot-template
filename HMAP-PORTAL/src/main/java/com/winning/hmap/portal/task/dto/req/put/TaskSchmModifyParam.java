package com.winning.hmap.portal.task.dto.req.put;

import lombok.Data;

@Data
public class TaskSchmModifyParam{

    /**
     * 任务ID
     */
    private Long taskId;

    /**
     * 执行方案ID
     */
    private Long oldSchmId;

    /**
     * 执行方案ID
     */
    private Long schmId;

    /**
     * 1-save 2-update 3-delete
     */
    private String exeType;

    /**
     * 删除标识
     */
    private String delFlag;

    private static final long serialVersionUID = 1L;
}
