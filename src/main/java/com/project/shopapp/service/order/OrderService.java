package com.project.shopapp.service.order;

import com.project.shopapp.dto.request.OrderRequest;
import com.project.shopapp.dto.response.CartItem;
import com.project.shopapp.dto.response.OrderResponse;
import com.project.shopapp.entity.*;
import com.project.shopapp.enums.OrderStatus;
import com.project.shopapp.exception.AppException;
import com.project.shopapp.exception.ErrorCode;
import com.project.shopapp.mapper.OrderMapper;
import com.project.shopapp.repository.*;
import com.project.shopapp.service.email.IEmailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderService implements IOrderService {

    UserRepository userRepository;
    OrderRepository orderRepository;
    OrderMapper orderMapper;
    OrderDetailRepository orderDetailRepository;
    ProductRepository productRepository;
    IEmailService emailService;
    CouponRepository couponRepository;

    @Override
    @Transactional
    public OrderResponse createOrder(OrderRequest request) {
        User user = userRepository.findById(request.getUserId()).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED, request.getUserId())
        );
        Order order = orderMapper.toOrder(request);
        order.setUser(user);
        order.setStatus(OrderStatus.pending);
        Date shippingDate = request.getShippingDate() == null
                ? new Date(Instant.now().plus(5, ChronoUnit.DAYS).toEpochMilli()) : request.getShippingDate();
        if (shippingDate.before(new Date())) {
            throw new AppException(ErrorCode.ORDER_SHIPPING_DATE_INVALID);
        }
        order.setShippingDate(shippingDate);
        order.setActive(true);
        order.setTotalMoney(request.getTotalMoney());
        List<OrderDetail> orderDetails = new ArrayList<>();
        for (CartItem cartItem : request.getCardItems()) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            Long productId = cartItem.getProductId();
            int quantity = cartItem.getQuantity();
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED, productId));
            orderDetail.setProduct(product);
            orderDetail.setNumberOfProducts(quantity);
            orderDetail.setPrice(product.getPrice());
            orderDetails.add(orderDetail);
        }
        orderDetailRepository.saveAll(orderDetails);
//coupon
        String couponCode = request.getCouponCode();
        if (!couponCode.isEmpty()) {
            Coupon coupon = couponRepository.findByCode(couponCode)
                    .orElseThrow(() -> new IllegalArgumentException("Coupon not found"));

            if (!coupon.isActive()) {
                throw new IllegalArgumentException("Coupon is not active");
            }

            order.setCoupon(coupon);
        } else {
            order.setCoupon(null);
        }
        order = orderRepository.save(order);

        emailService.sendSimpleMessage(user.getEmail(), "Verify order", "abcdef");

        return orderMapper.toOrderResponse(orderRepository.save(order));
    }

    @Override
    public OrderResponse getOrder(Long id) {
        return orderMapper.toOrderResponse(orderRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.ORDER_NOT_EXISTED, id)));
//        return orderRepository.findById(id).orElseThrow(null);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public OrderResponse updateOrder(Long id, OrderRequest request) {
        Order order = orderRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.ORDER_NOT_EXISTED, id));
        User existingUser = userRepository.findById(request.getUserId()).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED));
        orderMapper.updateOrder(order, request);
        order.setUser(existingUser);
        return orderMapper.toOrderResponse(orderRepository.save(order));
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.ORDER_NOT_EXISTED, id));
        order.setActive(false);
        orderRepository.save(order);
    }

    @Override
    public List<OrderResponse> findByUserId(Long userId) {
        return orderRepository.findByUserId(userId).stream()
                .map(orderMapper::toOrderResponse)
                .toList();
    }

    @Override
    public Page<OrderResponse> getOrdersByKeyword(String keyword, int page, int limit) {
        PageRequest pageRequest = PageRequest.of(
                page, limit,
                //Sort.by("createdAt").descending()
                Sort.by("id").ascending()
        );
        return orderRepository.findByKeyword(keyword, pageRequest)
                .map(orderMapper::toOrderResponse);
    }
}
