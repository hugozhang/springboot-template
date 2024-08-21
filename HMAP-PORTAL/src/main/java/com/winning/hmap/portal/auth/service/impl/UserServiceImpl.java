package com.winning.hmap.portal.auth.service.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollectionUtil;
import com.google.common.base.Joiner;
import com.winning.hmap.portal.auth.dto.auth.req.put.AddUserParam;
import com.winning.hmap.portal.auth.dto.auth.req.put.UpdateUserParam;
import com.winning.hmap.portal.auth.dto.auth.req.query.UserParam;
import com.winning.hmap.portal.auth.dto.auth.resp.*;
import com.winning.hmap.portal.auth.entity.*;
import com.winning.hmap.portal.auth.mapper.*;
import com.winning.hmap.portal.auth.service.DoctorService;
import com.winning.hmap.portal.auth.service.RoleService;
import com.winning.hmap.portal.auth.service.UserService;
import com.winning.hmap.portal.util.BizConstant;
import com.winning.hmap.portal.util.EncryptUtils;
import me.about.widget.mybatis.plugin.page.model.PageParam;
import me.about.widget.mybatis.plugin.page.model.PageResult;
import me.about.widget.spring.mvc.exception.BizException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private SysUserRoleMapper sysUserRoleMapper;

    @Resource
    private SysDoctorMapper sysDoctorMapper;

    @Resource
    private DoctorService doctorService;

    @Resource
    private RoleService roleService;

    @Resource
    private SysMedinsDeptDoctorMapper sysMedinsDeptDoctorMapper;

    @Override
    public List<UserRole> getUserRoleList(Long userId) {
        return sysUserRoleMapper.findRoleListByUserId(userId).stream().map(sysUserRole -> {
            UserRole userRole = new UserRole();
            Role role = roleService.getRoleById(sysUserRole.getRoleId());
            if (role != null) {
                userRole.setRoleId(role.getRoleId());
                userRole.setRoleName(role.getRoleName());
            }
            return userRole;
        }).collect(Collectors.toList());
    }

    @Override
    public SysUser getUserByLoginName(String loginName) {
        return sysUserMapper.getUserByLoginName(loginName);
    }

    @Override
    public SysUser getUserByDrId(Long drId) {
        return sysUserMapper.getUserByDrId(drId);
    }

    @Override
    public void add(AddUserParam addUserParam, LoginUser loginUser) throws Exception {

        SysUser userByLoginName = getUserByLoginName(addUserParam.getLoginName());
        if (userByLoginName != null) {
            throw new BizException(400, addUserParam.getLoginName() + ",该登录名已存在");
        }

        SysUser sysUser = new SysUser();
        sysUser.setLoginName(addUserParam.getLoginName());
        sysUser.setPassword(EncryptUtils.getInstance().encrypt(Base64.decodeStr(addUserParam.getPassword())));
        sysUser.setUsername(addUserParam.getDrName());
        //绑定医生
        sysUser.setDrId(addUserParam.getDrId());
        sysUser.setDelFlag(BizConstant.DEL_FLAG_DEFAULT);
        sysUser.setCrteTime(new Date());
        sysUser.setCrterId(loginUser.getUserId());
        sysUserMapper.insert(sysUser);
        //绑定角色
        extracted(addUserParam.getRoleIds(), sysUser, loginUser);
    }

    @Override
    public void addUserBatch(LoginUser loginUser) throws Exception {
        //查询未被使用的用户工号
        List<SysDoctor> sysDoctors = sysDoctorMapper.queryUserNotexit();
        List<SysUser> sysUsers = new ArrayList<>();
        for (SysDoctor sysDoctor : sysDoctors) {
            SysUser sysUser = new SysUser();
            sysUser.setCrteTime(new Date());
            sysUser.setUsername(sysDoctor.getDrName());
            sysUser.setDelFlag(BizConstant.DEL_FLAG_DEFAULT);
            sysUser.setLoginName(sysDoctor.getDrCode());
            sysUser.setPassword(EncryptUtils.getInstance().encrypt(sysDoctor.getDrCode()));
            sysUser.setDrId(sysDoctor.getId());
            sysUser.setCrterId(loginUser.getUserId());
            sysUsers.add(sysUser);
        }
        if (CollectionUtil.isNotEmpty(sysUsers)) {
            sysUserMapper.insertBatch(sysUsers);
            sysUserRoleMapper.insertUserRoleBatch(sysUsers);
        }
    }

    private void extracted(List<Long> roleIds, SysUser sysUser, LoginUser loginUser) {
        sysUserRoleMapper.insertBatch(roleIds.stream().map(roleId -> {
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setUserId(sysUser.getId());
            sysUserRole.setRoleId(roleId);
            sysUserRole.setCrteTime(new Date());
            sysUserRole.setCrterId(loginUser.getUserId());
            return sysUserRole;
        }).collect(Collectors.toList()));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(UpdateUserParam updateUserParam, LoginUser loginUser) throws Exception {

        SysUser sysUser = new SysUser();
        sysUser.setId(updateUserParam.getUserId());
        sysUser.setPassword(EncryptUtils.getInstance().encrypt(Base64.decodeStr(updateUserParam.getPassword())));
        sysUser.setUsername(updateUserParam.getDrName());
        //绑定医生
        sysUser.setDrId(updateUserParam.getDrId());
        sysUser.setUpdtTime(new Date());
        sysUser.setUpdtrId(loginUser.getUserId());
        sysUser.setLoginName(updateUserParam.getLoginName());
        sysUserMapper.updateByPrimaryKey(sysUser);

        //删除角色
        sysUserRoleMapper.deleteByUserId(updateUserParam.getUserId());
        //绑定角色
        extracted(updateUserParam.getRoleIds(), sysUser, loginUser);
    }

    @Override
    public void updatePassword(UpdateUserParam updateUserParam, LoginUser loginUser) throws Exception{

        //Base64解码
        String password = Base64.decodeStr(updateUserParam.getPassword());
        String oldPassword = Base64.decodeStr(updateUserParam.getOldPassword());

        //判断密码长度
        if(password.length() < 6|| password.length() > 20){
            throw new BizException(400, "必须包含字母、数字，且长度不小于6位字符!");
        }

        //判断密码是否一致
        if(password.equals(oldPassword)){
            throw new BizException(400, "新密码与确认密码不一致!");
        }

        //加密新旧密码
        String newPwd = EncryptUtils.getInstance().encrypt(password);
        String oldPwd = EncryptUtils.getInstance().encrypt(oldPassword);

        SysUser user;
        if(updateUserParam.getLoginName()!= null){
            user = sysUserMapper.getUserByLoginName(updateUserParam.getLoginName());
        }else{
            user = sysUserMapper.selectByUserId(updateUserParam.getUserId());
        }

        if (!oldPwd.equals(user.getPassword())) {
            throw new BizException(400, "原密码错误!");
        }

        //判断新密码是否与原密码一致
        if (newPwd.equals(user.getPassword())) {
            throw new BizException(400, "新密码不能与原密码一致!");
        }

        //判断密码是否初始密码
        Doctor doctor = doctorService.getDoctorById(user.getDrId());
        if (user.getDrId()!=-1L && password.equals(doctor.getDrCode())) {
            throw new BizException(403,"密码不能与初始密码相同！");
        }

        SysUser sysUser = new SysUser();
        sysUser.setId(user.getId());
        sysUser.setPassword(newPwd);
        sysUser.setUpdtTime(new Date());
        sysUser.setUpdtrId(loginUser==null?user.getId():loginUser.getUserId());
        sysUserMapper.updateByPrimaryKey(sysUser);
    }

    @Override
    public void disable(Long userId, LoginUser loginUser) {
        sysUserMapper.disable(userId);
    }

    @Override
    public void enable(Long userId, LoginUser loginUser) {
        sysUserMapper.enable(userId);
    }

    @Override
    public List<User> findUserList(UserParam userParam) {
        return sysUserMapper.findUserList(userParam).stream().map(sysUser -> {
            User user = new User();
            user.setUserId(sysUser.getId());
            user.setLoginName(sysUser.getLoginName());
            user.setUsername(sysUser.getUsername());
            user.setDelFlag(sysUser.getDelFlag());
            user.setCrteTime(sysUser.getCrteTime());
            return user;
        }).collect(Collectors.toList());
    }

    @Override
    public PageResult<User> selectByPage(PageParam<UserParam> pageParam) {
        PageResult<User> result = new PageResult<>();
        PageResult<SysUser> sysUserPageResult = sysUserMapper.selectByPage(pageParam);
        result.setRows(sysUserPageResult.getRows().stream().map(sysUser -> {
            User user = new User();
            user.setUserId(sysUser.getId());
            user.setLoginName(sysUser.getLoginName());
            user.setUsername(sysUser.getUsername());
            user.setDelFlag(sysUser.getDelFlag());
            user.setCrteTime(sysUser.getCrteTime());

            // 用户绑定的医生信息
            Doctor doctor = doctorService.getDoctorById(sysUser.getDrId());
            if (doctor != null) {
                user.setDrName(doctor.getDrName());
                user.setDrCode(doctor.getDrCode());
                user.setDrId(doctor.getDrId());
                user.setHiDrName(doctor.getHiDrName());
                user.setHiDrCode(doctor.getHiDrCode());
                user.setEmail(doctor.getEmail());
                user.setTel(doctor.getTel());
                user.setDutyName(doctor.getDutyName());
                user.setDutyCode(doctor.getDutyCode());
                List<String> deptNameCollect = doctor.getDeptList().stream().map(SysMedinsDept::getDeptName).collect(Collectors.toList());
                user.setDeptName(Joiner.on(",").join(deptNameCollect));
                List<String> deptCodeCollect = doctor.getDeptList().stream().map(SysMedinsDept::getDeptCode).collect(Collectors.toList());
                user.setDeptCode(Joiner.on(",").join(deptCodeCollect));
                List<String> medGrpNameCollect = doctor.getMedGrpList().stream().map(SysMedGrp::getMedGrpName).collect(Collectors.toList());
                user.setMedGrpName(Joiner.on(",").join(medGrpNameCollect));
            }
            return user;
        }).collect(Collectors.toList()));
        result.setTotalPage(sysUserPageResult.getTotalPage());
        result.setTotal(sysUserPageResult.getTotal());
        return result;
    }

    @Override
    public User selectByUserId(Long userId) throws Exception {
        SysUser sysUser = sysUserMapper.selectByUserId(userId);
        User user = new User();
        user.setUserId(sysUser.getId());
        user.setLoginName(sysUser.getLoginName());
        user.setUsername(sysUser.getUsername());
        user.setDelFlag(sysUser.getDelFlag());
        user.setCrteTime(sysUser.getCrteTime());
        user.setPassword(EncryptUtils.getInstance().decrypt(sysUser.getPassword()));

        // 用户绑定的医生信息
        Doctor doctor = doctorService.getDoctorById(sysUser.getDrId());
        if (doctor != null) {
            user.setDrName(doctor.getDrName());
            user.setDrCode(doctor.getDrCode());
            user.setDrId(doctor.getDrId());
            user.setHiDrName(doctor.getHiDrName());
            user.setHiDrCode(doctor.getHiDrCode());
            user.setEmail(doctor.getEmail());
            user.setTel(doctor.getTel());
            user.setDutyName(doctor.getDutyName());
            user.setDutyCode(doctor.getDutyCode());
            user.setDeptList(doctor.getDeptList());
            List<String> deptNameCollect = doctor.getDeptList().stream().map(SysMedinsDept::getDeptName).collect(Collectors.toList());
            user.setDeptName(Joiner.on(",").join(deptNameCollect));
            List<String> deptCodeCollect = doctor.getDeptList().stream().map(SysMedinsDept::getDeptCode).collect(Collectors.toList());
            user.setDeptCode(Joiner.on(",").join(deptCodeCollect));
            user.setMedGrpList(doctor.getMedGrpList());
            List<String> medGrpNameCollect = doctor.getMedGrpList().stream().map(SysMedGrp::getMedGrpName).collect(Collectors.toList());
            user.setMedGrpName(Joiner.on(",").join(medGrpNameCollect));
            List<UserRole> userRoleList = sysUserRoleMapper.findRoleListByUserId(sysUser.getId()).stream().map(sysUserRole -> {
                UserRole userRole = new UserRole();
                Role role = roleService.getRoleById(sysUserRole.getRoleId());
                if (role != null) {
                    userRole.setRoleId(role.getRoleId());
                    userRole.setRoleName(role.getRoleName());
                }
                return userRole;
            }).collect(Collectors.toList());
            user.setRoleList(userRoleList);
        }
        return user;
    }

    @Override
    public List<User> selectByUserName(String userName) {
        List<SysUser> sysUsers = sysUserMapper.selectByUserName(userName);
        List<User> userList = new ArrayList<>();
        for (SysUser sysUser : sysUsers) {
            User user = new User();
            user.setUserId(sysUser.getId());
            user.setLoginName(sysUser.getLoginName());
            user.setUsername(sysUser.getUsername());
            user.setDeptName(sysMedinsDeptDoctorMapper.queryDeptNameByUserId(sysUser.getId()));
            userList.add(user);
        }
        return userList;
    }

}
