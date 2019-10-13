package com.atguigu.gmall.service;

import com.atguigu.gmall.bean.PmsBaseAttrInfo;
import com.atguigu.gmall.bean.PmsBaseAttrValue;

import java.util.List;

public interface AttrService {
 public List<PmsBaseAttrInfo> attrInfoList(String catalog3Id);

 String saveAttrInfo(PmsBaseAttrInfo pmsBaseAttrInfo);

 List<PmsBaseAttrValue> attrValueList(String attrId);
}