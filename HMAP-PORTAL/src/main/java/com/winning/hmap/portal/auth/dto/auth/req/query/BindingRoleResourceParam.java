package com.winning.hmap.portal.auth.dto.auth.req.query;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class BindingRoleResourceParam {

    @NotNull
    private Long roleId;

    @NotEmpty
    private List<Long> resourceIds;

}
