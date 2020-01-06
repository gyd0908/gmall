package com.atguigu.gmall.pms.service;

import com.atguigu.gmall.pms.vo.AttrGroupVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.gmall.pms.entity.AttrGroupEntity;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.QueryCondition;

import java.util.List;


/**
 * 属性分组
 *
 * @author guoyuedong
 * @email gyd@atguigu.com
 * @date 2020-01-03 08:57:52
 */
public interface AttrGroupService extends IService<AttrGroupEntity> {

    PageVo queryPage(QueryCondition params);

    PageVo queryGroupById(Long cid, QueryCondition queryCondition);


    AttrGroupVo queryGroupBygid(Long gid);

    List<AttrGroupVo> queryByCid(Long cid);
}

