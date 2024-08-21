package com.winning.hmap.portal.dict.service.impl;

import com.winning.hmap.portal.dict.dto.req.DicReq;
import com.winning.hmap.portal.dict.dto.req.put.AddDictParam;
import com.winning.hmap.portal.dict.dto.req.put.UpdateDictParam;
import com.winning.hmap.portal.dict.dto.req.query.DictParam;
import com.winning.hmap.portal.dict.dto.resp.DicResp;
import com.winning.hmap.portal.dict.dto.resp.Dict;
import com.winning.hmap.portal.dict.entity.SysDictLog;
import com.winning.hmap.portal.dict.entity.SysDict;
import com.winning.hmap.portal.dict.mapper.SysDictMapper;
import com.winning.hmap.portal.dict.service.DictService;
import com.winning.hmap.portal.auth.dto.auth.resp.LoginUser;
import com.winning.hmap.portal.util.BizConstant;
import me.about.widget.mybatis.plugin.page.model.PageParam;
import me.about.widget.mybatis.plugin.page.model.PageResult;
import me.about.widget.spring.mvc.exception.BizException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DictServiceImpl implements DictService {

    @Resource
    private SysDictMapper sysDictMapper;

    @Override
    public void add(AddDictParam addDictParam, LoginUser loginUser) {
        SysDict insert = new SysDict();
        insert.setDictName(addDictParam.getDictName());
        insert.setDictCode(addDictParam.getDictCode());
        insert.setDictValue(addDictParam.getDictValue());
        insert.setDictType(addDictParam.getDictType());
        insert.setSortBy(addDictParam.getSortBy());
        insert.setParentId(addDictParam.getParentId());
        insert.setRemark(addDictParam.getRemark());
        insert.setDelFlag(BizConstant.DEL_FLAG_DEFAULT);
        insert.setCrterId(loginUser.getUserId());
        insert.setCrteTime(new Date());
        sysDictMapper.insert(insert);

        SysDictLog sysDictLog = new SysDictLog();
        sysDictLog.setOptType("1");
        sysDictLog.setOpter(loginUser.getUsername());
        String cznr="新增 : 字典名称【"+addDictParam.getDictName()+"】,字典类型名称【"+addDictParam.getParentName()+"】,字典值【"+addDictParam.getDictValue()+"】, 操作结果 "+ "【成功!】";
        sysDictLog.setOptContent(cznr);
        sysDictMapper.insertOperationLog(sysDictLog);
    }

    @Override
    public void update(UpdateDictParam updateDictParam, LoginUser loginUser) {
        SysDict findOne = sysDictMapper.getDictById(updateDictParam.getDictId());
        SysDict update = new SysDict();
        update.setId(updateDictParam.getDictId());
        update.setDictName(updateDictParam.getDictName());
        update.setDictCode(updateDictParam.getDictCode());
        update.setDictValue(updateDictParam.getDictValue());
        update.setDictType(updateDictParam.getDictType());
        update.setSortBy(updateDictParam.getSortBy());
        update.setRemark(updateDictParam.getRemark());
        update.setParentId(updateDictParam.getParentId());
        update.setUpdtrId(loginUser.getUserId());
        update.setUpdtTime(new Date());
        sysDictMapper.updateByPrimaryKey(update);
        SysDictLog sysDictLog = new SysDictLog();
        StringBuffer cznr = new StringBuffer();
        cznr.append("修改 : 字典类型名称 【"+ findOne.getParentName()+"】,");
        cznr.append("字典名称 由【"+findOne.getDictName()+"】变成【 "+ updateDictParam.getDictName()+ "】,");
        cznr.append(" 字典值 由【"+findOne.getDictValue()+"】,变成 【"+ updateDictParam.getDictValue() +"】,");
        sysDictLog.setOptType("2");
        sysDictLog.setOpter(loginUser.getUsername());
        cznr.append(" 操作结果 "+ "成功!");
        sysDictLog.setOptContent(cznr.toString());
        sysDictMapper.insertOperationLog(sysDictLog);

    }

    @Override
    public void enable(Long configId, LoginUser loginUser) {
        sysDictMapper.enable(configId);
    }

    @Override
    public void disable(Long configId, LoginUser loginUser) {
        sysDictMapper.disable(configId);
    }

    @Override
    public void disable(List<Long> configIds, LoginUser loginUser) {
        sysDictMapper.disableByIds(configIds);
        for (Long id : configIds) {
            SysDict findData = sysDictMapper.getDictById(id);
            SysDictLog sysDictLog = new SysDictLog();
            sysDictLog.setOptType("3");
            sysDictLog.setOpter(loginUser.getUsername());
            String cznr="删除 : 字典名称 【"+findData.getDictName()+"】 ,字典类型名称 【"+findData.getParentName()+"】, 字典值 【"+findData.getDictValue()+"】, 操作结果 "+ "成功!";
            sysDictLog.setOptContent(cznr);
            sysDictMapper.insertOperationLog(sysDictLog);
        }
    }

    @Override
    public void enable(List<Long> configIds, LoginUser loginUser) {
        sysDictMapper.enableByIds(configIds);
    }

    @Override
    public PageResult<Dict> findDictByParentId(PageParam<DictParam> pageParam) {
        PageResult<Dict> result = new PageResult<>();
        PageResult<SysDict> pageResult = sysDictMapper.selectByPage(pageParam);
        result.setTotal(pageResult.getTotal());
        result.setTotalPage(pageResult.getTotalPage());
        result.setRows(pageResult.getRows().stream().map(this::convert).collect(Collectors.toList()));
        return result;
    }

    @Override
    public List<Dict> findDirList(DictParam dictParam) {
        List<Dict> dictList = sysDictMapper.findDirList(dictParam)
                .stream().map(this::convert).collect(Collectors.toList());
        return recursion(0L, dictList);
    }

    @Override
    public Map<String,List<SysDict>> findList() {
        return sysDictMapper.getDictList().stream().collect(Collectors.groupingBy(SysDict::getParentCode));
    }

    private List<Dict> recursion(Long parentId, List<Dict> dictList) {
        return dictList.stream()
                .filter(item -> item.getParentId().equals(parentId))
                .peek((m) -> m.setChildren(recursion(m.getDictId(), dictList)))
                .collect(Collectors.toList());
    }


    @Override
    public Dict getDictByCode(String dictCode) {
        return convert(sysDictMapper.getDictByCode(dictCode));
    }

    private Dict convert(SysDict sysDict) {
        Dict dict = new Dict();
        dict.setParentId(sysDict.getParentId());
        dict.setDictId(sysDict.getId());
        dict.setDictName(sysDict.getDictName());
        dict.setDictCode(sysDict.getDictCode());
        dict.setDictValue(sysDict.getDictValue());
        dict.setRemark(sysDict.getRemark());
        dict.setParentName(sysDict.getParentName());
        return dict;
    }

    @Override
    public List<Dict> getDictListByParentDictCode(String parentDictCode) {
        return sysDictMapper.getDictListByParentDictCode(parentDictCode)
                .stream().map(this::convert)
                .collect(Collectors.toList());

    }

    @Override
    public String getDictNameByParentDictCode(String parentDictCode, String dictValue) {
        List<SysDict> sysDictList = sysDictMapper.getDictListByParentDictCode(parentDictCode).stream()
                .filter(sysDict -> sysDict.getDictValue().equals(dictValue))
                .collect(Collectors.toList());
        if (sysDictList.size() > 1) {
            throw new BizException(400, "目录:" + parentDictCode + ",字典值:" + dictValue + ",匹配了多个配置项");
        }
        return sysDictList.size() == 1 ? sysDictList.get(0).getDictName() : null;
    }

    @Override
    public PageResult<SysDictLog> queryLogDictionaries(PageParam<SysDictLog> param) {
        return sysDictMapper.queryDictLogByPage(param);
    }

    @Override
    public List<DicResp> syncDic(DicReq dicReq) {
        return sysDictMapper.getDictListByParentDictCode(dicReq.getParentCode())
                .stream().map(this::rccConvert)
                .collect(Collectors.toList());
    }

    private DicResp rccConvert(SysDict sysDict) {
        DicResp dicResp = new DicResp();
        dicResp.setDicCode(sysDict.getDictValue());
        dicResp.setDicName(sysDict.getDictName());
        return dicResp;
    }
}
