package com.atguigu.gmall.manage.controller;

import com.atguigu.gmall.bean.PmsSkuInfo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@CrossOrigin
public class SkuController {

 @RequestMapping("saveSkuInfo")
 @ResponseBody
 public String saveSkuInfo(@RequestBody PmsSkuInfo pmsSkuInfo){

  return "success";
 }
}