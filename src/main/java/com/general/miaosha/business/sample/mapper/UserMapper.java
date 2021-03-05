package com.general.miaosha.business.sample.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.general.miaosha.business.sample.entity.User;
import com.general.miaosha.business.sample.entity.dto.UserPage;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author John J
 * @since 2021-03-03
 */
public interface UserMapper extends BaseMapper<User> {

    UserPage selectUserPage(UserPage userPage);

}
