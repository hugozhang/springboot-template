package com.winning.hmap.portal.task.dto.req.query;

import lombok.Data;

import java.util.Date;

/**
 * @author cpj
 */
@Data
public class TaskRsltParam {

	/**
	 * 计划任务ID
	 */
	private Long taskId;

	/**
	 * 计划任务类型
	 */
	private String taskType;

	/**
	 * 计划任务名称
	 */
	private String taskName;

	/**
	 * 计划任务状态
	 */
	private String taskStas;

	/**
	 * 批次ID
	 */
	private String runBchno;

	/**
	 * 日志
	 */
	private String log;

	/**
	 * 执行结束时间
	 */
	private Date exeEndtime;

	/**
	 * 涉及数据量
	 */
	private Integer dataSum;

	/**
	 * 运行任务ID
	 */
	private String ticketCd;

	/**
	 * 已读状态
	 */
	private String read;
}