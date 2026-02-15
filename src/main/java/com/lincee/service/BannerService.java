package com.lincee.service;

import com.lincee.dto.BannerDTO;
import com.lincee.entity.Banner;
import com.lincee.repository.BannerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BannerService {
    
    @Autowired
    private BannerRepository bannerRepository;
    
    /**
     * Get all active banners within date range
     */
    public List<BannerDTO> getActiveBanners() {
        return bannerRepository.findActiveBannersWithinDateRange(LocalDateTime.now())
            .stream()
            .map(BannerDTO::new)
            .collect(Collectors.toList());
    }
    
    /**
     * Get active banners by position within date range
     */
    public List<BannerDTO> getActiveBannersByPosition(String position) {
        return bannerRepository.findActiveBannersByPositionWithinDateRange(position, LocalDateTime.now())
            .stream()
            .map(BannerDTO::new)
            .collect(Collectors.toList());
    }
    
    /**
     * Get all banners (admin only)
     */
    public List<BannerDTO> getAllBanners() {
        return bannerRepository.findAllByOrderByDisplayOrderAsc()
            .stream()
            .map(BannerDTO::new)
            .collect(Collectors.toList());
    }
    
    /**
     * Get banner by ID
     */
    public BannerDTO getBannerById(Long id) {
        Banner banner = bannerRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Banner not found with id: " + id));
        return new BannerDTO(banner);
    }
    
    /**
     * Create new banner
     */
    @Transactional
    public BannerDTO createBanner(BannerDTO bannerDTO) {
        // Validate position
        validatePosition(bannerDTO.getPosition());
        
        // Validate date range
        validateDateRange(bannerDTO.getStartDate(), bannerDTO.getEndDate());
        
        // Set display order if not provided
        if (bannerDTO.getDisplayOrder() == null) {
            Integer maxOrder = bannerRepository.findMaxDisplayOrder();
            bannerDTO.setDisplayOrder(maxOrder + 1);
        }
        
        Banner banner = bannerDTO.toEntity();
        Banner savedBanner = bannerRepository.save(banner);
        return new BannerDTO(savedBanner);
    }
    
    /**
     * Update banner
     */
    @Transactional
    public BannerDTO updateBanner(Long id, BannerDTO bannerDTO) {
        Banner existingBanner = bannerRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Banner not found with id: " + id));
        
        // Validate position
        validatePosition(bannerDTO.getPosition());
        
        // Validate date range
        validateDateRange(bannerDTO.getStartDate(), bannerDTO.getEndDate());
        
        // Update fields
        existingBanner.setTitle(bannerDTO.getTitle());
        existingBanner.setSubtitle(bannerDTO.getSubtitle());
        existingBanner.setImageUrl(bannerDTO.getImageUrl());
        existingBanner.setPosition(bannerDTO.getPosition());
        existingBanner.setLink(bannerDTO.getLink());
        existingBanner.setButtonText(bannerDTO.getButtonText());
        existingBanner.setIsActive(bannerDTO.getIsActive());
        existingBanner.setDisplayOrder(bannerDTO.getDisplayOrder());
        existingBanner.setStartDate(bannerDTO.getStartDate());
        existingBanner.setEndDate(bannerDTO.getEndDate());
        
        Banner updatedBanner = bannerRepository.save(existingBanner);
        return new BannerDTO(updatedBanner);
    }
    
    /**
     * Delete banner
     */
    @Transactional
    public void deleteBanner(Long id) {
        if (!bannerRepository.existsById(id)) {
            throw new RuntimeException("Banner not found with id: " + id);
        }
        bannerRepository.deleteById(id);
    }
    
    /**
     * Toggle banner status
     */
    @Transactional
    public BannerDTO toggleBannerStatus(Long id) {
        Banner banner = bannerRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Banner not found with id: " + id));
        
        banner.setIsActive(!banner.getIsActive());
        Banner updatedBanner = bannerRepository.save(banner);
        return new BannerDTO(updatedBanner);
    }
    
    /**
     * Reorder banners
     */
    @Transactional
    public void reorderBanners(List<Long> bannerIds) {
        for (int i = 0; i < bannerIds.size(); i++) {
            Long bannerId = bannerIds.get(i);
            Banner banner = bannerRepository.findById(bannerId)
                .orElseThrow(() -> new RuntimeException("Banner not found with id: " + bannerId));
            banner.setDisplayOrder(i);
            bannerRepository.save(banner);
        }
    }
    
    /**
     * Validate position
     */
    private void validatePosition(String position) {
        if (position == null || position.trim().isEmpty()) {
            throw new IllegalArgumentException("Position is required");
        }
        
        String normalizedPosition = position.toLowerCase();
        if (!normalizedPosition.equals("hero") && 
            !normalizedPosition.equals("secondary") && 
            !normalizedPosition.equals("promotional")) {
            throw new IllegalArgumentException("Position must be one of: hero, secondary, promotional");
        }
    }
    
    /**
     * Validate date range
     */
    private void validateDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate != null && endDate != null && endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date must be after start date");
        }
    }
}
