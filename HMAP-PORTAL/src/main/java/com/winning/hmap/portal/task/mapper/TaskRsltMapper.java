package com.winning.hmap.portal.task.mapper;

import com.winning.hmap.portal.task.entity.TaskRslt;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * @author cpj
 */
public interface TaskRsltMapper {
    Integer insertTaskRslt(@Param("taskId") Long taskId,@Param("schmId") Long schmId,@Param("exeBegntime") Date exeBegntime,
                           @Param("taskStas") String taskStas,@Param("log") String log, @Param("ticketCd") String ticket_cd,
                           @Param("dataSum") String dataSum,@Param("runBchno") String runBchno,@Param("taskName") String taskName,
                           @Param("schmName") String schmName,@Param("prrt") Integer prrt);

    int updateTaskRslt(TaskRslt dto);

    int updateTaskStas(@Param("runBchno") String runBchno, @Param("taskStas") String taskStas);
}
