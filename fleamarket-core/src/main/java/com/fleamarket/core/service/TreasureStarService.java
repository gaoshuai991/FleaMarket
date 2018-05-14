package com.fleamarket.core.service;

import com.fleamarket.core.model.TreasureStar;

public interface TreasureStarService extends IService<TreasureStar> {
    boolean isStar(Integer uid, Integer tid);
}
