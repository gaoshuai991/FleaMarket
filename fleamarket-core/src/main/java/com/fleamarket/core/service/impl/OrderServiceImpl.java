package com.fleamarket.core.service.impl;

import com.fleamarket.core.IMapper;
import com.fleamarket.core.mapper.OrderMapper;
import com.fleamarket.core.mapper.TreasureMapper;
import com.fleamarket.core.model.Order;
import com.fleamarket.core.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jackiegao on 2018/4/10.
 */
@Service
public class OrderServiceImpl extends BaseService<Order> implements OrderService {
	private final TreasureMapper tMapper;
	private final OrderMapper mapper;
	public Boolean addOrder(Order order){
		return mapper.addOrder(order);
	}
	@Autowired
	public OrderServiceImpl(OrderMapper mapper,TreasureMapper tMapper) {
		this.mapper = mapper;
		this.tMapper=tMapper;
	}
	@Override
	protected IMapper<Order> getMapper() {
		return mapper;
	}

	//通过用户ID获取订单信息和订单图片
	@Override
	public Map<String,Order> selectByUserId(Integer userId) {
		List<Order> list=mapper.selectByUserId(userId);
		Map<String,Order> map= new LinkedHashMap<String,Order>();
		for(Order order : list){
			Integer treasureId =order.getTreasureId();
			String photo=tMapper.selectByPrimaryKey(treasureId).getPicture();
			map.put(photo,order);
		}
		return map;
	}
}
