package com.winning.hmap.portal.util;

import lombok.extern.slf4j.Slf4j;
import me.about.widget.spring.mvc.exception.BizException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据excel导入util解析
 * @author WJ
 * @version v1.0
 * @date 2021-10-30
 */
@Slf4j
public class UploadExcelUtil {
    /**
     * 解析文件中的数据
     */
    public static List<Map<String, Object>> parseRowCell(String filename, InputStream is, Object[] colunms, Object[] colunmsName) throws IOException {
        Workbook workbook = null;

        if(filename == null || filename.isEmpty()){
            throw new BizException(400,"文件名不能为空!");
        }

        // 判断excel的后缀，不同的后缀用不同的对象去解析
        // xls是低版本的Excel文件
        if (filename.endsWith(".xls")) {
            workbook = new HSSFWorkbook(is);
        }
        // xlsx是高版本的Excel文件
        if (filename.endsWith(".xlsx")) {
            workbook = new XSSFWorkbook(is);
        }
        if (workbook == null) {
            throw new BizException(400,"文件导入失败,只能上传xlsx、xls文件!");
        }

        // 取到excel 中的第一张工作表
        Sheet sheet = workbook.getSheetAt(0);
        if (sheet == null) {
            throw new BizException(400,"文件导入失败!");
        }

        boolean isValid = validateExcelHeader(sheet,colunmsName);
        if (!isValid) {
            throw new BizException(400,"导入文件与模板文件表头名称不一致!");
        }
        return getExcelArraylist(sheet,colunms,colunmsName);
    }

    public static boolean validateExcelHeader(Sheet sheet, Object[] expectedHeaders){
        boolean isValid = true;
        // 获取第一行（表头行）
        Row headerRow = sheet.getRow(0);
        if (headerRow != null) {
            // 检查表头行中的单元格内容是否与预期的表头数组匹配
            for (int i = 0; i < expectedHeaders.length; i++) {
                Cell cell = headerRow.getCell(i);
                String cellValue = (cell != null) ? cell.getStringCellValue().trim() : "";

                // 检查单元格内容是否与预期的表头数组中的值匹配
                if (!cellValue.equalsIgnoreCase(expectedHeaders[i].toString())) {
                    isValid = false;
                    break;
                }
            }
        } else {
            // 表头行为空
            isValid = false;
        }
        return isValid;
    }

    /**
     * 获取Excel解析列表
     */
    private static List<Map<String, Object>> getExcelArraylist(Sheet sheet, Object[] colunms, Object[] colunmsName){
        List<Map<String, Object>> result = new ArrayList<>();
        // 最后一行
        int rowNum = sheet.getPhysicalNumberOfRows();
        Row row = sheet.getRow(1);
        if (row == null) {
            throw new BizException(400,"导入excel文件首行不能为空!");
        }

        for (int j = 0; j < colunmsName.length; j++) {
           getCellFormatValue(row.getCell(j)).trim();
        }
        //数据从2行开始
        try {
            for (int i = 1; i <= rowNum; i++) {
                row = sheet.getRow(i);
                // 空行跳过
                if (row == null) {
                    continue;
                }

                boolean isEmptyRow = true;
                // 检查行中的每个单元格是否为空
                for (int j = 1; j < row.getLastCellNum(); j++) {
                    // 单元格不为空
                    if (row.getCell(j)!=null
                            && row.getCell(j).getCellTypeEnum() != CellType.BLANK) {
                        isEmptyRow = false;
                        break;
                    }
                }

                if (!isEmptyRow) {
                    Map<String, Object> data = new HashMap<>();
                    for (int j = 0; j < colunms.length; j++) {
                        Cell c = row.getCell(j);
                        String cell = getCellFormatValue(c).trim();
                        data.put(colunms[j].toString(), cell);
                    }
                    result.add(data);
                }
            }
        }catch (Exception e){
            log.debug(e.getMessage());
        }
        return result;
    }

    /**
     * 根据Cell类型设置数据
     *
     * @param cell
     * @return
     */
    private static String getCellFormatValue(Cell cell) {
        String cellvalue = "";
        if (cell != null) {
            {
                cell.setCellType(CellType.STRING);
                cellvalue = cell.getStringCellValue();
            }
        }
        return cellvalue;
    }
}
