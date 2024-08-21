package com.winning.hmap.portal.auth.dto.auth.req.query;

import com.winning.hmap.portal.auth.entity.SysMedinsDeptDoctor;
import lombok.Data;

import java.util.List;

@Data
public class DoctorInfoDto {
	
	/**
     * 医生id
     */
    private String id;

	/**
	 * 序号
	 */
	private Integer sn;

	/**
     * 医生编码
     */
    private String doctorCode;
	
    /**
     * 医生姓名
     */
    private String doctorName;

	/**
	 * 医保医护人员代码
	 */
	private String hiCode;

	/**
	 * 医保医护人员名称
	 */
	private String hiName;

	/**
	 * 来联系方式
	 */
	private String tel;

	/**
	 * 邮箱
	 */
	private String email;

	/**
	 * 职务编码
	 */
	private String positionCode;

	/**
	 * 职务名称
	 */
	private String positionName;

	/**
	 * 医生与所属科室关联
	 */
	private List<SysMedinsDeptDoctor> doctorRelatedDept;

    /**
     * 科室id
     */
    private String deptId;
    
    /**
     * 科室名称
     */
    private String deptName;

	/**
	 * 所属科室 list
	 */
	private List<String> deptList;

	/**
	 * 医疗组编码
	 */
	private String teamCode;

	/**
	 * 医生与医疗组关联
	 */
	private List<DoctorMedGrpParam> doctorRelatedMedGroup;

	/**
	 * 所属医疗组编码
	 */
	private List<Integer> teamCodes;

	/**
	 * 所属医疗组 list
	 */
	private List<MedicalTeamDto> teamList;

    /**
     * 上级组织名称路径
     */
    private String prntPathName;

	/**
	 * 医疗机构代码
	 */
	private String organCode;

	/**
	 * 医疗机构名称
	 */
	private String organName;

	/**
	 * 启用状态  0:停用，1:启用
	 */
	private String enable;

}
