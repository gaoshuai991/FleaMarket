package com.fleamarket.core.mapper;

import com.fleamarket.core.IMapper;
import com.fleamarket.core.model.Treasure;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TreasureMapper extends IMapper<Treasure> {
	boolean insertTreasure(Treasure treasure);

	List<Treasure> selectByCategory(Integer category);
}