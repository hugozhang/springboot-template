package com.winning.hmap.portal.auth.controller;

import com.winning.hmap.portal.auth.dto.auth.req.query.BindingMedinsDeptParam;
import com.winning.hmap.portal.auth.dto.auth.req.put.AddDoctorParam;
import com.winning.hmap.portal.auth.dto.auth.req.put.UpdateDoctorParam;
import com.winning.hmap.portal.auth.dto.auth.resp.Doctor;
import com.winning.hmap.portal.auth.dto.auth.req.query.DoctorParam;
import com.winning.hmap.portal.auth.dto.auth.resp.DoctorItem;
import com.winning.hmap.portal.auth.dto.auth.resp.LoginUser;
import com.winning.hmap.portal.auth.entity.SysMedGrp;
import com.winning.hmap.portal.auth.service.DoctorService;
import com.winning.hmap.portal.dict.aop.WriteLog;
import com.winning.hmap.portal.util.UpDownloadUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.about.widget.mybatis.plugin.page.model.PageParam;
import me.about.widget.mybatis.plugin.page.model.PageResult;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Api(tags = "医生接口")
@RestController
@RequestMapping("/hmap/doctor")
public class DoctorController {

    @Resource
    private DoctorService doctorService;

    @ApiOperation(value = "添加医生")
    @WriteLog(type = "3", description = "添加医生")
    @PutMapping("/add")
    public void add(@RequestBody @Valid AddDoctorParam addDoctorParam, @ApiIgnore LoginUser loginUser) {
        doctorService.add(addDoctorParam,loginUser);
    }

    @ApiOperation(value = "更新医生")
    @WriteLog(type = "5", description = "添加医生")
    @PutMapping("/{drId}/update")
    public void update(@RequestBody @Valid UpdateDoctorParam updateDoctorParam, @ApiIgnore LoginUser loginUser) {
        doctorService.update(updateDoctorParam,loginUser);
    }

    @ApiOperation(value = "医生信息")
    @PostMapping("/{drId}/info")
    public Doctor getDoctorById(@PathVariable("drId")Long drId) {
        return doctorService.getDoctorById(drId);
    }

    @ApiOperation(value = "医生信息")
    @PostMapping("/drName")
    public List<Doctor> getDoctorById(@RequestBody Doctor doctor) {
        return doctorService.getDoctorByName(doctor);
    }

    @ApiOperation(value = "医生分页列表")
    @PostMapping("/list")
    @WriteLog(type = "6", description = "查看医生列表")
    public PageResult<Doctor> selectByPage(@RequestBody @Valid PageParam<DoctorParam> pageParam) {
        return doctorService.selectByPage(pageParam);
    }

//    @ApiOperation(value = "导出excel")
//    @PostMapping("/excel/export")
//    @XlsxView(fileName = "医生信息维护",inputClass = DoctorExcel.class)
//    public List<DoctorExcel> findDoctorList(@RequestBody DoctorParam doctorParam) {
//        return doctorService.findDoctorList(doctorParam);
//    }

    @ApiOperation(value = "医生绑定科室")
    @PutMapping("/{drId}/binding/dept")
    public void bindingMedinsDept(@RequestBody @Valid BindingMedinsDeptParam bindingMedinsDeptParam, @ApiIgnore LoginUser loginUser) {
        doctorService.bindingMedinsDept(bindingMedinsDeptParam,loginUser);
    }

    @ApiOperation(value = "医生绑定医疗组")
    @PostMapping("/binding/grp")
    public List<SysMedGrp> bindingMedinsGrp(@RequestBody Doctor doctor) {
        return doctorService.getMedGrpList(doctor.getDrId());
    }

    /**
     * 批量导入
     * @param file
     * @return
     */
    @ApiOperation(value = "医生批量导入")
    @WriteLog(type = "7", description = "医生批量导入")
    @PostMapping("/uploadExcel")
    public String uploadFile(@RequestParam MultipartFile file, @ApiIgnore LoginUser loginUser) throws Exception {
       return doctorService.uploadExcel(file,loginUser);
    }

    /**
     * @param response
     * @description: 下载模板接口
     * @author: cpj
     * @date: 2024/3/05 17:37
     */
    @ApiOperation(value = "医生导入模板下载")
    @PostMapping("/dowload")
    public void getDoctorMb(HttpServletRequest request, HttpServletResponse response){
        ApplicationHome ah = new ApplicationHome(getClass());
        String filePath = ah.getSource().getParentFile().getAbsolutePath();
        File downFiles = new File( filePath + File.separator + "exportmb/医生信息导入模版.xlsx");
        try {
            UpDownloadUtils.toDownload(request, response, downFiles, downFiles.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询科室下医生选择项
     *
     * @param doctorParam
     * @return
     */
    @ApiOperation(value = "查询科室下医生")
    @PostMapping("findDoctorList")
    public List<DoctorItem> findDeptDrOptions(@RequestBody DoctorParam doctorParam) {
        return doctorService.findDeptDrOptions(doctorParam);
    }

    /**
     * 查询医生选择项
     * @param doctorParam
     * @return
     */
    @ApiOperation(value = "查询医生选择项")
    @PostMapping("options")
    public List<DoctorItem> options(@RequestBody DoctorParam doctorParam) {
        return doctorService.options(doctorParam);
    }

    @ApiOperation(value = "停用医生")
    @WriteLog(type = "5", description = "停用医生")
    @PutMapping("/{drId}/disable")
    public void disable(@PathVariable("drId") Long drId) {
        doctorService.disable(drId);
    }

    @ApiOperation(value = "启用医生")
    @WriteLog(type = "5", description = "启用医生")
    @PutMapping("/{drId}/enable")
    public void enable(@PathVariable("drId") Long drId) {
        doctorService.enable(drId);
    }

}
