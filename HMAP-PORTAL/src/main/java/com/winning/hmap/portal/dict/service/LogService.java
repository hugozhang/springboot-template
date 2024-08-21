package com.winning.hmap.portal.dict.service;

import com.winning.hmap.portal.dict.dto.req.put.AddLogParam;
import com.winning.hmap.portal.dict.dto.req.query.LogParam;
import com.winning.hmap.portal.dict.dto.resp.Log;
import com.winning.hmap.portal.auth.dto.auth.resp.LoginUser;
import me.about.widget.mybatis.plugin.page.model.PageParam;
import me.about.widget.mybatis.plugin.page.model.PageResult;

public interface LogService {

    void add(AddLogParam addLogParam, LoginUser loginUser);

    PageResult<Log> selectByPage(PageParam<LogParam> pageParam);

}
