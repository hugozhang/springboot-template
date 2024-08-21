package com.winning.hmap.portal.dict.service;

import com.github.pagehelper.Page;
import com.winning.hmap.portal.dict.dto.req.DicReq;
import com.winning.hmap.portal.dict.dto.req.put.AddDictParam;
import com.winning.hmap.portal.dict.dto.req.put.UpdateDictParam;
import com.winning.hmap.portal.dict.dto.req.query.DictParam;
import com.winning.hmap.portal.dict.dto.resp.DicResp;
import com.winning.hmap.portal.dict.dto.resp.Dict;
import com.winning.hmap.portal.auth.dto.auth.resp.LoginUser;
import com.winning.hmap.portal.dict.entity.SysDictLog;
import com.winning.hmap.portal.dict.entity.SysDict;
import me.about.widget.mybatis.plugin.page.model.PageParam;
import me.about.widget.mybatis.plugin.page.model.PageResult;

import java.util.List;
import java.util.Map;

public interface DictService {

    /**
     * 添加配置
     * @param addDictParam
     * @param loginUser
     */
    void add(AddDictParam addDictParam, LoginUser loginUser);

    /**
     * 修改配置
     * @param updateDictParam
     * @param loginUser
     */
    void update(UpdateDictParam updateDictParam, LoginUser loginUser);

    /**
     * 启用配置
     * @param dictId
     * @param loginUser
     */
    void enable(Long dictId,LoginUser loginUser);


    /**
     * 禁用配置
     * @param dictId
     * @param loginUser
     */
    void disable(Long dictId,LoginUser loginUser);

    /**
     * 启用配置
     * @param dictIds
     * @param loginUser
     */
    void disable(List<Long> dictIds,LoginUser loginUser);


    /**
     * 禁用配置
     * @param dictIds
     * @param loginUser
     */
    void enable(List<Long> dictIds,LoginUser loginUser);


    /**
     * 查询配置
     * @param pageParam
     * @return
     */
    PageResult<Dict> findDictByParentId(PageParam<DictParam> pageParam);

    /**
     * 查询所有目录
     * @param dictParam
     * @return
     */
    List<Dict> findDirList(DictParam dictParam);

    Map<String,List<SysDict>> findList();

    /**
     * 根据code查询配置
     * @param dictCode
     * @return
     */
    Dict getDictByCode(String dictCode);

    /**
     * 根据父级code查询配置
     * @param parentConfigCode
     * @return
     */
    List<Dict> getDictListByParentDictCode(String parentConfigCode);

    /**
     * 根据父级code,字典值查名字
     * @param parentDictCode
     * @param dictValue
     * @return
     */
    String getDictNameByParentDictCode(String parentDictCode, String dictValue);

    PageResult<SysDictLog> queryLogDictionaries(PageParam<SysDictLog> param);


    /**
     * 风控字典同步
     * 根据父级code查询配置
     * @param dicReq
     * @return
     */
    List<DicResp> syncDic(DicReq dicReq);
}
