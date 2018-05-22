package com.fleamarket.core.mapper;

import com.fleamarket.core.IMapper;
import com.fleamarket.core.model.Treasure;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TreasureMapper extends IMapper<Treasure> {
    boolean insertTreasure(Treasure treasure);

    List<Treasure> selectByCategory(Integer category);

    List<Treasure> selectByCategoryId(Integer category);

    List<Treasure> selectByStatusAndKeyword(@Param("status") Integer status, @Param("categoryId") Integer categoryId, @Param("column") String column, @Param("keyword") String keyword);
}