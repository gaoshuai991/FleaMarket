package com.fleamarket.core.service.impl;

import com.fleamarket.core.IMapper;
import com.fleamarket.core.mapper.TreasureMapper;
import com.fleamarket.core.model.Treasure;
import com.fleamarket.core.service.TreasureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Gss in 2018/5/4.
 */
@Service
public class TreasureServiceImpl extends BaseService<Treasure> implements TreasureService {
	@Autowired
	private TreasureMapper treasureMapper;
	@Override
	public boolean treasurePublish(Treasure treasure) {
		return treasureMapper.insertTreasure(treasure);
	}

	@Override
	protected IMapper<Treasure> getMapper() {
		return treasureMapper;
	}
}
