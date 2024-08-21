package com.winning.hmap.portal.task.service;

import com.winning.hmap.portal.task.dto.QuartzDto;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;

import java.util.List;
import java.util.Set;

/**
 * JobService
 * @author cpj
 */
public interface JobService {

    /**
     * 新增任务
     */
    void save(QuartzDto quartz);

    /**
     * 立即执行任务
     */
    void trigger(QuartzDto quartz);

    /**
     * 停止任务
     */
    void pause(QuartzDto quartz);

    /**
     * 恢复任务
     */
    void resume(QuartzDto quartz);

    /**
     * 删除任务
     */
    void remove(QuartzDto quartz);

    /**
     * 判断任务是否存在
     */
    boolean checkExists(QuartzDto quartz);

    /**
     * 获取任务组名称
     *
     * @return
     */
    Set<JobKey> getJobNames();

    /**
     * 获取当前正在运行的任务
     *
     * @return
     */
    List<JobExecutionContext> getCurrentlyExecutingJobs();
}
