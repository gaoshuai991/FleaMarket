package com.fleamarket.core.mapper;

import com.fleamarket.core.IMapper;
import com.fleamarket.core.model.Letter;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LetterMapper extends IMapper<Letter> {
    List<Letter> selectSessionList(Integer uid);
}