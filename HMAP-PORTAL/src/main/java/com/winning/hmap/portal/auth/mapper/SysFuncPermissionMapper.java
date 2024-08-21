package com.winning.hmap.portal.auth.mapper;


import com.winning.hmap.portal.auth.dto.auth.resp.FuncPermission;

import java.util.List;

public interface SysFuncPermissionMapper  {
    List<FuncPermission> findAllFuncPermission();
}