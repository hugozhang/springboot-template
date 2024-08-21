package com.winning.hmap.portal.dict.mapper;

import com.winning.hmap.portal.dict.dto.req.query.LogParam;
import com.winning.hmap.portal.dict.entity.SysLog;
import me.about.widget.mybatis.plugin.page.model.PageParam;
import me.about.widget.mybatis.plugin.page.model.PageResult;
import me.about.widget.mybatis.plugin.showsql.IgnoreShowSql;

public interface SysLogMapper {

    @IgnoreShowSql
    void insert(SysLog sysLog);

    PageResult<SysLog> selectByPage(PageParam<LogParam> pageParam);

}