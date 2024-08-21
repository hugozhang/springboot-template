package com.winning.hmap.portal.auth.service.impl;

import com.winning.hmap.portal.auth.dto.auth.req.query.MedGrpLogParam;
import com.winning.hmap.portal.auth.entity.SysMedGrpLog;
import com.winning.hmap.portal.auth.mapper.MedicalTeamLogMapper;
import com.winning.hmap.portal.auth.service.*;
import me.about.widget.mybatis.plugin.page.model.PageParam;
import me.about.widget.mybatis.plugin.page.model.PageResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MedicalTeamLogServiceImpl implements MedicalTeamLogService {

    private static final Logger logger = LoggerFactory.getLogger(MedicalTeamLogServiceImpl.class);

    @Autowired
    private MedicalTeamLogMapper medicalTeamLogMapper;

    @Override
    public PageResult<SysMedGrpLog> queryLog(PageParam<MedGrpLogParam> pageParam) {
        return medicalTeamLogMapper.queryLog(pageParam);
    }
}
