package com.atguigu.gmall.service;

import com.atguigu.gmall.bean.PmsProductImage;
import com.atguigu.gmall.bean.PmsProductInfo;
import com.atguigu.gmall.bean.PmsProductSaleAttr;

import java.util.List;

public interface SpuService {

 public List<PmsProductInfo> spuList(String catalog3Id);

 public List<PmsProductSaleAttr> spuSaleAttrList(String spuId);

 void saveSpuInfo(PmsProductInfo pmsProductInfo);

 List<PmsProductImage> spuImageList(String spuId);

 List<PmsProductSaleAttr> spuSaleAttrListCheckBySku(String productId,String skuId);
}