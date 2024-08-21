package com.winning.hmap.portal.auth.controller;

import com.winning.hmap.portal.auth.dto.auth.req.query.MedGrpLogParam;
import com.winning.hmap.portal.auth.entity.SysMedGrpLog;
import com.winning.hmap.portal.auth.service.MedicalTeamLogService;
import com.winning.hmap.portal.dict.aop.WriteLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.about.widget.mybatis.plugin.page.model.PageParam;
import me.about.widget.mybatis.plugin.page.model.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author cpj
 * @date 2024/4/10 11:30
 * @desciption: 医疗组日志
 */
@Api(tags = "医疗组日志接口")
@RestController
@RequestMapping("/hmap/sysMedGrp/")
public class MedicalTeamLogController {

    @Autowired
    private MedicalTeamLogService medicalTeamLogService;

    /**
     * 查询医疗组日志
     */
    @ApiOperation(value = "查询医疗组日志")
    @WriteLog(type = "6", description = "查询医疗组日志")
    @PostMapping("queryLog")
    public PageResult<SysMedGrpLog> queryLog(@RequestBody @Valid PageParam<MedGrpLogParam> pageParam) {
        return medicalTeamLogService.queryLog(pageParam);
    }
}
