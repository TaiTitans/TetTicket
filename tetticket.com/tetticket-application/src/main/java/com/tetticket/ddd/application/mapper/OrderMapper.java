package com.tetticket.ddd.application.mapper;

import com.tetticket.ddd.application.model.OrderDTO;
import com.tetticket.ddd.domain.model.entity.Order;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {
    public static OrderDTO mapperToOrderDTO(Order order){
        if(order == null) return null;
        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(order, orderDTO);
        return orderDTO;
    }
    public static Order mapperToOrder(OrderDTO orderDTO){
        if(orderDTO == null) return null;
        Order order = new Order();
        BeanUtils.copyProperties(orderDTO, order);
        return order;
    }

}
