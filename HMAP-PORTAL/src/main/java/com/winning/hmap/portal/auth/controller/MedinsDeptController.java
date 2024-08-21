package com.winning.hmap.portal.auth.controller;

import com.winning.hmap.portal.auth.dto.auth.req.query.DoctorInfoDto;
import com.winning.hmap.portal.auth.dto.auth.req.query.MedinsDeptParam;
import com.winning.hmap.portal.auth.dto.auth.resp.MedinsDept;
import com.winning.hmap.portal.auth.dto.auth.resp.MedinsDeptLevel;
import com.winning.hmap.portal.auth.entity.SysMedGrp;
import com.winning.hmap.portal.auth.entity.SysMedinsDept;
import com.winning.hmap.portal.auth.service.MedinsDeptService;
import com.winning.hmap.portal.auth.dto.auth.req.put.AddMedinsDeptParam;
import com.winning.hmap.portal.auth.dto.auth.req.put.UpdateMedinsDeptParam;
import com.winning.hmap.portal.auth.dto.auth.resp.LoginUser;
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

@Api(tags = "科室接口")
@RestController
@RequestMapping("/hmap/medins-dept")
public class MedinsDeptController {

    @Resource
    private MedinsDeptService medinsDeptService;

    @ApiOperation(value = "停用科室")
    @WriteLog(type = "5", description = "停用科室")
    @PutMapping("/{medinsDeptId}/disable")
    public void disable(@PathVariable("medinsDeptId") Long medinsDeptId, @ApiIgnore LoginUser loginUser) {
        medinsDeptService.disable(medinsDeptId,loginUser);
    }

    @ApiOperation(value = "启用科室")
    @WriteLog(type = "5", description = "启用科室")
    @PutMapping("/{medinsDeptId}/enable")
    public void enable(@PathVariable("medinsDeptId") Long medinsDeptId, @ApiIgnore LoginUser loginUser) {
        medinsDeptService.enable(medinsDeptId,loginUser);
    }

    @ApiOperation(value = "添加科室")
    @WriteLog(type = "3", description = "添加科室")
    @PutMapping("/add")
    public void add(@RequestBody @Valid AddMedinsDeptParam addMedinsDeptParam, @ApiIgnore LoginUser loginUser) {
        medinsDeptService.add(addMedinsDeptParam,loginUser);
    }

    @ApiOperation(value = "更新科室")
    @WriteLog(type = "5", description = "更新科室")
    @PutMapping("/{deptId}/update")
    public void update(@RequestBody @Valid UpdateMedinsDeptParam updateMedinsDeptParam, @ApiIgnore LoginUser loginUser) {
        medinsDeptService.update(updateMedinsDeptParam,loginUser);
    }

    @ApiOperation(value = "科室列表")
    @WriteLog(type = "6", description = "科室列表")
    @PostMapping("/list")
    public PageResult<MedinsDept> selectByPage(@RequestBody @Valid PageParam<MedinsDeptParam> pageParam) {
        return medinsDeptService.selectByPage(pageParam);
    }

    @ApiOperation(value = "科室下拉选择")
    @PostMapping("/cascade/options")
    public List<MedinsDeptLevel> options(@RequestBody @Valid MedinsDeptParam medinsDeptParam) {
        return medinsDeptService.findMedinsDeptCascadeList(medinsDeptParam);
    }

    @ApiOperation(value = "科室信息")
    @PostMapping("/{deptId}/info")
    public MedinsDept getMedinsDeptById(@PathVariable("deptId")Long deptId) {
        return medinsDeptService.getMedinsDeptById(deptId);
    }

    /**
     * @return
     */
    @ApiOperation(value = "科室医疗组")
    @WriteLog(type = "6", description = "科室医疗组")
    @PostMapping("/queryMedGrpByDeptCodes")
    public List<SysMedGrp> queryMedGrpByDeptCodes(@RequestBody DoctorInfoDto doctorInfoDto) {
        return medinsDeptService.queryMedGrpByDeptCodes(doctorInfoDto);
    }

    /**
     * 科室查询
     * @param sysMedinsDept
     * @return
     */
    @ApiOperation(value = "科室列表")
    @WriteLog(type = "6", description = "科室列表")
    @PostMapping("/queryDeptList")
    public List<SysMedinsDept> queryDeptList(@RequestBody SysMedinsDept sysMedinsDept){
        return medinsDeptService.queryDeptList(sysMedinsDept);
    }

    /**
     * 批量导入
     *
     * @param file
     * @return
     */
    @ApiOperation(value = "科室批量导入")
    @WriteLog(type = "7", description = "科室批量导入")
    @PostMapping( "/uploadExcel")
    public String uploadFile(@RequestParam MultipartFile file, @ApiIgnore LoginUser loginUser) throws IOException {
        return medinsDeptService.uploadExcel(file,loginUser);
    }

    /**
     * @param response
     * @description: 下载模板接口
     * @author: cpj
     * @date: 2024/3/05 17:37
     */
    @ApiOperation(value = "科室导入模板下载")
    @PostMapping("/dowload")
    public void getDeptMb(HttpServletRequest request,HttpServletResponse response) throws IOException {
        ApplicationHome ah = new ApplicationHome(getClass());
        String filePath = ah.getSource().getParentFile().getAbsolutePath();
        File downFiles = new File( filePath + File.separator + "exportmb/科室导入列表模版.xlsx");
        UpDownloadUtils.toDownload(request, response, downFiles, downFiles.getName());
    }

}
