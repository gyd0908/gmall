package com.atguigu.sms.feign;

import com.atguigu.core.bean.Resp;
import com.atguigu.sms.vo.SkuSaleVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface smsApi {

    @PostMapping("/sms/skubounds/skusale/save")
    public Resp<Object> saveSaleInfo(@RequestBody SkuSaleVO skuSaleVO);
}
