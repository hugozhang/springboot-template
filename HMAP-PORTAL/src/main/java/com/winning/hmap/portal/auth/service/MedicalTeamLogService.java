package com.winning.hmap.portal.auth.service;

import com.winning.hmap.portal.auth.dto.auth.req.query.MedGrpLogParam;
import com.winning.hmap.portal.auth.entity.SysMedGrpLog;
import me.about.widget.mybatis.plugin.page.model.PageParam;
import me.about.widget.mybatis.plugin.page.model.PageResult;

public interface MedicalTeamLogService {

    PageResult<SysMedGrpLog> queryLog(PageParam<MedGrpLogParam> pageParam);

}
