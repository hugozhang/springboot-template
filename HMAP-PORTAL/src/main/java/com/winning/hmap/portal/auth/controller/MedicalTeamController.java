package com.winning.hmap.portal.auth.controller;

import com.winning.hmap.portal.auth.dto.auth.req.query.MedGrpParam;
import com.winning.hmap.portal.auth.dto.auth.resp.LoginUser;
import com.winning.hmap.portal.auth.dto.auth.resp.MedGrp;
import com.winning.hmap.portal.auth.entity.SysMedGrp;
import com.winning.hmap.portal.auth.service.MedicalTeamService;
import com.winning.hmap.portal.dict.aop.WriteLog;
import com.winning.hmap.portal.util.UpDownloadUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.about.widget.mybatis.plugin.page.model.PageParam;
import me.about.widget.mybatis.plugin.page.model.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Api(tags = "医疗组接口")
@RestController
@RequestMapping("/hmap/sysMedGrp/")
public class MedicalTeamController {
    @Autowired
    private MedicalTeamService medicalMaintainService;

    /**
     * 医疗组导入数据查询
     * @param pageParam
     * @return
     */
    @ApiOperation(value = "医疗组数据查询")
    @WriteLog(type = "6", description = "医疗组数据查询")
    @PostMapping("query")
    public PageResult<MedGrp> query(@RequestBody @Valid PageParam<MedGrpParam> pageParam) {
        return medicalMaintainService.query(pageParam);
    }

    /**
     * 批量导入医疗组
     */
    @ApiOperation(value = "医疗组批量导入")
    @WriteLog(type = "7", description = "医疗组批量导入")
    @PostMapping("uploadExcel")
    public String uploadFile(@RequestParam MultipartFile file) throws IOException {
        return medicalMaintainService.uploadExcel(file);
    }

    /**
     * 更新医疗组
     */
    @ApiOperation(value = "更新医疗组")
    @WriteLog(type = "7", description = "更新医疗组")
    @PostMapping("update")
    public void update(@RequestBody SysMedGrp sysMedGrp,@ApiIgnore LoginUser loginUser) {
        medicalMaintainService.update(sysMedGrp,loginUser);
    }

    /**
     * 删除医疗组
     */
    @ApiOperation(value = "删除医疗组")
    @WriteLog(type = "4", description = "删除医疗组")
    @PostMapping("delete")
    public void delete(@RequestBody List<String> list,@ApiIgnore LoginUser loginUser) {
        medicalMaintainService.delete(list,loginUser);
    }

    /**
     * 医疗组启用状态变更
     * @param sysMedGrp
     * @return
     */
    @ApiOperation(value = "医疗组启用状态变更")
    @WriteLog(type = "7", description = "医疗组启用状态变更")
    @PostMapping("updateStatus")
    public void updateStatus(@RequestBody SysMedGrp sysMedGrp) {
        medicalMaintainService.updateStatus(sysMedGrp);
    }

    /**
     * 下载模板接口
     * @param request
     * @param response
     * @throws IOException
     */
    @ApiOperation(value = "医疗组导入模板下载")
    @PostMapping("dowload")
    public void getMedGrpMb(HttpServletRequest request,HttpServletResponse response) throws IOException {
        ApplicationHome ah = new ApplicationHome(getClass());
        String filePath = ah.getSource().getParentFile().getAbsolutePath();
        File downFiles = new File( filePath + File.separator + "exportmb/医疗组维护模版.xlsx");
        System.err.println(downFiles.getPath());
        UpDownloadUtils.toDownload(request, response, downFiles, downFiles.getName());
    }

    /**
     * 下载导入结果接口
     * @param request
     * @param response
     */
    @ApiOperation(value = "医生组导入结果")
    @PostMapping("dowloadImportResult")
    public void getImportResult(HttpServletRequest request,HttpServletResponse response) throws IOException {
        String fileName = request.getParameter("fileName");
        ApplicationHome ah = new ApplicationHome(getClass());
        String filePath = ah.getSource().getParentFile().getAbsolutePath();
        File downFiles = new File(filePath + File.separator + "inlet-log/"+ fileName);
        UpDownloadUtils.toDownload(request, response, downFiles, fileName);
    }
}
