package com.winning.hmap.portal.auth.service;

import com.winning.hmap.portal.auth.dto.auth.req.query.DoctorInfoDto;
import com.winning.hmap.portal.auth.dto.auth.resp.*;
import com.winning.hmap.portal.auth.dto.auth.req.put.AddMedinsDeptParam;
import com.winning.hmap.portal.auth.dto.auth.req.put.UpdateMedinsDeptParam;
import com.winning.hmap.portal.auth.dto.auth.req.query.MedinsDeptParam;
import com.winning.hmap.portal.auth.entity.SysMedGrp;
import com.winning.hmap.portal.auth.entity.SysMedinsDept;
import me.about.widget.mybatis.plugin.page.model.PageParam;
import me.about.widget.mybatis.plugin.page.model.PageResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MedinsDeptService {

    /**
     * 根据id查询科室
     * @param deptId
     * @return
     */
    SimpleMedinsDept getSimpleMedinsDeptById(Long deptId);

    /**
     * 根据id查询科室
     * @param deptId
     * @return
     */
    MedinsDept getMedinsDeptById(Long deptId);

    /**
     * 根据deptCode查询科室
     * @param deptCode
     * @return
     */
    MedinsDept getMedinsDeptByCode(String deptCode);

    /**
     * 添加科室
     * @param addMedinsDeptParam
     */
    void add(AddMedinsDeptParam addMedinsDeptParam,LoginUser loginUser);

    /**
     * 更新科室
     * @param updateMedinsDeptParam
     */
    void update(UpdateMedinsDeptParam updateMedinsDeptParam, LoginUser loginUser);

    /**
     * 停用科室
     * @param deptId
     */
    void disable(Long deptId,LoginUser loginUser);

    /**
     * 启用科室
     * @param deptId
     */
    void enable(Long deptId,LoginUser loginUser);

    /**
     * 科室分页列表
     * @param pageParam
     * @return
     */
    PageResult<MedinsDept> selectByPage(PageParam<MedinsDeptParam> pageParam);

    /**
     * 查询科室下拉
     * @param medinsDeptParam
     * @return
     */
    List<MedinsDeptLevel> findMedinsDeptCascadeList(MedinsDeptParam medinsDeptParam);

    List<SysMedGrp> queryMedGrpByDeptCodes(DoctorInfoDto doctorInfoDto);


    /**
     * 查询有效的机构代码集合
     *
     * @return
     */
    List<SysMedinsDept> medinsDeptIdList();

    String uploadExcel(MultipartFile file, LoginUser loginUser) throws IOException;

    /**
     * 查询科室
     * @param sysMedinsDept
     * @return
     */
    List<SysMedinsDept> queryDeptList(SysMedinsDept sysMedinsDept);

}
