/**
 * MiaoShaTest
 * @description
 * @author jixinshi
 * @date 2021/3/5 4:26 PM
 * @version 2.11.2
 */
package com.general.miaosha;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.general.miaosha.business.goods.entity.Goods;
import com.general.miaosha.business.goods.entity.dto.OrderDTO;
import com.general.miaosha.business.goods.service.GoodsService;
import com.general.miaosha.business.order.entity.GoodsOrder;
import com.general.miaosha.business.order.service.GoodsOrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * MiaoShaTest
 * @author jixinshi
 */
@SpringBootTest
public class MiaoShaTest {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private GoodsOrderService goodsOrderService;

    private final int STOCK_COUNT = 100;

    /**
     * 模拟 THREAD_COUNT 人抢购 STOCK_COUNT 个商品
     */
    private final int THREAD_COUNT = 1000;

    @Test
    public void initData() {
        Goods goods = goodsService.getById(1);
        goods.setStockCount(STOCK_COUNT);
        goods.setSaleCount(0);
        goods.setCreateTime(LocalDateTime.now());
        goodsService.updateById(goods);

        goodsOrderService.remove(new LambdaQueryWrapper<GoodsOrder>().eq(GoodsOrder::getGoodsId, 1));
    }

    @Test
    public void miaosha() throws InterruptedException {
        System.out.println("============= 初始化数据 ===============");
        this.initData();

        System.out.println("============= goods 抢购前 ===============");
        Goods befor = goodsService.getById(1);
        System.out.println(befor);

        System.out.println("============= 开始抢购 ===============");

        CountDownLatch countDownLatch = new CountDownLatch(THREAD_COUNT);
        for (int i = 1; i <= THREAD_COUNT; i++) {
            new Thread(() -> {
                goodsService.order(new OrderDTO().setGoodsId(1));
                countDownLatch.countDown();
            }).start();

        }
        countDownLatch.await();
        System.out.println("============= goods 抢购后 ===============");
        Goods after = goodsService.getById(1);
        System.out.println(after);

        System.out.println("============= 订单数 ===============");
        int orderCount = goodsOrderService.count();
        System.out.println(orderCount);

        assertThat(STOCK_COUNT).isGreaterThanOrEqualTo(orderCount);

    }
}
