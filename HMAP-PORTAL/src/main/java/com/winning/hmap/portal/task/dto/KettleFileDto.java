package com.winning.hmap.portal.task.dto;

import lombok.Data;

import java.io.File;

/**
 * @author cpj
 * @date 2024/4/8 17:02
 * @desciption: 上传Kettle涉及文件
 */
@Data
public class KettleFileDto {

    private String fileName;

    private File file;

}
