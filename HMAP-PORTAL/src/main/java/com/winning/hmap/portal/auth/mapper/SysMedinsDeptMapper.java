package com.winning.hmap.portal.auth.mapper;


import com.winning.hmap.portal.auth.dto.auth.req.put.UploadMedinsDept;
import com.winning.hmap.portal.auth.dto.auth.req.query.MedinsDeptParam;
import com.winning.hmap.portal.auth.entity.SysMedGrp;
import com.winning.hmap.portal.auth.entity.SysMedinsDept;
import me.about.widget.mybatis.plugin.page.model.PageParam;
import me.about.widget.mybatis.plugin.page.model.PageResult;

import java.util.List;

public interface SysMedinsDeptMapper  {

    SysMedinsDept getMedinsDeptById(Long deptId);

    SysMedinsDept getMedinsDeptByCode(String deptCode);

    void insert(SysMedinsDept sysMedinsDept);

    void updateByPrimaryKey(SysMedinsDept sysMedinsDept);

    void disable(Long id);

    void enable(Long id);

    PageResult<SysMedinsDept> selectByPage(PageParam<MedinsDeptParam> pageParam);

    List<SysMedinsDept> findMedinsDeptList(MedinsDeptParam medinsDeptParam);

    List<SysMedinsDept> medinsDeptIdList();

    void insertDeptList(List<SysMedinsDept> list);

    int updateDeptDtos(List<SysMedinsDept> list);

    int updateDepts(List<UploadMedinsDept> list);

    List<SysMedGrp> queryMedGrpByDeptCodes(List<String> list);

    List<SysMedinsDept> queryDeptList(SysMedinsDept deptDto);



}