package com.winning.hmap.portal.auth.dto.auth.resp;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
public class Menu {

    private Long menuId;

    private Long parentId;

    private String name;

    private String code;

    private String icon;

    private String url;

    private Integer sortBy;

    private Boolean checked;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<Menu> children;

    private String title;

    private String mkid;

    private String id;

    private Boolean hidden;

    public Menu() {
        this.checked = false;
    }

}
