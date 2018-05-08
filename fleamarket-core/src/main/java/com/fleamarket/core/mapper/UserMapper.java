package com.fleamarket.core.mapper;

import com.fleamarket.core.IMapper;
import com.fleamarket.core.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper extends IMapper<User> {
    /**
     * 根据登录名（可以是用户名、手机号）取得用户信息
     * @param principal
     * @return
     */
    User selectByPrincipal(@Param("principal") String principal);
    Boolean addUser(User user);
}