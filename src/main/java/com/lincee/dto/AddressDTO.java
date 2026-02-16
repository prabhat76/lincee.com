package com.lincee.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import java.time.LocalDateTime;

public class AddressDTO {
    private Long id;
    private Long userId;
    @JsonAlias({"street"})
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String zipCode;
    private String country;
    private String phoneNumber;
    private Boolean isDefault;
    private Boolean isShippingAddress;
    private Boolean isBillingAddress;
    @JsonAlias({"type"})
    private String addressType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public AddressDTO() {}
    
    public AddressDTO(Long id, Long userId, String addressLine1, String city, String state, String zipCode, String country) {
        this.id = id;
        this.userId = userId;
        this.addressLine1 = addressLine1;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.country = country;
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
    
    public String getAddressLine1() {
        return addressLine1;
    }
    
    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }
    
    public String getAddressLine2() {
        return addressLine2;
    }
    
    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }
    
    public String getCity() {
        return city;
    }
    
    public void setCity(String city) {
        this.city = city;
    }
    
    public String getState() {
        return state;
    }
    
    public void setState(String state) {
        this.state = state;
    }
    
    public String getZipCode() {
        return zipCode;
    }
    
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
    
    public String getCountry() {
        return country;
    }
    
    public void setCountry(String country) {
        this.country = country;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public Boolean getIsDefault() {
        return isDefault;
    }
    
    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }
    
    public Boolean getIsShippingAddress() {
        return isShippingAddress;
    }
    
    public void setIsShippingAddress(Boolean isShippingAddress) {
        this.isShippingAddress = isShippingAddress;
    }
    
    public Boolean getIsBillingAddress() {
        return isBillingAddress;
    }
    
    public void setIsBillingAddress(Boolean isBillingAddress) {
        this.isBillingAddress = isBillingAddress;
    }
    
    public String getAddressType() {
        return addressType;
    }
    
    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
