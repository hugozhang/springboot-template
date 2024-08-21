package com.winning.hmap.portal.auth.mapper;


import com.winning.hmap.portal.auth.dto.auth.req.query.MedinsParam;
import com.winning.hmap.portal.auth.dto.auth.resp.OptionItem;
import com.winning.hmap.portal.auth.entity.SysMedins;
import com.winning.hmap.portal.auth.dto.auth.resp.Medins;
import me.about.widget.mybatis.plugin.page.model.PageParam;
import me.about.widget.mybatis.plugin.page.model.PageResult;

import java.util.List;

public interface SysMedinsMapper {

    SysMedins getMedinsById(Long medinsId);

    SysMedins getMedinsByCode(String medinsNo);

    void insert(SysMedins sysMedins);

    void updateByPrimaryKey(SysMedins sysMedins);

    void disable(Long id);

    void enable(Long id);

    PageResult<Medins> selectByPage(PageParam<MedinsParam> pageParam);

    List<OptionItem> options(MedinsParam medinsParam);

    List<String> medinsIdList();

}