package com.lincee.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class CartDTO {
    private Long id;
    private Long userId;
    private List<CartItemDTO> items;
    private BigDecimal totalPrice;
    private Integer itemCount;
    private LocalDateTime updatedAt;
    
    public CartDTO() {}
    
    public CartDTO(Long id, Long userId, BigDecimal totalPrice, Integer itemCount) {
        this.id = id;
        this.userId = userId;
        this.totalPrice = totalPrice;
        this.itemCount = itemCount;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public List<CartItemDTO> getItems() {
        return items;
    }
    
    public void setItems(List<CartItemDTO> items) {
        this.items = items;
    }
    
    public BigDecimal getTotalPrice() {
        return totalPrice;
    }
    
    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
    
    public Integer getItemCount() {
        return itemCount;
    }
    
    public void setItemCount(Integer itemCount) {
        this.itemCount = itemCount;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
