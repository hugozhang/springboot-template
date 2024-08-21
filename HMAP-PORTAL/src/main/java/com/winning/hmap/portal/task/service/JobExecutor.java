package com.winning.hmap.portal.task.service;

import com.winning.hmap.portal.task.dto.TimerDto;

public interface JobExecutor {

    void execute(TimerDto timerDto);

}
