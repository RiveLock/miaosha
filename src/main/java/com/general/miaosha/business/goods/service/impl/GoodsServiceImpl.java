package com.general.miaosha.business.goods.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.general.miaosha.business.goods.entity.Goods;
import com.general.miaosha.business.goods.entity.dto.OrderDTO;
import com.general.miaosha.business.goods.mapper.GoodsMapper;
import com.general.miaosha.business.goods.service.GoodsService;
import com.general.miaosha.business.order.entity.GoodsOrder;
import com.general.miaosha.business.order.service.GoodsOrderService;
import com.general.miaosha.common.consts.RedisConsts;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * <p>
 * 商品表 服务实现类
 * </p>
 *
 * @author John J
 * @since 2021-03-05
 */
@Slf4j
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {

    @Autowired
    private GoodsOrderService goodsOrderService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.REPEATABLE_READ)
    @Override
    public void order(OrderDTO dto) {
        // 检查库存

        //  使用缓存
        String sale = stringRedisTemplate.opsForValue().get(RedisConsts.GOOD_SALE + dto.getGoodsId());
        String stock = stringRedisTemplate.opsForValue().get(RedisConsts.GOOD_STOCK + dto.getGoodsId());
        String version = stringRedisTemplate.opsForValue().get(RedisConsts.GOOD_VERSION + dto.getGoodsId());
        if (!(StringUtils.isNotBlank(sale) && StringUtils.isBlank(stock) && StringUtils.isBlank(version))) {
            Goods goods = this.getById(dto.getGoodsId());
            Assert.notNull(goods, "商品不存在");

            sale = String.valueOf(goods.getSaleCount());
            stock = String.valueOf(goods.getStockCount());
            version = String.valueOf(goods.getVersion());
        }

        if (Objects.equals(sale, stock)) {
            log.error("库存已售完");
            //            throw new IllegalArgumentException("库存已售完");
            return;
        }


        Goods updateGoods = new Goods();
        updateGoods.setId(dto.getGoodsId());
        updateGoods.setSaleCount(Integer.valueOf(sale) + 1);
        updateGoods.setVersion(Integer.valueOf(version));
        updateGoods.setUpdateTime(LocalDateTime.now());
        if (!this.updateById(updateGoods)) {
            //            log.error("版本号未更新, Old: [{}], New[{}]", oldVersion, goods.getVersion());
            return;
        }

        //        log.info("版本号更新成功, Old: [{}], New[{}]", oldVersion, goods.getVersion());

        stringRedisTemplate.opsForValue().set(RedisConsts.GOOD_STOCK + dto.getGoodsId(), stock, Duration.ofMillis(RedisConsts.COMMON_TTL));
        stringRedisTemplate.opsForValue().set(RedisConsts.GOOD_SALE + dto.getGoodsId(), String.valueOf(updateGoods.getSaleCount()), Duration.ofMillis(RedisConsts.COMMON_TTL));
        stringRedisTemplate.opsForValue().set(RedisConsts.GOOD_VERSION + dto.getGoodsId(), String.valueOf(updateGoods.getVersion()), Duration.ofMillis(RedisConsts.COMMON_TTL));

        // 生成订单
        GoodsOrder order = new GoodsOrder();
        order.setGoodsId(dto.getGoodsId());
        order.setCreateTime(LocalDateTime.now());
        goodsOrderService.save(order);
    }
}
