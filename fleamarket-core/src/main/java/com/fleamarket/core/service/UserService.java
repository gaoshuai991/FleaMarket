package com.fleamarket.core.service;

import com.fleamarket.core.model.User;

import java.util.List;

public interface UserService extends IService<User> {
    User selectByPrincipal(String principal);
    Boolean addUser(User user);

    List<User> selectByKeyword(String column, String keyword);
}
