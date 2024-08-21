package com.winning.hmap.portal.auth.service;

import com.winning.hmap.portal.auth.dto.auth.req.query.BindingMedinsDeptParam;
import com.winning.hmap.portal.auth.dto.auth.req.query.DoctorParam;
import com.winning.hmap.portal.auth.dto.auth.resp.*;
import com.winning.hmap.portal.auth.dto.auth.req.put.AddDoctorParam;
import com.winning.hmap.portal.auth.dto.auth.req.put.UpdateDoctorParam;
import com.winning.hmap.portal.auth.entity.SysMedGrp;
import me.about.widget.mybatis.plugin.page.model.PageParam;
import me.about.widget.mybatis.plugin.page.model.PageResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DoctorService {

    /**
     * 根据id获取医生
     * @param drId
     * @return
     */
    Doctor getDoctorById(Long drId);

    /**
     * 根据drCode获取医生
     * @param drCode
     * @return
     */
    Doctor getDoctorByCode(String drCode);

    List<SysMedGrp> getMedGrpList(Long drId);

    List<Doctor> getDoctorByName(Doctor doctor);

    /**
     * 根据医生id获取医生
     * @param doctorCode
     * @return
     */
    List<Doctor> getDoctorByDrId(String doctorCode);

    /**
     * 添加医生
     * @param addDoctorParam
     */
    void add(AddDoctorParam addDoctorParam, LoginUser loginUser);

    /**
     * 更新医生
     * @param updateDoctorParam
     */
    void update(UpdateDoctorParam updateDoctorParam,LoginUser loginUser);

    /**
     * 医生分页列表
     * @param pageParam
     * @return
     */
    PageResult<Doctor> selectByPage(PageParam<DoctorParam> pageParam);

    /**
     * 医生列表
     * @param doctorParam
     * @return
     */
    List<DoctorExcel> findDoctorList(DoctorParam doctorParam);

    /**
     * 绑定科室
     * @param bindingMedinsDeptParam
     */
    void bindingMedinsDept(BindingMedinsDeptParam bindingMedinsDeptParam, LoginUser loginUser);

    /**
     * 查询科室下医生选择项
     * @param doctorParam
     * @return
     */
    List<DoctorItem> findDeptDrOptions(DoctorParam doctorParam);

    /**
     * 查询医生下拉框
     * @param doctorParam
     * @return
     */
    List<DoctorItem> options(DoctorParam doctorParam);

    String uploadExcel(MultipartFile file,LoginUser loginUser) throws Exception;

    /**
     * 停用医生
     * @param drId
     */
    void disable(Long drId);

    /**
     * 启用医生
     * @param drId
     */
    void enable(Long drId);

}
