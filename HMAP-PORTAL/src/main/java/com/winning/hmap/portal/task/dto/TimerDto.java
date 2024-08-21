package com.winning.hmap.portal.task.dto;

import lombok.Data;

@Data
public class TimerDto {

	/**
	 * 任务ID
	 */
	private Long taskId;

    /**
     * 计划任务名称
     */
    private String taskName;

	/**
	 * 执行方案ID
	 */
	private Long schmId;

    /**
     * 执行方案名称
     */
	private String schmName;

    /**
     * 方案实现 0-JavaClass 1-Procedure 2-Kettle
     */
    private String impl;

    /**
     * 计划任务ID
     */
    private String ticketCd;

    /**
     * 执行方案参数
     */
    private String[] paraArr;

    /**
     * 任务运行批次
     */
    private String runBchno;

    /**
     * 任务有效状态（仅识别执行任务是否存在）
     */
    private String taskValidState;

    /**
     * 任务优先级
     */
    private Integer prrt;
}
