package com.winning.hmap.portal.task.service;

import com.winning.hmap.portal.auth.dto.auth.resp.LoginUser;
import com.winning.hmap.portal.task.dto.KettleFileDto;
import com.winning.hmap.portal.task.dto.req.put.SchmParam;
import com.winning.hmap.portal.task.dto.req.put.TaskSchmModifyParam;
import com.winning.hmap.portal.task.dto.req.query.TaskSchmParam;
import com.winning.hmap.portal.task.dto.resp.TaskNotice;
import com.winning.hmap.portal.task.dto.resp.TaskSchm;
import com.winning.hmap.portal.task.entity.Schm;
import com.winning.hmap.portal.task.entity.Task;
import com.winning.hmap.portal.task.dto.req.query.TaskParam;
import me.about.widget.mybatis.plugin.page.model.PageParam;
import me.about.widget.mybatis.plugin.page.model.PageResult;

import java.util.List;

/**
 * @author cpj
 */
public interface SchduletaskService {

    /**
     * 执行任务失败提醒查询
     */
    TaskNotice taskNotice();

    /**
     * 任务列表查询
     */
    PageResult<Task> selectTaskByPage(PageParam<TaskSchmParam> pageParam);

    /**
     * 添加任务
     */
    void addTask(TaskParam taskParam, LoginUser loginUser);

    /**
     * 修改任务
     */
    void modifyTask(TaskParam taskParam, LoginUser loginUser);

    /**
     * 删除任务
     */
    void delTask(TaskParam taskParam, LoginUser loginUser);

    /**
     * 立即执行任务
     */
    void triggerTask(TaskParam dto);

    /**
     * 执行方案列表查询
     */
    PageResult<Schm> taskSchmList(PageParam<TaskSchmParam> pageParam);

    /**
     * 添加执行方案
     */
    void addTaskSchm(SchmParam schmParam, LoginUser loginUser);

    /**
     * 修改执行方案
     */
    void modifyTaskSchm(SchmParam schmParam, LoginUser loginUser);

    /**
     * 删除执行方案
     */
    void delTaskSchm(Long schmId, LoginUser loginUser);

    /**
     * 根据任务ID执行任务
     *
     * @param taskId 任务ID
     */
    void jobHandler(Long taskId);

    /**
     * 根据任务ID获取任务内容
     */
    PageResult<TaskSchm> getTaskContent(PageParam<TaskSchmParam> pageParam);

    /**
     * 添加任务内容
     *
     * @param dto
     * @return
     */
    void addTaskContent(TaskSchmModifyParam dto);

    /**
     * 修改任务内容
     *
     * @param dto
     * @return
     */
    void modifyTaskContent(TaskSchmModifyParam dto, LoginUser loginUser);

    /**
     * 删除任务内容
     *
     * @param dto
     * @return
     */
    void delTaskContent(TaskSchmModifyParam dto, LoginUser loginUser);

    /**
     * 获取指定目录下 kettle 文件名称及路径
     */
    List<KettleFileDto> kettleFilesList();

}
