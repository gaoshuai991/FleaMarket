package com.fleamarket.core.mapper;

import com.fleamarket.core.IMapper;
import com.fleamarket.core.model.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderMapper extends IMapper<Order> {
    Boolean addOrder(Order order);


    List<Order> selectByUserId(Integer userId);

    List<Order> selectByKeyword(@Param("column") String column,@Param("keyword") String keyword);

    List<Order> selectSales(Integer userId);
}