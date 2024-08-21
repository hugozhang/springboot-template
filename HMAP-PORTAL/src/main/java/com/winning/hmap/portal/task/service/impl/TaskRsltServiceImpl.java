package com.winning.hmap.portal.task.service.impl;

import com.winning.hmap.portal.auth.dto.auth.resp.LoginUser;
import com.winning.hmap.portal.task.constant.Constant;
import com.winning.hmap.portal.task.dto.*;
import com.winning.hmap.portal.task.dto.req.query.TaskRsltParam;
import com.winning.hmap.portal.task.dto.resp.TaskRslt;
import com.winning.hmap.portal.task.dto.resp.TaskSchm;
import com.winning.hmap.portal.task.mapper.SchmRsltMapper;
import com.winning.hmap.portal.task.mapper.TaskNoticeMapper;
import com.winning.hmap.portal.task.mapper.TaskRsltMapper;
import com.winning.hmap.portal.task.service.TaskRsltService;
import com.winning.hmap.portal.util.TimeTool;
import me.about.widget.mybatis.plugin.page.model.PageParam;
import me.about.widget.mybatis.plugin.page.model.PageResult;
import me.about.widget.spring.mvc.exception.BizException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author cpj
 */
@Service
public class TaskRsltServiceImpl implements TaskRsltService {

    @Autowired
    SchmRsltMapper schmRsltMapper;

    @Resource
    TaskNoticeMapper taskFailNotice;

    @Resource
    TaskRsltMapper taskRsltMapper;

    @Autowired
    TimerTaskExecutor timerTaskExecutor;

    @Override
    public PageResult<TaskRslt> querySchmRslt(PageParam<TaskRsltParam> pageParam) {
        return schmRsltMapper.querySchmRslt(pageParam);
    }

    @Override
    public List<TaskRslt> querySchmRsltDetails(TaskRsltParam pageParam) {
        return schmRsltMapper.querySchmDetails(pageParam);
    }

    @Override
    public void delSchmRslt(String runBchno) {
        taskRsltMapper.updateTaskStas(runBchno, Constant.JOB_STAS_YHG);
    }

    @Override
    public void rerunTaskSchm(String runBchno) {
        List<TimerDto> list = new ArrayList<>();
        List<TaskSchm> taskSchmList = schmRsltMapper.queryTaskSchmByRunBchno(runBchno);
        String time = TimeTool.getMillisecondTime().replaceAll("-", "").replaceAll(":", "").replaceAll(" ", "");
        for (TaskSchm taskSchm : taskSchmList) {
            if (!Constant.HMAP_JOB_FLAG.equals(taskSchm.getSchmType())) {
                taskRsltMapper.updateTaskStas(runBchno, Constant.JOB_STAS_BNCP);
                throw new BizException(400,"该任务不支持重跑");
            }else{
                String runBatch = taskSchm.getSchmId() + "_" + time;
                TimerDto dto = new TimerDto();
                dto.setSchmId(taskSchm.getSchmId());
                dto.setImpl(taskSchm.getImpl());
                dto.setRunBchno(taskSchm.getRunBchno());
                dto.setTaskId(taskSchm.getTaskId());
                dto.setPrrt(taskSchm.getPrrt());
                dto.setTaskName(taskSchm.getTaskName());
                dto.setSchmName(taskSchm.getSchmName());
                dto.setRunBchno(runBatch);
                list.add(dto);
            }
        }
        for (TimerDto timerDto : list) {
            timerTaskExecutor.executeSchduletask(timerDto);
        }
        taskRsltMapper.updateTaskStas(runBchno, Constant.JOB_STAS_DZX);
    }

    @Override
    public void taskFailNotice(String runBchno, LoginUser loginUser) {
        Long userId = loginUser.getUserId();
        taskFailNotice.taskFailNotice(runBchno,userId);
    }


}
