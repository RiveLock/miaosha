package com.general.miaosha.business.goods.controller;


import com.general.miaosha.business.goods.entity.Goods;
import com.general.miaosha.business.goods.entity.dto.OrderDTO;
import com.general.miaosha.business.goods.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 商品表 前端控制器
 * </p>
 *
 * @author John J
 * @since 2021-03-05
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;


    @GetMapping("")
    public ResponseEntity<List<Goods>> list() {
        return ResponseEntity.ok(goodsService.list());
    }

    /**
     * 获得库存数量
     * @param id
     * @return
     */
    @GetMapping("/stock-count/{id}")
    public ResponseEntity<Integer> stockCount(@PathVariable Integer id) {
        Integer count = goodsService.stockCount(id);
        return ResponseEntity.ok(count);
    }

    /**
     * 下订单
     * @param dto
     * @return
     */
    @PostMapping("/order")
    public ResponseEntity<Boolean> order(@RequestBody OrderDTO dto) {
        boolean success = goodsService.order(dto);
        if (success) {
            goodsService.clearStockCache(dto.getGoodsId());
        }
        return ResponseEntity.ok(true);
    }


}
