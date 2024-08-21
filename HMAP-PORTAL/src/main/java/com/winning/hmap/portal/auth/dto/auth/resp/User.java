package com.winning.hmap.portal.auth.dto.auth.resp;

import com.winning.hmap.portal.auth.entity.SysMedGrp;
import com.winning.hmap.portal.auth.entity.SysMedinsDept;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class User {

    private Long userId;

    private String loginName;

    private String username;

    private String medinsDeptId;

    private String deptName;

    private String deptCode;

    private String medGrpName;

    private List<SysMedinsDept> deptList = new ArrayList<>();

    private List<SysMedGrp> medGrpList = new ArrayList<>();

    private List<UserRole> roleList = new ArrayList<>();

    private Long drId;

    private String drName;

    private String drCode;

    private String hiDrCode;

    private String hiDrName;

    private String tel;

    private String dutyName;

    private String dutyCode;

    private String email;

    private String delFlag;

    private Date crteTime;

    private String password;



}
