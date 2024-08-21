package com.winning.hmap.portal.auth.service.impl;

import com.winning.hmap.portal.auth.dto.auth.req.query.MedinsParam;
import com.winning.hmap.portal.auth.service.MedinsDeptService;
import com.winning.hmap.portal.auth.service.MedinsService;
import com.winning.hmap.portal.auth.dto.auth.req.put.AddMedinsParam;
import com.winning.hmap.portal.auth.dto.auth.req.put.UpdateMedinsParam;
import com.winning.hmap.portal.auth.dto.auth.resp.LoginUser;
import com.winning.hmap.portal.auth.dto.auth.resp.Medins;
import com.winning.hmap.portal.auth.mapper.SysMedinsMapper;
import com.winning.hmap.portal.auth.entity.SysMedins;
import com.winning.hmap.portal.auth.dto.auth.resp.OptionItem;
import com.winning.hmap.portal.util.BizConstant;
import me.about.widget.mybatis.plugin.page.model.PageParam;
import me.about.widget.mybatis.plugin.page.model.PageResult;
import me.about.widget.spring.mvc.exception.BizException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class MedinsServiceImpl implements MedinsService {

    @Resource
    private SysMedinsMapper sysMedinsMapper;

    @Resource
    private MedinsDeptService medinsDeptService;

    @Override
    public Medins getMedinsById(Long medinsId) {
        SysMedins sysMedins = sysMedinsMapper.getMedinsById(medinsId);
        if (sysMedins != null) {
            Medins medins = new Medins();
            medins.setMedinsId(sysMedins.getId());
            medins.setMedinsName(sysMedins.getMedinsName());
            medins.setMedinsNo(sysMedins.getMedinsNo());
            medins.setMedinsLv(sysMedins.getMedinsLv());
            medins.setMedinsType(sysMedins.getMedinsType());
            medins.setMedinsCat(sysMedins.getMedinsCat());
            return medins;
        }
        return null;
    }

    @Override
    public void add(AddMedinsParam addMedinsParam, LoginUser loginUser) {
        SysMedins sysMedins = sysMedinsMapper.getMedinsByCode(addMedinsParam.getMedinsNo());
        if (sysMedins != null) {
            throw new BizException(400,addMedinsParam.getMedinsNo() + ", 机构编号已存在");
        }

        List<String> medinsIdList = sysMedinsMapper.medinsIdList();
        if(!medinsIdList.isEmpty()
                && BizConstant.DEL_FLAG_DEFAULT.equals(addMedinsParam.getDelFlag())
                && !medinsIdList.contains(addMedinsParam.getMedinsNo())){
            throw new BizException(400,"仅能启用一个医疗机构，目前已存在启用的机构");
        }
        SysMedins insert = new SysMedins();
        insert.setMedinsName(addMedinsParam.getMedinsName());
        insert.setMedinsNo(addMedinsParam.getMedinsNo());
        insert.setMedinsLv(addMedinsParam.getMedinsLv());
        insert.setMedinsType(addMedinsParam.getMedinsType());
        insert.setMedinsCat(addMedinsParam.getMedinsCat());
        insert.setDelFlag(addMedinsParam.getDelFlag());
        insert.setCrteTime(new Date());
        insert.setCrterId(loginUser.getUserId());
        sysMedinsMapper.insert(insert);
    }

    @Override
    public void update(UpdateMedinsParam updateMedinsParam, LoginUser loginUser) {
        SysMedins sysMedins = sysMedinsMapper.getMedinsByCode(updateMedinsParam.getMedinsNo());
        if (sysMedins != null && !sysMedins.getId().equals(updateMedinsParam.getMedinsId())) {
            throw new BizException(400,updateMedinsParam.getMedinsNo() + ", 机构编号已存在");
        }

        List<String> medinsIdList = sysMedinsMapper.medinsIdList();
        if(!medinsIdList.isEmpty()
                && BizConstant.DEL_FLAG_DEFAULT.equals(updateMedinsParam.getDelFlag())
                && !medinsIdList.contains(updateMedinsParam.getMedinsNo())){
            throw new BizException(400,"仅能启用一个医疗机构，目前已存在启用的机构");
        }

        if(!medinsDeptService.medinsDeptIdList().isEmpty()
                && medinsIdList.contains(updateMedinsParam.getMedinsNo())
                && !BizConstant.DEL_FLAG_DEFAULT.equals(updateMedinsParam.getDelFlag())){
            throw new BizException(400,"此机构下存在正在使用的科室，无法停用！");
        }
        SysMedins update = new SysMedins();
        update.setId(updateMedinsParam.getMedinsId());
        update.setMedinsName(updateMedinsParam.getMedinsName());
        update.setMedinsNo(updateMedinsParam.getMedinsNo());
        update.setMedinsLv(updateMedinsParam.getMedinsLv());
        update.setMedinsType(updateMedinsParam.getMedinsType());
        update.setMedinsCat(updateMedinsParam.getMedinsCat());
        update.setDelFlag(updateMedinsParam.getDelFlag());
        update.setUpdtTime(new Date());
        update.setUpdtrId(loginUser.getUserId());
        sysMedinsMapper.updateByPrimaryKey(update);
    }

    @Override
    public void disable(Long medinsId, LoginUser loginUser) {
        if(!medinsDeptService.medinsDeptIdList().isEmpty()){
            throw new BizException(400,"此机构下存在正在使用的科室，无法停用！");
        }
        sysMedinsMapper.disable(medinsId);
    }

    @Override
    public void enable(Long medinsId, LoginUser loginUser) {
        SysMedins sysMedins = sysMedinsMapper.getMedinsById(medinsId);
        List<String> medinsIdList = sysMedinsMapper.medinsIdList();
        if(!medinsIdList.isEmpty() && !medinsIdList.contains(sysMedins.getMedinsNo())){
            throw new BizException(400,"仅能启用一个医疗机构，目前已存在启用的机构");
        }
        sysMedinsMapper.enable(medinsId);
    }

    @Override
    public PageResult<Medins> selectByPage(PageParam<MedinsParam> pageParam) {
        return sysMedinsMapper.selectByPage(pageParam);
    }

    @Override
    public List<OptionItem> options(MedinsParam medinsParam) {
        return sysMedinsMapper.options(medinsParam);
    }

    @Override
    public List<String> medinsIdList() {
        return sysMedinsMapper.medinsIdList();
    }
}
