package com.winning.hmap.portal.auth.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.winning.hmap.portal.auth.dto.auth.req.query.MedGrpParam;
import com.winning.hmap.portal.auth.dto.auth.req.query.SysMedGrpParam;
import com.winning.hmap.portal.auth.dto.auth.resp.*;
import com.winning.hmap.portal.auth.entity.SysMedGrp;
import com.winning.hmap.portal.auth.entity.SysMedGrpLog;
import com.winning.hmap.portal.auth.entity.SysMedinsDept;
import com.winning.hmap.portal.auth.mapper.MedicalTeamLogMapper;
import com.winning.hmap.portal.auth.mapper.MedicalTeamMapper;
import com.winning.hmap.portal.auth.service.DoctorService;
import com.winning.hmap.portal.auth.service.MedicalTeamService;
import com.winning.hmap.portal.auth.service.MedinsDeptService;
import com.winning.hmap.portal.auth.service.MedinsService;
import com.winning.hmap.portal.util.UploadExcelUtil;
import me.about.widget.mybatis.plugin.page.model.PageParam;
import me.about.widget.mybatis.plugin.page.model.PageResult;
import me.about.widget.spring.mvc.exception.BizException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class MedicalTeamServiceImpl implements MedicalTeamService {

    private static final Logger logger = LoggerFactory.getLogger(MedicalTeamServiceImpl.class);

    @Resource
    private MedinsService medinsService;

    @Resource
    private MedinsDeptService medinsDeptService;

    @Resource
    private DoctorService doctorService;

    @Autowired
    private MedicalTeamMapper medicalTeamMapper;

    @Autowired
    private MedicalTeamLogMapper medicalTeamLogMapper;

    @Override
    public PageResult<MedGrp> query(PageParam<MedGrpParam> pageParam) {
        PageResult<MedGrp> result = new PageResult<>();
        PageResult<SysMedGrp> pageResult = medicalTeamMapper.query(pageParam);
        result.setTotal(pageResult.getTotal());
        result.setTotalPage(pageResult.getTotalPage());
        result.setRows(pageResult.getRows().stream().map(this::convert).collect(Collectors.toList()));
        return result;
    }

    private MedGrp convert(SysMedGrp sysMedGrp) {
        MedinsDept medinsDept = medinsDeptService.getMedinsDeptById(sysMedGrp.getDeptId());
        Doctor doctor = doctorService.getDoctorByCode(sysMedGrp.getGrpLeaderCode());
        MedGrp medGrp = new MedGrp();
        medGrp.setId(sysMedGrp.getId());
        medGrp.setDeptId(sysMedGrp.getDeptId());
        medGrp.setDeptCode(sysMedGrp.getDeptCode());
        medGrp.setDeptName(medinsDept==null?null:medinsDept.getDeptName());
        medGrp.setMedGrpName(sysMedGrp.getMedGrpName());
        medGrp.setMedGrpCode(sysMedGrp.getMedGrpCode());
        medGrp.setGrpLeaderName(doctor==null?null:doctor.getDrName());
        medGrp.setGrpLeaderCode(sysMedGrp.getGrpLeaderCode());
        medGrp.setValiFlag(sysMedGrp.getValiFlag());
        return medGrp;
    }

    @Override
    public String uploadExcel(MultipartFile file) throws IOException {
        if (file == null) {
            throw new BizException(400,"文件上传失败");
        }

        String filename = file.getOriginalFilename();
        Object[] colunmsName = {"序号","科室编码","科室名称", "医疗组名称","组长工号","组长姓名"};
        Object[] colunms = {"sn","deptCode","deptName","medGrpName","grpLeaderCode","grpLeaderName"};
        // 调用解析文件方法
        List<Map<String, Object>> excelList = UploadExcelUtil.parseRowCell(filename, file.getInputStream(),colunms,colunmsName);
        if (excelList.isEmpty()){
            throw new BizException(400, "文件未解析出数据");
        }

        //校验医疗组Excel导入数据
        File resultFile = checkMedicalTeam(excelList);
        if(resultFile.length()>0){
            logger.info("文件导入成功，返回结果文件名: {}", resultFile.getName());
            return resultFile.getName();
        }
        resultFile.delete();
        return "";
    }

    private File checkMedicalTeam(List<Map<String, Object>> excelList) throws IOException {
        //校验医疗机构编码是否存在组织机构管理中
        List<String> medinsIdList = medinsService.medinsIdList();
        if(medinsIdList.isEmpty()){
            throw new BizException(400, "组织机构管理中不存在有效的机构，无法导入");
        }

        //校验科室编码是否不存在科室管理中
        List<String> deptIdList = medinsDeptService.medinsDeptIdList().stream()
                .map(SysMedinsDept::getDeptCode)
                .collect(Collectors.toList());

        if(deptIdList.isEmpty()){
            throw new BizException(400, "科室管理中不存在有效的科室，无法导入");
        }

        //已有医疗组集合
        List<String> groupCodeList = medicalTeamMapper.queryGroupCodeList();

        //创建一个文件对象
        ApplicationHome ah = new ApplicationHome(getClass());
        String pathFile = ah.getSource().getParentFile().getAbsolutePath();
        String time= DateUtil.format(new Date(),"yyyyMMddHHmmss");
        File directory = new File(pathFile + File.separator + "inlet-log");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        File resultFile = new File(directory, time + "导入错误日志.txt");

        //创建输出流
        BufferedWriter bw = new BufferedWriter(new FileWriter(resultFile));
        List<SysMedGrpParam> insertList = new ArrayList<>();
        List<SysMedGrpParam> updateList = new ArrayList<>();

        for (Map<String, Object> map : excelList) {
            StringBuilder errorBuilder = new StringBuilder();
            StringBuilder isEmptyBuilder = new StringBuilder();
            SysMedGrpParam dto = BeanUtil.mapToBean(map, SysMedGrpParam.class, true);
            // 科室编码校验
            if (dto.getDeptCode().isEmpty()) {
                isEmptyBuilder.append("科室编码、");
            } else {
                if (!deptIdList.contains(dto.getDeptCode())) {
                    errorBuilder.append("科室编码不存在科室管理中，");
                } else {
                    dto.setDeptId(medinsDeptService.getMedinsDeptByCode(dto.getDeptCode()).getDeptId());
                }
            }

            if (dto.getDeptName().isEmpty()) {isEmptyBuilder.append("科室名称、");}
            if (dto.getGrpLeaderName().isEmpty()) {isEmptyBuilder.append("组长姓名、");}

            // 组长工号校验
            if (dto.getGrpLeaderCode() != null) {
                Doctor doctor = doctorService.getDoctorByCode(dto.getGrpLeaderCode());
                if (doctor==null) {
                    errorBuilder.append("组长工号不存在医生信息中，");
                }else{
                    boolean isExist = doctor.getDeptList().stream()
                            .anyMatch(e -> e.getDeptCode().equals(dto.getDeptCode()));
                    if (!isExist) {
                        errorBuilder.append("组长不属于新增医疗组科室，");
                    }
                }
            } else {
                isEmptyBuilder.append("组长工号、");
            }

            if (isEmptyBuilder.length() > 0) {
                isEmptyBuilder.deleteCharAt(isEmptyBuilder.length() - 1);
                isEmptyBuilder.append(" 不能为空，");
            }

            if (errorBuilder.length() == 0 && isEmptyBuilder.length() == 0) {
                String medicalGroupCode = dto.getDeptCode() + dto.getGrpLeaderCode();
                dto.setMedGrpCode(medicalGroupCode);
                dto.setUpdtTime(new Date());
                if (groupCodeList.contains(medicalGroupCode)) {
                    updateList.add(dto);
                } else {
                    insertList.add(dto);
                }
            } else {
                bw.write("序号：" + dto.getSn() + ", 结论【" + errorBuilder + isEmptyBuilder + "无法导入】\t");
                bw.newLine();
                continue;
            }
            //刷新
            bw.flush();
        }

        //已存在则更新数据
        if(!updateList.isEmpty()){
            medicalTeamMapper.updateSysMedGrps(updateList);
        }
        //不存在则更新数据
        if(!insertList.isEmpty()){
            insertList = new ArrayList<>(insertList.stream().collect(Collectors.toMap(SysMedGrpParam::getMedGrpCode, Function.identity()
                    , BinaryOperator.maxBy(Comparator.comparingLong(SysMedGrpParam::getSn)))).values());
            medicalTeamMapper.insertSysMedGrps(insertList);
        }
        bw.close();
        return resultFile;
    }

    @Override
    public void delete(List<String> list,LoginUser loginUser) {
        List<Long> msgList = new ArrayList<>();
        for (String id : list) {
            List<String> leaderIds = medicalTeamMapper.selectListByGroupCode(id).stream()
                    .map(SysMedGrp::getGrpLeaderCode).collect(Collectors.toList());
            msgList.addAll(medicalTeamMapper.findSysMedGrpByYsId(leaderIds));
        }

        if (!msgList.isEmpty()){
            throw new BizException(400,"当前选中医疗组存在已关联用户，不允许删除！");
        }

        for (String id : list) {
            int deleteImport = medicalTeamMapper.deleteById(id);
            if (deleteImport>0){
                List<SysMedGrp> sysMedGrps = medicalTeamMapper.selectListByGroupCode(id);
                for (SysMedGrp sysMedGrp : sysMedGrps) {
                    SysMedGrpLog sysMedGrpLog = new SysMedGrpLog();
                    if (sysMedGrp.getMedGrpCode()==null){
                        sysMedGrp.setMedGrpName("");
                    }
                    if (sysMedGrp.getMedGrpCode()==null){
                        sysMedGrp.setMedGrpCode("");
                    }
                    sysMedGrpLog.setMedGrp(sysMedGrp.getMedGrpName()+"("+sysMedGrp.getMedGrpCode()+")");
                    sysMedGrpLog.setOpter(loginUser.getUsername());
                    sysMedGrpLog.setOprtCont("删除：该数据已被逻辑删除");
                    medicalTeamLogMapper.addSysMedGrpLog(sysMedGrpLog);
                }
            }
        }
    }

    @Override
    public void updateStatus(SysMedGrp sysMedGrp) {
        Integer qyyh = medicalTeamMapper.queryUsedCountByDeptCode(sysMedGrp);
        if(qyyh > 0){
            throw new BizException(500,"当前医疗组存在已启用用户，无法停用！");
        }
        medicalTeamMapper.updateMedicalById(sysMedGrp);
    }

    @Override
    public void update(SysMedGrp sysMedGrp,LoginUser loginUser) {
        //校验当前医疗组是否已存在
        int count = medicalTeamMapper.getCountByGroupCode(sysMedGrp);
        if(count > 0){
            throw new BizException(400,"已存在此医疗组，请确认！");
        }

        if (sysMedGrp.getId() == null){
            sysMedGrp.setUpdtTime(new Date());
            medicalTeamMapper.insert(sysMedGrp);
            SysMedGrpLog sysMedGrpLog = new SysMedGrpLog();
            sysMedGrpLog.setMedGrp(sysMedGrp.getMedGrpName()+"("+sysMedGrp.getMedGrpCode()+")");
            sysMedGrpLog.setOpter(loginUser.getUsername());
            sysMedGrpLog.setOprtCont("新增；"+sysMedGrp.getId());
            medicalTeamLogMapper.addSysMedGrpLog(sysMedGrpLog);
        }else {
            SysMedGrp sysMedGrps = medicalTeamMapper.getById(sysMedGrp.getId());
            sysMedGrp.setUpdtTime(new Date());
            int addImport = medicalTeamMapper.updateSysMedGrp(sysMedGrp);
            if (addImport>0){
                SysMedGrpLog sysMedGrpLog = new SysMedGrpLog();
                sysMedGrpLog.setMedGrp(sysMedGrp.getMedGrpName()+"("+sysMedGrp.getMedGrpCode()+")");
                sysMedGrpLog.setOpter(loginUser.getUsername());
                if (!sysMedGrp.getGrpLeaderName().equals(sysMedGrps.getGrpLeaderName())){
                    sysMedGrpLog.setOprtCont("编辑：组长由"+sysMedGrps.getGrpLeaderName()+"变更为"+sysMedGrp.getGrpLeaderName());
                    medicalTeamLogMapper.addSysMedGrpLog(sysMedGrpLog);
                }
                if (!sysMedGrp.getMedGrpName().equals(sysMedGrps.getMedGrpName())){
                    sysMedGrpLog.setOprtCont("编辑：组名由"+sysMedGrps.getMedGrpName()+"变更为"+sysMedGrp.getMedGrpName());
                    medicalTeamLogMapper.addSysMedGrpLog(sysMedGrpLog);
                }
                if (!sysMedGrp.getDeptName().equals(sysMedGrps.getDeptName())){
                    sysMedGrpLog.setOprtCont("编辑：科室由"+sysMedGrps.getDeptName()+"变更为"+sysMedGrp.getDeptName());
                    medicalTeamLogMapper.addSysMedGrpLog(sysMedGrpLog);
                }
                if (!sysMedGrp.getValiFlag().equals(sysMedGrps.getValiFlag())){
                    sysMedGrpLog.setOprtCont("编辑：启用状态(0:启用 1:停用) 由"+sysMedGrps.getValiFlag()+"变更为"+sysMedGrp.getValiFlag());
                    medicalTeamLogMapper.addSysMedGrpLog(sysMedGrpLog);
                }
            }
        }
    }

    @Override
    public List<SysMedGrp> queryGroupList() {
        return medicalTeamMapper.queryGroupList();
    }

}
