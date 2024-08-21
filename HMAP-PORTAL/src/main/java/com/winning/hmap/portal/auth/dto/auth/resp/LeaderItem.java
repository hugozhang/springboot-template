package com.winning.hmap.portal.auth.dto.auth.resp;

import lombok.Data;

import java.io.Serializable;

/**
 * @author cpj
 * @date 2024/3/20 17:35
 * @desciption: 组长选择项
 */
@Data
public class LeaderItem implements Serializable {

    private String code;

    private String name;
}
