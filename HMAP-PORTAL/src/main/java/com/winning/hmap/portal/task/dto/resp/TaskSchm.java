package com.winning.hmap.portal.task.dto.resp;

import lombok.Data;

@Data
public class TaskSchm {

    /**
     * 执行方案ID
     */
    private Long schmId;

    /**
     * 执行方案名称
     */
    private String schmName;

    /**
     * 执行方案类型
     */
    private String schmType;

    /**
     * 方案实现 0-JavaClass 1-Procedure 2-Kettle
     */
    private String impl;

    /**
     * 方案参数
     */
    private String para;

    /**
     * 任务优先级
     */
    private Integer prrt;

    /**
     * 任务ID
     */
    private Long taskId;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 执行方案附件
     */
    private String attUuid;

    /**
     * 执行方案有效标志
     */
    private String valiFlag;

    /**
     * 任务运行批次
     */
    private String runBchno;

    /**
     * 批次ID
     */
    private String bchno;

    /**
     * 任务ID
     */
    private String ticketCd;

}
