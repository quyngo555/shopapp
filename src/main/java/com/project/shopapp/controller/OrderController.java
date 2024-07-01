package com.project.shopapp.controller;

import com.project.shopapp.dto.request.OrderRequest;
import com.project.shopapp.dto.response.ApiResponse;
import com.project.shopapp.service.order.IOrderService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/orders")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderController {
    IOrderService orderService;

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> createOrder(@Valid @RequestBody OrderRequest request){
        return ResponseEntity.ok().body(ApiResponse.builder()
                .data(orderService.createOrder(request))
                .build());
    }

    @GetMapping("/user/{user_id}")
    public ResponseEntity<?> getOrders(@PathVariable("user_id") Long userId){
        return ResponseEntity.ok().body(ApiResponse.builder()
                .data(orderService.findByUserId(userId))
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable("id") Long orderId){
        return ResponseEntity.ok().body(ApiResponse.builder()
                .data(orderService.getOrder(orderId))
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(
            @PathVariable long id,
            @Valid @RequestBody OrderRequest request
    ){
        return ResponseEntity.ok().body(ApiResponse.builder()
                .data(orderService.updateOrder(id, request))
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long id){
        orderService.deleteOrder(id);
        return ResponseEntity.ok().body(ApiResponse.builder()
                .message("delete order successfully!")
                .build());
    }

    @GetMapping("/get-orders-by-keyword")
    public ResponseEntity<?> getOrdersByKeyword(
            @RequestParam(defaultValue = "", required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ){
        return ResponseEntity.ok().body(ApiResponse.builder()
                .data(orderService.getOrdersByKeyword(keyword, page, limit))
                .build());
    }
}
