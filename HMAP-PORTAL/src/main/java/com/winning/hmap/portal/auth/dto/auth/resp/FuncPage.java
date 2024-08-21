package com.winning.hmap.portal.auth.dto.auth.resp;

import lombok.Data;

import java.util.List;

@Data
public class FuncPage {

    private Long resourceId;

    private String resourceName;

    private List<FuncPermission> funcPermissions;

}
