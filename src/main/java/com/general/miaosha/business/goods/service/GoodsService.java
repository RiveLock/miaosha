package com.general.miaosha.business.goods.service;

import com.general.miaosha.business.goods.entity.Goods;
import com.baomidou.mybatisplus.extension.service.IService;
import com.general.miaosha.business.goods.entity.dto.OrderDTO;

/**
 * <p>
 * 商品表 服务类
 * </p>
 *
 * @author John J
 * @since 2021-03-05
 */
public interface GoodsService extends IService<Goods> {

    boolean order(OrderDTO dto);

    Integer stockCount(Integer id);

    /**
     * 清除库存缓存
     * @param goodsId
     */
    void clearStockCache(Integer goodsId);

}
