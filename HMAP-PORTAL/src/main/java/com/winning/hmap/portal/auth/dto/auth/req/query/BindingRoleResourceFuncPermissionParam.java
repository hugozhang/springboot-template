package com.winning.hmap.portal.auth.dto.auth.req.query;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class BindingRoleResourceFuncPermissionParam {

    @NotNull
    private Long roleId;

    @NotNull
    private Long resourceId;

    @NotEmpty
    private List<Long> funcPermissionIds;

}
