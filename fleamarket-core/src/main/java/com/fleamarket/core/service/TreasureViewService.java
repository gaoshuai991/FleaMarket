package com.fleamarket.core.service;

import com.fleamarket.core.model.TreasureView;

public interface TreasureViewService extends IService<TreasureView> {
    boolean treasureView(Integer uid, Integer tid);
}
