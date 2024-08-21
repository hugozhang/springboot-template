package com.winning.hmap.portal.auth.service;

import com.winning.hmap.portal.auth.dto.auth.req.query.MedGrpParam;
import com.winning.hmap.portal.auth.dto.auth.resp.LoginUser;
import com.winning.hmap.portal.auth.dto.auth.resp.MedGrp;
import com.winning.hmap.portal.auth.entity.SysMedGrp;
import me.about.widget.mybatis.plugin.page.model.PageParam;
import me.about.widget.mybatis.plugin.page.model.PageResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MedicalTeamService {
    PageResult<MedGrp> query(PageParam<MedGrpParam> pageParam);

    String uploadExcel(MultipartFile file) throws IOException;

    void delete(List<String> list,LoginUser loginUser);

    void updateStatus(SysMedGrp medicalMaintain);

    void update(SysMedGrp medicalMaintain,LoginUser loginUser);

    List<SysMedGrp> queryGroupList();

}
