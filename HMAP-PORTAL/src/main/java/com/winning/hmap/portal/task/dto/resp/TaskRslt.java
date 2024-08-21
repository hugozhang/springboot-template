package com.winning.hmap.portal.task.dto.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author cpj
 * @date 2024/4/1 10:53
 * @desciption: 执行结果
 */
@Data
public class TaskRslt {

    /**
     * 执行结果ID
     */
    private Long id;

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
     * 任务类型名称
     */
    private String taskTypeName;

    /**
     * 任务状态
     */
    private String taskStas;

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
     * 执行开始时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date exeBegntime;

    /**
     * 执行结束时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date exeEndtime;

    /**
     * 执行状态
     */
    private Date exeStas;

    /**
     * 任务执行数据量
     */
    private Integer dataSum;

    /**
     * 任务结果已读
     */
    private String read;

    /**
     * 方案参数
     */
    private String para;

    /**
     * 任务优先级
     */
    private Integer prrt;

    /**
     * 执行日志
     */
    private String log;

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
