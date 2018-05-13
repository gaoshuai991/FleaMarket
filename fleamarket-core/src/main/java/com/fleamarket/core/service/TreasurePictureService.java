package com.fleamarket.core.service;

import com.fleamarket.core.model.Treasure;
import com.fleamarket.core.model.TreasurePicture;

import java.util.List;

public interface TreasurePictureService extends IService<TreasurePicture> {
    List<TreasurePicture> selectAllTreasurePicture(Integer treasureId);

    boolean insertTreasurePicture(TreasurePicture treasurePicture);

    boolean deleteTreasurePicture(Integer tpid);
}
