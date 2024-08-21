package com.winning.hmap.portal.task.controller;

import com.winning.hmap.portal.auth.dto.auth.resp.LoginUser;
import com.winning.hmap.portal.task.dto.req.query.TaskRsltParam;
import com.winning.hmap.portal.task.dto.resp.TaskRslt;
import com.winning.hmap.portal.task.service.TaskRsltService;
import io.swagger.annotations.Api;
import me.about.widget.mybatis.plugin.page.model.PageParam;
import me.about.widget.mybatis.plugin.page.model.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * @author cpj
 */
@Api(tags = "执行结果接口")
@RestController
@RequestMapping("/hmap/taskRslt/")
public class TaskRsltController {

    @Autowired
    TaskRsltService taskRsltService;

    @PostMapping("querySchmRslt")
    public PageResult<TaskRslt> querySchmRslt(@RequestBody PageParam<TaskRsltParam> planTaskDto) {
        return taskRsltService.querySchmRslt(planTaskDto);
    }

    @PostMapping("getSchmRsltDetails")
    public List<TaskRslt> getSchmRsltDetails(@RequestBody TaskRsltParam taskRsltParam) {
        return taskRsltService.querySchmRsltDetails(taskRsltParam);
    }

    @PostMapping("delSchmRslt")
    public void delSchmRslt(String runBchno) {
        taskRsltService.delSchmRslt(runBchno);
    }

    @PostMapping("rerun")
    public void taskRerun(String runBchno) {
        taskRsltService.rerunTaskSchm(runBchno);
    }

    @PostMapping("failNotice")
    public void taskFailNotice(String runBchno, @ApiIgnore LoginUser loginUser) {
        taskRsltService.taskFailNotice(runBchno,loginUser);
    }

}
