package com.fleamarket.core.service;

import com.fleamarket.core.model.Treasure;

/**
 * Created by Gss in 2018/5/4.
 */
public interface TreasureService extends IService<Treasure> {
	boolean treasurePublish(Treasure treasure);
}
