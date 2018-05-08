package com.fleamarket.core.service;

import com.fleamarket.core.model.User;

public interface UserService extends IService<User> {
    User selectByPrincipal(String principal);
    Boolean addUser(User user);
}
