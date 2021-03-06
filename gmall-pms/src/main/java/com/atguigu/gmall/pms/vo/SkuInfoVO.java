package com.atguigu.gmall.pms.vo;

import com.atguigu.gmall.pms.entity.SkuInfoEntity;
import com.atguigu.gmall.pms.entity.SkuSaleAttrValueEntity;
import lombok.Data;


import java.math.BigDecimal;
import java.util.List;
@Data
public class SkuInfoVO extends SkuInfoEntity {
    private List<String> images;
    private BigDecimal growBounds;
    private BigDecimal buyBounds;
    private Integer work;
    private BigDecimal fullPrice;
    private BigDecimal reducePrice;
    private Integer fullAddOther;
    private Integer fullCount;
    private BigDecimal discount;
    private Integer addOther;
    private List<SkuSaleAttrValueEntity> saleAttrs;
}
