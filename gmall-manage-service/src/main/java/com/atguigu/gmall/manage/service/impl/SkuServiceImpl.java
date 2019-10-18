package com.atguigu.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.bean.PmsSkuAttrValue;
import com.atguigu.gmall.bean.PmsSkuImage;
import com.atguigu.gmall.bean.PmsSkuInfo;
import com.atguigu.gmall.bean.PmsSkuSaleAttrValue;
import com.atguigu.gmall.manage.mapper.PmsSkuAttrValueMapper;
import com.atguigu.gmall.manage.mapper.PmsSkuImageMapper;
import com.atguigu.gmall.manage.mapper.PmsSkuInfoMapper;
import com.atguigu.gmall.manage.mapper.PmsSkuSaleAttrValueMapper;
import com.atguigu.gmall.service.SkuService;
import com.atguigu.gmall.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

import java.util.List;

@Service
public class SkuServiceImpl implements SkuService {

 @Autowired
 PmsSkuInfoMapper pmsSkuInfoMapper;
 @Autowired
 PmsSkuAttrValueMapper pmsSkuAttrValueMapper;
 @Autowired
 PmsSkuSaleAttrValueMapper pmsSkuSaleAttrValueMapper;
 @Autowired
 PmsSkuImageMapper pmsSkuImageMapper;
 @Autowired
 RedisUtil redisUtil;

 @Override
 public void saveSkuInfo(PmsSkuInfo pmsSkuInfo) {
   //插入skuInfo
  int i=pmsSkuInfoMapper.insertSelective(pmsSkuInfo);
  String skuId=pmsSkuInfo.getId();

  //插入平台属性关联
  List<PmsSkuAttrValue> skuAttrValueList=pmsSkuInfo.getSkuAttrValueList();
  for (PmsSkuAttrValue pmsSkuAttrValue : skuAttrValueList) {
   pmsSkuAttrValue.setSkuId(skuId);
   pmsSkuAttrValueMapper.insertSelective(pmsSkuAttrValue);
  }

  //插入销售属性关联
  List<PmsSkuSaleAttrValue> skuSaleAttrValueList=pmsSkuInfo.getSkuSaleAttrValueList();
  for (PmsSkuSaleAttrValue pmsSkuSaleAttrValue : skuSaleAttrValueList) {
   pmsSkuSaleAttrValue.setSkuId(skuId);
   pmsSkuSaleAttrValueMapper.insertSelective(pmsSkuSaleAttrValue);
  }
  //插入图片信息
  List<PmsSkuImage> skuImageList=pmsSkuInfo.getSkuImageList();
  for (PmsSkuImage pmsSkuImage : skuImageList) {
   pmsSkuImage.setSkuId(skuId);
   pmsSkuImageMapper.insertSelective(pmsSkuImage);
  }
 }

 public PmsSkuInfo getSkuByIdProm(String skuId) {
  //sku商品对象
  PmsSkuInfo pmsSkuInfo=new PmsSkuInfo();
  pmsSkuInfo.setId(skuId);

  PmsSkuInfo skuInfo=pmsSkuInfoMapper.selectOne(pmsSkuInfo);
  //sku图片集合
  PmsSkuImage pmsSkuImage=new PmsSkuImage();
  pmsSkuImage.setSkuId(skuId);
  List<PmsSkuImage> pmsSkuImages=pmsSkuImageMapper.select(pmsSkuImage);
  skuInfo.setSkuImageList(pmsSkuImages);
  return skuInfo;
 }

 @Override
 public PmsSkuInfo getSkuById(String skuId) {

  PmsSkuInfo pmsSkuInfo=new PmsSkuInfo();

  //链接缓存
  Jedis jedis =redisUtil.getJedis();

  //查询缓存
  String skuKey="sku:"+skuId+"info";
  String skuJson=jedis.get(skuKey);
  if (StringUtils.isNotBlank(skuJson)){ //相当于 if (skuJson!=null && !skuJson.equals(""))
   pmsSkuInfo=JSON.parseObject(skuJson,PmsSkuInfo.class);
  }else {
   //如果缓存中没有，查询mysql
  pmsSkuInfo =getSkuByIdProm(skuId);
   if (pmsSkuInfo!=null){
    //mysql查询结果存入redis
    jedis.set("sku:"+skuId+":info",JSON.toJSONString(pmsSkuInfo));
   }
  }
  jedis.close();
  return pmsSkuInfo;
 }

 @Override
 public List<PmsSkuInfo> getSkuSaleAttrValueListBySpu(String productId) {
  List<PmsSkuInfo> pmsSkuInfos=pmsSkuInfoMapper.selectSkuSaleAttrValueListBySpu(productId);

  return pmsSkuInfos;
 }
}