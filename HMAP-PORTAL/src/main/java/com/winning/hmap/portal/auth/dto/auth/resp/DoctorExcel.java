package com.winning.hmap.portal.auth.dto.auth.resp;

import lombok.Data;
import me.about.widget.excel.annotation.ExcelColumn;

@Data
public class DoctorExcel {

    @ExcelColumn(name = "序号")
    private Integer id;

    @ExcelColumn(name = "医生编码")
    private String drCode;

    @ExcelColumn(name = "医生姓名")
    private String drName;

    @ExcelColumn(name = "医保医护人员编码")
    private String hiDrCode;

    @ExcelColumn(name = "医保医护人员名称")
    private String hiDrName;

    @ExcelColumn(name = "医护人员所属科室")
    private String medinsDepts;

}
