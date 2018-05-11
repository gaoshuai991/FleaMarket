package com.fleamarket.core.service;

import com.fleamarket.core.model.Treasure;
import com.fleamarket.core.model.TreasurePicture;

import java.util.List;

/**
 * Created by Gss in 2018/5/4.
 */
public interface TreasureService extends IService<Treasure> {
	boolean treasurePublish(Treasure treasure);

	List<TreasurePicture> selectAllTreasurePicture(Integer treasureId);

	List<Treasure> selectTreasureByUid(Integer uid);
}
