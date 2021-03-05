/**
 * OrderDTO
 * @description
 * @author jixinshi
 * @date 2021/3/5 3:20 PM
 * @version 2.11.2
 */
package com.general.miaosha.business.goods.entity.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * OrderDTO
 * @author jixinshi
 */
@Data
@Accessors(chain = true)
public class OrderDTO {

    private Integer goodsId;

}
