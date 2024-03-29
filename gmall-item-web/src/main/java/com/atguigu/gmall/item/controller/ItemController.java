package com.atguigu.gmall.item.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.bean.PmsProductSaleAttr;
import com.atguigu.gmall.bean.PmsSkuInfo;
import com.atguigu.gmall.bean.PmsSkuSaleAttrValue;
import com.atguigu.gmall.service.SkuService;
import com.atguigu.gmall.service.SpuService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

@Controller
public class ItemController {

 @Reference
 SkuService skuService;
 @Reference
 SpuService spuService;

 @RequestMapping("{skuId}.html")
 public String item(@PathVariable String skuId, ModelMap map, HttpServletRequest request){

  String remoteAddr=request.getRemoteAddr();

  //request.getHeader("");

  PmsSkuInfo pmsSkuInfo=skuService.getSkuById(skuId,remoteAddr);

  //sk对象
  map.put("skuInfo",pmsSkuInfo);
  //销售属性列表
  List<PmsProductSaleAttr> pmsProductSaleAttrs=spuService.spuSaleAttrListCheckBySku(pmsSkuInfo.getProductId(),pmsSkuInfo.getId());
  map.put("spuSaleAttrListCheckBySku",pmsProductSaleAttrs);

  //查询当前sku的其他sku集合hash表
  HashMap<String,String> skuSaleAttrHash=new HashMap<>();
  List<PmsSkuInfo> pmsSkuInfos=skuService.getSkuSaleAttrValueListBySpu(pmsSkuInfo.getProductId());
  for (PmsSkuInfo skuInfo : pmsSkuInfos) {
   String k="";
   String v=skuInfo.getId();

   List<PmsSkuSaleAttrValue> skuSaleAttrValueList=skuInfo.getSkuSaleAttrValueList();

   for (PmsSkuSaleAttrValue pmsSkuSaleAttrValue : skuSaleAttrValueList) {
    k+=pmsSkuSaleAttrValue.getSaleAttrId()+"|";
   }
   skuSaleAttrHash.put(k,v);
  }
  //将sku的销售属性hash表放到页面
  String skuSaleAttrHashJsonStr=JSON.toJSONString(skuSaleAttrHash);
  map.put("skuSaleAttrHashJsonStr",skuSaleAttrHashJsonStr);
  return "item";
 }
}