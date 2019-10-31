package com.atguigu.gmall.passport.controller;

import com.atguigu.gmall.util.HttpclientUtil;

public class TestOauth2 {
 public static void main(String[] args) {

  //1479835292
  //http://passport.gmall.com:8085/vlogin
  //String s1=HttpclientUtil.doGet("https://api.weibo.com/oauth2/authorize?client_id=1479835292&response_type=code&redirect_uri=http://passport.gmall.com:8085/vlogin");

  //System.out.println(s1);

  //30ff5cb202dcc361c6431fab310589cf
  String s2="http://passport.gmall.com:8085/vlogin?code=30ff5cb202dcc361c6431fab310589cf";

  //30ff5cb202dcc361c6431fab310589cf
  //c83e53ecfcce2d325346b2a6e95940b7
  String s3="https://api.weibo.com/oauth2/access_token?client_id=1479835292&client_secret=c83e53ecfcce2d325346b2a6e95940b7&grant_type=authorization_code&redirect_uri=http://passport.gmall.com:8085/vlogin";

  String access_token=HttpclientUtil.doPost(s3,null);

  System.out.println(access_token);
 }
}