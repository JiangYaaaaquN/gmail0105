package com.atguigu.gmall.service;

import com.atguigu.gmall.bean.PmsBaseAttrInfo;
import com.atguigu.gmall.bean.PmsBaseAttrValue;
import com.atguigu.gmall.bean.PmsBaseSaleAttr;

import java.util.List;
import java.util.Set;

public interface AttrService {
 public List<PmsBaseAttrInfo> attrInfoList(String catalog3Id);

 String saveAttrInfo(PmsBaseAttrInfo pmsBaseAttrInfo);

 List<PmsBaseAttrValue> attrValueList(String attrId);

 List<PmsBaseSaleAttr> baseSaleAttrList();

 List<PmsBaseAttrInfo> getAttrValueListByValueId(Set<String> valueIdSet);
}