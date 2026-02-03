package com.lincee.controller;

import com.lincee.entity.Order.OrderStatus;
import com.lincee.entity.Payment.PaymentStatus;
import com.lincee.service.OrderService;
import com.lincee.service.PaymentService;
import com.lincee.repository.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/dashboard")
@Tag(name = "Dashboard Analytics", description = "APIs for admin dashboard analytics and statistics")
public class DashboardController {
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private PaymentService paymentService;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private CartRepository cartRepository;
    
    @Autowired
    private ReviewRepository reviewRepository;
    
    @GetMapping("/overview")
    @Operation(summary = "Get dashboard overview", description = "Get key metrics overview for the dashboard")
    @ApiResponse(responseCode = "200", description = "Overview retrieved successfully")
    public ResponseEntity<Map<String, Object>> getDashboardOverview() {
        Map<String, Object> overview = new HashMap<>();
        
        // Order Statistics
        overview.put("totalOrders", orderService.getTotalOrderCount());
        overview.put("pendingOrders", orderService.getOrderCountByStatus(OrderStatus.PENDING));
        overview.put("confirmedOrders", orderService.getOrderCountByStatus(OrderStatus.CONFIRMED));
        overview.put("processingOrders", orderService.getOrderCountByStatus(OrderStatus.PROCESSING));
        overview.put("shippedOrders", orderService.getOrderCountByStatus(OrderStatus.SHIPPED));
        overview.put("deliveredOrders", orderService.getOrderCountByStatus(OrderStatus.DELIVERED));
        overview.put("cancelledOrders", orderService.getOrderCountByStatus(OrderStatus.CANCELLED));
        
        // Payment Statistics
        overview.put("totalPayments", paymentService.getTotalPaymentCount());
        overview.put("completedPayments", paymentService.getCompletedPaymentCount());
        overview.put("pendingPayments", paymentService.getPendingPaymentCount());
        
        // Product Statistics
        overview.put("totalProducts", productRepository.count());
        overview.put("activeProducts", productRepository.countByActiveTrue());
        
        // User Statistics
        overview.put("totalUsers", userRepository.count());
        
        // Cart Statistics
        overview.put("totalCarts", cartRepository.count());
        
        // Review Statistics
        overview.put("totalReviews", reviewRepository.count());
        
        return ResponseEntity.ok(overview);
    }
    
    @GetMapping("/orders/statistics")
    @Operation(summary = "Get order statistics", description = "Get detailed order statistics and breakdown")
    @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully")
    public ResponseEntity<Map<String, Object>> getOrderStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("totalOrders", orderService.getTotalOrderCount());
        stats.put("pending", orderService.getOrderCountByStatus(OrderStatus.PENDING));
        stats.put("confirmed", orderService.getOrderCountByStatus(OrderStatus.CONFIRMED));
        stats.put("processing", orderService.getOrderCountByStatus(OrderStatus.PROCESSING));
        stats.put("shipped", orderService.getOrderCountByStatus(OrderStatus.SHIPPED));
        stats.put("delivered", orderService.getOrderCountByStatus(OrderStatus.DELIVERED));
        stats.put("cancelled", orderService.getOrderCountByStatus(OrderStatus.CANCELLED));
        stats.put("returned", orderService.getOrderCountByStatus(OrderStatus.RETURNED));
        
        return ResponseEntity.ok(stats);
    }
    
    @GetMapping("/payments/statistics")
    @Operation(summary = "Get payment statistics", description = "Get detailed payment statistics")
    @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully")
    public ResponseEntity<Map<String, Object>> getPaymentStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("totalPayments", paymentService.getTotalPaymentCount());
        stats.put("completed", paymentService.getCompletedPaymentCount());
        stats.put("pending", paymentService.getPendingPaymentCount());
        
        return ResponseEntity.ok(stats);
    }
    
    @GetMapping("/products/statistics")
    @Operation(summary = "Get product statistics", description = "Get product catalog statistics")
    @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully")
    public ResponseEntity<Map<String, Object>> getProductStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("totalProducts", productRepository.count());
        stats.put("activeProducts", productRepository.countByActiveTrue());
        stats.put("inactiveProducts", productRepository.count() - productRepository.countByActiveTrue());
        
        return ResponseEntity.ok(stats);
    }
    
    @GetMapping("/users/statistics")
    @Operation(summary = "Get user statistics", description = "Get user management statistics")
    @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully")
    public ResponseEntity<Map<String, Object>> getUserStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("totalUsers", userRepository.count());
        
        return ResponseEntity.ok(stats);
    }
    
    @GetMapping("/cart/statistics")
    @Operation(summary = "Get cart statistics", description = "Get shopping cart statistics")
    @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully")
    public ResponseEntity<Map<String, Object>> getCartStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("totalCarts", cartRepository.count());
        
        return ResponseEntity.ok(stats);
    }
    
    @GetMapping("/reviews/statistics")
    @Operation(summary = "Get review statistics", description = "Get review and rating statistics")
    @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully")
    public ResponseEntity<Map<String, Object>> getReviewStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("totalReviews", reviewRepository.count());
        
        return ResponseEntity.ok(stats);
    }
    
    @GetMapping("/summary")
    @Operation(summary = "Get dashboard summary", description = "Get a quick summary of all key metrics")
    @ApiResponse(responseCode = "200", description = "Summary retrieved successfully")
    public ResponseEntity<Map<String, Object>> getDashboardSummary() {
        Map<String, Object> summary = new HashMap<>();
        
        // Key Metrics
        summary.put("orders", new HashMap<String, Object>() {{
            put("total", orderService.getTotalOrderCount());
            put("pending", orderService.getOrderCountByStatus(OrderStatus.PENDING));
            put("delivered", orderService.getOrderCountByStatus(OrderStatus.DELIVERED));
        }});
        
        summary.put("payments", new HashMap<String, Object>() {{
            put("total", paymentService.getTotalPaymentCount());
            put("completed", paymentService.getCompletedPaymentCount());
        }});
        
        summary.put("products", new HashMap<String, Object>() {{
            put("total", productRepository.count());
            put("active", productRepository.countByActiveTrue());
        }});
        
        summary.put("users", new HashMap<String, Object>() {{
            put("total", userRepository.count());
        }});
        
        summary.put("reviews", new HashMap<String, Object>() {{
            put("total", reviewRepository.count());
        }});
        
        return ResponseEntity.ok(summary);
    }
    
    @GetMapping("/health")
    @Operation(summary = "Check dashboard health", description = "Check if all dashboard systems are operational")
    @ApiResponse(responseCode = "200", description = "Dashboard is healthy")
    public ResponseEntity<Map<String, String>> getDashboardHealth() {
        Map<String, String> health = new HashMap<>();
        health.put("status", "OPERATIONAL");
        health.put("message", "Dashboard is running properly");
        return ResponseEntity.ok(health);
    }
}
