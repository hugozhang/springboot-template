package com.winning.hmap.portal.auth.mapper;

import com.winning.hmap.portal.auth.entity.SysInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysInfoMapper {

    SysInfo querySysInfo();

}
