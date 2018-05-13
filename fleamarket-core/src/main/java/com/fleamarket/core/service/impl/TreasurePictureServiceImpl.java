package com.fleamarket.core.service.impl;

import com.fleamarket.core.IMapper;
import com.fleamarket.core.mapper.TreasurePictureMapper;
import com.fleamarket.core.model.TreasurePicture;
import com.fleamarket.core.service.TreasurePictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TreasurePictureServiceImpl extends BaseService<TreasurePicture> implements TreasurePictureService {
    private final TreasurePictureMapper mapper;

    @Autowired
    public TreasurePictureServiceImpl(TreasurePictureMapper mapper) {
        this.mapper = mapper;
    }
    @Override
    public List<TreasurePicture> selectAllTreasurePicture(Integer treasureId) {
        TreasurePicture treasurePicture = new TreasurePicture();
        treasurePicture.setTreasureId(treasureId);
        return mapper.select(treasurePicture);
    }
    @Override
    public boolean insertTreasurePicture(TreasurePicture treasurePicture) {
        return mapper.insertSelective(treasurePicture) == 1;
    }

    @Override
    public boolean deleteTreasurePicture(Integer tpid) {
        return mapper.deleteByPrimaryKey(tpid) == 1;
    }
    @Override
    protected IMapper<TreasurePicture> getMapper() {
        return mapper;
    }
}
