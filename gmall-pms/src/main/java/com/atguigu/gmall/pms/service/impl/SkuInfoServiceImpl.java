package com.atguigu.gmall.pms.service.impl;

import com.alibaba.nacos.client.utils.StringUtils;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.Query;
import com.atguigu.core.bean.QueryCondition;

import com.atguigu.gmall.pms.dao.SkuInfoDao;
import com.atguigu.gmall.pms.entity.SkuInfoEntity;
import com.atguigu.gmall.pms.service.SkuInfoService;




@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {

    @Override
    public PageVo queryPage(QueryCondition params) {
        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                new QueryWrapper<SkuInfoEntity>()
        );

        return new PageVo(page);
    }

    @Override
    public PageVo queryInfoById(QueryCondition queryCondition, Long cid) {
        IPage<SkuInfoEntity> page = this.page(new Query<SkuInfoEntity>().getPage(queryCondition));
        QueryWrapper<SkuInfoEntity> wrapper = new QueryWrapper<>();
              if (cid!=0){
                  wrapper.eq("catalog_id",cid);
              }
       String key= queryCondition.getKey();
             if (StringUtils.isNotBlank(key)){
                 wrapper.and(t->t.like("spu_name",key).or().like("id",key));
             }
        return new PageVo(this.page(page,wrapper));
    }

}