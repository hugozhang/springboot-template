package com.winning.hmap.portal.task.mapper;

import com.winning.hmap.portal.task.dto.resp.TaskNotice;
import com.winning.hmap.portal.task.entity.Task;
import com.winning.hmap.portal.task.dto.req.query.TaskSchmParam;
import me.about.widget.mybatis.plugin.page.model.PageParam;
import me.about.widget.mybatis.plugin.page.model.PageResult;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author cpj
 */
public interface TaskMapper {

    int insert(Task record);

    int insertSelective(Task task);

    Task selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Task record);

    int updateByPrimaryKey(Task record);

    int deleteById(Long id);

    String getJobNameById(Long id);

    List<Task> list(@Param("taskName") String taskName);

    PageResult<Task> selectTaskByPage(PageParam<TaskSchmParam> pageParam);

    List<Map<String, Object>> getFileInfo(String sjid);

}




