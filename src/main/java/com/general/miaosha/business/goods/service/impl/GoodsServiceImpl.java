package com.general.miaosha.business.goods.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.general.miaosha.business.goods.entity.Goods;
import com.general.miaosha.business.goods.entity.dto.OrderDTO;
import com.general.miaosha.business.goods.mapper.GoodsMapper;
import com.general.miaosha.business.goods.service.GoodsService;
import com.general.miaosha.business.order.entity.GoodsOrder;
import com.general.miaosha.business.order.service.GoodsOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {

    @Autowired
    private GoodsOrderService goodsOrderService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void order(OrderDTO dto) {
        // 检查库存
        Goods goods = this.getById(dto.getGoodsId());
        if (Objects.equals(goods.getSaleCount(), goods.getStockCount())) {
            throw new IllegalArgumentException("库存已售完");
        }

        // 删除库存
        goods.setStockCount(goods.getSaleCount() - 1);
        goods.setUpdateTime(LocalDateTime.now());
        this.updateById(goods);

        // 生成订单
        GoodsOrder order = new GoodsOrder();
        order.setGoodsId(goods.getId());
        order.setCreateTime(LocalDateTime.now());
        goodsOrderService.save(order);
    }
}
