package com.fleamarket.core.service.impl;

import com.fleamarket.core.IMapper;
import com.fleamarket.core.mapper.LetterMapper;
import com.fleamarket.core.model.Letter;
import com.fleamarket.core.service.LetterService;
import com.fleamarket.core.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Log4j2
public class LetterServiceImpl extends BaseService<Letter> implements LetterService {
    private final LetterMapper letterMapper;
    private final UserService userService;

    @Autowired
    public LetterServiceImpl(LetterMapper letterMapper, UserService userService) {
        this.letterMapper = letterMapper;
        this.userService = userService;
    }

    @Override
    protected IMapper<Letter> getMapper() {
        return letterMapper;
    }

    @Override
    public Set<Map<String, Object>> selectSessionList(Integer uid) {
        Set<Map<String, Object>> sessionList = new LinkedHashSet<>(); // TODO

        List<Letter> letters = letterMapper.selectSessionList(uid);
        letters.forEach(letter -> {
            Integer targetId = letter.getSourceUserId().equals(uid) ? letter.getTargetUserId() : letter.getSourceUserId();
            sessionList.add(new LinkedHashMap<String, Object>(){{
                put("user", userService.selectByPrimaryKey(targetId));
                put("letter", letter);
            }});
        });
        return sessionList;
    }
}
