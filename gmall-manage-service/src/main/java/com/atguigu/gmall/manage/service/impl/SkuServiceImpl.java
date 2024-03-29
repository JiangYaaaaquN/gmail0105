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

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

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
 public PmsSkuInfo getSkuById(String skuId,String ip) {

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

   //设置分布式锁
   String token= UUID.randomUUID().toString();
   String OK=jedis.set("sku:"+skuId+":lock",token,"nx","px",10*1000);
   if (StringUtils.isNotBlank(OK) && OK.equals("OK")){
    //设置成功，有权在10秒的过期时间内访问数据库
    pmsSkuInfo=getSkuByIdProm(skuId);

    try {
     Thread.sleep(1000*5);
    } catch (InterruptedException e) {
     e.printStackTrace();
    }

    if (pmsSkuInfo!=null){
     //mysql查询结果存入redis
     jedis.set("sku:"+skuId+":info",JSON.toJSONString(pmsSkuInfo));
    }else{
     //数据库中不存在该sku
     //为了防止缓存穿透，null或空字符串值设置给redis
     jedis.setex("sku:"+skuId+":info",60*3,JSON.toJSONString(""));
    }
    //在访问mysql，将Mysql释放
   String lockToken= jedis.get("sku:"+skuId+":lock");
    if (StringUtils.isNotBlank(lockToken) && lockToken.equals(token)){
     //jedis.eval("lua");可与用lua脚本，在查询到key同时删除key，防止高并发下的意外的发生
     jedis.del("sku:"+skuId+":lock"); //用token确认删除的是自己的sku的锁
    }
   }else {
    //设置失败，自选(该线程在睡眠几秒后，重新尝试访问本方法)
    try {
     Thread.sleep(3000);
    } catch (InterruptedException e) {
     e.printStackTrace();
    }
    return  getSkuById(skuId,ip);
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

 @Override
 public List<PmsSkuInfo> getAllSku(String catalog3Id) {
  List<PmsSkuInfo> pmsSkuInfos=pmsSkuInfoMapper.selectAll();
  for (PmsSkuInfo pmsSkuInfo : pmsSkuInfos) {
   String skuId=pmsSkuInfo.getId();
   PmsSkuAttrValue pmsSkuAttrValue=new PmsSkuAttrValue();
   List<PmsSkuAttrValue> select=pmsSkuAttrValueMapper.select(pmsSkuAttrValue);
   pmsSkuInfo.setSkuAttrValueList(select);
  }
  return pmsSkuInfos;
 }

 @Override
 public boolean checkPrice(String productSkuId, BigDecimal productPrice) {

  boolean b=false;

  PmsSkuInfo pmsSkuInfo=new PmsSkuInfo();
  pmsSkuInfo.setId(productSkuId);
  PmsSkuInfo pmsSkuInfo1 = pmsSkuInfoMapper.selectOne(pmsSkuInfo);

  BigDecimal price=pmsSkuInfo1.getPrice();

  if (price.compareTo(productPrice)==0){
   b=true;
  }

  return b;
 }
}