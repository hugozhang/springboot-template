package com.winning.hmap.portal.dict.dto.req.put;

import lombok.Data;

@Data
public class AddLogParam {

    private String opnType;

    private Long userId;

    /**
     * 姓名
     */
    private String username;

    /**
     * 账号
     */
    private String loginName;

    private String ip;

    /**
     * 内容
     */
    private String content;

}
