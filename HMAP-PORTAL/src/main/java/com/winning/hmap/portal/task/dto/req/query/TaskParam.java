package com.winning.hmap.portal.task.dto.req.query;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;


/**
 * @author cpj
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskParam {
    /**
     * ID
     */
    private Long id;

    /**
     * 任务名称
     */
    private String name;

    /**
     * 执行表达式
     */
    private String exeExpr;

    /**
     * 任务描述
     */
    private String dscr;

    /**
     * 修改人名称
     */
    private String modifyName;

    /**
     * 修改人ID
     */
    private Long updtrId;

    /**
     * 删除标识
     */
    private String delFlag;

    /**
     * 计划类型
     */
    private String type;

    /**
     * 任务状态
     */
    private String valiFlag;

}
