package com.winning.hmap.portal.task.service.impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.winning.hmap.portal.task.dto.QuartzDto;
import com.winning.hmap.portal.task.service.JobService;
import com.winning.hmap.portal.util.SnowflakeUtil;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author cpj
 */
@Service
public class JobServiceImpl implements JobService {

    private final Logger logger = LoggerFactory.getLogger(JobServiceImpl.class);

    //执行计划的变量参数
    private static final String STR_FALSE = "false";
    private static final String PLAN_NAME = "plan";
    private static final String SERVER_NAME = "executor";
    private static final String TRIGGER_NAME_PREX = "trigger";


    Scheduler scheduler= null;
    
    @Autowired
    TimerTaskExecutor timerTaskService;
    
    
    /**
     * 新增任务
     */
    @Override
    public void save(QuartzDto quartz) {
        if (StrUtil.isEmpty(quartz.getJobName())) {
            String num = SnowflakeUtil.nextIdStr();
            quartz.setJobGroup(Scheduler.DEFAULT_GROUP);
            quartz.setJobName(num);
        }

        try {
            scheduler= StdSchedulerFactory.getDefaultScheduler();
            //获取Scheduler实例、废弃、使用自动注入的scheduler、否则spring的service将无法注入
            //Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            //如果是修改  展示旧的 任务
            if (quartz.getOldJobName() != null) {
                remove(new QuartzDto(quartz.getOldJobName(), Scheduler.DEFAULT_GROUP));
            }

            Class<Job> clazz = (Class<Job>) Class.forName(quartz.getJobClassName());

            //构建job信息
            JobDetail job = JobBuilder.newJob(clazz).withIdentity(quartz.getJobName(),
                            Scheduler.DEFAULT_GROUP)
                    .withDescription(quartz.getDescription()).build();
            if (StrUtil.isNotEmpty(quartz.getJobMethodName())) {
                job.getJobDataMap().put("jobMethodName", quartz.getJobMethodName());
            }
            if (MapUtil.isNotEmpty(quartz.getJobDataMap())) {
                for (Map.Entry<String, Long> entry : quartz.getJobDataMap().entrySet()) {
                    job.getJobDataMap().put(entry.getKey(), entry.getValue());
                }
            }

            Date triggerStartTime = quartz.getTriggerStartTime() == null ? new Date() : quartz.getTriggerStartTime();

            // 触发时间点
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(quartz.getCronExpression());
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(quartz.getJobName(), Scheduler.DEFAULT_GROUP)
                    .startAt(triggerStartTime)
                    .withSchedule(cronScheduleBuilder).build();
            //交由Scheduler安排触发
            scheduler.scheduleJob(job, trigger);
        } catch (Exception e) {
            logger.error("新增任务异常", e);
        }
    }


    /**
     * 立即执行任务
     */
    @Override
    public void trigger(QuartzDto quartz) {
        try {
            scheduler= StdSchedulerFactory.getDefaultScheduler();
            JobKey key = new JobKey(quartz.getJobName(), Scheduler.DEFAULT_GROUP);
            scheduler.triggerJob(key);
        } catch (SchedulerException e) {
            logger.error("立即执行任务异常", e);
        }
    }

    /**
     * 停止任务
     */
    @Override
    public void pause(QuartzDto quartz) {
        JobKey jobKey =JobKey.jobKey(quartz.getJobName(), Scheduler.DEFAULT_GROUP);
        try {
            scheduler= StdSchedulerFactory.getDefaultScheduler();
            scheduler.pauseJob(jobKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
            logger.error("停止任务异常", e);
        }
    }

    /**
     * 恢复任务
     */
    @Override
    public void resume(QuartzDto quartz) {
        try {
            scheduler= StdSchedulerFactory.getDefaultScheduler();
            JobKey key = new JobKey(quartz.getJobName(), Scheduler.DEFAULT_GROUP);
            scheduler.resumeJob(key);
        } catch (SchedulerException e) {
            logger.error("恢复任务异常", e);
        }
    }

    /**
     * 删除任务
     */
    @Override
    public void remove(QuartzDto quartz) {
        try {
            scheduler= StdSchedulerFactory.getDefaultScheduler();
            TriggerKey triggerKey = TriggerKey.triggerKey(quartz.getJobName(), Scheduler.DEFAULT_GROUP);
            // 停止触发器
            scheduler.pauseTrigger(triggerKey);
            // 移除触发器
            scheduler.unscheduleJob(triggerKey);
            // 删除任务
            scheduler.deleteJob(JobKey.jobKey(quartz.getJobName(), Scheduler.DEFAULT_GROUP));
        } catch (Exception e) {
            //logger.error("删除任务异常", e);
        }
    }

    /**
     * 判断任务是否存在
     */
    @Override
    public boolean checkExists(QuartzDto quartz) {
        try {
            scheduler= StdSchedulerFactory.getDefaultScheduler();
            JobKey key = new JobKey(quartz.getJobName(), Scheduler.DEFAULT_GROUP);
            // 判断任务是否存在
            return scheduler.checkExists(key);
        } catch (Exception e) {
            logger.error("判断任务是否存在异常", e);
            return false;
        }
    }

    /**
     * 获取任务组名称
     *
     * @return
     */
    @Override
    public Set<JobKey> getJobNames() {
        try {
            scheduler = StdSchedulerFactory.getDefaultScheduler();
            return scheduler.getJobKeys(GroupMatcher.jobGroupEquals(Scheduler.DEFAULT_GROUP));
        } catch (Exception e) {
            logger.error("获取任务组名称异常", e);
            return new HashSet<>();
        }
    }



    /**
     * 获取当前正在运行的任务
     *
     * @return
     */
    @Override
    public List<JobExecutionContext> getCurrentlyExecutingJobs() {
        try {
            scheduler =  StdSchedulerFactory.getDefaultScheduler();
            return scheduler.getCurrentlyExecutingJobs();
        } catch (Exception e) {
            logger.error("获取任务组名称异常", e);
            return new ArrayList<>();
        }
    }
}
