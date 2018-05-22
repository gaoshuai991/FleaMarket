package com.fleamarket.core.service;

import com.fleamarket.core.model.Treasure;

import java.util.List;

/**
 * Created by Gss in 2018/5/4.
 */
public interface TreasureService extends IService<Treasure> {
	boolean treasurePublish(Treasure treasure);

	List<Treasure> selectByCategory(Integer category);

	List<Treasure> selectTreasureByUid(Integer uid);

	List<Treasure> selectViewList(Integer uid);

	/**
	 * 查找当前分类及其子分类
	 * @param cid
	 * @return
	 */
	List<Treasure> selectByCategoryId(Integer cid);

    List<Treasure> selectByStatusAndKeyword(Integer status,Integer categoryId, String column, String keyword);
}
