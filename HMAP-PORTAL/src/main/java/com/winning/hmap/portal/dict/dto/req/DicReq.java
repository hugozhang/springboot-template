package com.winning.hmap.portal.dict.dto.req;

import lombok.Data;

import java.io.Serializable;

/**
 * 字典请求(风控使用)
 *
 * @auther: hugo.zxh
 * @date: 2022/05/11 14:46
 * @description:
 */
@Data
public class DicReq implements Serializable {

    private String syscode;

    private String parentCode;

}
