package com.project.shopapp.service.orderDetail;

import com.project.shopapp.dto.request.OrderDetailRequest;
import com.project.shopapp.dto.response.OrderDetailResponse;

import java.util.List;

public interface IOrderDetailService {
    OrderDetailResponse createOrderDetail(OrderDetailRequest request);
    OrderDetailResponse getOrderDetail(Long id);
    OrderDetailResponse updateOrderDetail(Long id, OrderDetailRequest request);
    void deleteById(Long id);
    List<OrderDetailResponse> findByOrderId(Long orderId);
}
