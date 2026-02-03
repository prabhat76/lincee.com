package com.lincee.controller;

import com.lincee.dto.PaymentDTO;
import com.lincee.entity.Payment.PaymentStatus;
import com.lincee.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/payments")
@Tag(name = "Payment Management", description = "APIs for managing payments")
public class PaymentController {
    
    @Autowired
    private PaymentService paymentService;
    
    @PostMapping
    @Operation(summary = "Create new payment", description = "Create a new payment for an order")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Payment created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid payment data"),
        @ApiResponse(responseCode = "404", description = "Order not found")
    })
    public ResponseEntity<PaymentDTO> createPayment(
            @Parameter(description = "Order ID") @RequestParam Long orderId,
            @RequestBody PaymentDTO paymentDTO) {
        PaymentDTO createdPayment = paymentService.createPayment(orderId, paymentDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPayment);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get payment by ID", description = "Retrieve a specific payment by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Payment found"),
        @ApiResponse(responseCode = "404", description = "Payment not found")
    })
    public ResponseEntity<PaymentDTO> getPaymentById(
            @Parameter(description = "Payment ID") @PathVariable Long id) {
        PaymentDTO payment = paymentService.getPaymentById(id);
        return ResponseEntity.ok(payment);
    }
    
    @GetMapping("/order/{orderId}")
    @Operation(summary = "Get payment by order ID", description = "Retrieve payment details for a specific order")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Payment found"),
        @ApiResponse(responseCode = "404", description = "Payment not found")
    })
    public ResponseEntity<PaymentDTO> getPaymentByOrderId(
            @Parameter(description = "Order ID") @PathVariable Long orderId) {
        PaymentDTO payment = paymentService.getPaymentByOrderId(orderId);
        return ResponseEntity.ok(payment);
    }
    
    @GetMapping("/transaction/{transactionId}")
    @Operation(summary = "Get payment by transaction ID", description = "Retrieve payment details by transaction ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Payment found"),
        @ApiResponse(responseCode = "404", description = "Payment not found")
    })
    public ResponseEntity<PaymentDTO> getPaymentByTransactionId(
            @Parameter(description = "Transaction ID") @PathVariable String transactionId) {
        PaymentDTO payment = paymentService.getPaymentByTransactionId(transactionId);
        return ResponseEntity.ok(payment);
    }
    
    @GetMapping("/status/{status}")
    @Operation(summary = "Get payments by status", description = "Retrieve all payments with a specific status")
    @ApiResponse(responseCode = "200", description = "Payments retrieved successfully")
    public ResponseEntity<List<PaymentDTO>> getPaymentsByStatus(
            @Parameter(description = "Payment Status") @PathVariable PaymentStatus status) {
        List<PaymentDTO> payments = paymentService.getPaymentsByStatus(status);
        return ResponseEntity.ok(payments);
    }
    
    @GetMapping("/user/{userId}")
    @Operation(summary = "Get user payments", description = "Retrieve all payments made by a specific user")
    @ApiResponse(responseCode = "200", description = "Payments retrieved successfully")
    public ResponseEntity<List<PaymentDTO>> getUserPayments(
            @Parameter(description = "User ID") @PathVariable Long userId) {
        List<PaymentDTO> payments = paymentService.getUserPayments(userId);
        return ResponseEntity.ok(payments);
    }
    
    @PatchMapping("/{id}/status")
    @Operation(summary = "Update payment status", description = "Update the status of a payment")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Payment status updated successfully"),
        @ApiResponse(responseCode = "404", description = "Payment not found")
    })
    public ResponseEntity<PaymentDTO> updatePaymentStatus(
            @Parameter(description = "Payment ID") @PathVariable Long id,
            @Parameter(description = "New Status") @RequestParam PaymentStatus status) {
        PaymentDTO updatedPayment = paymentService.updatePaymentStatus(id, status);
        return ResponseEntity.ok(updatedPayment);
    }
    
    @PatchMapping("/{id}/complete")
    @Operation(summary = "Complete payment", description = "Mark a payment as completed")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Payment completed successfully"),
        @ApiResponse(responseCode = "404", description = "Payment not found")
    })
    public ResponseEntity<PaymentDTO> completePayment(
            @Parameter(description = "Payment ID") @PathVariable Long id,
            @Parameter(description = "Transaction ID") @RequestParam String transactionId) {
        PaymentDTO completedPayment = paymentService.completePayment(id, transactionId);
        return ResponseEntity.ok(completedPayment);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update payment", description = "Update payment details")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Payment updated successfully"),
        @ApiResponse(responseCode = "404", description = "Payment not found")
    })
    public ResponseEntity<PaymentDTO> updatePayment(
            @Parameter(description = "Payment ID") @PathVariable Long id,
            @RequestBody PaymentDTO paymentDTO) {
        PaymentDTO updatedPayment = paymentService.updatePayment(id, paymentDTO);
        return ResponseEntity.ok(updatedPayment);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete payment", description = "Delete a specific payment record")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Payment deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Payment not found")
    })
    public ResponseEntity<Void> deletePayment(
            @Parameter(description = "Payment ID") @PathVariable Long id) {
        paymentService.deletePayment(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/stats/count")
    @Operation(summary = "Get payment statistics", description = "Get payment count statistics")
    @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully")
    public ResponseEntity<Map<String, Long>> getPaymentStatistics() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("totalPayments", paymentService.getTotalPaymentCount());
        stats.put("completedPayments", paymentService.getCompletedPaymentCount());
        stats.put("pendingPayments", paymentService.getPendingPaymentCount());
        return ResponseEntity.ok(stats);
    }
}
