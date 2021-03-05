package com.general.miaosha.business.sample.entity.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.general.miaosha.business.sample.entity.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 自定义分页
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class UserPage extends Page<User> {

    private Integer selectInt;
    private String selectStr;

    public UserPage(long current, long size) {
        super(current, size);
    }
}
