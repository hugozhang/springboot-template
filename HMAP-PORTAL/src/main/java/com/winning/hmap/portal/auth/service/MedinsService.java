package com.winning.hmap.portal.auth.service;

import com.winning.hmap.portal.auth.dto.auth.req.put.AddMedinsParam;
import com.winning.hmap.portal.auth.dto.auth.req.put.UpdateMedinsParam;
import com.winning.hmap.portal.auth.dto.auth.resp.LoginUser;
import com.winning.hmap.portal.auth.dto.auth.resp.Medins;
import com.winning.hmap.portal.auth.dto.auth.req.query.MedinsParam;
import com.winning.hmap.portal.auth.dto.auth.resp.OptionItem;
import me.about.widget.mybatis.plugin.page.model.PageParam;
import me.about.widget.mybatis.plugin.page.model.PageResult;

import java.util.List;

public interface MedinsService {

    /**
     * 根据id查询机构
     * @param medinsId
     * @return
     */
    Medins getMedinsById(Long medinsId);

    /**
     * 插入机构
     * @param addMedinsParam
     */
    void add(AddMedinsParam addMedinsParam, LoginUser loginUser);

    /**
     * 更新机构
     * @param updateMedinsParam
     */
    void update(UpdateMedinsParam updateMedinsParam, LoginUser loginUser);

    /**
     * 停用机构
     * @param medinsId
     */
    void disable(Long medinsId, LoginUser loginUser);

    /**
     * 启用机构
     * @param medinsId
     */
    void enable(Long medinsId, LoginUser loginUser);

    /**
     * 机构分页列表
     * @param pageParam
     * @return
     */
    PageResult<Medins> selectByPage(PageParam<MedinsParam> pageParam);

    /**
     * 查询机构下拉框
     * @param medinsParam
     * @return
     */
    List<OptionItem> options(MedinsParam medinsParam);

    /**
     * 查询有效的机构代码集合
     * @return
     */
    List<String> medinsIdList();

}
