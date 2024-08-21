package com.winning.hmap.portal.dict.dto.resp;

import lombok.Data;

import java.util.List;

@Data
public class Dict {

    private Long dictId;

    private Long parentId;

    private String parentName;

    private String dictName;

    private String dictCode;

    private String dictValue;

    private String remark;

    private List<Dict> children;

}
