package com.general.miaosha.business.order.service.impl;

import com.general.miaosha.business.order.entity.Order;
import com.general.miaosha.business.order.mapper.OrderMapper;
import com.general.miaosha.business.order.service.OrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author John J
 * @since 2021-03-05
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

}
