package com.atguigu.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.bean.PmsBaseAttrInfo;
import com.atguigu.gmall.bean.PmsBaseAttrValue;
import com.atguigu.gmall.bean.PmsBaseSaleAttr;
import com.atguigu.gmall.manage.mapper.PmsBaseAttrInfoMapper;
import com.atguigu.gmall.manage.mapper.PmsBaseAttrValueMapper;
import com.atguigu.gmall.manage.mapper.PmsBaseSaleAttrMapper;
import com.atguigu.gmall.service.AttrService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class AttrServiceImpl implements AttrService {

 @Autowired
 PmsBaseAttrInfoMapper pmsBaseAttrInfoMapper;

 @Autowired
 PmsBaseAttrValueMapper pmsBaseAttrValueMapper;

 @Autowired
 PmsBaseSaleAttrMapper pmsBaseSaleAttrMapper;


 @Override
 public List<PmsBaseAttrInfo> attrInfoList(String catalog3Id) {
  PmsBaseAttrInfo pmsBaseAttrInfo=new PmsBaseAttrInfo();
  pmsBaseAttrInfo.setCatalog3Id(catalog3Id);
  List<PmsBaseAttrInfo> pmsBaseAttrInfos= pmsBaseAttrInfoMapper.select(pmsBaseAttrInfo);
  return pmsBaseAttrInfos;
 }

 @Override
 public String saveAttrInfo(PmsBaseAttrInfo pmsBaseAttrInfo) {

  String id=pmsBaseAttrInfo.getId();
  if (StringUtils.isBlank(id)){
   //id为空，保存

  //保存属性
  pmsBaseAttrInfoMapper.insertSelective(pmsBaseAttrInfo);
 //保存属性值
  List<PmsBaseAttrValue> attrValueList=pmsBaseAttrInfo.getAttrValueList();
   for (PmsBaseAttrValue pmsBaseAttrValue : attrValueList) {
   pmsBaseAttrValue.setAttrId(pmsBaseAttrInfo.getId());
    pmsBaseAttrValueMapper.insertSelective(pmsBaseAttrValue);
  }
  }else {
   //否则修改

   //属性
   Example example=new Example(PmsBaseAttrInfo.class);
   example.createCriteria().andEqualTo("id",pmsBaseAttrInfo.getId());
   pmsBaseAttrInfoMapper.updateByExampleSelective(pmsBaseAttrInfo,example);

   //属性值
   List<PmsBaseAttrValue> attrValueList=pmsBaseAttrInfo.getAttrValueList();

   //按照属性id删除所有属性值
   PmsBaseAttrValue pmsBaseAttrValueDel= new PmsBaseAttrValue();
   pmsBaseAttrValueDel.setAttrId(pmsBaseAttrInfo.getId());
   pmsBaseAttrValueMapper.delete(pmsBaseAttrValueDel);

   //删除之后，将新的属性直插入
   for (PmsBaseAttrValue pmsBaseAttrValue:attrValueList){
    pmsBaseAttrValueMapper.insertSelective(pmsBaseAttrValue);
   }
  }
  return "success";
 }

 @Override
 public List<PmsBaseAttrValue> attrValueList(String attrId) {
  PmsBaseAttrValue pmsBaseAttrValue=new PmsBaseAttrValue();
  pmsBaseAttrValue.setAttrId(attrId);
  List<PmsBaseAttrValue> pmsBaseAttrValues=pmsBaseAttrValueMapper.select(pmsBaseAttrValue);
  return pmsBaseAttrValues;
 }

 @Override
 public List<PmsBaseSaleAttr> baseSaleAttrList() {
  return pmsBaseSaleAttrMapper.selectAll();
 }
}