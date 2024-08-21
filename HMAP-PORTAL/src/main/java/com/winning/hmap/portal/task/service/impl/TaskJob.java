package com.winning.hmap.portal.task.service.impl;

import com.winning.hmap.portal.task.service.SchduletaskService;
import lombok.Getter;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * TaskJob
 */
@Component
public class TaskJob implements Job,ApplicationContextAware {

    @Getter
    private static ApplicationContext applicationContext;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap map = context.getMergedJobDataMap();
        Long taskId = (Long) map.get("taskId");
        SchduletaskService schduletaskService = getBean(SchduletaskService.class);
        //执行任务
        schduletaskService.jobHandler(taskId);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        TaskJob.applicationContext = applicationContext;
    }

    /**
     * 通过class获取Bean.
     */
    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }
}
