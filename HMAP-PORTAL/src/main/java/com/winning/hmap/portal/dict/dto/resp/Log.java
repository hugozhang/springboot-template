package com.winning.hmap.portal.dict.dto.resp;

import lombok.Data;

import java.util.Date;

@Data
public class Log {

    private Long logId;

    private String opnType;

    private Long userId;

    private String loginName;

    private String username;

    private Date opnTime;

    private String ip;

    private String content;
}
