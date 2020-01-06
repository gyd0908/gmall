package com.atguigu.gmall.ums.dao;

import com.atguigu.gmall.ums.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author guoyuedong
 * @email gyd@atguigu.com
 * @date 2020-01-03 09:10:06
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
