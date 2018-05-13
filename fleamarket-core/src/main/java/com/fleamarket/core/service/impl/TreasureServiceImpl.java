package com.fleamarket.core.service.impl;

import com.fleamarket.core.IMapper;
import com.fleamarket.core.mapper.TreasureMapper;
import com.fleamarket.core.mapper.TreasurePictureMapper;
import com.fleamarket.core.model.Treasure;
import com.fleamarket.core.model.TreasurePicture;
import com.fleamarket.core.service.TreasureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Gss in 2018/5/4.
 */
@Service
public class TreasureServiceImpl extends BaseService<Treasure> implements TreasureService {
	private final TreasureMapper treasureMapper;
	private final TreasurePictureMapper treasurePictureMapper;

	@Autowired
	public TreasureServiceImpl(TreasureMapper treasureMapper, TreasurePictureMapper treasurePictureMapper) {
		this.treasureMapper = treasureMapper;
		this.treasurePictureMapper = treasurePictureMapper;
	}

	@Override
	public boolean treasurePublish(Treasure treasure) {
		return treasureMapper.insertTreasure(treasure);
	}

    @Override
    public List<Treasure> selectTreasureByUid(Integer uid) {
		Treasure treasure = new Treasure();
		treasure.setUid(uid);
        return treasureMapper.select(treasure);
    }


	@Override
	protected IMapper<Treasure> getMapper() {
		return treasureMapper;
	}
}
