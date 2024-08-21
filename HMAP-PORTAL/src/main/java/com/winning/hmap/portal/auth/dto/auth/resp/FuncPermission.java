package com.winning.hmap.portal.auth.dto.auth.resp;

import lombok.Data;

@Data
public class FuncPermission {

    private Long id;

    private String name;

    private String code;

    private Boolean checked;

    public FuncPermission() {
        this.checked = false;
    }
}
