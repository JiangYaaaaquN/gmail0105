package com.atguigu.gmall.user.service.impl;


import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.bean.UmsMember;
import com.atguigu.gmall.bean.UmsMemberReceiveAddress;
import com.atguigu.gmall.service.UserService;

import com.atguigu.gmall.user.mapper.UmsMemberReceiveAddressMapper;
import com.atguigu.gmall.user.mapper.UserMapper;
import com.atguigu.gmall.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

 @Resource
 UserMapper userMapper;
 @Resource
 UmsMemberReceiveAddressMapper umsMemberReceiveAddressMapper;
 @Autowired
 RedisUtil redisUtil;

 @Override
 public List<UmsMember> getAllUser() {
  List<UmsMember> umsMembersList = userMapper.selectAll();
  return umsMembersList;
 }

 @Override
 public List<UmsMemberReceiveAddress> getReceiveAddressByMemberId(String memberId) {

//  UmsMemberReceiveAddress umsMemberReceiveAddresss=new UmsMemberReceiveAddress();
//  umsMemberReceiveAddresss.setMemberId(memberId);
//
//  List<UmsMemberReceiveAddresss> umsMemberReceiveAddressses= umsMemberReceiveAddressMapper.selectByExample(umsMemberReceiveAddresss);
//  List<UmsMemberReceiveAddress> umsMemberReceiveAddressses=umsMemberReceiveAddressMapper.select(umsMemberReceiveAddresss);
  Example example = new Example(UmsMemberReceiveAddress.class);
  example.createCriteria().andEqualTo("memberId", memberId);
  List<UmsMemberReceiveAddress> umsMemberReceiveAddressses = umsMemberReceiveAddressMapper.selectByExample(example);

  return umsMemberReceiveAddressses;
 }

 @Override
 public UmsMember login(UmsMember umsMember) {
  Jedis jedis=null;
  try {
   jedis = redisUtil.getJedis();
   if (jedis != null) {
    String umsMemberStr = jedis.get("user:" + umsMember.getPassword() + ":password");

    if (StringUtils.isNotBlank(umsMemberStr)) {
     //密码正确
     UmsMember umsMemberFromCache = JSON.parseObject(umsMemberStr, UmsMember.class);
     return umsMemberFromCache;
    }
   }
    //开启数据库
    UmsMember umsMemberFromDb=loginFromDb(umsMember);
    if (umsMemberFromDb!=null){
     jedis.setex("user:"+umsMember.getPassword()+":info",60*60*24,JSON.toJSONString(umsMemberFromDb));
    }
    return umsMemberFromDb;
  } finally {
   jedis.close();
  }
 }

 private UmsMember loginFromDb(UmsMember umsMember) {

  List<UmsMember> umsMembers= userMapper.select(umsMember);

  if (umsMembers!=null){
   return umsMembers.get(0);
  }

  return null;
 }
}