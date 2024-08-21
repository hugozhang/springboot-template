package com.winning.hmap.portal.auth.dto.auth.resp;


import lombok.Data;

import java.io.Serializable;

@Data
public class UserRole implements Serializable {

    private Long roleId;

    private String roleName;

}
