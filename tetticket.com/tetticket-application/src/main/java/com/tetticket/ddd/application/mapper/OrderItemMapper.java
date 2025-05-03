package com.tetticket.ddd.application.mapper;

import com.tetticket.ddd.application.model.OrderItemDTO;
import com.tetticket.ddd.domain.model.entity.OrderItem;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class OrderItemMapper {
    public static OrderItemDTO mapperOrderItemToDTO(OrderItem orderItem){
        if(orderItem == null) return null;
        OrderItemDTO orderItemDTO = new OrderItemDTO();
        BeanUtils.copyProperties(orderItem, orderItemDTO);
        return orderItemDTO;
    }
    public static OrderItem mapperDTOToOrderItem(OrderItemDTO orderItemDTO){
        if(orderItemDTO == null) return null;
        OrderItem orderItem = new OrderItem();
        BeanUtils.copyProperties(orderItemDTO, orderItem);
        return orderItem;
    }
}
