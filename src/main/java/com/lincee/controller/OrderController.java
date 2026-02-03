package com.lincee.controller;

import com.lincee.dto.OrderDTO;
import com.lincee.entity.Order.OrderStatus;
import com.lincee.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
@Tag(name = "Order Management", description = "APIs for managing orders")
public class OrderController {
    
    @Autowired
    private OrderService orderService;
    
    @PostMapping
    @Operation(summary = "Create new order", description = "Create a new order for a user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Order created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid order data"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<OrderDTO> createOrder(
            @Parameter(description = "User ID") @RequestParam Long userId,
            @RequestBody OrderDTO orderDTO) {
        OrderDTO createdOrder = orderService.createOrder(userId, orderDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get order by ID", description = "Retrieve a specific order by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Order found"),
        @ApiResponse(responseCode = "404", description = "Order not found")
    })
    public ResponseEntity<OrderDTO> getOrderById(
            @Parameter(description = "Order ID") @PathVariable Long id) {
        OrderDTO order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }
    
    @GetMapping("/number/{orderNumber}")
    @Operation(summary = "Get order by order number", description = "Retrieve a specific order by its order number")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Order found"),
        @ApiResponse(responseCode = "404", description = "Order not found")
    })
    public ResponseEntity<OrderDTO> getOrderByNumber(
            @Parameter(description = "Order Number") @PathVariable String orderNumber) {
        OrderDTO order = orderService.getOrderByNumber(orderNumber);
        return ResponseEntity.ok(order);
    }
    
    @GetMapping("/user/{userId}")
    @Operation(summary = "Get user orders", description = "Retrieve all orders for a specific user")
    @ApiResponse(responseCode = "200", description = "Orders retrieved successfully")
    public ResponseEntity<Page<OrderDTO>> getUserOrders(
            @Parameter(description = "User ID") @PathVariable Long userId,
            Pageable pageable) {
        Page<OrderDTO> orders = orderService.getUserOrders(userId, pageable);
        return ResponseEntity.ok(orders);
    }
    
    @GetMapping("/user/{userId}/list")
    @Operation(summary = "Get user orders list", description = "Retrieve all orders for a user as a list")
    @ApiResponse(responseCode = "200", description = "Orders retrieved successfully")
    public ResponseEntity<List<OrderDTO>> getUserOrdersList(
            @Parameter(description = "User ID") @PathVariable Long userId) {
        List<OrderDTO> orders = orderService.getUserOrderList(userId);
        return ResponseEntity.ok(orders);
    }
    
    @GetMapping("/status/{status}")
    @Operation(summary = "Get orders by status", description = "Retrieve orders filtered by status")
    @ApiResponse(responseCode = "200", description = "Orders retrieved successfully")
    public ResponseEntity<Page<OrderDTO>> getOrdersByStatus(
            @Parameter(description = "Order Status") @PathVariable OrderStatus status,
            Pageable pageable) {
        Page<OrderDTO> orders = orderService.getOrdersByStatus(status, pageable);
        return ResponseEntity.ok(orders);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update order", description = "Update order details")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Order updated successfully"),
        @ApiResponse(responseCode = "404", description = "Order not found")
    })
    public ResponseEntity<OrderDTO> updateOrder(
            @Parameter(description = "Order ID") @PathVariable Long id,
            @RequestBody OrderDTO orderDTO) {
        OrderDTO updatedOrder = orderService.updateOrder(id, orderDTO);
        return ResponseEntity.ok(updatedOrder);
    }
    
    @PatchMapping("/{id}/status")
    @Operation(summary = "Update order status", description = "Update the status of an order")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Order status updated successfully"),
        @ApiResponse(responseCode = "404", description = "Order not found")
    })
    public ResponseEntity<OrderDTO> updateOrderStatus(
            @Parameter(description = "Order ID") @PathVariable Long id,
            @Parameter(description = "New Status") @RequestParam OrderStatus status) {
        OrderDTO updatedOrder = orderService.updateOrderStatus(id, status);
        return ResponseEntity.ok(updatedOrder);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete order", description = "Delete a specific order")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Order deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Order not found")
    })
    public ResponseEntity<Void> deleteOrder(
            @Parameter(description = "Order ID") @PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/stats/count")
    @Operation(summary = "Get total order count", description = "Get the total number of orders")
    @ApiResponse(responseCode = "200", description = "Total count retrieved successfully")
    public ResponseEntity<Map<String, Long>> getTotalOrderCount() {
        Long totalCount = orderService.getTotalOrderCount();
        Map<String, Long> response = new HashMap<>();
        response.put("totalOrders", totalCount);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/stats/status/{status}/count")
    @Operation(summary = "Get order count by status", description = "Get the number of orders with a specific status")
    @ApiResponse(responseCode = "200", description = "Count retrieved successfully")
    public ResponseEntity<Map<String, Long>> getOrderCountByStatus(
            @Parameter(description = "Order Status") @PathVariable OrderStatus status) {
        Long count = orderService.getOrderCountByStatus(status);
        Map<String, Long> response = new HashMap<>();
        response.put(status.toString(), count);
        return ResponseEntity.ok(response);
    }
}
