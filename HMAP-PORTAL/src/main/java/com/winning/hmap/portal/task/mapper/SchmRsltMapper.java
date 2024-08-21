package com.winning.hmap.portal.task.mapper;

import com.winning.hmap.portal.task.dto.req.query.TaskRsltParam;
import com.winning.hmap.portal.task.dto.resp.TaskRslt;
import com.winning.hmap.portal.task.dto.resp.TaskSchm;
import me.about.widget.mybatis.plugin.page.model.PageParam;
import me.about.widget.mybatis.plugin.page.model.PageResult;

import java.util.List;

/**
 * @author cpj
 */
public interface SchmRsltMapper {

    //TODO sql优化： SQL 太过复杂
    PageResult<TaskRslt> querySchmRslt(PageParam<TaskRsltParam> pageParam);

    //TODO sql优化： SQL 太过复杂
    List<TaskRslt> querySchmDetails(TaskRsltParam taskRsltParam);

    List<TaskSchm> queryTaskSchmByRunBchno(String runBchno);

}
