package com.project.shopapp.service.orderDetail;

import com.project.shopapp.dto.request.OrderDetailRequest;
import com.project.shopapp.dto.response.OrderDetailResponse;
import com.project.shopapp.entity.Order;
import com.project.shopapp.entity.OrderDetail;
import com.project.shopapp.entity.Product;
import com.project.shopapp.exception.AppException;
import com.project.shopapp.exception.ErrorCode;
import com.project.shopapp.mapper.OrderDetailMapper;
import com.project.shopapp.repository.OrderDetailRepository;
import com.project.shopapp.repository.OrderRepository;
import com.project.shopapp.repository.ProductRepository;
import com.project.shopapp.service.orderDetail.IOrderDetailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderDetailService implements IOrderDetailService {
    OrderDetailRepository orderDetailRepository;
    OrderDetailMapper orderDetailMapper;
    OrderRepository orderRepository;
    ProductRepository productRepository;

    @Override
    @Transactional
    public OrderDetailResponse createOrderDetail(OrderDetailRequest request) {
        Order order = orderRepository.findById(request.getOrderId()).orElseThrow(
                () -> new AppException(ErrorCode.ORDER_NOT_EXISTED, request.getOrderId()));
        Product product = productRepository.findById(request.getOrderId()).orElseThrow(
                () -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED, request.getProductId()));
        OrderDetail orderDetail = orderDetailMapper.toOrderDetail(request);
        orderDetail.setProduct(product);
        orderDetail.setOrder(order);
        return orderDetailMapper.toOrderDetailResponse(orderDetailRepository.save(orderDetail));
    }

    @Override
    public OrderDetailResponse getOrderDetail(Long id) {
        return orderDetailMapper.toOrderDetailResponse(orderDetailRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.ORDER_DETAIL_NOT_EXISTED, id)));
    }

    @Override
    @Transactional
    public OrderDetailResponse updateOrderDetail(Long id, OrderDetailRequest request) {
        OrderDetail existingOrderDetail = orderDetailRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.ORDER_DETAIL_NOT_EXISTED, id));
        Order existingOrder = orderRepository.findById(request.getOrderId()).orElseThrow(
                () -> new AppException(ErrorCode.ORDER_DETAIL_NOT_EXISTED, request.getOrderId()));
        Product existingProduct = productRepository.findById(request.getOrderId()).orElseThrow(
                () -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED, request.getProductId()));
        orderDetailMapper.updateOrder(existingOrderDetail, request);
        existingOrderDetail.setProduct(existingProduct);
        existingOrderDetail.setOrder(existingOrder);
        return orderDetailMapper.toOrderDetailResponse(orderDetailRepository.save(existingOrderDetail));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        orderDetailRepository.deleteById(id);
    }

    @Override
    public List<OrderDetailResponse> findByOrderId(Long orderId) {
        return orderDetailRepository.findByOrderId(orderId).stream()
                .map(orderDetailMapper::toOrderDetailResponse)
                .toList();
    }
}
