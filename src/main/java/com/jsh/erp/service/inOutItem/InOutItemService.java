package com.jsh.erp.service.inOutItem;

import com.alibaba.fastjson.JSONObject;
import com.jsh.erp.datasource.entities.InOutItem;
import com.jsh.erp.datasource.entities.InOutItemExample;
import com.jsh.erp.datasource.mappers.InOutItemMapper;
import com.jsh.erp.datasource.mappers.InOutItemMapperEx;
import com.jsh.erp.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class InOutItemService {
    private Logger logger = LoggerFactory.getLogger(InOutItemService.class);

    @Resource
    private InOutItemMapper inOutItemMapper;

    @Resource
    private InOutItemMapperEx inOutItemMapperEx;

    public InOutItem getInOutItem(long id) {
        return inOutItemMapper.selectByPrimaryKey(id);
    }

    public List<InOutItem> getInOutItem() {
        InOutItemExample example = new InOutItemExample();
        return inOutItemMapper.selectByExample(example);
    }

    public List<InOutItem> select(String name, String type, String remark, int offset, int rows) {
        return inOutItemMapperEx.selectByConditionInOutItem(name, type, remark, offset, rows);
    }

    public Long countInOutItem(String name, String type, String remark) {
        return inOutItemMapperEx.countsByInOutItem(name, type, remark);
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int insertInOutItem(String beanJson, HttpServletRequest request) {
        InOutItem depot = JSONObject.parseObject(beanJson, InOutItem.class);
        return inOutItemMapper.insertSelective(depot);
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int updateInOutItem(String beanJson, Long id) {
        InOutItem depot = JSONObject.parseObject(beanJson, InOutItem.class);
        depot.setId(id);
        return inOutItemMapper.updateByPrimaryKeySelective(depot);
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int deleteInOutItem(Long id) {
        return inOutItemMapper.deleteByPrimaryKey(id);
    }

    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public int batchDeleteInOutItem(String ids) {
        List<Long> idList = StringUtil.strToLongList(ids);
        InOutItemExample example = new InOutItemExample();
        example.createCriteria().andIdIn(idList);
        return inOutItemMapper.deleteByExample(example);
    }

    public int checkIsNameExist(Long id, String name) {
        InOutItemExample example = new InOutItemExample();
        example.createCriteria().andIdNotEqualTo(id).andNameEqualTo(name);
        List<InOutItem> list = inOutItemMapper.selectByExample(example);
        return list.size();
    }

    public List<InOutItem> findBySelect(String type) {
        InOutItemExample example = new InOutItemExample();
        if (type.equals("in")) {
            example.createCriteria().andTypeEqualTo("收入");
        } else if (type.equals("out")) {
            example.createCriteria().andTypeEqualTo("支出");
        }
        example.setOrderByClause("id desc");
        return inOutItemMapper.selectByExample(example);
    }
}
