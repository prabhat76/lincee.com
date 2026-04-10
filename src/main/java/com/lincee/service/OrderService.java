package com.lincee.service;

import com.lincee.dto.OrderDTO;
import com.lincee.entity.Order;
import com.lincee.entity.Order.OrderStatus;
import com.lincee.entity.OrderItem;
import com.lincee.entity.Product;
import com.lincee.entity.User;
import com.lincee.dto.OrderItemDTO;
import com.lincee.repository.OrderRepository;
import com.lincee.repository.ProductRepository;
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
    private ProductRepository productRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    public OrderDTO createOrder(Long userId, OrderDTO orderDTO) {
        Optional<User> user = userRepository.findById(userId);
        if (!user.isPresent()) {
            throw new RuntimeException("User not found");
        }

        // Support both 'items' and 'orderItems' in payload
        if ((orderDTO.getOrderItems() == null || orderDTO.getOrderItems().isEmpty()) &&
            orderDTO instanceof java.util.Map) {
            // If deserialized as a Map (e.g., from Jackson), try to extract 'items'
            Object itemsObj = ((java.util.Map<?, ?>) orderDTO).get("items");
            if (itemsObj instanceof java.util.List) {
                @SuppressWarnings("unchecked")
                java.util.List<java.util.Map<String, Object>> itemsList = (java.util.List<java.util.Map<String, Object>>) itemsObj;
                java.util.List<OrderItemDTO> mappedItems = itemsList.stream().map(map -> {
                    OrderItemDTO dto = new OrderItemDTO();
                    if (map.get("productId") != null) dto.setProductId(Long.valueOf(map.get("productId").toString()));
                    if (map.get("quantity") != null) dto.setQuantity(Integer.valueOf(map.get("quantity").toString()));
                    if (map.get("unitPrice") != null) dto.setUnitPrice(new java.math.BigDecimal(map.get("unitPrice").toString()));
                    if (map.get("discountPrice") != null) dto.setDiscountPrice(new java.math.BigDecimal(map.get("discountPrice").toString()));
                    if (map.get("totalPrice") != null) dto.setTotalPrice(new java.math.BigDecimal(map.get("totalPrice").toString()));
                    if (map.get("size") != null) dto.setSize(map.get("size").toString());
                    if (map.get("color") != null) dto.setColor(map.get("color").toString());
                    return dto;
                }).collect(java.util.stream.Collectors.toList());
                orderDTO.setOrderItems(mappedItems);
            }
        }

        if (orderDTO.getOrderItems() == null || orderDTO.getOrderItems().isEmpty()) {
            throw new com.lincee.exception.ApiException(
                com.lincee.exception.ErrorCode.INVALID_ORDER,
                "Order must contain at least one item"
            );
        }

        Order order = new Order();
        order.setUser(user.get());
        order.setDiscountAmount(orderDTO.getDiscountAmount() != null ? orderDTO.getDiscountAmount() : BigDecimal.ZERO);
        order.setShippingCost(orderDTO.getShippingCost() != null ? orderDTO.getShippingCost() : BigDecimal.ZERO);
        order.setTaxAmount(orderDTO.getTaxAmount() != null ? orderDTO.getTaxAmount() : BigDecimal.ZERO);
        order.setOrderNumber(generateOrderNumber());
        order.setStatus(OrderStatus.PENDING);
        order.setNotes(orderDTO.getNotes());
        order.setEstimatedDeliveryDate(LocalDateTime.now().plusDays(7));
        
        // Process Order Items and Calculate Total
        BigDecimal calculatedTotal = BigDecimal.ZERO;
        
        for (OrderItemDTO itemDTO : orderDTO.getOrderItems()) {
            Product product = productRepository.findById(itemDTO.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + itemDTO.getProductId()));
            
            // Check Stock
            if (product.getStockQuantity() < itemDTO.getQuantity()) {
                throw new RuntimeException("Insufficient stock for product: " + product.getName());
            }
            
            // Deduct Stock
            product.setStockQuantity(product.getStockQuantity() - itemDTO.getQuantity());
            productRepository.save(product);
            
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemDTO.getQuantity());
            orderItem.setUnitPrice(product.getDiscountPrice() != null ? product.getDiscountPrice() : product.getPrice());
            orderItem.setTotalPrice(orderItem.getUnitPrice().multiply(new BigDecimal(itemDTO.getQuantity())));
            orderItem.setSize(itemDTO.getSize());
            orderItem.setColor(itemDTO.getColor());
            
            order.getOrderItems().add(orderItem);
            calculatedTotal = calculatedTotal.add(orderItem.getTotalPrice());
        }
        
        // Set final total (Items + Tax + Shipping - Discount)
        order.setTotalAmount(calculatedTotal.add(order.getTaxAmount()).add(order.getShippingCost()).subtract(order.getDiscountAmount()));

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

        // Map order items to DTOs
        if (order.getOrderItems() != null) {
            java.util.List<com.lincee.dto.OrderItemDTO> itemDTOs = order.getOrderItems().stream().map(item -> {
                com.lincee.dto.OrderItemDTO itemDTO = new com.lincee.dto.OrderItemDTO();
                itemDTO.setId(item.getId());
                itemDTO.setOrderId(order.getId());
                if (item.getProduct() != null) {
                    itemDTO.setProductId(item.getProduct().getId());
                    itemDTO.setProductName(item.getProduct().getName());
                }
                itemDTO.setQuantity(item.getQuantity());
                itemDTO.setUnitPrice(item.getUnitPrice());
                itemDTO.setDiscountPrice(item.getDiscountPrice());
                itemDTO.setTotalPrice(item.getTotalPrice());
                itemDTO.setSize(item.getSize());
                itemDTO.setColor(item.getColor());
                return itemDTO;
            }).collect(java.util.stream.Collectors.toList());
            dto.setOrderItems(itemDTOs);
        }
        return dto;
    }
}
