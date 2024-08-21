package com.winning.hmap.portal.auth.dto.auth.req.query;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class BindingMedinsDeptParam {

    @NotNull
    private Long doctorId;

    @NotEmpty
    private List<Long> deptIds;

}
