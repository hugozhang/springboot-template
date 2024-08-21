package com.winning.hmap.portal.task.service.impl;

import com.winning.hmap.portal.task.constant.Constant;
import com.winning.hmap.portal.task.dto.TimerDto;
import com.winning.hmap.portal.task.mapper.TaskRsltMapper;
import com.winning.hmap.portal.task.service.JobExecutor;
import com.winning.hmap.portal.util.TimeTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author cpj
 */
@Service
public class TimerTaskExecutor {

    private static final BlockingQueue<TimerDto> BLOCKING_QUEUE = new LinkedBlockingQueue<>(100);

    @Autowired
    ApplicationContext context;

    @Autowired
    Map<String, JobExecutor> timerTaskServiceMap;

    @Resource
    TaskRsltMapper taskRsltMapper;

    @Resource(name = "taskAsyncPool")
    private Executor executor;

    @PostConstruct
    public void init() {
        executor.execute(() -> {
            try {
                processBlockingQueue(BLOCKING_QUEUE);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    public void executeSchduletask(TimerDto timerDto) {
        BLOCKING_QUEUE.offer(timerDto);
    }

    public void processBlockingQueue(BlockingQueue<TimerDto> blockingQueue) throws InterruptedException {
        while (true) {
            TimerDto timerDto;
            while ((timerDto = blockingQueue.poll()) != null) {
                String sign = timerDto.getImpl();
                TimerDto obj = timerDto;
                CountDownLatch countDownLatch = new CountDownLatch(1);
                executor.execute(() -> {
                    try {
                        timerTaskServiceMap.get(sign).execute(obj);
                    } catch (Exception e) {
                        String ticketId = TimeTool.getMillisecondTime().replaceAll("-", "").replaceAll(":", "");//预留
                        taskRsltMapper.insertTaskRslt(obj.getTaskId(),obj.getSchmId(),new Date(),Constant.JOB_STAS_ZXSB, "任务执行失败（执行方案配置有误）", ticketId, "0",obj.getRunBchno(),obj.getTaskName(),obj.getSchmName(),obj.getPrrt());
                        e.printStackTrace();
                    } finally {
                        countDownLatch.countDown();
                    }
                });
                countDownLatch.await();
            }
        }
    }

}
