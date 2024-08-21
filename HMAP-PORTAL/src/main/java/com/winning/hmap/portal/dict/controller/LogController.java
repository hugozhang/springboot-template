package com.winning.hmap.portal.dict.controller;


import com.winning.hmap.portal.dict.dto.req.put.AddLogParam;
import com.winning.hmap.portal.dict.dto.req.query.LogParam;
import com.winning.hmap.portal.dict.dto.resp.Log;
import com.winning.hmap.portal.dict.service.LogService;
import com.winning.hmap.portal.auth.dto.auth.resp.LoginUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.about.widget.mybatis.plugin.page.model.PageParam;
import me.about.widget.mybatis.plugin.page.model.PageResult;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.validation.Valid;

@Api(tags = "日志接口")
@RestController
@RequestMapping("/hmap/log")
public class LogController {

    @Resource
    private LogService logService;

    @ApiOperation(value = "添加日志")
    @PutMapping("/add")
    public void add(@RequestBody @Valid AddLogParam addLogParam, @ApiIgnore LoginUser loginUser) {
//        logService.add(addLogParam,loginUser);
    }

    @ApiOperation(value = "日志列表")
    @PostMapping("/list")
    public PageResult<Log> findConfigByParentId(@RequestBody PageParam<LogParam> pageParam) {
        return logService.selectByPage(pageParam);
    }

}
