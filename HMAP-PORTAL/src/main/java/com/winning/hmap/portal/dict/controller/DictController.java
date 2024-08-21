package com.winning.hmap.portal.dict.controller;


import com.winning.hmap.portal.dict.dto.req.DicReq;
import com.winning.hmap.portal.dict.dto.req.put.AddDictParam;
import com.winning.hmap.portal.dict.dto.req.put.UpdateDictParam;
import com.winning.hmap.portal.dict.dto.req.query.DictParam;
import com.winning.hmap.portal.dict.dto.resp.DicResp;
import com.winning.hmap.portal.dict.dto.resp.Dict;
import com.winning.hmap.portal.dict.entity.SysDictLog;
import com.winning.hmap.portal.dict.entity.SysDict;
import com.winning.hmap.portal.dict.service.DictService;
import com.winning.hmap.portal.auth.dto.auth.resp.LoginUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.about.widget.mybatis.plugin.page.model.PageParam;
import me.about.widget.mybatis.plugin.page.model.PageResult;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Api(tags = "字典配置接口")
@RestController
@RequestMapping("/hmap/dict")
public class DictController {

    @Resource
    private DictService dictService;

    @ApiOperation(value = "添加配置")
    @PutMapping("/add")
    public void add(@RequestBody @Valid AddDictParam addDictParam, @ApiIgnore LoginUser loginUser) {
        dictService.add(addDictParam,loginUser);
    }

    @ApiOperation(value = "更新配置")
    @PutMapping("/{dictId}/update")
    public void update(@RequestBody @Valid UpdateDictParam updateDictParam, @ApiIgnore LoginUser loginUser) {
        dictService.update(updateDictParam,loginUser);
    }

    @ApiOperation(value = "启用配置")
    @PutMapping("/{dictId}/enable")
    public void enable(@PathVariable("dictId") Long dictId, @ApiIgnore LoginUser loginUser) {
        dictService.enable(dictId,loginUser);
    }

    @ApiOperation(value = "禁用配置")
    @PutMapping("/{dictId}/disable")
    public void disable(@PathVariable("dictId") Long dictId, @ApiIgnore LoginUser loginUser) {
        dictService.disable(dictId,loginUser);
    }

    @ApiOperation(value = "批量启用配置")
    @PutMapping("/enable")
    public void enableByIds(@RequestBody List<Long> dictIds, @ApiIgnore LoginUser loginUser) {
        dictService.enable(dictIds,loginUser);
    }

    @ApiOperation(value = "批量禁用配置")
    @PutMapping("/disable")
    public void disableByIds(@RequestBody List<Long> dictIds, @ApiIgnore LoginUser loginUser) {
        dictService.disable(dictIds,loginUser);
    }

    @ApiOperation(value = "值域列表")
    @PostMapping("/{parentId}/children")
    public PageResult<Dict> findDictByParentId(@RequestBody PageParam<DictParam> pageParam) {
        return dictService.findDictByParentId(pageParam);
    }

    @ApiOperation(value = "目录列表")
    @PostMapping("/dir")
    public List<Dict> findDirByParentId(@RequestBody DictParam dictParam) {
        return dictService.findDirList(dictParam);
    }

    @ApiOperation(value = "目录列表")
    @PostMapping("/list")
    public Map<String,List<SysDict>> findDictList() {
        return dictService.findList();
    }

    @ApiOperation(value = "按父节点code 查询值域列表")
    @PostMapping("/{dictCode}/dir")
    public List<Dict> getDictListByParentCode(@PathVariable("dictCode") String dictCode) {
        return dictService.getDictListByParentDictCode(dictCode);
    }

    @PostMapping("/dict_query_log")
    public PageResult<SysDictLog> queryLogDictionaries(@RequestBody PageParam<SysDictLog> pageParam) {
        return dictService.queryLogDictionaries(pageParam);
    }


    @ApiOperation(value = "风控字典同步")
    @RequestMapping("/query")
    public List<DicResp> syncDic(@RequestBody DicReq dicReq) {
        return dictService.syncDic(dicReq);
    }

}
