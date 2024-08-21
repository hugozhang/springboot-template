package com.winning.hmap.portal.auth.dto.auth.resp;

import lombok.Data;

import java.util.List;

@Data
public class MenuVO {

    private List<Menu> menus;

    private List<Long> resourceIds;

}
