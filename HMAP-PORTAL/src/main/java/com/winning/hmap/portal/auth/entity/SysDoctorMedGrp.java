package com.winning.hmap.portal.auth.entity;

import lombok.Data;

import java.util.Date;

@Data
public class SysDoctorMedGrp {

    private Long id;

    private Long drId;

    private Long medGrpId;

    private Date crteTime;

    private Date updtTime;

}
