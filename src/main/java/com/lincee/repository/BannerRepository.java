package com.lincee.repository;

import com.lincee.entity.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BannerRepository extends JpaRepository<Banner, Long> {
    
    /**
     * Find all active banners
     */
    List<Banner> findByIsActiveTrueOrderByDisplayOrderAsc();
    
    /**
     * Find active banners by position
     */
    List<Banner> findByPositionAndIsActiveTrueOrderByDisplayOrderAsc(String position);
    
    /**
     * Find active banners within date range
     */
    @Query("SELECT b FROM Banner b WHERE b.isActive = true " +
           "AND (b.startDate IS NULL OR b.startDate <= :now) " +
           "AND (b.endDate IS NULL OR b.endDate >= :now) " +
           "ORDER BY b.displayOrder ASC")
    List<Banner> findActiveBannersWithinDateRange(LocalDateTime now);
    
    /**
     * Find active banners by position within date range
     */
    @Query("SELECT b FROM Banner b WHERE b.position = :position " +
           "AND b.isActive = true " +
           "AND (b.startDate IS NULL OR b.startDate <= :now) " +
           "AND (b.endDate IS NULL OR b.endDate >= :now) " +
           "ORDER BY b.displayOrder ASC")
    List<Banner> findActiveBannersByPositionWithinDateRange(String position, LocalDateTime now);
    
    /**
     * Find all banners ordered by display order
     */
    List<Banner> findAllByOrderByDisplayOrderAsc();
    
    /**
     * Get max display order
     */
    @Query("SELECT COALESCE(MAX(b.displayOrder), -1) FROM Banner b")
    Integer findMaxDisplayOrder();
}
