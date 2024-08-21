package com.winning.hmap.portal.auth.mapper;

import com.winning.hmap.portal.auth.dto.auth.req.query.MedGrpLogParam;
import com.winning.hmap.portal.auth.entity.SysMedGrpLog;
import me.about.widget.mybatis.plugin.page.model.PageParam;
import me.about.widget.mybatis.plugin.page.model.PageResult;

public interface MedicalTeamLogMapper {

    int addSysMedGrpLog(SysMedGrpLog sysMedGrpLog);

    PageResult<SysMedGrpLog> queryLog(PageParam<MedGrpLogParam> pageParam);
}
