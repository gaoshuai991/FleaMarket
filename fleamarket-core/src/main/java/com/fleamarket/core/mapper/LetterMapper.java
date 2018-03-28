package com.fleamarket.core.mapper;

import com.fleamarket.core.IMapper;
import com.fleamarket.core.model.Letter;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LetterMapper extends IMapper<Letter> {
}