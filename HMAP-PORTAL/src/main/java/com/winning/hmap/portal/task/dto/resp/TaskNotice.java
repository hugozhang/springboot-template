package com.winning.hmap.portal.task.dto.resp;

import lombok.Data;

/**
 * @author cpj
 * @date 2024/3/28 14:16
 * @desciption: 执行任务失败提醒
 */
@Data
public class TaskNotice {

    /**
     * 任务ID
     */
    private Long taskId;

    /**
     * 方案ID
     */
    private Long schmId;

    /**
     * 失败提醒数
     */
    private Integer noticeNum;
}
