package com.winning.hmap.portal.auth.service.impl;

import com.winning.hmap.portal.auth.entity.SysInfo;
import com.winning.hmap.portal.auth.mapper.SysInfoMapper;
import com.winning.hmap.portal.auth.service.SysService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysServiceImpl implements SysService {

    @Autowired
	SysInfoMapper sysInfoMapper;

    @Override
	public SysInfo querySysInfo() {
		return sysInfoMapper.querySysInfo();
	}
}
