package com.lincee.service;

import com.lincee.dto.PaymentDTO;
import com.lincee.entity.Order;
import com.lincee.entity.Payment;
import com.lincee.entity.Payment.PaymentMethod;
import com.lincee.entity.Payment.PaymentStatus;
import com.lincee.repository.OrderRepository;
import com.lincee.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class PaymentService {
    
    @Autowired
    private PaymentRepository paymentRepository;
    
    @Autowired
    private OrderRepository orderRepository;
    
    public PaymentDTO createPayment(Long orderId, PaymentDTO paymentDTO) {
        Optional<Order> order = orderRepository.findById(orderId);
        if (!order.isPresent()) {
            throw new RuntimeException("Order not found");
        }
        
        Payment payment = new Payment();
        payment.setOrder(order.get());
        payment.setPaymentMethod(PaymentMethod.valueOf(paymentDTO.getPaymentMethod()));
        payment.setAmount(paymentDTO.getAmount());
        payment.setStatus(PaymentStatus.PENDING);
        payment.setPaymentGateway(paymentDTO.getPaymentGateway());
        payment.setCardLastFour(paymentDTO.getCardLastFour());
        
        Payment savedPayment = paymentRepository.save(payment);
        return convertToDTO(savedPayment);
    }
    
    public PaymentDTO getPaymentById(Long paymentId) {
        Optional<Payment> payment = paymentRepository.findById(paymentId);
        return payment.map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
    }
    
    public PaymentDTO getPaymentByOrderId(Long orderId) {
        Optional<Payment> payment = paymentRepository.findByOrderId(orderId);
        return payment.map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Payment not found for this order"));
    }
    
    public PaymentDTO getPaymentByTransactionId(String transactionId) {
        Optional<Payment> payment = paymentRepository.findByTransactionId(transactionId);
        return payment.map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
    }
    
    public List<PaymentDTO> getPaymentsByStatus(PaymentStatus status) {
        return paymentRepository.findByStatus(status).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<PaymentDTO> getUserPayments(Long userId) {
        return paymentRepository.findByUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public PaymentDTO updatePaymentStatus(Long paymentId, PaymentStatus status) {
        Optional<Payment> payment = paymentRepository.findById(paymentId);
        if (!payment.isPresent()) {
            throw new RuntimeException("Payment not found");
        }
        
        Payment existingPayment = payment.get();
        existingPayment.setStatus(status);
        
        if (status == PaymentStatus.COMPLETED) {
            existingPayment.setPaidAt(LocalDateTime.now());
        }
        
        Payment updatedPayment = paymentRepository.save(existingPayment);
        return convertToDTO(updatedPayment);
    }
    
    public PaymentDTO completePayment(Long paymentId, String transactionId) {
        Optional<Payment> payment = paymentRepository.findById(paymentId);
        if (!payment.isPresent()) {
            throw new RuntimeException("Payment not found");
        }
        
        Payment existingPayment = payment.get();
        existingPayment.setStatus(PaymentStatus.COMPLETED);
        existingPayment.setTransactionId(transactionId);
        existingPayment.setPaidAt(LocalDateTime.now());
        
        Payment updatedPayment = paymentRepository.save(existingPayment);
        return convertToDTO(updatedPayment);
    }
    
    public PaymentDTO updatePayment(Long paymentId, PaymentDTO paymentDTO) {
        Optional<Payment> payment = paymentRepository.findById(paymentId);
        if (!payment.isPresent()) {
            throw new RuntimeException("Payment not found");
        }
        
        Payment existingPayment = payment.get();
        if (paymentDTO.getPaymentMethod() != null) {
            existingPayment.setPaymentMethod(PaymentMethod.valueOf(paymentDTO.getPaymentMethod()));
        }
        if (paymentDTO.getStatus() != null) {
            existingPayment.setStatus(PaymentStatus.valueOf(paymentDTO.getStatus()));
        }
        if (paymentDTO.getTransactionId() != null) {
            existingPayment.setTransactionId(paymentDTO.getTransactionId());
        }
        if (paymentDTO.getReferenceNumber() != null) {
            existingPayment.setReferenceNumber(paymentDTO.getReferenceNumber());
        }
        if (paymentDTO.getCardLastFour() != null) {
            existingPayment.setCardLastFour(paymentDTO.getCardLastFour());
        }
        
        Payment updatedPayment = paymentRepository.save(existingPayment);
        return convertToDTO(updatedPayment);
    }
    
    public void deletePayment(Long paymentId) {
        if (!paymentRepository.existsById(paymentId)) {
            throw new RuntimeException("Payment not found");
        }
        paymentRepository.deleteById(paymentId);
    }
    
    public Long getTotalPaymentCount() {
        return paymentRepository.count();
    }
    
    public Long getCompletedPaymentCount() {
        return paymentRepository.countByStatus(PaymentStatus.COMPLETED);
    }
    
    public Long getPendingPaymentCount() {
        return paymentRepository.countByStatus(PaymentStatus.PENDING);
    }
    
    private PaymentDTO convertToDTO(Payment payment) {
        PaymentDTO dto = new PaymentDTO();
        dto.setId(payment.getId());
        dto.setOrderId(payment.getOrder().getId());
        dto.setPaymentMethod(payment.getPaymentMethod().toString());
        dto.setStatus(payment.getStatus().toString());
        dto.setAmount(payment.getAmount());
        dto.setTransactionId(payment.getTransactionId());
        dto.setReferenceNumber(payment.getReferenceNumber());
        dto.setCardLastFour(payment.getCardLastFour());
        dto.setPaymentGateway(payment.getPaymentGateway());
        dto.setPaidAt(payment.getPaidAt());
        dto.setCreatedAt(payment.getCreatedAt());
        return dto;
    }
}
