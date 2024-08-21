package com.winning.hmap.portal.task.constant;

/**
 * @author cpj
 */
public class Constant {
    /** @deprecated */
    @Deprecated
    public static final int STSATUS_SUCCESS = 0;
    /** @deprecated */
    @Deprecated
    public static final int STSTUS_FAILED = 1;
    /** @deprecated */
    @Deprecated
    public static final int SYSTEM_STSTUS_FAILED = 2;

    /**
     * 数据回滚中
     */
    public static final String JOB_STAS_HGZ = "-1";
    /**
     * 已回滚
     */
    public static final String JOB_STAS_YHG = "0";
    /**
     * 任务队列待执行
     */
    public static final String JOB_STAS_DZX = "1";
    /**
     * 任务准备就绪
     */
    public static final String JOB_STAS_ZBJX = "2";
    /**
     * 正在发送数据
     */
    public static final String JOB_STAS_FSSJ = "3";
    /**
     * 数据发送成功
     */
    public static final String JOB_STAS_FSCG = "4";
    /**
     * 正在分析
     */
    public static final String JOB_STAS_ZZFX = "5";
    /**
     * 正在接收结果
     */
    public static final String JOB_STAS_JSJG = "6";
    /**
     * 不支持重跑
     */
    public static final String JOB_STAS_BNCP = "7";
    /**
     * 任务执行成功
     */
    public static final String JOB_STAS_ZXCG = "8";
    /**
     * 任务执行失败
     */
    public static final String JOB_STAS_ZXSB = "9";

    /**
     * ETL抽取数据计划任务
     */
    public static final String KETTLE = "KETTLE";

    /**
     * 【ETL数据抽取】（时间：
     */
    public static final String ETLSJCQ = "【ETL数据抽取】(时间：";

    /**
     * 开始时间不能大于结束时间!
     */
    public static final String KSSJBNDYJSSJ = "开始时间不能大于结束时间！";

    /**
     * 开始时间不能为空!
     */
    public static final String KSSJBNWK = "开始时间不能为空！";

    /**
     * 结束时间不能为空!
     */
    public static final String JSSJBNWK = "结束时间不能为空！";

    /**
     * 开始时间与结束时间不能为空!
     */
    public static final String KSJSSJBNWK = "开始时间与结束时间不能为空!";

    /**
     * 方案类型,1:医院端原有定时任务(支持重跑)
     */
    public static final String HMAP_JOB_FLAG = "1";

    /**
     * 方案类型,2:DRG新增的定时任务（不支持重跑）
     */
    public static final String DRG_JOB_FLAG = "2";

    /**
     * 任务支持立即执行
     */
    public static final String SUPPORT_RUN = "0";

    /**
     * 任务不支持立即执行
     */
    public static final String NOT_SUPPORT_RUN = "1";

    /**
     * 方案类型 0-JavaClass
     */
    public static final String JAVACLASS = "0";

    /**
     * 方案类型 1-Procedure
     */
    public static final String PROCEDURE = "1";

    /**
     * 方案类型 2-Kettle
     */
    public static final String KETTLE_TYPE = "2";

    /**
     * 待申诉疑点数据查询
     */
    public static final String YDSSSJCX = "YDSSSJCX";

    /**
     * 计划任务开始运行时，设置开关为1
     */
    public static final String RUN_STATUS_1 = "1";

    /**
     * 计划任务运行结束时，设置开关为0
     */
    public static final String RUN_STATUS_0 = "0";

}
