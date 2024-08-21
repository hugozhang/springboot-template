package com.winning.hmap.portal.auth.dto.auth.req.query;

import lombok.Data;
import java.util.Date;

@Data
public class DoctorMedGrpParam {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 医生id
     */
    private Long drId;

    /**
     * 医疗组ID
     */
    private Long medGrpId;

    /**
     * 创建时间
     */
    private Date crteTime;

    /**
     * 更新时间
     */
    private Date updtTime;

}
