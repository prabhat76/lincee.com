package com.lincee.service;

import com.lincee.dto.OrderDTO;
import com.lincee.entity.Order;
import com.lincee.entity.Order.OrderStatus;
import com.lincee.entity.OrderItem;
import com.lincee.entity.User;
import com.lincee.repository.OrderRepository;
import com.lincee.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    public OrderDTO createOrder(Long userId, OrderDTO orderDTO) {
        Optional<User> user = userRepository.findById(userId);
        if (!user.isPresent()) {
            throw new RuntimeException("User not found");
        }
        
        Order order = new Order();
        order.setUser(user.get());
        order.setTotalAmount(orderDTO.getTotalAmount());
        order.setDiscountAmount(orderDTO.getDiscountAmount() != null ? orderDTO.getDiscountAmount() : BigDecimal.ZERO);
        order.setShippingCost(orderDTO.getShippingCost() != null ? orderDTO.getShippingCost() : BigDecimal.ZERO);
        order.setTaxAmount(orderDTO.getTaxAmount() != null ? orderDTO.getTaxAmount() : BigDecimal.ZERO);
        order.setOrderNumber(generateOrderNumber());
        order.setStatus(OrderStatus.PENDING);
        order.setNotes(orderDTO.getNotes());
        order.setEstimatedDeliveryDate(LocalDateTime.now().plusDays(7));
        
        Order savedOrder = orderRepository.save(order);
        return convertToDTO(savedOrder);
    }
    
    public OrderDTO getOrderById(Long orderId) {
        Optional<Order> order = orderRepository.findById(orderId);
        return order.map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }
    
    public OrderDTO getOrderByNumber(String orderNumber) {
        Optional<Order> order = orderRepository.findByOrderNumber(orderNumber);
        return order.map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }
    
    public Page<OrderDTO> getUserOrders(Long userId, Pageable pageable) {
        return orderRepository.findByUserId(userId, pageable).map(this::convertToDTO);
    }
    
    public List<OrderDTO> getUserOrderList(Long userId) {
        return orderRepository.findByUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public Page<OrderDTO> getOrdersByStatus(OrderStatus status, Pageable pageable) {
        return orderRepository.findByStatus(status, pageable).map(this::convertToDTO);
    }
    
    public OrderDTO updateOrderStatus(Long orderId, OrderStatus status) {
        Optional<Order> order = orderRepository.findById(orderId);
        if (!order.isPresent()) {
            throw new RuntimeException("Order not found");
        }
        
        Order existingOrder = order.get();
        existingOrder.setStatus(status);
        Order updatedOrder = orderRepository.save(existingOrder);
        return convertToDTO(updatedOrder);
    }
    
    public OrderDTO updateOrder(Long orderId, OrderDTO orderDTO) {
        Optional<Order> order = orderRepository.findById(orderId);
        if (!order.isPresent()) {
            throw new RuntimeException("Order not found");
        }
        
        Order existingOrder = order.get();
        if (orderDTO.getTotalAmount() != null) {
            existingOrder.setTotalAmount(orderDTO.getTotalAmount());
        }
        if (orderDTO.getDiscountAmount() != null) {
            existingOrder.setDiscountAmount(orderDTO.getDiscountAmount());
        }
        if (orderDTO.getShippingCost() != null) {
            existingOrder.setShippingCost(orderDTO.getShippingCost());
        }
        if (orderDTO.getTaxAmount() != null) {
            existingOrder.setTaxAmount(orderDTO.getTaxAmount());
        }
        if (orderDTO.getNotes() != null) {
            existingOrder.setNotes(orderDTO.getNotes());
        }
        if (orderDTO.getTrackingNumber() != null) {
            existingOrder.setTrackingNumber(orderDTO.getTrackingNumber());
        }
        if (orderDTO.getStatus() != null) {
            existingOrder.setStatus(OrderStatus.valueOf(orderDTO.getStatus()));
        }
        
        Order updatedOrder = orderRepository.save(existingOrder);
        return convertToDTO(updatedOrder);
    }
    
    public void deleteOrder(Long orderId) {
        if (!orderRepository.existsById(orderId)) {
            throw new RuntimeException("Order not found");
        }
        orderRepository.deleteById(orderId);
    }
    
    public List<OrderDTO> getOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.findOrdersByDateRange(startDate, endDate).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public Long getTotalOrderCount() {
        return orderRepository.count();
    }
    
    public Long getOrderCountByStatus(OrderStatus status) {
        return orderRepository.countByStatus(status);
    }
    
    public Long getUserOrderCount(Long userId) {
        return orderRepository.countByUserId(userId);
    }
    
    private String generateOrderNumber() {
        return "ORD-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
    
    private OrderDTO convertToDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setUserId(order.getUser().getId());
        dto.setOrderNumber(order.getOrderNumber());
        dto.setStatus(order.getStatus().toString());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setDiscountAmount(order.getDiscountAmount());
        dto.setShippingCost(order.getShippingCost());
        dto.setTaxAmount(order.getTaxAmount());
        dto.setNotes(order.getNotes());
        dto.setTrackingNumber(order.getTrackingNumber());
        dto.setEstimatedDeliveryDate(order.getEstimatedDeliveryDate());
        dto.setDeliveryDate(order.getDeliveryDate());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setUpdatedAt(order.getUpdatedAt());
        return dto;
    }
}
