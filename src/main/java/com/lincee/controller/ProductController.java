package com.lincee.controller;

import com.lincee.entity.Product;
import com.lincee.repository.ProductRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "*")
@Tag(name = "Product Management", description = "APIs for managing streetwear products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    @Operation(summary = "Get all products", description = "Retrieve all products or filter by active status")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved products")
    public ResponseEntity<List<Product>> getAllProducts(
            @Parameter(description = "Filter by active status") @RequestParam(required = false) Boolean active) {
        if (active != null && active) {
            return ResponseEntity.ok(productRepository.findByActiveTrue());
        }
        return ResponseEntity.ok(productRepository.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID", description = "Retrieve a specific product by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product found"),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<Product> getProductById(
            @Parameter(description = "Product ID") @PathVariable Long id) {
        Optional<Product> product = productRepository.findById(id);
        return product.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create new product", description = "Add a new streetwear product to the catalog")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid product data")
    })
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product savedProduct = productRepository.save(product);
        return ResponseEntity.ok(savedProduct);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update product", description = "Update an existing product")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product updated successfully"),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<Product> updateProduct(
            @Parameter(description = "Product ID") @PathVariable Long id,
            @RequestBody Product productDetails) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            product.setName(productDetails.getName());
            product.setDescription(productDetails.getDescription());
            product.setPrice(productDetails.getPrice());
            product.setDiscountPrice(productDetails.getDiscountPrice());
            product.setCategory(productDetails.getCategory());
            product.setSubCategory(productDetails.getSubCategory());
            product.setBrand(productDetails.getBrand());
            product.setStockQuantity(productDetails.getStockQuantity());
            product.setAvailableSizes(productDetails.getAvailableSizes());
            product.setAvailableColors(productDetails.getAvailableColors());
            product.setImageUrls(productDetails.getImageUrls());
            product.setActive(productDetails.getActive());
            product.setFeatured(productDetails.getFeatured());
            product.setWeightGrams(productDetails.getWeightGrams());
            product.setTags(productDetails.getTags());
            return ResponseEntity.ok(productRepository.save(product));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete product", description = "Remove a product from the catalog")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<Void> deleteProduct(
            @Parameter(description = "Product ID") @PathVariable Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/category/{category}")
    @Operation(summary = "Get products by category", description = "Retrieve products filtered by category")
    @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    public ResponseEntity<List<Product>> getProductsByCategory(
            @Parameter(description = "Product category") @PathVariable String category) {
        return ResponseEntity.ok(productRepository.findByCategory(category));
    }

    @GetMapping("/brand/{brand}")
    @Operation(summary = "Get products by brand", description = "Retrieve products filtered by brand")
    @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    public ResponseEntity<List<Product>> getProductsByBrand(
            @Parameter(description = "Product brand") @PathVariable String brand) {
        return ResponseEntity.ok(productRepository.findByBrand(brand));
    }

    @GetMapping("/featured")
    @Operation(summary = "Get featured products", description = "Retrieve all featured products")
    @ApiResponse(responseCode = "200", description = "Featured products retrieved successfully")
    public ResponseEntity<List<Product>> getFeaturedProducts() {
        return ResponseEntity.ok(productRepository.findByFeaturedTrue());
    }

    @GetMapping("/search")
    @Operation(summary = "Search products", description = "Search products by name or description")
    @ApiResponse(responseCode = "200", description = "Search completed successfully")
    public ResponseEntity<List<Product>> searchProducts(
            @Parameter(description = "Search keyword") @RequestParam String keyword) {
        return ResponseEntity.ok(productRepository.searchProducts(keyword));
    }

    @PatchMapping("/{id}/stock")
    @Operation(summary = "Update product stock", description = "Update the stock quantity of a product")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Stock updated successfully"),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<Product> updateStock(
            @Parameter(description = "Product ID") @PathVariable Long id,
            @Parameter(description = "New stock quantity") @RequestParam Integer quantity) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            product.setStockQuantity(quantity);
            return ResponseEntity.ok(productRepository.save(product));
        }
        return ResponseEntity.notFound().build();
    }
}