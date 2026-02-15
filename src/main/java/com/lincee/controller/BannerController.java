package com.lincee.controller;

import com.lincee.dto.BannerDTO;
import com.lincee.service.BannerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Banner Management", description = "Manage hero banners, secondary banners, and promotional content")
public class BannerController {
    
    @Autowired
    private BannerService bannerService;
    
    // ==================== PUBLIC ENDPOINTS ====================
    
    @GetMapping("/banners/active")
    @Operation(summary = "Get all active banners", description = "Returns all active banners within valid date range")
    @ApiResponse(responseCode = "200", description = "Active banners retrieved successfully")
    public ResponseEntity<List<BannerDTO>> getActiveBanners() {
        try {
            List<BannerDTO> banners = bannerService.getActiveBanners();
            return ResponseEntity.ok(banners);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/banners/position/{position}")
    @Operation(summary = "Get banners by position", description = "Returns active banners for specific position (hero, secondary, promotional)")
    @ApiResponse(responseCode = "200", description = "Banners retrieved successfully")
    public ResponseEntity<List<BannerDTO>> getBannersByPosition(@PathVariable String position) {
        try {
            List<BannerDTO> banners = bannerService.getActiveBannersByPosition(position);
            return ResponseEntity.ok(banners);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // ==================== ADMIN ENDPOINTS ====================
    
    @GetMapping("/banners")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all banners (Admin)", description = "Returns all banners including inactive ones")
    @ApiResponse(responseCode = "200", description = "All banners retrieved successfully")
    public ResponseEntity<List<BannerDTO>> getAllBanners() {
        try {
            List<BannerDTO> banners = bannerService.getAllBanners();
            return ResponseEntity.ok(banners);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PostMapping("/admin/banners")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create banner (Admin)", description = "Create a new banner")
    @ApiResponse(responseCode = "201", description = "Banner created successfully")
    public ResponseEntity<?> createBanner(@Valid @RequestBody BannerDTO bannerDTO) {
        try {
            BannerDTO createdBanner = bannerService.createBanner(bannerDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdBanner);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to create banner: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    @PutMapping("/admin/banners/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update banner (Admin)", description = "Update an existing banner")
    @ApiResponse(responseCode = "200", description = "Banner updated successfully")
    public ResponseEntity<?> updateBanner(@PathVariable Long id, @Valid @RequestBody BannerDTO bannerDTO) {
        try {
            BannerDTO updatedBanner = bannerService.updateBanner(id, bannerDTO);
            return ResponseEntity.ok(updatedBanner);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to update banner: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    @DeleteMapping("/admin/banners/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete banner (Admin)", description = "Delete a banner by ID")
    @ApiResponse(responseCode = "204", description = "Banner deleted successfully")
    public ResponseEntity<?> deleteBanner(@PathVariable Long id) {
        try {
            bannerService.deleteBanner(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to delete banner: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    @PatchMapping("/admin/banners/{id}/toggle")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Toggle banner status (Admin)", description = "Toggle banner active/inactive status")
    @ApiResponse(responseCode = "200", description = "Banner status toggled successfully")
    public ResponseEntity<?> toggleBannerStatus(@PathVariable Long id) {
        try {
            BannerDTO updatedBanner = bannerService.toggleBannerStatus(id);
            return ResponseEntity.ok(updatedBanner);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to toggle banner status: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    @PostMapping("/admin/banners/reorder")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Reorder banners (Admin)", description = "Update display order of multiple banners")
    @ApiResponse(responseCode = "200", description = "Banners reordered successfully")
    public ResponseEntity<?> reorderBanners(@RequestBody List<Long> bannerIds) {
        try {
            bannerService.reorderBanners(bannerIds);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Banners reordered successfully");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to reorder banners: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}
