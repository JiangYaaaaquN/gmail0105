package com.atguigu.gmall.cart.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.bean.OmsCartItem;
import com.atguigu.gmall.cart.mapper.OmsCartItemMapper;
import com.atguigu.gmall.service.CartService;
import com.atguigu.gmall.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CartServiceImpl implements CartService {

 @Autowired
 OmsCartItemMapper omsCartItemMapper;

 @Autowired
 RedisUtil redisUtil;

 @Override
 public OmsCartItem ifCartExistByUser(String memberId, String skuId) {
  OmsCartItem omsCartItem=new OmsCartItem();
  omsCartItem.setMemberId(memberId);
  omsCartItem.setProductSkuId(skuId);
  OmsCartItem omsCartItem1=omsCartItemMapper.selectOne(omsCartItem);
  return omsCartItem1;
 }

 @Override
 public void addCart(OmsCartItem omsCartItem) {

  if (StringUtils.isNotBlank(omsCartItem.getMemberId())){
   omsCartItemMapper.insertSelective(omsCartItem);//避免添加空值
  }
 }

 @Override
 public void updateCart(OmsCartItem omsCartItemFromDb) {

  Example e=new Example(OmsCartItem.class);
  e.createCriteria().andEqualTo("id",omsCartItemFromDb.getId());
  omsCartItemMapper.updateByExampleSelective(omsCartItemFromDb,e);
 }

 @Override
 public void flushCartCache(String memberId) {
  OmsCartItem omsCartItem=new OmsCartItem();
  omsCartItem.setMemberId(memberId);
  List<OmsCartItem> omsCartItems=omsCartItemMapper.select(omsCartItem);

  //同步到redis缓存中
  Jedis jedis=redisUtil.getJedis();

  Map<String,String> map=new HashMap<>();
  for (OmsCartItem cartItem : omsCartItems) {
   map.put(cartItem.getProductSkuId(),JSON.toJSONString(omsCartItem));
  }

  jedis.hmset("user"+memberId+":cart",map);

  jedis.close();
 }
}