package com.winning.hmap.portal.auth.dto.auth.req.query;

import lombok.Data;
import java.util.List;

@Data
public class UserParam {

    private String username;

    private List<String> deptCodes;

    private String loginName;

    private Long deptId;

}
