package com.winning.hmap.portal.auth.mapper;

import com.winning.hmap.portal.auth.entity.SysMedinsDeptDoctor;
import com.winning.hmap.portal.auth.dto.auth.resp.DoctorDept;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysMedinsDeptDoctorMapper {

    void insertBatch(List<SysMedinsDeptDoctor> rows);

    void deleteByDoctorId(Long drId);

    List<DoctorDept> findDeptListByDoctorId(Long drId);

    String queryDeptNameByUserId(@Param("userId") Long userId);

}