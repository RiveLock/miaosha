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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDateTime;

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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean order(OrderDTO dto) {
        // 检查库存
        int stockCount = this.stockCount(dto.getGoodsId());
        if (stockCount <= 0) {
            log.error("库存已售完");
            return false;
        }

        Goods goods = this.getById(dto.getGoodsId());
        goods.setSaleCount(goods.getSaleCount()+ 1);
        goods.setUpdateTime(LocalDateTime.now());
        if (!this.updateById(goods)) {
            return false;
        }

        // 生成订单
        GoodsOrder order = new GoodsOrder();
        order.setGoodsId(dto.getGoodsId());
        order.setCreateTime(LocalDateTime.now());
        goodsOrderService.save(order);
        return true;
    }

    @Override
    public Integer stockCount(Integer id) {
        String stockCount = stringRedisTemplate.opsForValue().get(RedisConsts.GOOD_STOCK_COUNT + id);
        if (StringUtils.isNotBlank(stockCount)) {
            return Integer.valueOf(stockCount);
        }
        int count = getStockCount(id);
        stringRedisTemplate.opsForValue().set(RedisConsts.GOOD_STOCK_COUNT + id, String.valueOf(count), RedisConsts.COMMON_TTL);
        return count;
    }

    /**
     * 获得库存数量
     * @param id
     * @return
     */
    private int getStockCount(Integer id) {
        Goods goods = this.getById(id);
        Assert.notNull(goods, "商品不存在");
        return goods.getAllCount() - goods.getSaleCount();
    }

    @Override
    public void clearStockCache(Integer goodsId) {
        stringRedisTemplate.delete(RedisConsts.GOOD_STOCK_COUNT + goodsId);
    }
}
