package com.lincee.controller;

import com.lincee.service.CloudinaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/images")
@Tag(name = "Image Upload", description = "Upload and manage product images to Cloudinary")
public class ImageController {

    @Autowired
    private CloudinaryService cloudinaryService;

    @PostMapping("/upload")
    @Operation(summary = "Upload single product image", description = "Upload an image file to Cloudinary and get the URL")
    @ApiResponse(responseCode = "200", description = "Image uploaded successfully")
    public ResponseEntity<Map<String, Object>> uploadImage(
            @Parameter(description = "Image file to upload") @RequestParam("file") MultipartFile file,
            @Parameter(description = "Cloudinary folder path (e.g., products/hoodies)") @RequestParam(defaultValue = "products") String folder) {
        try {
            // Upload to Cloudinary
            String imageUrl = cloudinaryService.uploadImage(file, folder);
            
            // Return response
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("imageUrl", imageUrl);
            response.put("message", "Image uploaded successfully to Cloudinary");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("error", "Failed to upload image: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @PostMapping("/upload/multiple")
    @Operation(summary = "Upload multiple product images", description = "Upload multiple images to Cloudinary at once")
    @ApiResponse(responseCode = "200", description = "Images uploaded successfully")
    public ResponseEntity<Map<String, Object>> uploadMultipleImages(
            @Parameter(description = "Array of image files") @RequestParam("files") MultipartFile[] files,
            @Parameter(description = "Cloudinary folder path") @RequestParam(defaultValue = "products") String folder) {
        try {
            List<String> imageUrls = new ArrayList<>();
            List<String> failedFiles = new ArrayList<>();
            
            for (MultipartFile file : files) {
                try {
                    String imageUrl = cloudinaryService.uploadImage(file, folder);
                    imageUrls.add(imageUrl);
                } catch (Exception e) {
                    failedFiles.add(file.getOriginalFilename() + ": " + e.getMessage());
                }
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("uploadedCount", imageUrls.size());
            response.put("failedCount", failedFiles.size());
            response.put("imageUrls", imageUrls);
            if (!failedFiles.isEmpty()) {
                response.put("failures", failedFiles);
            }
            response.put("message", String.format("Uploaded %d of %d images successfully", 
                imageUrls.size(), files.length));
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("error", "Failed to upload images: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}