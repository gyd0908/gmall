package com.atguigu.gmall.pms.service.impl;


import com.alibaba.nacos.client.utils.StringUtils;
import com.atguigu.gmall.pms.dao.AttrDao;
import com.atguigu.gmall.pms.dao.SkuInfoDao;
import com.atguigu.gmall.pms.dao.SpuInfoDescDao;
import com.atguigu.gmall.pms.entity.*;
import com.atguigu.gmall.pms.feign.SkuSaleFeign;
import com.atguigu.gmall.pms.service.ProductAttrValueService;
import com.atguigu.gmall.pms.service.SkuImagesService;
import com.atguigu.gmall.pms.service.SkuSaleAttrValueService;

import com.atguigu.gmall.pms.vo.ProductAttrValueVO;
import com.atguigu.gmall.pms.vo.SkuInfoVO;
import com.atguigu.gmall.pms.vo.SpuInfoVO;


import com.atguigu.sms.vo.SkuSaleVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.Query;
import com.atguigu.core.bean.QueryCondition;

import com.atguigu.gmall.pms.dao.SpuInfoDao;
import com.atguigu.gmall.pms.service.SpuInfoService;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {
    @Autowired
    private SpuInfoDescDao spuInfoDescDao;
    @Autowired
    private ProductAttrValueService productAttrValueService;

    @Autowired
    private SkuInfoDao skuInfoDao;

    @Autowired
    private SkuImagesService skuImagesService;

    @Autowired
    private AttrDao attrDao;

    @Autowired
    private SkuSaleAttrValueService skuSaleAttrValueService;
    @Autowired
    private SkuSaleFeign skuSaleFeign;

    @Override
    public PageVo queryPage(QueryCondition params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageVo(page);
    }

    @Override
    public void saveSpuInfoVO(SpuInfoVO spuInfoVO) {
        saveStatus(spuInfoVO);
        saveAttrvalue(spuInfoVO);
        saveDescInfo(spuInfoVO);
        saveImages(spuInfoVO);

    }

    private void saveStatus(SpuInfoVO spuInfoVO) {
        spuInfoVO.setPublishStatus(1);
        spuInfoVO.setCreateTime(new Date());
        spuInfoVO.setUodateTime(spuInfoVO.getCreateTime());
        this.save(spuInfoVO);
    }

    private void saveImages(SpuInfoVO spuInfoVO) {
        List<SkuInfoVO> skuInfoVOS = spuInfoVO.getSkus();
        if (CollectionUtils.isEmpty(skuInfoVOS)) {
            return;
        }
        skuInfoVOS.forEach((SkuInfoVO sku) -> {
            SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
            BeanUtils.copyProperties(sku, skuInfoEntity);
            skuInfoEntity.setBrandId(sku.getBrandId());
            skuInfoEntity.setCatalogId(sku.getCatalogId());
            skuInfoEntity.setSkuCode(UUID.randomUUID().toString().substring(0, 10).toUpperCase());
            List<String> images = sku.getImages();
            if (!CollectionUtils.isEmpty(images)) {
                skuInfoEntity.setSkuDefaultImg(skuInfoEntity.getSkuDefaultImg() == null ? images.get(0) : skuInfoEntity.getSkuDefaultImg());
            }
            skuInfoEntity.setSpuId(spuInfoVO.getId());
            this.skuInfoDao.insert(skuInfoEntity);
            Long skuId = skuInfoEntity.getSkuId();

            if (!CollectionUtils.isEmpty(images)) {
                String defaultImage = images.get(0);
                List<SkuImagesEntity> imagesEntities = images.stream().map(image -> {
                    SkuImagesEntity imagesEntity = new SkuImagesEntity();
                    imagesEntity.setDefaultImg(StringUtils.equals(defaultImage, image) ? 1 : 0);
                    imagesEntity.setSkuId(skuId);
                    imagesEntity.setImgUrl(image);
                    imagesEntity.setImgSort(0);
                    return imagesEntity;
                }).collect(Collectors.toList());
                this.skuImagesService.saveBatch(imagesEntities);
            }
            List<SkuSaleAttrValueEntity> saleAttrs = sku.getSaleAttrs();
            saleAttrs.forEach(saleAttr->{
                saleAttr.setAttrName(this.attrDao.selectById(saleAttr.getId()).getAttrName());
                saleAttr.setAttrSort(0);
                saleAttr.setSkuId(skuId);
            });
            this.skuSaleAttrValueService.saveBatch(saleAttrs);
            SkuSaleVO skuSaleVO = new SkuSaleVO();
            BeanUtils.copyProperties(sku,skuSaleVO);
            this.skuSaleFeign.saveSaleInfo(skuSaleVO);
        });
    }

    private void saveAttrvalue(SpuInfoVO spuInfoVO) {
        List<ProductAttrValueVO> baseAttrs = spuInfoVO.getBaseAttrs();
        if (!CollectionUtils.isEmpty(baseAttrs)) {
            List<ProductAttrValueEntity> attrValueEntities = baseAttrs.stream().map(
                    baseAttr -> {
                        baseAttr.setSpuId(spuInfoVO.getId());
                        baseAttr.setAttrSort(1);
                        baseAttr.setQuickShow(1);
                        return baseAttr;
                    }).collect(Collectors.toList());
            this.productAttrValueService.saveBatch(attrValueEntities);
        }
    }

    private void saveDescInfo(SpuInfoVO spuInfoVO) {
        SpuInfoDescEntity spuInfoDescEntity = new SpuInfoDescEntity();
        spuInfoDescEntity.setSpuId(spuInfoVO.getId());
        spuInfoDescEntity.setDecript(StringUtils.join(spuInfoVO.getSpuImages(), ","));
        this.spuInfoDescDao.insert(spuInfoDescEntity);
    }
}