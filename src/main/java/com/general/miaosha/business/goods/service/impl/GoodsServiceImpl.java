package com.general.miaosha.business.goods.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.general.miaosha.business.goods.entity.Goods;
import com.general.miaosha.business.goods.entity.dto.OrderDTO;
import com.general.miaosha.business.goods.mapper.GoodsMapper;
import com.general.miaosha.business.goods.service.GoodsService;
import com.general.miaosha.business.order.entity.GoodsOrder;
import com.general.miaosha.business.order.service.GoodsOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
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
@Slf4j
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {

    @Autowired
    private GoodsOrderService goodsOrderService;

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.REPEATABLE_READ)
    @Override
    public void order(OrderDTO dto) {
        // 检查库存
        Goods goods = this.getById(dto.getGoodsId());
        if (Objects.equals(goods.getSaleCount(), goods.getStockCount())) {
//            log.error("库存已售完");
            //            throw new IllegalArgumentException("库存已售完");
            return;
        }

        Integer oldVersion = goods.getVersion();
        goods.setSaleCount(goods.getSaleCount() + 1);
        goods.setUpdateTime(LocalDateTime.now());
        if (!this.updateById(goods)) {
//            log.error("版本号未更新, Old: [{}], New[{}]", oldVersion, goods.getVersion());
            return;
        }

//        log.info("版本号更新成功, Old: [{}], New[{}]", oldVersion, goods.getVersion());

        // 生成订单
        GoodsOrder order = new GoodsOrder();
        order.setGoodsId(goods.getId());
        order.setCreateTime(LocalDateTime.now());
        goodsOrderService.save(order);
    }
}
