package com.winning.hmap.portal.auth.mapper;


import com.winning.hmap.portal.auth.dto.auth.req.query.DoctorParam;
import com.winning.hmap.portal.auth.dto.auth.req.query.DoctorMedGrpParam;
import com.winning.hmap.portal.auth.dto.auth.resp.Doctor;
import com.winning.hmap.portal.auth.dto.auth.resp.DoctorItem;
import com.winning.hmap.portal.auth.entity.SysDoctor;
import com.winning.hmap.portal.auth.entity.SysMedinsDeptDoctor;
import me.about.widget.mybatis.plugin.page.model.PageParam;
import me.about.widget.mybatis.plugin.page.model.PageResult;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysDoctorMapper {

    SysDoctor getDoctorById(Long drId);

    List<SysDoctor> getDoctorByName(String drName);

    List<SysDoctor> getDoctorByDrId(String drCode);

    SysDoctor getDoctorByCode(String drCode);

    void insert(SysDoctor sysDoctor);

    void updateByPrimaryKey(SysDoctor sysDoctor);

    List<Doctor> findDoctorList(DoctorParam pageParam);

    PageResult<Doctor> selectByPage(PageParam<DoctorParam> pageParam);

    List<SysDoctor>  queryUserNotexit();

    List<DoctorItem> options(DoctorParam doctorParam);

    List<SysDoctor> queryAllDoctor();

    int updateDoctorDtos(List<SysDoctor> list);

    int deleteUserDeptByList(List<SysDoctor> list);

    int deleteMedicalTeams(List<Long> userIds);

    int bindingDoctorDept(@Param("list") List<SysMedinsDeptDoctor> list, @Param("drId") Long drId);

    int bindingDoctorMedTeam(@Param("list") List<DoctorMedGrpParam> list, @Param("drId") Long drId);

    void insertDoctorList(List<SysDoctor> list);

    void disable(Long id);

    void enable(Long id);



}