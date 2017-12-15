package com.eastsoft.esgjyj;

import com.eastsoft.esgjyj.dao.GeneratorDao;
import com.eastsoft.esgjyj.dao.YjkhDao;
import com.eastsoft.esgjyj.dao.YjkhKhdxDao;
import com.eastsoft.esgjyj.domain.YjkhDO;
import com.eastsoft.esgjyj.domain.YjkhKhdxDO;
import com.eastsoft.esgjyj.form.TreeNode;
import com.eastsoft.esgjyj.service.impl.GeneratorServiceImpl;
import com.eastsoft.esgjyj.service.impl.OfficeServiceImpl;
import com.eastsoft.esgjyj.util.GeneratorUtilSupplyForSybase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EsgjyjApplicationTests {
    @Autowired
    YjkhKhdxDao yjkhKhdxDao;

    @Test
    public void contextLoads() {
        YjkhKhdxDO yjkhKhdxDO = new YjkhKhdxDO();
        yjkhKhdxDO.setUserid("111");
        yjkhKhdxDO.setId("222");
        yjkhKhdxDO.setMbjas(0);
        yjkhKhdxDO.setPxh(0);
        yjkhKhdxDao.save(yjkhKhdxDO);
    }

}
