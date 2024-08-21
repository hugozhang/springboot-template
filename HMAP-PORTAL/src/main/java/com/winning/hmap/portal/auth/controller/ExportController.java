package com.winning.hmap.portal.auth.controller;

import com.winning.hmap.portal.auth.dto.auth.req.query.DoctorParam;
import com.winning.hmap.portal.auth.dto.auth.resp.DoctorExcel;
import com.winning.hmap.portal.auth.service.DoctorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.about.widget.excel.spring.support.writer.ExcelResponseBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


@Api(tags = "导出接口")
@RestController
@RequestMapping("/hmap/export")
public class ExportController {

    @Resource
    private DoctorService doctorService;

    @ApiOperation(value = "医生信息导出excel")
    @PostMapping("/doctor/excel")
    @ExcelResponseBody(fileName = "医生信息维护",inputClass = DoctorExcel.class)
    public List<DoctorExcel> findDoctorList(@RequestBody DoctorParam doctorParam) {
        return doctorService.findDoctorList(doctorParam);
    }

}
