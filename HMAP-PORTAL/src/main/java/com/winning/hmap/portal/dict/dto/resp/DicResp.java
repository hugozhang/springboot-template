package com.winning.hmap.portal.dict.dto.resp;

import lombok.Data;

import java.io.Serializable;

/**
 * 业务字典(风控使用)
 *
 * @auther: hugo.zxh
 * @date: 2022/05/11 14:47
 * @description:
 */
@Data
public class DicResp implements Serializable {

    private String dicCode;

    private String dicName;

}
