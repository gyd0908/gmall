package com.atguigu.gmall.wms.dao;

import com.atguigu.gmall.wms.entity.WareInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 仓库信息
 * 
 * @author guoyuedong
 * @email gyd@atguigu.com
 * @date 2020-01-02 21:51:42
 */
@Mapper
public interface WareInfoDao extends BaseMapper<WareInfoEntity> {
	
}
