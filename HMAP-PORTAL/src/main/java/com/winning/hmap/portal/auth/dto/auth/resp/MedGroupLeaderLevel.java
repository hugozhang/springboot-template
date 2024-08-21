package com.winning.hmap.portal.auth.dto.auth.resp;

import lombok.Data;

/**
 * @author cpj
 * @date 2024/4/8 13:57
 * @desciption: 医疗组成员
 */
@Data
public class MedGroupLeaderLevel {

    /**
     * 医疗组代码
     */
    private String medGrpCode;

    /**
     * 医疗组名称
     */
    private String medGrpName;

    /**
     * 组长工号
     */
    private String grpLeaderCode;

    /**
     * 组长名称
     */
    private String grpLeaderName;

}
