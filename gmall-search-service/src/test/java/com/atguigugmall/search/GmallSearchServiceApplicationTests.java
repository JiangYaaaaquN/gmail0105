package com.atguigugmall.search;


import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.service.SkuService;
import io.searchbox.client.JestClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
class GmallSearchServiceApplicationTests {

 @Reference
 SkuService skuService;

 @Autowired
 JestClient jestClient;


 @Test
 void contextLoads() throws IOException {
  //查询Mysql数据

  //转化为es的数据结构

  //导入es
  jestClient.execute(null);
 }

}
