package com.winning.hmap.portal.task.controller;

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
import com.winning.hmap.portal.task.service.SchduletaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.about.widget.mybatis.plugin.page.model.PageParam;
import me.about.widget.mybatis.plugin.page.model.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * @author cpj
 */
@Api(tags = "任务方案接口")
@RestController
@RequestMapping("/hmap/timer/")
public class TaskSchmController {

    @Autowired
    SchduletaskService schduletaskService;

    /**
     * 执行任务失败提醒查询
     */
    @ResponseBody
    @PostMapping("taskNotice")
    public TaskNotice taskNotice() {
        return schduletaskService.taskNotice();
    }

    /**
     * 任务列表查询
     */
    @ApiOperation(value = "任务分页列表")
    @PostMapping("selectTaskByPage")
    public PageResult<Task> selectTaskByPage(@RequestBody PageParam<TaskSchmParam> pageParam) {
        return schduletaskService.selectTaskByPage(pageParam);
    }

    /**
     * 添加任务
     */
    @ResponseBody
    @PostMapping("addTask")
    public void addTask(@RequestBody TaskParam taskParam, @ApiIgnore LoginUser loginUser){
        schduletaskService.addTask(taskParam,loginUser);
    }

    /**
     * 修改任务
     */
    @ResponseBody
    @PostMapping("modifyTask")
    public void modifyTask(@RequestBody TaskParam taskParam, @ApiIgnore LoginUser loginUser){
        schduletaskService.modifyTask(taskParam,loginUser);
    }

    /**
     * 删除任务
     */
    @ResponseBody
    @PostMapping("delTask")
    public void delTask(@RequestBody TaskParam taskParam, @ApiIgnore LoginUser loginUser){
        schduletaskService.delTask(taskParam,loginUser);
    }

    /**
     * 立即执行任务
     */
    @ResponseBody
    @PostMapping("triggerTask")
    public void triggerTask(@RequestBody TaskParam dto) {
        schduletaskService.triggerTask(dto);
    }

    /**
     * 执行方案列表查询
     */
    @ResponseBody
    @PostMapping("taskSchmList")
    public PageResult<Schm> taskSchmList(@RequestBody PageParam<TaskSchmParam> pageParam) {
        return schduletaskService.taskSchmList(pageParam);
    }

    /**
     * 添加任务执行方案
     */
    @ResponseBody
    @PostMapping("addTaskSchm")
    public void addTaskSchm(@RequestBody SchmParam schmParam, @ApiIgnore LoginUser loginUser) {
        schduletaskService.addTaskSchm(schmParam,loginUser);
    }

    /**
     * 修改任务执行方案
     */
    @ResponseBody
    @PostMapping("modifyTaskSchm")
    public void modifyTaskSchm(@RequestBody SchmParam schmParam, @ApiIgnore LoginUser loginUser) {
        schduletaskService.modifyTaskSchm(schmParam,loginUser);
    }

    /**
     * 删除任务执行方案
     */
    @ResponseBody
    @PostMapping("/{schmId}/delTaskSchm")
    public void delTaskSchm(@PathVariable("schmId") Long schmId, @ApiIgnore LoginUser loginUser) {
        schduletaskService.delTaskSchm(schmId,loginUser);
    }

    /**
     * 根据任务ID获取任务内容
     */
    @ResponseBody
    @PostMapping("getTaskContent")
    public PageResult<TaskSchm> getTaskContent(@RequestBody PageParam<TaskSchmParam> pageParam) {
        return schduletaskService.getTaskContent(pageParam);
    }

    /**
     * 新增任务内容
     */
    @ResponseBody
    @PostMapping("saveTaskContent")
    public void saveTaskContent(@RequestBody TaskSchmModifyParam dto) {
        schduletaskService.addTaskContent(dto);
    }

    /**
     * 修改任务内容
     */
    @ResponseBody
    @PostMapping("modifyTaskContent")
    public void modifyTaskContent(@RequestBody TaskSchmModifyParam dto, @ApiIgnore LoginUser loginUser) {
        schduletaskService.modifyTaskContent(dto,loginUser);
    }

    /**
     * 删除任务内容
     */
    @ResponseBody
    @PostMapping("deleteTaskContent")
    public void deleteTaskContent(@RequestBody TaskSchmModifyParam dto, @ApiIgnore LoginUser loginUser) {
        schduletaskService.delTaskContent(dto,loginUser);
    }

    /**
     * 获取指定目录下 kettle 文件名称及路径
     */
    @ResponseBody
    @PostMapping("kettleFilesList")
    public List<KettleFileDto> kettleFilesList() {
        return schduletaskService.kettleFilesList();
    }

}
