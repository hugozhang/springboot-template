package com.winning.hmap.portal.auth.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.winning.hmap.portal.auth.dto.auth.req.put.UploadMedinsDept;
import com.winning.hmap.portal.auth.dto.auth.req.query.DoctorInfoDto;
import com.winning.hmap.portal.auth.dto.auth.req.query.MedinsDeptParam;
import com.winning.hmap.portal.auth.dto.auth.req.query.MedinsParam;
import com.winning.hmap.portal.auth.dto.auth.req.query.UserParam;
import com.winning.hmap.portal.auth.dto.auth.resp.*;
import com.winning.hmap.portal.auth.entity.SysMedGrp;
import com.winning.hmap.portal.auth.entity.SysMedinsDept;
import com.winning.hmap.portal.auth.service.MedinsDeptService;
import com.winning.hmap.portal.auth.service.MedinsService;
import com.winning.hmap.portal.auth.dto.auth.req.put.AddMedinsDeptParam;
import com.winning.hmap.portal.auth.dto.auth.req.put.UpdateMedinsDeptParam;
import com.winning.hmap.portal.auth.mapper.SysMedinsDeptMapper;
import com.winning.hmap.portal.auth.service.UserService;
import com.winning.hmap.portal.util.BizConstant;
import com.winning.hmap.portal.util.UploadExcelUtil;
import lombok.extern.slf4j.Slf4j;
import me.about.widget.mybatis.plugin.page.model.PageParam;
import me.about.widget.mybatis.plugin.page.model.PageResult;
import me.about.widget.spring.mvc.exception.BizException;
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
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MedinsDeptServiceImpl implements MedinsDeptService {

    @Resource
    private SysMedinsDeptMapper sysMedinsDeptMapper;

    @Resource
    private MedinsService medinsService;

    @Resource
    private UserService userService;

    @Override
    public SimpleMedinsDept getSimpleMedinsDeptById(Long deptId) {
        SysMedinsDept sysMedinsDept = sysMedinsDeptMapper.getMedinsDeptById(deptId);
        if (sysMedinsDept != null) {
            SimpleMedinsDept simpleMedinsDept = new SimpleMedinsDept();
            simpleMedinsDept.setDeptId(sysMedinsDept.getId());
            simpleMedinsDept.setDeptName(sysMedinsDept.getDeptName());
            simpleMedinsDept.setDeptCode(sysMedinsDept.getDeptCode());
            simpleMedinsDept.setHiDeptCode(sysMedinsDept.getHiDeptCode());
            simpleMedinsDept.setHiDeptName(sysMedinsDept.getHiDeptName());
            return simpleMedinsDept;
        }
        return null;
    }

    @Override
    public MedinsDept getMedinsDeptById(Long deptId) {
        SysMedinsDept sysMedinsDept = sysMedinsDeptMapper.getMedinsDeptById(deptId);
        if (sysMedinsDept != null) {
            return buildMedinsDept(sysMedinsDept);
        }
        return null;
    }

    @Override
    public MedinsDept getMedinsDeptByCode(String deptCode) {
        SysMedinsDept sysMedinsDept = sysMedinsDeptMapper.getMedinsDeptByCode(deptCode);
        if (sysMedinsDept != null) {
            return buildMedinsDept(sysMedinsDept);
        }
        return null;
    }

    @Override
    public void add(AddMedinsDeptParam addMedinsDeptParam, LoginUser loginUser) {

        //校验医疗机构编码是否存在组织机构管理中
        List<OptionItem> medinsIdList = medinsService.options(new MedinsParam());
        if(medinsIdList.isEmpty()){
            throw new BizException(400, "机构管理中不存在有效的组织机构，请先添加");
        }

        SysMedinsDept sysMedinsDept = sysMedinsDeptMapper.getMedinsDeptByCode(addMedinsDeptParam.getDeptCode());
        if (sysMedinsDept != null) {
            throw new BizException(400,addMedinsDeptParam.getDeptCode() + " , 该科室编码已存在");
        }

        SysMedinsDept insert = new SysMedinsDept();
        insert.setMedinsId(medinsIdList.get(0).getValue());
        insert.setDeptName(addMedinsDeptParam.getDeptName());
        insert.setDeptCode(addMedinsDeptParam.getDeptCode());
        insert.setHiDeptName(addMedinsDeptParam.getHiDeptName());
        insert.setHiDeptCode(addMedinsDeptParam.getHiDeptCode());
        insert.setParentId(addMedinsDeptParam.getParentId());
        insert.setDelFlag(BizConstant.DEL_FLAG_DEFAULT);
        insert.setCrteTime(new Date());
        insert.setCrterId(loginUser.getUserId());
        sysMedinsDeptMapper.insert(insert);
    }

    @Override
    public void update(UpdateMedinsDeptParam updateMedinsDeptParam,LoginUser loginUser) {
        SysMedinsDept sysMedinsDept = sysMedinsDeptMapper.getMedinsDeptByCode(updateMedinsDeptParam.getDeptCode());
        if (sysMedinsDept != null && !sysMedinsDept.getId().equals(updateMedinsDeptParam.getDeptId())) {
            throw new BizException(400,updateMedinsDeptParam.getDeptCode() + " , 该科室编码已存在");
        }
        SysMedinsDept update = new SysMedinsDept();
        update.setId(updateMedinsDeptParam.getDeptId());
        update.setDeptName(updateMedinsDeptParam.getDeptName());
        update.setDeptCode(updateMedinsDeptParam.getDeptCode());
        update.setHiDeptCode(updateMedinsDeptParam.getHiDeptCode());
        update.setHiDeptName(updateMedinsDeptParam.getHiDeptName());
        update.setParentId(updateMedinsDeptParam.getParentId());
        update.setUpdtTime(new Date());
        update.setUpdtrId(loginUser.getUserId());
        sysMedinsDeptMapper.updateByPrimaryKey(update);
    }

    @Override
    public void disable(Long deptId,LoginUser loginUser) {
        UserParam userParam = new UserParam();
        userParam.setDeptId(deptId);
        if(!userService.findUserList(userParam).isEmpty()){
            throw new BizException(400,"该科室下存在用户，无法停用");
        }
        sysMedinsDeptMapper.disable(deptId);
    }

    @Override
    public void enable(Long deptId,LoginUser loginUser) {
        sysMedinsDeptMapper.enable(deptId);
    }

    @Override
    public PageResult<MedinsDept> selectByPage(PageParam<MedinsDeptParam> pageParam) {
        PageResult<MedinsDept> result = new PageResult<>();
        PageResult<SysMedinsDept> sysMedinsDeptPageResult = sysMedinsDeptMapper.selectByPage(pageParam);
        result.setTotal(sysMedinsDeptPageResult.getTotal());
        result.setTotalPage(sysMedinsDeptPageResult.getTotalPage());
        result.setRows(sysMedinsDeptPageResult.getRows()
                .stream()
                .map(this::buildMedinsDept).collect(Collectors.toList()));
        return result;
    }

    private MedinsDept buildMedinsDept(SysMedinsDept sysMedinsDept) {
        MedinsDept medinsDept = new MedinsDept();
        medinsDept.setDeptId(sysMedinsDept.getId());
        medinsDept.setDeptName(sysMedinsDept.getDeptName());
        medinsDept.setDeptCode(sysMedinsDept.getDeptCode());
        medinsDept.setHiDeptName(sysMedinsDept.getHiDeptName());
        medinsDept.setHiDeptCode(sysMedinsDept.getHiDeptCode());
//        medinsDept.setMedins(medinsService.getMedinsById(sysMedinsDept.getMedinsId()));
        MedinsDept medinsDeptById = getMedinsDeptById(sysMedinsDept.getParentId());
        if(medinsDeptById == null){
            medinsDept.setParentId(0L);
            medinsDept.setParentName("-");
        } else {
            medinsDept.setParentId(medinsDeptById.getDeptId());
            medinsDept.setParentName(medinsDeptById.getDeptName());
        }

        medinsDept.setDelFlag(sysMedinsDept.getDelFlag());
        return medinsDept;
    }

    @Override
    public List<MedinsDeptLevel> findMedinsDeptCascadeList(MedinsDeptParam medinsDeptParam) {
        List<MedinsDeptLevel> medinsDeptLevelList = sysMedinsDeptMapper.findMedinsDeptList(medinsDeptParam).stream().map(sysMedinsDept -> {
            MedinsDeptLevel medinsDept = new MedinsDeptLevel();
            medinsDept.setCode(sysMedinsDept.getCode());
            medinsDept.setDeptId(sysMedinsDept.getId());
            medinsDept.setLabel(sysMedinsDept.getDeptName());
            medinsDept.setHiDeptCode(sysMedinsDept.getHiDeptCode());
            medinsDept.setValue(sysMedinsDept.getCode());
            medinsDept.setParentId(sysMedinsDept.getParentId());
            medinsDept.setHiDeptName(sysMedinsDept.getHiDeptName());
            medinsDept.setLabel(sysMedinsDept.getDeptName());
            medinsDept.setDeptCode(sysMedinsDept.getDeptCode());
            return medinsDept;
        }).collect(Collectors.toList());

        List<MedinsDeptLevel> filterList = medinsDeptLevelList.stream().filter(e -> e.getParentId() != null).collect(Collectors.toList());
        if (filterList.isEmpty()) {
            return medinsDeptLevelList;
        }
        return recursion(0L,medinsDeptLevelList);
    }

    @Override
    public List<SysMedGrp> queryMedGrpByDeptCodes(DoctorInfoDto doctorInfoDto) {
        List<SysMedGrp> list = new ArrayList<>();
        List<String> deptList =  doctorInfoDto.getDeptList();
        if(deptList != null && !deptList.isEmpty()){
            list = sysMedinsDeptMapper.queryMedGrpByDeptCodes(deptList);
        }
        return list;
    }

    private List<MedinsDeptLevel> recursion(Long parentId,List<MedinsDeptLevel> nodes) {
        return nodes.stream().filter(item -> item.getParentId().equals(parentId))
                .peek((m) -> m.setChildren(recursion(m.getDeptId(),nodes)))
                .collect(Collectors.toList());
    }

    @Override
    public List<SysMedinsDept> medinsDeptIdList() {
        return sysMedinsDeptMapper.medinsDeptIdList();
    }

    @Override
    @Transactional
    public String uploadExcel(MultipartFile file, LoginUser user) throws IOException {
        if (file == null) {
            throw new BizException(400,"文件导入失败");
        }

        String filename = file.getOriginalFilename();
        if (filename == null) {
            throw new BizException(400,"文件导入失败");
        }

        Object[] colunmsName = {"序号","科室编码","科室名称", "上级科室编码","上级科室名称","医保科室编码","医保科室名称"};
        Object[] colunms = {"sn","deptCode","deptName","parentCode","parentName","hiDeptCode","hiDeptName"};

        // 调用解析文件方法
        List<Map<String, Object>> excelList = UploadExcelUtil.parseRowCell(filename, file.getInputStream(),colunms,colunmsName);
        if (excelList.isEmpty()) {
            throw new BizException(500,"文件未解析出数据");
        }

        //校验科室Excel导入数据
        File resultFile = checkMedicalDept(excelList,user);
        if(resultFile.length()>0){
            return resultFile.getName();
        }
        return "";
    }

    private File checkMedicalDept(List<Map<String,Object>> excelList,LoginUser user) throws IOException {

        //校验医疗机构编码是否存在组织机构管理中
        List<OptionItem> medinsIdList = medinsService.options(new MedinsParam());
        if(medinsIdList.isEmpty()){
            throw new BizException(400, "组织机构管理中不存在有效的机构，无法导入");
        }

        //校验医疗机构编码是否存在组织机构管理中
        List<SysMedinsDept> orgAllList = sysMedinsDeptMapper.findMedinsDeptList(new MedinsDeptParam());

        //校验科室编码是否不存在科室管理中
        Set<String> deptSet = orgAllList.stream()
                .map(SysMedinsDept::getDeptCode).collect(Collectors.toSet());

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
        BufferedWriter bw=new BufferedWriter(new FileWriter(resultFile));
        List<UploadMedinsDept> insertList = new ArrayList<>();
        List<UploadMedinsDept> updateList = new ArrayList<>();

        Date date = new Date();
        for (Map<String, Object> map : excelList) {
            StringBuilder builder = new StringBuilder();
            UploadMedinsDept deptDto = BeanUtil.mapToBean(map,UploadMedinsDept.class, true);
            deptDto.setMedinsId(medinsIdList.get(0).getValue());

            // 字段校验
            if (deptDto.getDeptCode().isEmpty()){ builder.append("科室编码、");}
            if (deptDto.getDeptName().isEmpty()){ builder.append("科室名称、");}
            if (deptDto.getHiDeptCode().isEmpty()){ builder.append("医保科室编码、");}
            if (deptDto.getHiDeptName().isEmpty()){ builder.append("医保科室名称、");}

            if (builder.length() > 0) {
                builder.deleteCharAt(builder.length() - 1);
                builder.append(" 不能为空，");
                bw.write("序号：" + deptDto.getSn() + ", 结论【" + builder + "无法导入】\t");
                bw.newLine();
                continue;
            }

            // 校验科室编码是否重复
            if (deptSet.contains(deptDto.getDeptCode())) {
                deptDto.setUpdtTime(date);
                deptDto.setUpdtrId(user.getUserId());
                updateList.add(deptDto);
            } else {
                deptDto.setCrteTime(date);
                deptDto.setCrterId(user.getUserId());
                insertList.add(deptDto);
            }
        }
        bw.close();
        medinsDeptIntoDb(updateList,insertList);
        return resultFile;
    }

    protected void medinsDeptIntoDb(List<UploadMedinsDept> updateList,List<UploadMedinsDept> insertList) {

        //查询数据库中已有的所有科室
        List<SysMedinsDept> existingDepts = new ArrayList<>(sysMedinsDeptMapper.medinsDeptIdList());
        List<SysMedinsDept> mergedList;

        //已存在则更新数据
        if(!updateList.isEmpty()){
            sysMedinsDeptMapper.updateDeptDtos(updateList.stream().map(this::convertToDept).collect(Collectors.toList()));
        }

        //不存在则更新数据
        if(!insertList.isEmpty()){
            mergedList = insertList.stream().collect(Collectors.toMap(UploadMedinsDept::getDeptCode, Function.identity()
                                      ,BinaryOperator.maxBy(Comparator.comparingInt(UploadMedinsDept::getSn))))
                                   .values().stream().map(this::convertToDept).collect(Collectors.toList());
            sysMedinsDeptMapper.insertDeptList(mergedList);
            existingDepts.addAll(mergedList);
        }

        //查询所有科室
        Map<String,Long> deptCodeIdMap = existingDepts.stream()
                .collect(Collectors.toMap(SysMedinsDept::getDeptCode, SysMedinsDept::getId));

        //更新父节点id
        updateList.addAll(insertList);
        updateList.forEach(item -> {
            Long parentId = deptCodeIdMap.get(item.getParentCode());
            item.setParentId(parentId == null ? 0L : parentId);
        });
        if(!updateList.isEmpty()){
            sysMedinsDeptMapper.updateDepts(updateList);
        }
    }

    private SysMedinsDept convertToDept(UploadMedinsDept uploadMedinsDept) {
        SysMedinsDept sysMedinsDept = new SysMedinsDept();
        sysMedinsDept.setId(uploadMedinsDept.getId());
        sysMedinsDept.setMedinsId(uploadMedinsDept.getMedinsId());
        sysMedinsDept.setDeptCode(uploadMedinsDept.getDeptCode());
        sysMedinsDept.setDeptName(uploadMedinsDept.getDeptName());
        sysMedinsDept.setHiDeptCode(uploadMedinsDept.getHiDeptCode());
        sysMedinsDept.setHiDeptName(uploadMedinsDept.getHiDeptName());
        sysMedinsDept.setParentId(uploadMedinsDept.getParentId());
        sysMedinsDept.setDelFlag(BizConstant.DEL_FLAG_DEFAULT);
        sysMedinsDept.setCrteTime(uploadMedinsDept.getCrteTime());
        sysMedinsDept.setCrterId(uploadMedinsDept.getCrterId());
        sysMedinsDept.setUpdtTime(uploadMedinsDept.getUpdtTime());
        sysMedinsDept.setUpdtrId(uploadMedinsDept.getUpdtrId());
        return sysMedinsDept;
    }

    @Override
    public List<SysMedinsDept> queryDeptList(SysMedinsDept sysMedinsDept) {
        return sysMedinsDeptMapper.queryDeptList(sysMedinsDept);
    }

}
