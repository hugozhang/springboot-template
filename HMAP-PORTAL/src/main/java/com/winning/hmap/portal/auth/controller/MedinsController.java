package com.winning.hmap.portal.auth.controller;


import com.winning.hmap.portal.auth.dto.auth.req.query.MedinsParam;
import com.winning.hmap.portal.auth.service.MedinsService;
import com.winning.hmap.portal.auth.dto.auth.req.put.AddMedinsParam;
import com.winning.hmap.portal.auth.dto.auth.req.put.UpdateMedinsParam;
import com.winning.hmap.portal.auth.dto.auth.resp.LoginUser;
import com.winning.hmap.portal.auth.dto.auth.resp.Medins;
import com.winning.hmap.portal.auth.dto.auth.resp.OptionItem;
import com.winning.hmap.portal.dict.aop.WriteLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.about.widget.mybatis.plugin.page.model.PageParam;
import me.about.widget.mybatis.plugin.page.model.PageResult;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@Api(tags = "机构接口")
@RestController
@RequestMapping("/hmap/medins")
public class MedinsController {

    @Resource
    private MedinsService medinsService;

    @ApiOperation(value = "停用机构")
    @WriteLog(type = "5", description = "停用机构")
    @PutMapping("/{medinsId}/disable")
    public void disable(@PathVariable("medinsId") Long medinsId, @ApiIgnore LoginUser loginUser) {
        medinsService.disable(medinsId,loginUser);
    }

    @ApiOperation(value = "启用机构")
    @WriteLog(type = "5", description = "启用机构")
    @PutMapping("/{medinsId}/enable")
    public void enable(@PathVariable("medinsId") Long medinsId, @ApiIgnore LoginUser loginUser) {
        medinsService.enable(medinsId,loginUser);
    }

    @ApiOperation(value = "添加机构")
    @WriteLog(type = "3", description = "添加机构")
    @PutMapping("/add")
    public void add(@RequestBody @Valid AddMedinsParam addMedinsParam, @ApiIgnore LoginUser loginUser) {
        medinsService.add(addMedinsParam,loginUser);
    }

    @ApiOperation(value = "更新机构")
    @WriteLog(type = "5", description = "更新机构")
    @PutMapping("/{medinsId}/update")
    public void update(@RequestBody @Valid UpdateMedinsParam updateMedinsParam, @ApiIgnore LoginUser loginUser) {
        medinsService.update(updateMedinsParam,loginUser);
    }

    @ApiOperation(value = "机构列表")
    @WriteLog(type = "6", description = "机构列表")
    @PostMapping("/list")
    public PageResult<Medins> selectByPage(@RequestBody @Valid PageParam<MedinsParam> pageParam) {
        return medinsService.selectByPage(pageParam);
    }

    @ApiOperation(value = "机构下拉选择")
    @PostMapping("/options")
    public List<OptionItem> options(@RequestBody @Valid MedinsParam medinsParam) {
        return medinsService.options(medinsParam);
    }

}
