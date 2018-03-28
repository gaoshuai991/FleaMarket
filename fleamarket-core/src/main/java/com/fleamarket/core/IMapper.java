package com.fleamarket.core;

import tk.mybatis.mapper.annotation.RegisterMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * Created by Gss in 2018/3/17.
 * 在tkmybatis提供的通用接口上进行的扩展，使其支持MySQL特性
 * 所有的Mapper都应该实现此接口
 */
@RegisterMapper
public interface IMapper<T> extends Mapper<T>,MySqlMapper<T> {
	// TODO 可扩展通用接口
}