package com.project.shopapp.mapper;

import com.project.shopapp.dto.request.OrderDetailRequest;
import com.project.shopapp.dto.response.OrderDetailResponse;
import com.project.shopapp.entity.OrderDetail;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface OrderDetailMapper {
    OrderDetail toOrderDetail(OrderDetailRequest request);
    OrderDetailResponse toOrderDetailResponse(OrderDetail orderDetail);
    void updateOrder(@MappingTarget OrderDetail orderDetail, OrderDetailRequest request);
}
