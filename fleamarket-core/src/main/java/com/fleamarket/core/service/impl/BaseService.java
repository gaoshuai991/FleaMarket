package com.fleamarket.core.service.impl;

import com.fleamarket.core.mapper.IMapper;
import com.fleamarket.core.service.IService;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;

/**
 * Created by Gss in 2018/3/17.
 * Service抽象类，提供有基础CRUD方法的实现
 */
public abstract class BaseService<T> implements IService<T> {
	/**
	 * 继承此抽象类的Service都必须使用此方法返回对应的Mapper对象
	 * @return
	 */
	protected abstract IMapper<T> getMapper();
	@Override
	public boolean insert(T pojo) {
		return getMapper().insert(pojo) > 0;
	}

	@Override
	public boolean insertUseGeneratedKeys(T pojo) {
		return getMapper().insertUseGeneratedKeys(pojo) > 0;
	}

	@Override
	public boolean insertSelective(T pojo) {
		return getMapper().insertSelective(pojo) > 0;
	}

	@Override
	public boolean existsWithPrimaryKey(Object key) {
		return getMapper().existsWithPrimaryKey(key);
	}

	@Override
	public boolean updateByPrimaryKey(T pojo) {
		return getMapper().updateByPrimaryKey(pojo) > 0;
	}

	@Override
	public boolean updateByPrimaryKeySelective(T pojo) {
		return getMapper().updateByPrimaryKeySelective(pojo) > 0;
	}

	@Override
	public int delete(T key) {
		return getMapper().delete(key);
	}

	@Override
	public int deleteByPrimaryKey(Object key) {
		return getMapper().deleteByPrimaryKey(key);
	}

	@Override
	public boolean deleteByPrimaryKeyList(Set<Integer> keys){
		if (keys == null || CollectionUtils.isEmpty(keys))
			return false;
		keys.forEach(getMapper()::deleteByPrimaryKey);
		return true;
	}

	@Override
	public List<T> selectList(T pojo) {
		return getMapper().select(pojo);
	}

	@Override
	public T selectOne(T pojo) {
		return getMapper().selectOne(pojo);
	}

	@Override
	public int selectCount(T record) {
		return getMapper().selectCount(record);
	}

	@Override
	public T selectByPrimaryKey(Object key) {
		return getMapper().selectByPrimaryKey(key);
	}

	@Override
	public List<T> selectAll() {
		return getMapper().selectAll();
	}

}
