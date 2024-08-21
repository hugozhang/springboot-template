package com.winning.hmap.portal.task.mapper;

import com.winning.hmap.portal.task.dto.req.put.SchmParam;
import com.winning.hmap.portal.task.dto.req.query.TaskSchmParam;
import com.winning.hmap.portal.task.entity.Schm;
import me.about.widget.mybatis.plugin.page.model.PageParam;
import me.about.widget.mybatis.plugin.page.model.PageResult;

/**
 * @author cpj
 */
public interface SchmMapper {

    Schm selectByPrimaryKey(Long id);

    PageResult<Schm> taskSchmList(PageParam<TaskSchmParam> pageParam);

    int insertSelective(SchmParam schm);

    int updateByPrimaryKey(Schm record);

    int deleteById(Long id);

    void updateProcedure(String procedureName);
}




