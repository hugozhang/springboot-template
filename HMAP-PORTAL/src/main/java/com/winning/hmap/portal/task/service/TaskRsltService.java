package com.winning.hmap.portal.task.service;

import com.winning.hmap.portal.auth.dto.auth.resp.LoginUser;
import com.winning.hmap.portal.task.dto.req.query.TaskRsltParam;
import com.winning.hmap.portal.task.dto.resp.TaskRslt;
import me.about.widget.mybatis.plugin.page.model.PageParam;
import me.about.widget.mybatis.plugin.page.model.PageResult;

import java.util.List;

public interface TaskRsltService {

    PageResult<TaskRslt> querySchmRslt(PageParam<TaskRsltParam> pageParam);

    List<TaskRslt> querySchmRsltDetails(TaskRsltParam taskRsltParam);

    void delSchmRslt(String runBchno);

    void rerunTaskSchm(String runBchno);

    void taskFailNotice(String runBchno, LoginUser loginUser);
}
