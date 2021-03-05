/**
 * BaseEntity
 * @description
 * @author jixinshi
 * @date 2021/3/1 3:47 PM
 * @version 2.11.2
 */
package com.general.miaosha.common.pojo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * BaseEntity
 * @description
 * @author jixinshi
 * @date 2021/3/1 3:47 PM
 * @version 2.11.2
 */
@Data
public class BaseEntity implements Serializable {

    private String id;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
