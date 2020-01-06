package com.atguigu.gmall.pms.feign;

import com.atguigu.sms.feign.smsApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("sms-service")
public interface SkuSaleFeign extends smsApi {

}
