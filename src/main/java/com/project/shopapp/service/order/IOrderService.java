package com.project.shopapp.service.order;

import com.project.shopapp.dto.request.OrderRequest;
import com.project.shopapp.dto.response.OrderResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IOrderService {
    OrderResponse createOrder(OrderRequest request);
    OrderResponse getOrder(Long id);
    OrderResponse updateOrder(Long id, OrderRequest request) ;
    void deleteOrder(Long id);
    List<OrderResponse> findByUserId(Long userId);
    Page<OrderResponse> getOrdersByKeyword(String keyword, int limit, int page);
}
