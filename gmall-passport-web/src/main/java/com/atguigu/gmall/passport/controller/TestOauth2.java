package com.atguigu.gmall.passport.controller;

import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.util.HttpclientUtil;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.util.HashMap;
import java.util.Map;

public class TestOauth2 {

 public static String getCode(){
  //1479835292
  //http://passport.gmall.com:8085/vlogin
  String s1=HttpclientUtil.doGet("https://api.weibo.com/oauth2/authorize?client_id=1479835292&response_type=code&redirect_uri=http://passport.gmall.com:8085/vlogin");

  System.out.println(s1);

  //30ff5cb202dcc361c6431fab310589cf
  String s2="http://passport.gmall.com:8085/vlogin?code=34cab92ffd7a23f920d2d00dc172158f";

  return s2;
 }

 public String getAccess_token(){
  //30ff5cb202dcc361c6431fab310589cf
  //c83e53ecfcce2d325346b2a6e95940b7
  String s3="https://api.weibo.com/oauth2/access_token?";//client_id=1479835292&client_secret=c83e53ecfcce2d325346b2a6e95940b7&grant_type=authorization_code&redirect_uri=http://passport.gmall.com:8085/vlogin";

  Map<String,String> paramMap=new HashMap<>();
  paramMap.put("client_id","1479835292");
  paramMap.put("client_secret","c83e53ecfcce2d325346b2a6e95940b7");
  paramMap.put("grant_type","authorization_code");
  paramMap.put("redirect_uri","http://passport.gmall.com:8085/vlogin");
  paramMap.put("code","34cab92ffd7a23f920d2d00dc172158f");//授权有效期内可以使用，每新生成一次授权码，说明用户对第三方数据进行重启授权，之前的access

  String access_token=HttpclientUtil.doPost(s3,paramMap);
  Map<String,String> access_map=JSON.parseObject(access_token,Map.class);

  System.out.println(access_map.get("access_token"));
  return access_map.get("access_token");
 }

 public Map<String,String> getUser_info(){
  //4 用access_token查询用户信息
  String s4="https://api.weibo.com/2/users/show.json?access_token=2.00h73nmFKlOJcBd5827191c1xryPJE&uid=6809985023";
  String user_json=HttpclientUtil.doGet(s4);
  Map<String,String> user_map=JSON.parseObject(user_json,Map.class);
  System.out.println(user_map.get("1"));
  return user_map;
 }


 public static void main(String[] args) {

  getCode();
 }
}