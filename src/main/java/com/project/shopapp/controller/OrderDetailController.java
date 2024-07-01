package com.project.shopapp.controller;

import com.project.shopapp.dto.request.OrderDetailRequest;
import com.project.shopapp.dto.response.ApiResponse;
import com.project.shopapp.service.orderDetail.IOrderDetailService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/order-details")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderDetailController {
    IOrderDetailService orderDetailService;
    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> createOrderDetail(@Valid @RequestBody OrderDetailRequest request){
        return ResponseEntity.ok().body(ApiResponse.builder()
                .data(orderDetailService.createOrderDetail(request))
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderDetail(@PathVariable("id") Long id){
        return ResponseEntity.ok().body(ApiResponse.builder()
                .data(orderDetailService.getOrderDetail(id))
                .build());
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<?> getOrderDetails(@PathVariable("orderId") Long orderId){
        return ResponseEntity.ok().body(ApiResponse.builder()
                .data(orderDetailService.findByOrderId(orderId))
                .build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> updateOrderDetail(
            @PathVariable("id") Long id,
            @Valid @RequestBody OrderDetailRequest request
    ){
        return ResponseEntity.ok().body(ApiResponse.builder()
                .data(orderDetailService.updateOrderDetail(id, request))
                .build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> deleteOrderDetail(@PathVariable("id") Long id){
        orderDetailService.deleteById(id);
        return ResponseEntity.ok().body(ApiResponse.builder()
                .message("Delete success")
                .build());
    }
}
