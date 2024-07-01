package com.project.shopapp.mapper;

import com.project.shopapp.dto.request.OrderRequest;
import com.project.shopapp.dto.response.OrderResponse;
import com.project.shopapp.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface OrderMapper {
//    @Mapping()
    OrderResponse toOrderResponse(Order order);
    Order toOrder(OrderRequest request);
    void updateOrder(@MappingTarget Order order, OrderRequest request);
}
