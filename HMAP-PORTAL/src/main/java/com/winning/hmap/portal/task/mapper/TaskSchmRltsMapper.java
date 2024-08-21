package com.winning.hmap.portal.task.mapper;

import com.winning.hmap.portal.task.dto.req.put.SchmParam;
import com.winning.hmap.portal.task.dto.req.put.TaskSchmModifyParam;
import com.winning.hmap.portal.task.dto.req.query.TaskSchmParam;
import com.winning.hmap.portal.task.dto.resp.TaskSchm;
import com.winning.hmap.portal.task.entity.TaskSchmRlts;
import me.about.widget.mybatis.plugin.page.model.PageParam;
import me.about.widget.mybatis.plugin.page.model.PageResult;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author cpj
 */
public interface TaskSchmRltsMapper {

    int insert(TaskSchmRlts record);

    PageResult<TaskSchm> getPlanByPage(PageParam<TaskSchmParam> pageParam);

    List<TaskSchm> getByTaskId(Long id);

    TaskSchmRlts getTaskSchmRltsBySchmIdAndTaskId(TaskSchmModifyParam dto);

    int deleteByTaskId(Long TaskId);

    int enableSchm(@Param("taskId") Long taskId, @Param("schmId") Long schmId);

    int disableSchm(@Param("taskId") Long taskId, @Param("schmId") Long schmId);

    int updateByOldPlanId(TaskSchmModifyParam taskSchmModifyParam);

    int getCountByPlanId(Long planId);

    int getCountByDto(SchmParam dto);

    Integer getMaxSort(Long taskId);

}




