package com.winning.hmap.portal.task.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.util.Date;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuartzDto{

    //任务名称
    private String jobName;

    //任务分组
    private String jobGroup;

    //任务描述
    private String description;

    //执行类
    private String jobClassName;

    //执行方法
    private String jobMethodName;

    //执行时间
    private String cronExpression;

    //执行时间
    private String triggerName;

    //任务状态
    private String triggerState;

    //任务名称 用于修改
    private String oldJobName;

    //任务分组 用于修改
    private String oldJobGroup;

    /**
     * 定时任务数据
     */
    private Map<String, Long> jobDataMap;

    /**
     * 触发器执行开始时间
     */
    private Date triggerStartTime;

    public QuartzDto() {
    }

    public QuartzDto(String jobName) {
        this.jobName = jobName;
    }

    public QuartzDto(String jobName, String jobGroup) {
        this.jobName = jobName;
        this.jobGroup = jobGroup;
    }
}
