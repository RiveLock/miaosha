/**
 * GoodsTest
 * @description
 * @author jixinshi
 * @date 2021/3/5 3:48 PM
 * @version 2.11.2
 */
package com.general.miaosha;

import com.general.miaosha.business.goods.entity.Goods;
import com.general.miaosha.business.goods.entity.dto.OrderDTO;
import com.general.miaosha.business.goods.service.GoodsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * GoodsTest
 * @description
 * @author jixinshi
 */
@SpringBootTest
public class GoodsTest {

    @Autowired
    private GoodsService goodsService;

    @Test
    public void addGoodsTest() {
        Goods goods = new Goods();
        goods.setName("iphone 12");
        goods.setPrice(new BigDecimal(2000));
        goods.setStockCount(100);
        goods.setSaleCount(0);
        goods.setVersion(0);
        goods.setCreateTime(LocalDateTime.now());

        goodsService.save(goods);
    }

    @Test
    public void listTest() {
        System.out.println(goodsService.list());
    }


    @Test
    public void orderTest() {
        goodsService.order(new OrderDTO().setGoodsId(1));
    }

}
