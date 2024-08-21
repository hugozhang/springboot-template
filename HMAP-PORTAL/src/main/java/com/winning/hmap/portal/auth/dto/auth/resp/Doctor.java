package com.winning.hmap.portal.auth.dto.auth.resp;

import com.winning.hmap.portal.auth.entity.SysMedGrp;
import com.winning.hmap.portal.auth.entity.SysMedinsDept;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class Doctor implements Serializable {

    private static final long serialVersionUID = 6609552091343870348L;

    private Long id;

    private Long drId;

    private String drCode;

    private String drName;

    private String hiDrCode;

    private String hiDrName;

    private String tel;

    private String dutyName;

    private String dutyCode;

    private String email;

    private String delFlag;

    private String username;

    private List<SimpleDoctorMedinsDept> medinsList;

    private String deptName;

    private String medGrpName;

    private List<SysMedinsDept> deptList = new ArrayList<>();

    private List<SysMedGrp> medGrpList = new ArrayList<>();

}
