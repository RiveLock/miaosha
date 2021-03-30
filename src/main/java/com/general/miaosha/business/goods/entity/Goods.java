package com.general.miaosha.business.goods.entity;

import com.baomidou.mybatisplus.annotation.Version;
import com.general.miaosha.common.pojo.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * <p>
 * 商品表
 * </p>
 *
 * @author John J
 * @since 2021-03-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "Goods对象", description = "商品表")
public class Goods extends BaseEntity {


    @ApiModelProperty(value = "商品名称")
    private String name;

    @ApiModelProperty(value = "价格")
    private BigDecimal price;

    @ApiModelProperty(value = "总数量")
    private Integer allCount;

    @ApiModelProperty(value = "销售数量")
    private Integer saleCount;

    @ApiModelProperty(value = "版本号，用于乐观锁")
    @Version
    private Integer version;

}
