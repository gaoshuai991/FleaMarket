package com.fleamarket.core.service;

import com.fleamarket.core.model.Letter;

import java.util.Map;
import java.util.Set;

public interface LetterService extends IService<Letter> {
    /**
     * 获取用户会话列表
     * @param uid
     * @return e.g. [{"user": userObject,"letter": letterObject}]
     */
    Set<Map<String,Object>> selectSessionList(Integer uid);
}
