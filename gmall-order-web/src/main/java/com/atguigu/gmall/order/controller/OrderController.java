package com.atguigu.gmall.order.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.annotations.LoginRequired;
import com.atguigu.gmall.bean.OmsCartItem;
import com.atguigu.gmall.bean.OmsOrder;
import com.atguigu.gmall.bean.OmsOrderItem;
import com.atguigu.gmall.bean.UmsMemberReceiveAddress;
import com.atguigu.gmall.service.CartService;
import com.atguigu.gmall.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class OrderController {

 @Reference
 CartService cartService;
 @Reference
 UserService userService;

 @RequestMapping("toTrade")
 @LoginRequired(loginSuccess = true)
 public String toTrade(HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap modelMap) {

  String memberId = (String) request.getAttribute("memberId");
  String nickname = (String) request.getAttribute("nickname");

  //收件人地址列表
  List<UmsMemberReceiveAddress> umsMemberReceiveAddresses=userService.getReceiveAddressByMemberId(memberId);
  //将购物车集合转化为页面计算结算清单
  List<OmsCartItem> omsCartItems=cartService.cartList(memberId);

  List<OmsOrderItem> omsOrderItems=new ArrayList<>();
  for (OmsCartItem omsCartItem : omsCartItems) {
   //每循环一个购物车对象就封装一个商品的详情
   if (omsCartItem.getIsChecked().equals("1")){
    OmsOrderItem omsOrderItem=new OmsOrderItem();
    omsOrderItem.setProductName(omsCartItem.getProductName());
    omsOrderItem.setProductPic(omsCartItem.getProductPic());
    omsOrderItems.add(omsOrderItem);
   }
  }
  modelMap.put("omsOrderItems",omsOrderItems);
  modelMap.put("userAddressList",umsMemberReceiveAddresses);
  modelMap.put("totalAmount",getTotaAmount(omsCartItems));

  //生成交易密码，为了在提交订单时做交易码的校验
  return "trade";
 }

 private BigDecimal getTotaAmount(List<OmsCartItem> omsCartItems) {
  BigDecimal totalAmount=new BigDecimal("0");

  for (OmsCartItem omsCartItem : omsCartItems) {
   BigDecimal totalPrice=omsCartItem.getTotalPrice();
   if (omsCartItem.getIsChecked().equals("1")) {
    totalAmount = totalAmount.add(totalPrice);
   }
  }
  return totalAmount;
 }

 @RequestMapping("submitOrder")
 @LoginRequired(loginSuccess = true)
 public String submitOrder(String receiveAddressId,BigDecimal totalAmount,OmsOrder omsOrder,HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap modelMap) {

  String memberId = (String) request.getAttribute("memberId");
  String nickname = (String) request.getAttribute("nickname");

  //根据用户id获得要购买的商品列表（购物车），和总价格

  //将订单和订单详情写入数据库

  //删除购物车的对应商品

  //重定向到支付系统
  return null;
 }
 }