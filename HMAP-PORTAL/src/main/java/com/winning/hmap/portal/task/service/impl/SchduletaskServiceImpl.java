package com.winning.hmap.portal.task.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.winning.hmap.portal.auth.dto.auth.resp.LoginUser;
import com.winning.hmap.portal.task.dto.KettleFileDto;
import com.winning.hmap.portal.task.constant.Constant;
import com.winning.hmap.portal.task.dto.*;
import com.winning.hmap.portal.task.dto.req.put.SchmParam;
import com.winning.hmap.portal.task.dto.req.put.TaskSchmModifyParam;
import com.winning.hmap.portal.task.dto.resp.TaskNotice;
import com.winning.hmap.portal.task.dto.resp.TaskSchm;
import com.winning.hmap.portal.task.entity.Schm;
import com.winning.hmap.portal.task.entity.Task;
import com.winning.hmap.portal.task.dto.req.query.TaskSchmParam;
import com.winning.hmap.portal.task.dto.req.query.TaskParam;
import com.winning.hmap.portal.task.entity.TaskRslt;
import com.winning.hmap.portal.task.entity.TaskSchmRlts;
import com.winning.hmap.portal.task.mapper.*;
import com.winning.hmap.portal.task.service.JobService;
import com.winning.hmap.portal.task.service.SchduletaskService;
import com.winning.hmap.portal.util.TimeTool;
import com.winning.hmap.portal.util.CronUtils;
import com.winning.hmap.portal.util.SnowflakeUtil;
import me.about.widget.mybatis.plugin.page.model.PageParam;
import me.about.widget.mybatis.plugin.page.model.PageResult;
import me.about.widget.spring.mvc.exception.BizException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.job.Job;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author cpj
 */
@Service("timerSchduletaskServiceImpl")
public class SchduletaskServiceImpl implements SchduletaskService {

    private static final Log logger = LogFactory.getLog(SchduletaskServiceImpl.class);

    private static final String taskJob = "com.winning.hmap.portal.task.service.impl.TaskJob";

    //错误信息
    private static final String[] SCHEDULER_MESSAGE = {
            "参数为空",
            "无效的计划名称",
            "无效的计划时间",
            "无效的执行路径",
            "计划任务更新失败!",
            "计划任务持久化失败",
            "计划任务加载失败",
            "token不能为空"};

    //执行计划的变量参数
    private static final String STR_FALSE = "false";
    private static final String PLAN_NAME = "plan";
    private static final String SERVER_NAME = "executor";
    private static final String TRIGGER_NAME_PREX = "trigger";

    //执行计划
    private Scheduler scheduler = null;

    @Value("${KJBNAME}")
    private String filePath;

    @Autowired
    TimerTaskExecutor timerTaskService;

    @Resource
    JobService jobService;

    @Resource
    TaskMapper taskMapper;

    @Resource
    SchmMapper schmMapper;

    @Resource
    TaskSchmRltsMapper taskSchmRltsMapper;
    
    @Autowired
    TimerTaskExecutor timerTaskExecutor;
    
    @Autowired
    SchmRsltMapper schmRsltMapper;

    @Resource
    TaskRsltMapper taskRsltMapper;

    @Resource
    TaskNoticeMapper taskNoticeMapper;

    @Override
    public TaskNotice taskNotice() {
        return taskNoticeMapper.taskNotice();
    }

    @Override
    public PageResult<Task> selectTaskByPage(PageParam<TaskSchmParam> pageParam) {
        return taskMapper.selectTaskByPage(pageParam);
    }

    /**
     * 添加任务
     */
    @Override
    public void addTask(TaskParam taskParam, LoginUser loginUser) {
        checkParams(taskParam);
        Date now = new Date();
        taskParam.setValiFlag("0");
        Task task = new Task();
        BeanUtil.copyProperties(taskParam, task);
        task.setCrteTime(now);
        task.setCrterId(loginUser.getUserId());
        task.setUpdtrId(loginUser.getUserId());
        task.setUpdtTime(now);
        task.setDelFlag("0");
        task.setTaskFlag(SnowflakeUtil.nextIdStr());
        taskMapper.insertSelective(task);

        // 添加 job
        QuartzDto quartzDto = new QuartzDto();
        quartzDto.setJobClassName(taskJob);
        quartzDto.setJobDataMap(setJobDataMap(task.getId()));
        quartzDto.setCronExpression(taskParam.getExeExpr());
        jobService.save(quartzDto);
        //保存任务名称
        task.setTaskFlag(quartzDto.getJobName());
        taskMapper.updateByPrimaryKeySelective(task);
        doByStatus(taskParam.getValiFlag(), quartzDto.getJobName());
    }

    /**
     * 修改任务
     */
    @Override
    public void modifyTask(TaskParam taskParam, LoginUser loginUser) {
        checkParams(taskParam);
        Date now = new Date();
        Task task = new Task();
        BeanUtil.copyProperties(taskParam, task);
        task.setUpdtrId(loginUser.getUserId());
        task.setUpdtTime(now);
        taskMapper.updateByPrimaryKeySelective(task);
        Task oledTask = taskMapper.selectByPrimaryKey(taskParam.getId());
        // 更新job：先删除job后新增job
        QuartzDto quartzDto = new QuartzDto();
        quartzDto.setJobClassName(taskJob);
        quartzDto.setJobDataMap(setJobDataMap(oledTask.getId()));
        quartzDto.setCronExpression(task.getExeExpr());
        quartzDto.setJobName(task.getTaskFlag());
        quartzDto.setOldJobName(oledTask.getTaskFlag());
        jobService.save(quartzDto);
        doByStatus(taskParam.getValiFlag(), quartzDto.getJobName());
        logger.info("计划任务更新操作：操作人用户ID 【"+loginUser.getUserId()+"】--- 计划任务名称 【"+ taskParam.getName()+"】");
    }

    /**
     * 删除任务
     */
    @Override
    public void delTask(TaskParam taskParam, LoginUser loginUser) {
        checkParams(taskParam);
        //获取taskFlag
        String taskFlag = taskMapper.getJobNameById(taskParam.getId());
        //获取当前正在运行的任务
        List<JobExecutionContext> jobs = jobService.getCurrentlyExecutingJobs();
        List<String> jobNames = new ArrayList<>();
        for (JobExecutionContext context : jobs) {
            jobNames.add(context.getJobDetail().getKey().getName());
        }
        if (jobNames.contains(taskFlag)) {
            throw new BizException(400,"此任务正在执行，请稍后再试！");
        } else {
            //删除job
            jobService.remove(new QuartzDto(taskFlag));
        }

        //删除执行方案关联
        taskSchmRltsMapper.deleteByTaskId(taskParam.getId());
        //删除任务
        taskMapper.deleteById(taskParam.getId());
        logger.info("计划任务删除操作：操作人用户ID 【"+loginUser.getUserId()+"】--- 计划任务名称 【"+ taskParam.getName()+"】");
    }

    @Override
    public void triggerTask(TaskParam dto) {
        List<TaskSchm> planList = taskSchmRltsMapper.getByTaskId(dto.getId());
        if(!planList.isEmpty()){
           jobHandler(dto.getId());
        }else{
            if(Constant.SUPPORT_RUN.equals(dto.getValiFlag())){
                throw new BizException(400,"当前计划任务为无效状态，不支持“立即执行”操作，请重新选择。");
            }else{
                throw new BizException(400,"当前计划任务未配置执行方案，请完成配置后执行。");
            }
        }
    }


    private void checkParams(TaskParam dto) {
        if (StringUtils.isBlank(dto.getName())) {
            throw new BizException(400,"请输入任务名称");
        }
        if (StringUtils.isBlank(dto.getExeExpr())) {
            throw new BizException(400,"请输入执行表达式");
        }
        if (!CronUtils.checkValid(dto.getExeExpr())) {
            throw new BizException(400,"执行表达式不合法");
        }

        if (dto.getId() !=null) {
            Task task = taskMapper.selectByPrimaryKey(dto.getId());
            if (task == null) {
                throw new BizException(400,"任务不存在");
            }
        }
    }

    //任务表中的 JOB_NAME
    private void doByStatus(String valiFlag, String jobName) {
        if ("0".equals(valiFlag)) {
            //无效 停止任务
            jobService.pause(new QuartzDto(jobName));
        } else {
            //有效 恢复任务
            jobService.resume(new QuartzDto(jobName));
        }
    }

    private Map<String, Long> setJobDataMap(Long taskId) {
        Map<String, Long> dataMap = new HashMap<>(4);
        dataMap.put("taskId", taskId);
        return dataMap;
    }

    /**
     * 定时从数据库加载任务
     * @throws SchedulerException 
     */
    @PostConstruct
    public void reloadJobFromDb(){
    	try {
            List<Task> list = taskMapper.list(null);
            Set<String> jobNameList = new HashSet<>();
            scheduler = StdSchedulerFactory.getDefaultScheduler();
            for (Task task : list) {
                jobNameList.add(task.getTaskFlag());
                try {
                    QuartzDto dto = new QuartzDto(task.getTaskFlag());
                    if (!jobService.checkExists(dto)) {
                        dto.setJobClassName(taskJob);
                        dto.setJobDataMap(setJobDataMap(task.getId()));
                        dto.setCronExpression(task.getExeExpr());
                        jobService.save(dto);
                    }
                    doByStatus(task.getValiFlag(), task.getTaskFlag());
                } catch (Exception e) {
                    logger.error("从数据库加载任务异常", e);
                }
            }

            //删除无效的Job
            for (JobKey jobKey : jobService.getJobNames()) {
                if (!jobNameList.contains(jobKey.getName())) {
                    QuartzDto dto = new QuartzDto(jobKey.getName());
                    jobService.remove(dto);
                }
            }
            scheduler.start();
		} catch (Exception e) {
			logger.error("加载计划任务异常", e);
		}
    	
    }

    /**
     * 列表查询
     */
    @Override
    public PageResult<Schm> taskSchmList(PageParam<TaskSchmParam> pageParam) {
        return schmMapper.taskSchmList(pageParam);
    }

    private void checkTaskSchmParams(SchmParam dto){
        if (StringUtils.isBlank(dto.getName())) {
            throw new BizException(400,"请输入方案名称");
        }
        if (StringUtils.isBlank(dto.getType())) {
            throw new BizException(400,"请选择方案类型");
        }

        //ETL 数据抽取无方案实现
        if(!Constant.KETTLE_TYPE.equals(dto.getType())){
            if (StringUtils.isBlank(dto.getImpl())) {
                throw new BizException(400,"请输入方案实现");
            }
        }

        int count = taskSchmRltsMapper.getCountByDto(dto);
        if(count>0){
            throw new BizException(400,"方案务已存在");
        }

    }

    /**
     * 添加执行方案
     */
    @Override
    public void addTaskSchm(SchmParam schmParam, LoginUser loginUser) {
        checkTaskSchmParams(schmParam);
        schmParam.setCrterId(schmParam.getUpdtrId());
        schmParam.setCrteTime(new Date());
        schmParam.setImpl(schmParam.getImpl()==null?"": schmParam.getImpl());
        schmParam.setCrterId(loginUser.getUserId());
        schmParam.setUpdtrId(loginUser.getUserId());
        schmParam.setDelFlag("0");
        schmParam.setValiFlag("1");
        schmParam.setAttr("clinicalDispatchServiceSZ.execClinicalRulesSZ");
        schmMapper.insertSelective(schmParam);
    }

    /**
     * 修改执行方案
     */
    @Override
    public void modifyTaskSchm(SchmParam schmParam, LoginUser loginUser) {
        checkTaskSchmParams(schmParam);
        Schm schm = schmMapper.selectByPrimaryKey(schmParam.getId());
        BeanUtil.copyProperties(schmParam, schm);
        schm.setUpdtTime(new Date());
        schm.setImpl(schmParam.getImpl()==null?"": schmParam.getImpl());
        schm.setCrterId(loginUser.getUserId());
        schm.setUpdtrId(loginUser.getUserId());
        schmMapper.updateByPrimaryKey(schm);
        logger.info("任务执行方案更新：操作人用户ID 【"+loginUser.getUserId()+"】--- 执行方案名称 【"+ schmParam.getName()+"】");
    }

    /**
     * 删除执行方案
     */
    @Override
    public void delTaskSchm(Long schmId, LoginUser loginUser) {
        int count = taskSchmRltsMapper.getCountByPlanId(schmId);
        if (count > 0) {
            throw new BizException(400,"此执行方案已被使用，请先删除相关任务 ！");
        } else {
            schmMapper.deleteById(schmId);
            logger.info("任务执行方案删除：操作人用户ID 【"+loginUser.getUserId().toString()+"】--- 执行方案ID 【"+ schmId+"】");
        }
    }


    /**
     * 根据任务ID执行任务
     *
     * @param taskId 任务ID
     */
    @Override
    public void jobHandler(Long taskId) {
        List<TaskSchm> planList = taskSchmRltsMapper.getByTaskId(taskId);
        String time = TimeTool.getMillisecondTime().replaceAll("-", "").replaceAll(":", "").replaceAll(" ", "");
        String runBatch = taskId + "_" + time;
        for (TaskSchm taskSchm : planList) {
            taskSchm.setRunBchno(runBatch);
            taskSchm.setTaskId(taskId);
            if (Constant.JAVACLASS.equals(taskSchm.getSchmType())) {
                javaClassHandler(taskSchm);
            } else if (Constant.PROCEDURE.equals(taskSchm.getSchmType())) {
                new Thread() {
                    @Override
                    public void run() {
                        procedureHandler(taskSchm);
                    }
                }.start();
            } else if (Constant.KETTLE_TYPE.equals(taskSchm.getSchmType())) {
                new Thread() {
                    @Override
                    public void run() {
                        etlKettleHandler(taskSchm);
                    }
                }.start();
            }
        }
    }

    private void javaClassHandler(TaskSchm taskSchm) {
        //获取参数
        String arg = taskSchm.getPara();
        String[] argArr = new String[]{};
        if (StringUtils.isNotBlank(arg)) {
            argArr = arg.split(",");
        }
        TimerDto timerDto = new TimerDto();
        timerDto.setSchmId(taskSchm.getSchmId());
        timerDto.setImpl(taskSchm.getImpl());
        timerDto.setParaArr(argArr);
        timerDto.setRunBchno(taskSchm.getRunBchno());
        timerDto.setTaskId(taskSchm.getTaskId());
        timerDto.setPrrt(taskSchm.getPrrt());
        timerDto.setTaskName(taskSchm.getTaskName());
        timerDto.setSchmName(taskSchm.getSchmName());
        timerTaskExecutor.executeSchduletask(timerDto);
    }

    private void procedureHandler(TaskSchm taskSchm) {
        StringBuilder loggerStringBuilder = new StringBuilder();
        //预留
        String ticketCd = TimeTool.getMillisecondTime().replaceAll("-", "").replaceAll(":", "");
        TaskRslt param = new TaskRslt();
        param.setTicketCd(ticketCd);
        try {
            loggerStringBuilder.append("【执行存储").append(taskSchm.getSchmName()).append("】开始时间：").append(TimeTool.getDateTime()).append("<br/>");
            taskRsltMapper.insertTaskRslt(taskSchm.getTaskId(),taskSchm.getSchmId(),new Date(), Constant.JOB_STAS_ZZFX, loggerStringBuilder.toString(), ticketCd, "0",taskSchm.getRunBchno(),taskSchm.getTaskName(),taskSchm.getSchmName(),taskSchm.getPrrt());
            StringBuilder procedureName = new StringBuilder();
            procedureName.append(taskSchm.getImpl());
            procedureName.append("(");
            if(taskSchm.getPara()!=null&& !taskSchm.getPara().isEmpty()){
                procedureName.append(taskSchm.getPara());
            }
            procedureName.append(")");
            // 执行数据库存储
            schmMapper.updateProcedure(procedureName.toString());
            loggerStringBuilder.append("【执行存储"+taskSchm.getSchmName()+"】结束时间：" + TimeTool.getDateTime()).append("<br/>");
            updateJobInfo(param, loggerStringBuilder, Constant.JOB_STAS_ZXCG);
        } catch (Exception e) {
            logger.error("调用存储过程异常" + taskSchm.getImpl(), e);
            loggerStringBuilder.append("调用存储过程异常 ").append(taskSchm.getImpl()).append(e.getMessage()).append("<br/>");
            updateJobInfo(param, loggerStringBuilder, Constant.JOB_STAS_ZXSB);
        }
    }

    private void updateJobInfo(TaskRslt param, StringBuilder loggerStringBuilder, String stas) {
        param.setLog(loggerStringBuilder.toString());
        param.setTaskStas(stas);
        if (Constant.JOB_STAS_ZXSB.equals(stas) || Constant.JOB_STAS_ZXCG.equals(stas)) {
            param.setExeEndtime(new Date());
        }
        taskRsltMapper.updateTaskRslt(param);
    }

    /**
     * 调用 kettle 作业文件
     *
     * @throws Exception
     */
    public void etlKettleHandler(TaskSchm taskSchm){
        String batchId = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        StringBuilder loggerStringBuilder = new StringBuilder();
        String executeStatus = Constant.JOB_STAS_ZBJX;
        TaskRslt taskRslt = new TaskRslt();
        taskRslt.setTicketCd(batchId);
        try {
            logger.info("【ETL KETTLE数据抽取】开始时间：" + TimeTool.getDateTime());
            loggerStringBuilder.append("【ETL KETTLE数据抽取】开始时间：" + TimeTool.getDateTime()).append("<br>");
            taskRsltMapper.insertTaskRslt(taskSchm.getTaskId(), taskSchm.getSchmId(),new Date(), executeStatus, "【ETL KETTLE数据抽取】开始时间：" + TimeTool.getDateTime(), batchId, null,taskSchm.getRunBchno(),taskSchm.getTaskName(),taskSchm.getSchmName(),taskSchm.getPrrt());
            try {
                String attUuid  = taskSchm.getAttUuid();
                if(!"".equals(attUuid) && attUuid!=null&& !taskMapper.getFileInfo(attUuid).isEmpty()){
                    logger.info("开始执行Kettle脚本");
                    loggerStringBuilder.append(Constant.ETLSJCQ + TimeTool.getDateTime() +") 开始执行Kettle脚本"+ "<br/>");
                    logger.info("为执行Kettle脚本初始化Kettle环境");
                    loggerStringBuilder.append(Constant.ETLSJCQ + TimeTool.getDateTime() +") 为执行Kettle脚本初始化Kettle环境"+ "<br/>");
                    KettleEnvironment.init();
                    List<Map<String, Object>> fileinfo = taskMapper.getFileInfo(attUuid);
                    String ktrOrKjbFilePath = fileinfo.get(0).get("path").toString();
                    String fileName = fileinfo.get(0).get("filename").toString();
                    //TransMeta 转换
                    if(fileName.endsWith(".ktr")){
                        TransMeta transMeta = new TransMeta(ktrOrKjbFilePath);
                        Trans trans = new Trans(transMeta);
                        loggerStringBuilder.append(Constant.ETLSJCQ + TimeTool.getDateTime() +") 一切就绪，正式执行Kettle脚本中......"+ "<br/>");
                        // 执行转换
                        trans.execute(null);
                        // 等待转换执行结束
                        trans.waitUntilFinished();
                        if (trans.getErrors() > 0) {
                            logger.info("Kettle脚本(ktr)执行过程中出错了");
                            loggerStringBuilder.append(Constant.ETLSJCQ + TimeTool.getDateTime() +") Kettle脚本(ktr)执行过程中出错了"+ "<br/>");
                            taskRslt.setTaskStas(Constant.JOB_STAS_ZXSB);
                            throw new Exception(trans.getResult().getLogText());
                        } else {
                            logger.info("Kettle脚本(ktr)已执行完");
                            loggerStringBuilder.append(Constant.ETLSJCQ + TimeTool.getDateTime() +") Kettle脚本(ktr)已执行完"+ "<br/>");
                            taskRslt.setTaskStas(Constant.JOB_STAS_ZXCG);
                        }
                    }else{
                        //JobMeta
                        JobMeta jobMeta = new JobMeta(ktrOrKjbFilePath,null );
                        Job job = new Job(null, jobMeta);
                        logger.info("一切就绪，正式执行Kettle脚本中......");
                        loggerStringBuilder.append(Constant.ETLSJCQ + TimeTool.getDateTime() +") 一切就绪，正式执行Kettle脚本中......"+ "<br/>");
                        job.start();
                        job.waitUntilFinished();
                        if (job.getErrors() > 0){
                            logger.info("Kettle脚本(kjb)执行过程中出错了");
                            loggerStringBuilder.append(Constant.ETLSJCQ + TimeTool.getDateTime() +") Kettle脚本(kjb)执行过程中出错了"+ "<br/>");
                            taskRslt.setTaskStas(Constant.JOB_STAS_ZXSB);
                            throw new Exception(job.getResult().getLogText());
                        }else{
                            logger.info("Kettle脚本(kjb)已执行完");
                            loggerStringBuilder.append(Constant.ETLSJCQ + TimeTool.getDateTime() +") Kettle脚本(kjb)已执行完"+ "<br/>");
                            taskRslt.setTaskStas(Constant.JOB_STAS_ZXCG);
                        }
                    }
                }else{
                    loggerStringBuilder.append("该方案未配置 Kettle 脚本 <br/>");
                    taskRslt.setTaskStas(Constant.JOB_STAS_ZXSB);
                }
            } catch (KettleException e){
                logger.info("Kettle环境初始化过程中出错了");
                loggerStringBuilder.append(Constant.ETLSJCQ + TimeTool.getDateTime() +") Kettle环境初始化过程中出错了"+ "<br/>");
                taskRslt.setExeEndtime(new Date());
                taskRslt.setTaskStas(Constant.JOB_STAS_ZXSB);
            }

        } catch (Exception e) {
            taskRslt.setTaskStas(Constant.JOB_STAS_ZXSB);
            logger.error("【ETL KETTLE数据抽取】（时间：" + TimeTool.getDateTime() + "）定时任务异常：", e);
            loggerStringBuilder.append("【ETL KETTLE数据抽取】（时间：" + TimeTool.getDateTime() + "）定时任务异常：");
        } finally {
            String endTime = TimeTool.getDateTime();
            logger.info("【ETL KETTLE数据抽取】结束时间：" + endTime);
            loggerStringBuilder.append("【ETL KETTLE数据抽取】结束时间：" + endTime).append("<br>");
            taskRslt.setLog(loggerStringBuilder.toString());
            taskRslt.setExeEndtime(new Date());
            taskRsltMapper.updateTaskRslt(taskRslt);
        }
    }

    /**
     * 根据任务ID获取任务内容
     */
    @Override
    public PageResult<TaskSchm> getTaskContent(PageParam<TaskSchmParam> pageParam) {
        return taskSchmRltsMapper.getPlanByPage(pageParam);
    }

    @Override
    public void addTaskContent(TaskSchmModifyParam taskSchmModifyParam) {
        //参数校验
        checkContentParams(taskSchmModifyParam);
        TaskSchmRlts taskSchmRlts = taskSchmRltsMapper.getTaskSchmRltsBySchmIdAndTaskId(taskSchmModifyParam);
        if(taskSchmRlts == null){
            //获取最高优先级
            Integer max = taskSchmRltsMapper.getMaxSort(taskSchmModifyParam.getTaskId());
            if (max == null) { max = 0;}
            TaskSchmRlts rlts = new TaskSchmRlts();
            rlts.setTaskId(taskSchmModifyParam.getTaskId());
            rlts.setSchmId(taskSchmModifyParam.getSchmId());
            rlts.setPrrt(max + 1);
            rlts.setDelFlag("0");
            taskSchmRltsMapper.insert(rlts);
        }else{
            if("0".equals(taskSchmRlts.getDelFlag())){
                throw new BizException(400,"当前配置任务存在方案重复，请重新配置");
            }
            taskSchmRltsMapper.enableSchm(taskSchmModifyParam.getTaskId(), taskSchmModifyParam.getSchmId());
        }
    }

    /**
     * 修改任务内容
     *
     * @param dto
     * @return
     */
    @Override
    public void modifyTaskContent(TaskSchmModifyParam dto, LoginUser loginUser) {
        //参数校验
        checkContentParams(dto);
        TaskSchmRlts taskSchmRlts = taskSchmRltsMapper.getTaskSchmRltsBySchmIdAndTaskId(dto);
        if(taskSchmRlts == null) {
            throw new BizException(400,"当前执行方案配置有误或未启用，请重新配置");
        }

        if ("0".equals(taskSchmRlts.getDelFlag())
                && dto.getSchmId().equals(taskSchmRlts.getSchmId())) {
            throw new BizException(400,"当前配置任务存在方案重复，请重新配置");
        }
        taskSchmRltsMapper.enableSchm(dto.getTaskId(), dto.getSchmId());
        taskSchmRltsMapper.disableSchm(dto.getTaskId(), dto.getOldSchmId());
        taskSchmRltsMapper.updateByOldPlanId(dto);
        logger.info("计划任务关联执行方案更新操作：操作人用户ID 【"+loginUser.getUserId()+"】--- 计划任务ID 【"+ dto.getTaskId()+"】--- 执行方案ID 【"+ dto.getOldSchmId()+"--》"+dto.getSchmId()+"】");
    }

    @Override
    public void delTaskContent(TaskSchmModifyParam dto, LoginUser loginUser) {
        taskSchmRltsMapper.disableSchm(dto.getTaskId(), dto.getSchmId());
        logger.info("计划任务关联执行方案删除操作：操作人用户ID 【"+loginUser.getUserId()+"】--- 计划任务ID 【"+ dto.getTaskId()+"】--- 执行方案ID 【 "+ dto.getSchmId()+" 】");
    }

    private void checkContentParams(TaskSchmModifyParam dto) {
        if (dto.getTaskId()==null) {
            throw new BizException(400,"请选择任务");
        }

        if (dto.getSchmId()==null) {
            throw new BizException(400,"请选择任务内容");
        }

        Task task = taskMapper.selectByPrimaryKey(dto.getTaskId());
        if (task == null) {
            throw new BizException(400,"任务不存在");
        }

        Schm schm = schmMapper.selectByPrimaryKey(dto.getSchmId());
        if (schm == null) {
            throw new BizException(400,"执行方案不存在");
        }

        if (dto.getOldSchmId()!=null) {
            Schm oldPlan = schmMapper.selectByPrimaryKey(dto.getOldSchmId());
            if (oldPlan == null) {
                throw new BizException(400,"执行方案不存在");
            }
        }
    }

    /**
     * 获取指定目录下 kettle 文件名称及路径
     */
    @Override
    public List<KettleFileDto> kettleFilesList() {
        File dir = new File(filePath);
        return getKettleAllFile(dir);
    }

    public List<KettleFileDto> getKettleAllFile(File fileDir) {
        List<KettleFileDto> list = new ArrayList<>();
        File[] files = fileDir.listFiles();
        if(files!=null){
            for (File file : files) {
                KettleFileDto kettleFileDto = new KettleFileDto();
                if (file.getName().endsWith(".ktr") || file.getName().endsWith(".kjb")) {
                    kettleFileDto.setFileName(file.getName());
                    kettleFileDto.setFile(file);
                    list.add(kettleFileDto);
                }

                if (file.isDirectory()) {
                    list.addAll(getKettleAllFile(file));
                }
            }
        }
        return list;
    }

}
