package com.winning.hmap.portal.dict.service.impl;

import com.winning.hmap.portal.dict.dto.req.put.AddLogParam;
import com.winning.hmap.portal.dict.dto.req.query.LogParam;
import com.winning.hmap.portal.dict.dto.resp.Log;
import com.winning.hmap.portal.dict.entity.SysLog;
import com.winning.hmap.portal.dict.mapper.SysLogMapper;
import com.winning.hmap.portal.dict.service.LogService;
import com.winning.hmap.portal.auth.dto.auth.resp.LoginUser;
import me.about.widget.mybatis.plugin.page.model.PageParam;
import me.about.widget.mybatis.plugin.page.model.PageResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.stream.Collectors;

@Service
public class LogServiceImpl implements LogService {

    @Resource
    private SysLogMapper sysLogMapper;

    @Override
    public void add(AddLogParam addLogParam, LoginUser loginUser) {
        SysLog sysLog = new SysLog();
        sysLog.setOpnType(addLogParam.getOpnType());
        sysLog.setIp(addLogParam.getIp());
        sysLog.setContent(addLogParam.getContent());
        sysLog.setUserId(loginUser.getUserId());
        sysLog.setUsername(loginUser.getUsername());
        sysLog.setLoginName(loginUser.getLoginName());
        sysLog.setOpnTime(new Date());
        sysLog.setCrterId(loginUser.getUserId());
        sysLog.setCrteTime(new Date());
        sysLogMapper.insert(sysLog);
    }

    @Override
    public PageResult<Log> selectByPage(PageParam<LogParam> pageParam) {

        PageResult<Log> result = new PageResult<>();

        PageResult<SysLog> sysLogPageResult = sysLogMapper.selectByPage(pageParam);

        result.setTotal(sysLogPageResult.getTotal());
        result.setTotalPage(sysLogPageResult.getTotalPage());
        result.setRows(sysLogPageResult.getRows().stream().map(sysLog -> {
            Log log = new Log();
            log.setLogId(sysLog.getId());
            log.setIp(sysLog.getIp());
            log.setOpnTime(sysLog.getOpnTime());
            log.setContent(sysLog.getContent());
            log.setLoginName(sysLog.getLoginName());
            log.setOpnType(sysLog.getOpnType());
            log.setUserId(sysLog.getUserId());
            log.setUsername(sysLog.getUsername());
            log.setLoginName(sysLog.getLoginName());
            log.setIp(sysLog.getIp());
            return log;
        }).collect(Collectors.toList()));

        return result;
    }
}
