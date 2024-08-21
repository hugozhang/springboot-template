package com.winning.hmap.portal.auth.dto.auth.resp;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class SimpleDoctorMedinsDept implements Serializable {

    /**
     * 机构ID
     */
    private Long medinsId;

    /**
     * 机构名称
     */
    private String medinsName;

    /**
     * 机构编码
     */
    private String medinsNo;


    /**
     * 科室列表
     */
    private List<SimpleMedinsDept> deptList = new ArrayList<>();


}
