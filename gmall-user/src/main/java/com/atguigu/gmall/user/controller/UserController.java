package com.atguigu.gmall.user.controller;



import com.atguigu.gmall.bean.UmsMember;
import com.atguigu.gmall.bean.UmsMemberReceiveAddress;
import com.atguigu.gmall.service.UserService;
import com.atguigu.gmall.user.mapper.UmsMemberReceiveAddressMapper;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
public class UserController {

 @Resource
 UserService userService;
 @Resource
 UmsMemberReceiveAddressMapper umsMemberReceiveAddressMapper;

 @RequestMapping("getReceiveAddressByMemberId")
 @ResponseBody
 public List<UmsMemberReceiveAddress> getReceiveAddressByMemberId(String memberId){
  List<UmsMemberReceiveAddress> umsMemberReceiveAddressses= userService.getReceiveAddressByMemberId(memberId);
  return umsMemberReceiveAddressses;
 }

 @RequestMapping("getAllUser")
 @ResponseBody
 public List<UmsMember> getAllUser(){
  List<UmsMember> userMenbers= userService.getAllUser();
  return userMenbers;
 }

 @RequestMapping("index")
 @ResponseBody
 public  String index(){
  return  "hello index";
 }
}
