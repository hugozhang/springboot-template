package com.winning.hmap.portal.auth.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.google.common.collect.Lists;
import com.google.common.base.Joiner;
import com.winning.hmap.portal.auth.dto.auth.req.query.*;
import com.winning.hmap.portal.auth.dto.auth.resp.*;
import com.winning.hmap.portal.auth.entity.*;
import com.winning.hmap.portal.auth.mapper.MedicalTeamMapper;
import com.winning.hmap.portal.auth.mapper.SysMedinsDeptDoctorMapper;
import com.winning.hmap.portal.auth.mapper.SysMedinsDeptMapper;
import com.winning.hmap.portal.auth.service.*;
import com.winning.hmap.portal.auth.dto.auth.req.put.AddDoctorParam;
import com.winning.hmap.portal.auth.dto.auth.req.put.UpdateDoctorParam;
import com.winning.hmap.portal.auth.mapper.SysDoctorMapper;
import com.winning.hmap.portal.util.BizConstant;
import com.winning.hmap.portal.util.UploadExcelUtil;
import me.about.widget.mybatis.plugin.page.model.PageParam;
import me.about.widget.mybatis.plugin.page.model.PageResult;
import me.about.widget.spring.mvc.exception.BizException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DoctorServiceImpl implements DoctorService {

    private static final Logger logger = LoggerFactory.getLogger(DoctorServiceImpl.class);

    @Resource
    private SysDoctorMapper sysDoctorMapper;

    @Resource
    private SysMedinsDeptDoctorMapper sysMedinsDeptDoctorMapper;

    @Resource
    private MedicalTeamMapper medicalTeamMapper;

    @Resource
    private SysMedinsDeptMapper sysMedinsDeptMapper;

    @Resource
    private MedinsService medinsService;

    @Resource
    private MedinsDeptService medinsDeptService;

    @Resource
    private MedicalTeamService medicalTeamService;

    @Resource
    private UserService userService;

    private List<SimpleDoctorMedinsDept> getSimpleDoctorDeptList(Long drId) {
        List<SimpleDoctorMedinsDept> doctorDeptList = Lists.newArrayList();
        sysMedinsDeptDoctorMapper
                .findDeptListByDoctorId(drId)
                .stream()
                .collect(Collectors.groupingBy(DoctorDept::getMedinsId, Collectors.toList()))
                .forEach((k, v) -> {
                    Medins medins = medinsService.getMedinsById(k);
                    if (medins != null) {
                        SimpleDoctorMedinsDept doctorMedinsDept = new SimpleDoctorMedinsDept();
                        doctorMedinsDept.setMedinsId(medins.getMedinsId());
                        doctorMedinsDept.setMedinsNo(medins.getMedinsNo());
                        doctorMedinsDept.setMedinsName(medins.getMedinsName());
                        v.forEach(item -> {
                            SimpleMedinsDept doctorDept = medinsDeptService.getSimpleMedinsDeptById(item.getDeptId());
                            doctorMedinsDept.getDeptList().add(doctorDept);
                        });
                        doctorDeptList.add(doctorMedinsDept);
                    }
                });
        return doctorDeptList;
    }

    private List<SysMedinsDept> getDeptList(Long drId) {
        MedinsDeptParam medinsDeptParam = new MedinsDeptParam();
        medinsDeptParam.setDrId(drId);
        return sysMedinsDeptMapper.findMedinsDeptList(medinsDeptParam);
    }

    @Override
    public List<SysMedGrp> getMedGrpList(Long drId) {
        return medicalTeamMapper.queryGrpByDrId(drId);
    }

    @Override
    public Doctor getDoctorById(Long drId) {

        List<SysMedinsDept> deptList = getDeptList(drId);

        List<SysMedGrp> medGrpList = getMedGrpList(drId);

        SysDoctor sysDoctor = sysDoctorMapper.getDoctorById(drId);
        if (sysDoctor != null) {
            Doctor doctor = new Doctor();
            doctor.setDrId(sysDoctor.getId());
            doctor.setDrCode(sysDoctor.getDrCode());
            doctor.setDrName(sysDoctor.getDrName());
            doctor.setHiDrCode(sysDoctor.getHiDrCode());
            doctor.setHiDrName(sysDoctor.getHiDrName());
            doctor.setDeptList(deptList);
            doctor.setMedGrpList(medGrpList);
            doctor.setTel(sysDoctor.getTel());
            doctor.setDutyName(sysDoctor.getDutyName());
            doctor.setDutyCode(sysDoctor.getDutyCode());
            doctor.setEmail(sysDoctor.getEmail());
            doctor.setId(sysDoctor.getId());
            doctor.setUsername(sysDoctor.getDrCode());
            return doctor;
        }
        return null;
    }

    @Override
    public Doctor getDoctorByCode(String drCode) {
        SysDoctor sysDoctor = sysDoctorMapper.getDoctorByCode(drCode);
        if (sysDoctor != null) {
            List<SysMedinsDept> deptList = getDeptList(sysDoctor.getId());

            Doctor doctor = new Doctor();
            doctor.setDrId(sysDoctor.getId());
            doctor.setDrCode(sysDoctor.getDrCode());
            doctor.setDrName(sysDoctor.getDrName());
            doctor.setDeptList(deptList);
            doctor.setId(sysDoctor.getId());
            return doctor;
        }
        return null;
    }

    @Override
    public List<Doctor> getDoctorByName(Doctor doctorDTO) {

        List<SimpleDoctorMedinsDept> doctorDeptList = getSimpleDoctorDeptList(doctorDTO.getDrId());

        List<SysDoctor> sysDoctor = sysDoctorMapper.getDoctorByName(doctorDTO.getDrName());
        List<Doctor> doctorList = new ArrayList<>();
        if (sysDoctor != null) {
            for (SysDoctor sysD : sysDoctor) {
                Doctor doctor = new Doctor();
                doctor.setDrId(sysD.getId());
                doctor.setDrCode(sysD.getDrCode());
                doctor.setDrName(sysD.getDrName());
                doctor.setHiDrCode(sysD.getHiDrCode());
                doctor.setHiDrName(sysD.getHiDrName());
                doctor.setTel(sysD.getTel());
                doctor.setEmail(sysD.getEmail());
                doctor.setMedinsList(doctorDeptList);
                doctor.setUsername(sysD.getDrCode());
                doctorList.add(doctor);
            }
            return doctorList;
        }
        return null;
    }

    @Override
    public List<Doctor> getDoctorByDrId(String doctorCode) {
        List<Doctor> doctorList = new ArrayList<>();
        List<SysDoctor> list = sysDoctorMapper.getDoctorByDrId(doctorCode);
        for (SysDoctor sysDoctor : list) {
            Doctor doctor = new Doctor();
            doctor.setDrId(sysDoctor.getId());
            doctor.setDrCode(sysDoctor.getDrCode());
            doctor.setDrName(sysDoctor.getDrName());
            doctor.setHiDrCode(sysDoctor.getDrCode());
            doctor.setHiDrName(sysDoctor.getDrName());
            doctor.setMedinsList(getSimpleDoctorDeptList(sysDoctor.getId()));
            doctorList.add(doctor);
        }
        return doctorList;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void add(AddDoctorParam addDoctorParam, LoginUser loginUser) {

        SysDoctor sysDoctor = sysDoctorMapper.getDoctorByCode(addDoctorParam.getDrCode());
        if (sysDoctor != null) {
            throw new BizException(400,addDoctorParam.getDrName() + ",医生已存在");
        }
        SysDoctor insert = insertDoctor(addDoctorParam, loginUser);

        //绑定新的科室
        sysMedinsDeptDoctorMapper.insertBatch(addDoctorParam.getDeptIds().stream().map(item -> {
            SysMedinsDeptDoctor sysMedinsDeptDoctor = new SysMedinsDeptDoctor();
            sysMedinsDeptDoctor.setDrId(insert.getId());
            sysMedinsDeptDoctor.setMedinsDeptId(item);
            sysMedinsDeptDoctor.setCrterId(loginUser.getUserId());
            sysMedinsDeptDoctor.setCrteTime(new Date());
            return sysMedinsDeptDoctor;
        }).collect(java.util.stream.Collectors.toList()));
    }

    private SysDoctor insertDoctor(AddDoctorParam addDoctorParam, LoginUser loginUser) {
        SysDoctor insert = new SysDoctor();
        insert.setDrName(addDoctorParam.getDrName());
        insert.setDrCode(addDoctorParam.getDrCode());
        insert.setHiDrCode(addDoctorParam.getHiDrCode());
        insert.setHiDrName(addDoctorParam.getHiDrName());
        insert.setDutyName(addDoctorParam.getDutyName());
        insert.setDutyCode(addDoctorParam.getDutyCode());
        insert.setTel(addDoctorParam.getTel());
        insert.setEmail(addDoctorParam.getEmail());
        insert.setDelFlag(BizConstant.DEL_FLAG_DEFAULT);
        insert.setCrteTime(new Date());
        insert.setCrterId(loginUser.getUserId());
        sysDoctorMapper.insert(insert);
        return insert;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(UpdateDoctorParam updateDoctorParam,LoginUser loginUser) {
        SysDoctor sysDoctor = sysDoctorMapper.getDoctorByCode(updateDoctorParam.getDrCode());
        if (sysDoctor != null && !sysDoctor.getId().equals(updateDoctorParam.getDrId())) {
            throw new BizException(400,updateDoctorParam.getDrName() + ",医生已存在");
        }
        //更新
        updateDoctor(updateDoctorParam, loginUser);
        //删除之前绑定的科室
        sysMedinsDeptDoctorMapper.deleteByDoctorId(updateDoctorParam.getDrId());
        //绑定新的科室
        sysMedinsDeptDoctorMapper.insertBatch(updateDoctorParam.getDeptIds().stream().map(item -> {
            SysMedinsDeptDoctor sysMedinsDeptDoctor = new SysMedinsDeptDoctor();
            sysMedinsDeptDoctor.setDrId(updateDoctorParam.getDrId());
            sysMedinsDeptDoctor.setMedinsDeptId(item);
            sysMedinsDeptDoctor.setCrterId(loginUser.getUserId());
            sysMedinsDeptDoctor.setCrteTime(new Date());
            return sysMedinsDeptDoctor;
        }).collect(java.util.stream.Collectors.toList()));

        //删除之前绑定的医疗组
        medicalTeamMapper.deleteSysMedGrpsByDeId(updateDoctorParam.getDrId());
        //绑定新的医疗组
        if(CollectionUtil.isNotEmpty(updateDoctorParam.getMedGrpIds())){
            medicalTeamMapper.insertBatch(updateDoctorParam.getMedGrpIds().stream().map(item -> {
                SysDoctorMedGrp sysDoctorMedGrp = new SysDoctorMedGrp();
                sysDoctorMedGrp.setDrId(updateDoctorParam.getDrId());
                sysDoctorMedGrp.setMedGrpId(item);
                sysDoctorMedGrp.setCrteTime(new Date());
                return sysDoctorMedGrp;
            }).collect(java.util.stream.Collectors.toList()));
        }
    }

    private void updateDoctor(UpdateDoctorParam updateDoctorParam, LoginUser loginUser) {
        SysDoctor update = new SysDoctor();
        update.setId(updateDoctorParam.getDrId());
        update.setDrName(updateDoctorParam.getDrName());
        update.setDrCode(updateDoctorParam.getDrCode());
        update.setHiDrCode(updateDoctorParam.getHiDrCode());
        update.setHiDrName(updateDoctorParam.getHiDrName());
        update.setDutyName(updateDoctorParam.getDutyName());
        update.setDutyCode(updateDoctorParam.getDutyCode());
        update.setTel(updateDoctorParam.getTel());
        update.setEmail(updateDoctorParam.getEmail());
        update.setUpdtTime(new Date());
        update.setUpdtrId(loginUser.getUserId());
        sysDoctorMapper.updateByPrimaryKey(update);
    }

    @Override
    public PageResult<Doctor> selectByPage(PageParam<DoctorParam> pageParam) {
        PageResult<Doctor> pageResult = sysDoctorMapper.selectByPage(pageParam);
        pageResult.getRows().forEach(item -> {
            Doctor doctor = getDoctorById(item.getDrId());
            if (doctor != null) {
                List<String> deptNameCollect = doctor.getDeptList().stream().map(SysMedinsDept::getDeptName).collect(Collectors.toList());
                item.setDeptName(Joiner.on(",").join(deptNameCollect));
                item.setDeptList(doctor.getDeptList());
                item.setMedGrpList(doctor.getMedGrpList());
            }
        });
        return pageResult;
    }

    @Override
    public List<DoctorExcel> findDoctorList(DoctorParam doctorParam) {
        List<DoctorExcel> doctorExcelList = new ArrayList<>();
        List<Doctor> doctorList = sysDoctorMapper.findDoctorList(doctorParam);
        for(int i=0,len = doctorList.size();i<len;i++) {
            Doctor doctor = doctorList.get(i);
            DoctorExcel doctorExcel = new DoctorExcel();
            doctorExcel.setId(i + 1);
            doctorExcel.setDrCode(doctor.getDrCode());
            doctorExcel.setDrName(doctor.getDrName());
            doctorExcel.setHiDrCode(doctor.getHiDrCode());
            doctorExcel.setHiDrName(doctor.getHiDrName());

            List<String> medinsDeptList = new ArrayList<>();

            List<SimpleDoctorMedinsDept> simpleDoctorDeptList = getSimpleDoctorDeptList(doctor.getDrId());
            for(SimpleDoctorMedinsDept simpleDoctorMedinsDept : simpleDoctorDeptList) {
                List<SimpleMedinsDept> deptList = simpleDoctorMedinsDept.getDeptList();
                String medinsName = "**" + simpleDoctorMedinsDept.getMedinsName().substring(2);
                for (SimpleMedinsDept simpleMedinsDept : deptList) {
                    String deptName = "**" +  simpleMedinsDept.getDeptName().substring(2);
                    medinsDeptList.add(medinsName + "/" + deptName);
                }
            }
            doctorExcel.setMedinsDepts(Joiner.on(",").join(medinsDeptList));
            doctorExcelList.add(doctorExcel);
        }
        return doctorExcelList;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void bindingMedinsDept(BindingMedinsDeptParam bindingMedinsDeptParam, LoginUser loginUser) {
        if (bindingMedinsDeptParam.getDoctorId() == null
                || bindingMedinsDeptParam.getDeptIds() == null
                || bindingMedinsDeptParam.getDeptIds().isEmpty()) {
            throw new BizException(400, "医生绑定科室参数错误");
        }
        //删除医生科室
        sysMedinsDeptDoctorMapper.deleteByDoctorId(bindingMedinsDeptParam.getDoctorId());
        //绑定医生科室
        sysMedinsDeptDoctorMapper.insertBatch(bindingMedinsDeptParam.getDeptIds().stream().map(item -> {
            SysMedinsDeptDoctor sysMedinsDeptDoctor = new SysMedinsDeptDoctor();
            sysMedinsDeptDoctor.setDrId(bindingMedinsDeptParam.getDoctorId());
            sysMedinsDeptDoctor.setCrterId(loginUser.getUserId());
            sysMedinsDeptDoctor.setCrteTime(new Date());
            sysMedinsDeptDoctor.setMedinsDeptId(item);
            return sysMedinsDeptDoctor;
        }).collect(java.util.stream.Collectors.toList()));
    }

    @Override
    @Transactional
    public String uploadExcel(MultipartFile file,LoginUser loginUser) throws IOException {
        if (file == null) {
            throw new BizException(400,"文件导入失败!");
        }

        String filename = file.getOriginalFilename();
        Object[] colunmsName = {"序号","医护人员编码","医护人员名称","医保医护人员编码","医保医护人员姓名","医护人员所属医院科室编码","医护人员所属医院科室","医护人员所属医疗组代码","联系方式","职务编码","邮箱地址"};
        Object[] colunms = {"sn","doctorCode","doctorName","hiCode","hiName","deptId","deptName","teamCode","tel","positionCode","email"};

        // 调用解析文件方法
        List<Map<String, Object>> excelList = UploadExcelUtil.parseRowCell(filename, file.getInputStream(),colunms,colunmsName);
        if (excelList.isEmpty()){
            throw new BizException(400,"文件未解析出数据!");
        }

        //校验医疗组Excel导入数据
        File resultFile = checkMedicalDoctor(excelList,loginUser);
        if(resultFile.length()>0){
            logger.info("文件导入成功，返回结果文件名: {}", resultFile.getName());
            return resultFile.getName();
        }
        return "";
    }

    private File checkMedicalDoctor(List<Map<String, Object>> excelList,LoginUser loginUser) throws IOException {
        List<SysMedinsDept> orgAllList = sysMedinsDeptMapper.findMedinsDeptList(new MedinsDeptParam());

        //校验科室编码是否不存在科室管理中
        Set<String> deptSet = orgAllList.stream().map(SysMedinsDept::getDeptCode).collect(Collectors.toSet());

        //已有医生集合
        List<SysDoctor> doctorList =  sysDoctorMapper.queryAllDoctor();
        Map<String, Long> doctorMap = doctorList.stream().collect(Collectors.toMap(SysDoctor::getDrCode, SysDoctor::getId));

        //已有医疗组集合
        List<SysMedGrp> groupCodeList = medicalTeamService.queryGroupList();
        Map<String, Long> groupCodeMap = groupCodeList.stream().collect(Collectors.toMap(SysMedGrp::getMedGrpCode, SysMedGrp::getId));

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
        List<SysDoctor> insertList = new ArrayList<>();
        List<SysDoctor> updateList = new ArrayList<>();
        for (int i = 0; i < excelList.size(); i++) {

            StringBuilder logicErroBuilder = new StringBuilder();
            StringBuilder isEmptyBuilder = new StringBuilder();
            DoctorInfoDto doctorInfoDto = BeanUtil.mapToBean(excelList.get(i),DoctorInfoDto.class,true);
            String[] deptIds = doctorInfoDto.getDeptId().split("，");
            String[] teamIds = doctorInfoDto.getTeamCode().split("，");

            //科室校验
            List<SysMedinsDeptDoctor> deptDoctorList = Arrays.stream(deptIds)
                    .filter(deptId -> !deptId.trim().isEmpty())
                    .map(deptId -> {
                        if (!deptSet.contains(deptId)) {
                            logicErroBuilder.append("科室编码[" + deptId + "]不存在科室管理中，");
                            return null;
                        } else {
                            SysMedinsDeptDoctor sysMedinsDeptDoctor = new SysMedinsDeptDoctor();
                            sysMedinsDeptDoctor.setId(sysMedinsDeptMapper.getMedinsDeptByCode(deptId).getId());
                            sysMedinsDeptDoctor.setCrteTime(new Date());
                            sysMedinsDeptDoctor.setCrterId(loginUser.getUserId());
                            return sysMedinsDeptDoctor;
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            doctorInfoDto.setDoctorRelatedDept(deptDoctorList);

            //字段校验
            if(doctorInfoDto.getDeptId().isEmpty()){isEmptyBuilder.append("科室编码、");}
            if(doctorInfoDto.getDoctorCode().isEmpty()){isEmptyBuilder.append("医护人员编码、");}
            if(doctorInfoDto.getDoctorName().isEmpty()){isEmptyBuilder.append("医护人员名称、");}
            if(doctorInfoDto.getHiCode().isEmpty()){isEmptyBuilder.append("医保医护人员编码、");}
            if(doctorInfoDto.getHiName().isEmpty()){isEmptyBuilder.append("医保医护人员名称、");}

            //医疗组校验
            List<DoctorMedGrpParam> doctorMedGrplist = Arrays.stream(teamIds)
                    .filter(teamId -> !teamId.trim().isEmpty())
                    .map(teamId -> {
                        if (!groupCodeMap.containsKey(teamId)) {
                            logicErroBuilder.append("医疗组编码[")
                                            .append(teamId)
                                            .append("]不存在医疗组管理中，");
                            return null;
                        } else {
                            DoctorMedGrpParam doctorMedGrpParam = new DoctorMedGrpParam();
                            doctorMedGrpParam.setMedGrpId(groupCodeMap.get(teamId));
                            return doctorMedGrpParam;
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            doctorInfoDto.setDoctorRelatedMedGroup(doctorMedGrplist);

            if(!"".contentEquals(isEmptyBuilder)){
                isEmptyBuilder.deleteCharAt(isEmptyBuilder.length() - 1);
                isEmptyBuilder.append(" 不能为空，");
            }

            if("".contentEquals(isEmptyBuilder) && "".contentEquals(logicErroBuilder)){
                String docterCode  = doctorInfoDto.getDoctorCode();
                SysDoctor sysDoctor = new SysDoctor();
                sysDoctor.setId(doctorMap.get(docterCode));
                sysDoctor.setDrCode(docterCode);
                sysDoctor.setDrName(doctorInfoDto.getDoctorName());
                sysDoctor.setHiDrCode(doctorInfoDto.getHiCode());
                sysDoctor.setHiDrName(doctorInfoDto.getHiName());
                sysDoctor.setDutyCode(doctorInfoDto.getPositionCode());
                sysDoctor.setTel(doctorInfoDto.getTel());
                sysDoctor.setEmail(doctorInfoDto.getEmail());
                sysDoctor.setDoctorRelatedDept(doctorInfoDto.getDoctorRelatedDept());
                sysDoctor.setDoctorRelatedMedGroup(doctorInfoDto.getDoctorRelatedMedGroup());

                if(doctorMap.containsKey(docterCode)){
                    sysDoctor.setUpdtTime(new Date());
                    sysDoctor.setUpdtrId(loginUser.getUserId());
                    doctorInfoDto.setId(doctorMap.get(docterCode).toString());
                    updateList.add(sysDoctor);
                }else{
                    sysDoctor.setDelFlag("0");
                    sysDoctor.setCrteTime(new Date());
                    sysDoctor.setCrterId(loginUser.getUserId());
                    insertList.add(sysDoctor);
                }
            }else{
                bw.write("序号："+doctorInfoDto.getSn()+", 结论【"+ logicErroBuilder + isEmptyBuilder +"无法导入】\t");
                bw.newLine();
            }
        }
        bw.close();
        medicalDoctorIntoDb(updateList,insertList);
        return resultFile;
    }

    protected void medicalDoctorIntoDb(List<SysDoctor> updateList, List<SysDoctor> insertList) {
        //已存在则更新数据
        if(!updateList.isEmpty()){
            sysDoctorMapper.updateDoctorDtos(updateList);
            //绑定与科室关联关系
            List<Long> list = updateList.stream()
                    .map(SysDoctor::getId).collect(Collectors.toList());
            sysDoctorMapper.deleteUserDeptByList(updateList);
            sysDoctorMapper.deleteMedicalTeams(list);
            for (SysDoctor dto : updateList) {
                sysDoctorMapper.bindingDoctorDept(dto.getDoctorRelatedDept(),dto.getId());
                if(!dto.getDoctorRelatedMedGroup().isEmpty()){
                    sysDoctorMapper.bindingDoctorMedTeam(dto.getDoctorRelatedMedGroup(),dto.getId());
                }
            }
        }

        //不存在则新增数据
        if(!insertList.isEmpty()){
            insertList = new ArrayList<>(insertList.stream().collect(Collectors.toMap(
                    SysDoctor::getDrCode,
                    t -> t,
                    (existing, replacement) -> replacement,
                    LinkedHashMap::new
            )).values());
            sysDoctorMapper.insertDoctorList(insertList);
            for (SysDoctor dto : insertList) {
                sysDoctorMapper.bindingDoctorDept(dto.getDoctorRelatedDept(),dto.getId());
                if(!dto.getDoctorRelatedMedGroup().isEmpty()){
                    sysDoctorMapper.bindingDoctorMedTeam(dto.getDoctorRelatedMedGroup(),dto.getId());
                }
            }
        }
    }

    @Override
    public List<DoctorItem> findDeptDrOptions(DoctorParam doctorParam) {
        List<DoctorItem> list = new ArrayList<>();
        this.findDoctorList(doctorParam).forEach(e -> {
            DoctorItem item = new DoctorItem();
            item.setCode(e.getDrCode());
            item.setName(e.getDrName());
            list.add(item);
        });
        return list;
    }

    @Override
    public List<DoctorItem> options(DoctorParam doctorParam) {
        return sysDoctorMapper.options(doctorParam);
    }

    @Override
    public void disable(Long drId) {
        SysUser sysUser = userService.getUserByDrId(drId);
        if(sysUser != null){
            throw new BizException(400,"该医生已绑定启用用户，无法禁用");
        }
        sysDoctorMapper.disable(drId);
    }

    @Override
    public void enable(Long drId) {
        sysDoctorMapper.enable(drId);
    }

}
