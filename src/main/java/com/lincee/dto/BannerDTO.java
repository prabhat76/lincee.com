package com.lincee.dto;

import com.lincee.entity.Banner;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public class BannerDTO {
    
    private Long id;
    
    @NotBlank(message = "Title is required")
    private String title;
    
    private String subtitle;
    
    @NotBlank(message = "Image URL is required")
    private String imageUrl;
    
    @NotBlank(message = "Position is required")
    private String position;
    
    private String link;
    
    private String buttonText;
    
    private Boolean isActive = true;
    
    private Integer displayOrder = 0;
    
    private LocalDateTime startDate;
    
    private LocalDateTime endDate;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    // Constructors
    public BannerDTO() {}
    
    public BannerDTO(Banner banner) {
        this.id = banner.getId();
        this.title = banner.getTitle();
        this.subtitle = banner.getSubtitle();
        this.imageUrl = banner.getImageUrl();
        this.position = banner.getPosition();
        this.link = banner.getLink();
        this.buttonText = banner.getButtonText();
        this.isActive = banner.getIsActive();
        this.displayOrder = banner.getDisplayOrder();
        this.startDate = banner.getStartDate();
        this.endDate = banner.getEndDate();
        this.createdAt = banner.getCreatedAt();
        this.updatedAt = banner.getUpdatedAt();
    }
    
    // Convert DTO to Entity
    public Banner toEntity() {
        Banner banner = new Banner();
        banner.setId(this.id);
        banner.setTitle(this.title);
        banner.setSubtitle(this.subtitle);
        banner.setImageUrl(this.imageUrl);
        banner.setPosition(this.position);
        banner.setLink(this.link);
        banner.setButtonText(this.buttonText);
        banner.setIsActive(this.isActive != null ? this.isActive : true);
        banner.setDisplayOrder(this.displayOrder != null ? this.displayOrder : 0);
        banner.setStartDate(this.startDate);
        banner.setEndDate(this.endDate);
        return banner;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getSubtitle() {
        return subtitle;
    }
    
    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    public String getPosition() {
        return position;
    }
    
    public void setPosition(String position) {
        this.position = position;
    }
    
    public String getLink() {
        return link;
    }
    
    public void setLink(String link) {
        this.link = link;
    }
    
    public String getButtonText() {
        return buttonText;
    }
    
    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }
    
    public Boolean getIsActive() {
        return isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    
    public Integer getDisplayOrder() {
        return displayOrder;
    }
    
    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }
    
    public LocalDateTime getStartDate() {
        return startDate;
    }
    
    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }
    
    public LocalDateTime getEndDate() {
        return endDate;
    }
    
    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
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
