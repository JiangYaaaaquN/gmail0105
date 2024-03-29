package com.atguigu.gmall.payment.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.atguigu.gmall.annotations.LoginRequired;
import com.atguigu.gmall.bean.OmsOrder;
import com.atguigu.gmall.bean.PaymentInfo;
import com.atguigu.gmall.payment.config.AlipayConfig;
import com.atguigu.gmall.service.OrderService;
import com.atguigu.gmall.service.PaymentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class PaymentController {

// @Autowired
// AlipayClient alipayClient;
 @Autowired
 PaymentService paymentService;
 @Reference
 OrderService orderService;

 @RequestMapping("alipay/callback/return")
 @LoginRequired(loginSuccess = true)
 public String aliPayCallBackReturn( HttpServletRequest request) {

  //回调请求中获取支付宝参数
  String sign=request.getParameter("sign");
  String trade_no=request.getParameter("trade_no");
  String out_trade_no=request.getParameter("out_trade_no");
  String trade_status=request.getParameter("trade_status");
  String total_amount=request.getParameter("total_amount");
  String subject=request.getParameter("subject");
  String call_back_content=request.getQueryString();

  //通过支付宝的paramsMap进行验签，2.0版本的接口将paramsMap参数去掉了，导致同步请求没法验签
  if (StringUtils.isNotBlank(sign)){
   //验证成功
   PaymentInfo paymentInfo=new PaymentInfo();
   paymentInfo.setOrderSn(out_trade_no);
   paymentInfo.setPaymentStatus("已支付");
   paymentInfo.setAlipayTradeNo(trade_no);//支付宝的交易凭证号
   paymentInfo.setCallbackContent(call_back_content);//回调请求字符串
   paymentInfo.setCallbackTime(new Date());
   //更新用户的支付状态
   paymentService.updatePayment(paymentInfo);
  }

  //支付成功后，引起的系统服务—》订单服务更新—》库存服务—》物流服务

  //更新用户的支付状态
  return "finish";
 }

 @RequestMapping("mx/submit")
 @LoginRequired(loginSuccess = true)
 public String mx(String outTradeNo, BigDecimal totalAmount, ModelMap modelMap, HttpServletRequest request) {

  return null;
 }

 @RequestMapping("alipay/submit")
 @LoginRequired(loginSuccess = true)
 @ResponseBody
 public String send(HttpServletRequest request, HttpServletResponse response) throws Exception {
  //获得初始化的AlipayClient
  AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id, AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key, AlipayConfig.sign_type);

  //设置请求参数
  AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
  alipayRequest.setReturnUrl(AlipayConfig.return_url);
  alipayRequest.setNotifyUrl(AlipayConfig.notify_url);

  //商户订单号，商户网站订单系统中唯一订单号，必填
  String out_trade_no = new String(request.getParameter("outTradeNo").getBytes("ISO-8859-1"), "UTF-8");
  //付款金额，必填
  String total_amount = new String(request.getParameter("totalAmount").getBytes("ISO-8859-1"), "UTF-8");
  //订单名称，必填
  String subject = new String("测试");
  //商品描述，可空
  String body = new String("");

  alipayRequest.setBizContent("{\"out_trade_no\":\"" + out_trade_no + "\","
          + "\"total_amount\":\"" + total_amount + "\","
          + "\"subject\":\"" + subject + "\","
          + "\"body\":\"" + body + "\","
          + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

  //若想给BizContent增加其他可选请求参数，以增加自定义超时时间参数timeout_express来举例说明
  //alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
  //		+ "\"total_amount\":\""+ total_amount +"\","
  //		+ "\"subject\":\""+ subject +"\","
  //		+ "\"body\":\""+ body +"\","
  //		+ "\"timeout_express\":\"10m\","
  //		+ "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
  //请求参数可查阅【电脑网站支付的API文档-alipay.trade.page.pay-请求参数】章节

  String head = "<html><head><meta http-equiv='Content-Type' content='text/html;charset=utf-8'></head>";

  //请求
  String result = alipayClient.pageExecute(alipayRequest).getBody();

  String buttom = "<body></body></html>";
//  Map<String,Object> map=new HashMap<>();
//  map.put("out_trade_no",outTradeNo);
//  map.put("product_code","FAST_INSTANT_TRADE_PAY");
//  map.put("total_amount",totalAmount);
//  map.put("subject","dd");

//  String param=JSON.toJSONString(map);

//  alipayRequest.setBizContent(param);
//  try {
//   form=alipayClient.pageExecute(alipayRequest).getBody();//调用SDK生成表单
//  } catch (AlipayApiException e) {
//   e.printStackTrace();
//  }

  //生成并且保存用户的支付信息
  OmsOrder omsOrder= orderService.getOrderByOutTradeNo(out_trade_no);
  PaymentInfo paymentInfo=new PaymentInfo();
  paymentInfo.setCreateTime(new Date());
  paymentInfo.setOrderId(omsOrder.getId());
  paymentInfo.setOrderSn(out_trade_no);
  paymentInfo.setPaymentStatus("未付款");
  paymentInfo.setSubject("谷粒商城商品意见");
  paymentInfo.setTotalAmount(new BigDecimal(total_amount));
  paymentService.savePaymentInfo(paymentInfo);

  //提交请求到支付宝
  return result;
 }

 @RequestMapping("index")
 @LoginRequired(loginSuccess = true)
 public String index(String outTradeNo, BigDecimal totalAmount, ModelMap modelMap, HttpServletRequest request) {
  String memberId = (String) request.getAttribute("memberId");
  String nickName = (String) request.getAttribute("nickName");

  modelMap.put("nickName", nickName);
  modelMap.put("outTradeNo", outTradeNo);
  modelMap.put("totalAmount", totalAmount);
  return "index";
 }
}