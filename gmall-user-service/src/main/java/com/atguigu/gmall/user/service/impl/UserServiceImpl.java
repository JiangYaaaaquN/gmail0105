package com.atguigu.gmall.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.bean.UmsMember;
import com.atguigu.gmall.bean.UmsMemberReceiveAddress;
import com.atguigu.gmall.service.UserService;
import com.atguigu.gmall.user.mapper.UmsMemberReceiveAddressMapper;
import com.atguigu.gmall.user.mapper.UserMapper;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

 @Resource
 UserMapper userMapper;
 @Resource
 UmsMemberReceiveAddressMapper umsMemberReceiveAddressMapper;

 @Override
 public List<UmsMember> getAllUser() {
  List<UmsMember> umsMembersList=userMapper.selectAll();
  return  umsMembersList;
 }

 @Override
 public List<UmsMemberReceiveAddress> getReceiveAddressByMemberId(String memberId) {

//  UmsMemberReceiveAddress umsMemberReceiveAddresss=new UmsMemberReceiveAddress();
//  umsMemberReceiveAddresss.setMemberId(memberId);
//
//  List<UmsMemberReceiveAddresss> umsMemberReceiveAddressses= umsMemberReceiveAddressMapper.selectByExample(umsMemberReceiveAddresss);
//  List<UmsMemberReceiveAddress> umsMemberReceiveAddressses=umsMemberReceiveAddressMapper.select(umsMemberReceiveAddresss);
  Example example=new Example(UmsMemberReceiveAddress.class);
  example.createCriteria().andEqualTo("memberId",memberId);
  List<UmsMemberReceiveAddress> umsMemberReceiveAddressses=umsMemberReceiveAddressMapper.selectByExample(example);

  return umsMemberReceiveAddressses;
 }
}
