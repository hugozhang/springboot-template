package com.winning.hmap.portal.auth.mapper;

import com.winning.hmap.portal.auth.dto.auth.req.query.MedGrpParam;
import com.winning.hmap.portal.auth.dto.auth.req.query.SysMedGrpParam;
import com.winning.hmap.portal.auth.entity.SysDoctorMedGrp;
import com.winning.hmap.portal.auth.entity.SysMedGrp;
import me.about.widget.mybatis.plugin.page.model.PageParam;
import me.about.widget.mybatis.plugin.page.model.PageResult;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MedicalTeamMapper {

    PageResult<SysMedGrp> query(PageParam<MedGrpParam> pageParam);

    int insert(SysMedGrp sysMedGrp);

    int insertSysMedGrps(List<SysMedGrpParam> list);

    int deleteById(String id);

    SysMedGrp getById(Long id);

    int getCountByGroupCode(SysMedGrp sysMedGrp);

    List<SysMedGrp> selectListByGroupCode(String groupCode);

    List<String> queryGroupCodeList();

    int updateSysMedGrp(SysMedGrp sysMedGrp);

    int updateSysMedGrps(List<SysMedGrpParam> list);

    int updateMedicalById(SysMedGrp sysMedGrp);

    List<Long> findSysMedGrpByYsId(List<String> list);

    Integer queryUsedCountByDeptCode(SysMedGrp sysMedGrp);

    List<SysMedGrp> queryGrpByDrId(@Param("drId") Long drId);

    void deleteSysMedGrpsByDeId(@Param("drId") Long drId);

    void insertBatch(List<SysDoctorMedGrp> rows);

    List<SysMedGrp> queryGroupList();
}
