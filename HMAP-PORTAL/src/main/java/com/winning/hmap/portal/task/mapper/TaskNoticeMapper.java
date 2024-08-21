package com.winning.hmap.portal.task.mapper;

import com.winning.hmap.portal.task.dto.resp.TaskNotice;
import org.apache.ibatis.annotations.Param;

/**
 * @author cpj
 */
public interface TaskNoticeMapper {

    TaskNotice taskNotice();

    int taskFailNotice(@Param("runBchno") String runBchno, @Param("userId") Long userId);

}




