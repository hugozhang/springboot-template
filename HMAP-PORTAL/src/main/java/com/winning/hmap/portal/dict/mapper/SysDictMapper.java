package com.winning.hmap.portal.dict.mapper;

import com.winning.hmap.portal.dict.dto.req.query.DictParam;
import com.winning.hmap.portal.dict.entity.SysDictLog;
import com.winning.hmap.portal.dict.entity.SysDict;
import me.about.widget.mybatis.plugin.page.model.PageParam;
import me.about.widget.mybatis.plugin.page.model.PageResult;

import java.util.List;


public interface SysDictMapper {

    void insert(SysDict record);

    void updateByPrimaryKey(SysDict record);

    void disable(Long id);

    void enable(Long id);

    void disableByIds(List<Long> ids);

    void enableByIds(List<Long> ids);

    PageResult<SysDict> selectByPage(PageParam<DictParam> pageParam);

    List<SysDict> findDirList(DictParam dictParam);

    SysDict getDictByCode(String dictCode);

    SysDict getDictById(Long id);

    List<SysDict> getDictListByParentDictCode(String parentDictCode);

    List<SysDict> getDictList();

    int insertOperationLog(SysDictLog sysDictLog);

    PageResult<SysDictLog> queryDictLogByPage(PageParam<SysDictLog> pageParam);

}