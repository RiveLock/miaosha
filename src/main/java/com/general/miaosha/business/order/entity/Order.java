package com.general.miaosha.business.order.entity;

import com.general.miaosha.common.pojo.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 订单表
 * </p>
 *
 * @author John J
 * @since 2021-03-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "Order对象", description = "订单表")
public class Order extends BaseEntity {


    @ApiModelProperty(value = "商品id")
    private Integer goodsId;


}
