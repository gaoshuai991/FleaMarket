package com.fleamarket.core.service.impl;

import com.fleamarket.core.IMapper;
import com.fleamarket.core.mapper.TreasureStarMapper;
import com.fleamarket.core.model.TreasureStar;
import com.fleamarket.core.service.TreasureStarService;
import org.springframework.stereotype.Service;

@Service
public class TreasureStarServiceImpl extends BaseService<TreasureStar> implements TreasureStarService {
    private final TreasureStarMapper treasureStarMapper;

    public TreasureStarServiceImpl(TreasureStarMapper treasureStarMapper) {
        this.treasureStarMapper = treasureStarMapper;
    }

    @Override
    protected IMapper<TreasureStar> getMapper() {
        return treasureStarMapper;
    }

    @Override
    public boolean isStar(Integer uid, Integer tid) {
        TreasureStar treasureStar = new TreasureStar();
        treasureStar.setUserId(uid);
        treasureStar.setTreasureId(tid);
        return selectOne(treasureStar) != null;
    }
}
