package com.lincee.controller;

import com.lincee.dto.CollectionDTO;
import com.lincee.entity.Collection;
import com.lincee.service.CollectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/collections")
@Tag(name = "Collection Management", description = "APIs for managing product collections")
public class CollectionController {

    @Autowired
    private CollectionService collectionService;

    // Public endpoints
    @GetMapping
    @Operation(summary = "Get all active collections", description = "Retrieve all active collections ordered by display order")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved collections")
    public ResponseEntity<List<CollectionDTO>> getActiveCollections() {
        return ResponseEntity.ok(collectionService.getActiveCollections());
    }

    @GetMapping("/featured")
    @Operation(summary = "Get featured collections", description = "Retrieve all featured and active collections")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved featured collections")
    public ResponseEntity<List<CollectionDTO>> getFeaturedCollections() {
        return ResponseEntity.ok(collectionService.getFeaturedCollections());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get collection by ID", description = "Retrieve a specific collection by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Collection found"),
        @ApiResponse(responseCode = "404", description = "Collection not found")
    })
    public ResponseEntity<CollectionDTO> getCollectionById(
            @Parameter(description = "Collection ID") @PathVariable Long id) {
        try {
            return ResponseEntity.ok(collectionService.getCollectionById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/slug/{slug}")
    @Operation(summary = "Get collection by slug", description = "Retrieve a specific collection by its slug")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Collection found"),
        @ApiResponse(responseCode = "404", description = "Collection not found")
    })
    public ResponseEntity<CollectionDTO> getCollectionBySlug(
            @Parameter(description = "Collection slug") @PathVariable String slug) {
        try {
            return ResponseEntity.ok(collectionService.getCollectionBySlug(slug));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Admin endpoints
    @GetMapping("/admin/all")
    @Operation(summary = "[ADMIN] Get all collections", description = "Retrieve all collections including inactive ones")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved collections")
    public ResponseEntity<List<CollectionDTO>> getAllCollections() {
        return ResponseEntity.ok(collectionService.getAllCollections());
    }

    @PostMapping("/admin")
    @Operation(summary = "[ADMIN] Create new collection", description = "Create a new product collection")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Collection created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid collection data")
    })
    public ResponseEntity<?> createCollection(@RequestBody Collection collection) {
        try {
            CollectionDTO createdCollection = collectionService.createCollection(collection);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCollection);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/admin/{id}")
    @Operation(summary = "[ADMIN] Update collection", description = "Update an existing collection")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Collection updated successfully"),
        @ApiResponse(responseCode = "404", description = "Collection not found"),
        @ApiResponse(responseCode = "400", description = "Invalid collection data")
    })
    public ResponseEntity<?> updateCollection(
            @Parameter(description = "Collection ID") @PathVariable Long id,
            @RequestBody Collection collectionDetails) {
        try {
            CollectionDTO updatedCollection = collectionService.updateCollection(id, collectionDetails);
            return ResponseEntity.ok(updatedCollection);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/admin/{id}")
    @Operation(summary = "[ADMIN] Delete collection", description = "Remove a collection from the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Collection deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Collection not found")
    })
    public ResponseEntity<?> deleteCollection(
            @Parameter(description = "Collection ID") @PathVariable Long id) {
        try {
            collectionService.deleteCollection(id);
            return ResponseEntity.ok(Map.of("message", "Collection deleted successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/admin/{collectionId}/products/{productId}")
    @Operation(summary = "[ADMIN] Add product to collection", description = "Add a single product to a collection")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product added successfully"),
        @ApiResponse(responseCode = "404", description = "Collection or Product not found")
    })
    public ResponseEntity<?> addProductToCollection(
            @Parameter(description = "Collection ID") @PathVariable Long collectionId,
            @Parameter(description = "Product ID") @PathVariable Long productId) {
        try {
            CollectionDTO updatedCollection = collectionService.addProductToCollection(collectionId, productId);
            return ResponseEntity.ok(updatedCollection);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/admin/{collectionId}/products/{productId}")
    @Operation(summary = "[ADMIN] Remove product from collection", description = "Remove a product from a collection")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product removed successfully"),
        @ApiResponse(responseCode = "404", description = "Collection or Product not found")
    })
    public ResponseEntity<?> removeProductFromCollection(
            @Parameter(description = "Collection ID") @PathVariable Long collectionId,
            @Parameter(description = "Product ID") @PathVariable Long productId) {
        try {
            CollectionDTO updatedCollection = collectionService.removeProductFromCollection(collectionId, productId);
            return ResponseEntity.ok(updatedCollection);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/admin/{collectionId}/products/batch")
    @Operation(summary = "[ADMIN] Add multiple products to collection", description = "Add multiple products to a collection at once")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Products added successfully"),
        @ApiResponse(responseCode = "404", description = "Collection or Products not found")
    })
    public ResponseEntity<?> addMultipleProductsToCollection(
            @Parameter(description = "Collection ID") @PathVariable Long collectionId,
            @RequestBody List<Long> productIds) {
        try {
            CollectionDTO updatedCollection = collectionService.addMultipleProductsToCollection(collectionId, productIds);
            return ResponseEntity.ok(updatedCollection);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }
}
