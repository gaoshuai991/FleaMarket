package com.fleamarket.core.service.impl;

import com.fleamarket.core.IMapper;
import com.fleamarket.core.mapper.TreasureViewMapper;
import com.fleamarket.core.model.TreasureView;
import com.fleamarket.core.service.TreasureViewService;
import org.springframework.stereotype.Service;

@Service
public class TreasureViewServiceImpl extends BaseService<TreasureView> implements TreasureViewService {
    private final TreasureViewMapper treasureViewMapper;

    public TreasureViewServiceImpl(TreasureViewMapper treasureViewMapper) {
        this.treasureViewMapper = treasureViewMapper;
    }

    @Override
    protected IMapper<TreasureView> getMapper() {
        return treasureViewMapper;
    }

    @Override
    public boolean treasureView(Integer uid, Integer tid) {
        TreasureView treasureView = new TreasureView();
        treasureView.setUserId(uid);
        treasureView.setTreasureId(tid);
        delete(treasureView);
        return insertSelective(treasureView);
    }
}
