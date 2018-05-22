package com.fleamarket.core.service.impl;

import com.fleamarket.core.IMapper;
import com.fleamarket.core.mapper.TreasureMapper;
import com.fleamarket.core.mapper.TreasureViewMapper;
import com.fleamarket.core.model.Treasure;
import com.fleamarket.core.model.TreasureView;
import com.fleamarket.core.service.TreasureService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gss in 2018/5/4.
 */
@Service
public class TreasureServiceImpl extends BaseService<Treasure> implements TreasureService {
	private final TreasureMapper treasureMapper;
	private final TreasureViewMapper treasureViewMapper;

	@Autowired
	public TreasureServiceImpl(TreasureMapper treasureMapper, TreasureViewMapper treasureViewMapper) {
		this.treasureMapper = treasureMapper;
		this.treasureViewMapper = treasureViewMapper;
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
    public List<Treasure> selectViewList(Integer uid) {
		List<Treasure> res = new ArrayList<>();
		TreasureView treasureView = new TreasureView();
		treasureView.setUserId(uid);
		PageHelper.orderBy("view_time desc");
		List<TreasureView> treasureViews = treasureViewMapper.select(treasureView);
		treasureViews.forEach(view -> res.add(selectByPrimaryKey(view.getTreasureId())));
		return res;
    }

    @Override
    public List<Treasure> selectByCategoryId(Integer cid) {
        return treasureMapper.selectByCategoryId(cid);
    }

	@Override
	public List<Treasure> selectByStatusAndKeyword(Integer status,Integer categoryId, String column, String keyword) {
		return treasureMapper.selectByStatusAndKeyword(status,categoryId,column,keyword);
	}

	@Override
	public List<Treasure> selectByCategory(Integer category) {
		return treasureMapper.selectByCategory(category);

	}

    @Override
	protected IMapper<Treasure> getMapper() {
		return treasureMapper;
	}
}
