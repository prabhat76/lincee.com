package com.lincee.controller;

import com.lincee.dto.CartDTO;
import com.lincee.dto.CartItemDTO;
import com.lincee.service.CartService;
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
@RequestMapping("/api/v1/cart")
@Tag(name = "Shopping Cart", description = "APIs for managing shopping cart")
public class CartController {
    
    @Autowired
    private CartService cartService;
    
    @GetMapping("/user/{userId}")
    @Operation(summary = "Get or create cart", description = "Get existing cart or create a new one for user")
    @ApiResponse(responseCode = "200", description = "Cart retrieved or created successfully")
    public ResponseEntity<CartDTO> getOrCreateCart(
            @Parameter(description = "User ID") @PathVariable Long userId) {
        CartDTO cart = cartService.getOrCreateCart(userId);
        return ResponseEntity.ok(cart);
    }
    
    @GetMapping("/{cartId}")
    @Operation(summary = "Get cart by ID", description = "Retrieve a specific cart by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cart found"),
        @ApiResponse(responseCode = "404", description = "Cart not found")
    })
    public ResponseEntity<CartDTO> getCartById(
            @Parameter(description = "Cart ID") @PathVariable Long cartId) {
        CartDTO cart = cartService.getCartById(cartId);
        return ResponseEntity.ok(cart);
    }
    
    @PostMapping("/user/{userId}/items")
    @Operation(summary = "Add item to cart", description = "Add a product to the user's shopping cart")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Item added to cart successfully"),
        @ApiResponse(responseCode = "404", description = "User or Product not found")
    })
    public ResponseEntity<CartItemDTO> addItemToCart(
            @Parameter(description = "User ID") @PathVariable Long userId,
            @Parameter(description = "Product ID") @RequestParam Long productId,
            @Parameter(description = "Quantity") @RequestParam(defaultValue = "1") Integer quantity,
            @Parameter(description = "Size (optional)") @RequestParam(required = false) String size,
            @Parameter(description = "Color (optional)") @RequestParam(required = false) String color) {
        CartItemDTO cartItem = cartService.addItemToCart(userId, productId, quantity, size, color);
        return ResponseEntity.status(HttpStatus.CREATED).body(cartItem);
    }
    
    @PutMapping("/items/{cartItemId}")
    @Operation(summary = "Update cart item", description = "Update quantity of a cart item")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cart item updated successfully"),
        @ApiResponse(responseCode = "404", description = "Cart item not found")
    })
    public ResponseEntity<CartItemDTO> updateCartItem(
            @Parameter(description = "User ID") @RequestParam Long userId,
            @Parameter(description = "Cart Item ID") @PathVariable Long cartItemId,
            @Parameter(description = "New Quantity") @RequestParam Integer quantity) {
        CartItemDTO cartItem = cartService.updateCartItem(userId, cartItemId, quantity);
        return ResponseEntity.ok(cartItem);
    }
    
    @DeleteMapping("/user/{userId}/items/{cartItemId}")
    @Operation(summary = "Remove item from cart", description = "Remove a product from the user's shopping cart")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Item removed from cart successfully"),
        @ApiResponse(responseCode = "404", description = "Cart item not found")
    })
    public ResponseEntity<Void> removeItemFromCart(
            @Parameter(description = "User ID") @PathVariable Long userId,
            @Parameter(description = "Cart Item ID") @PathVariable Long cartItemId) {
        cartService.removeItemFromCart(userId, cartItemId);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/user/{userId}/items")
    @Operation(summary = "Get cart items", description = "Retrieve all items in the user's cart")
    @ApiResponse(responseCode = "200", description = "Cart items retrieved successfully")
    public ResponseEntity<List<CartItemDTO>> getCartItems(
            @Parameter(description = "User ID") @PathVariable Long userId) {
        List<CartItemDTO> items = cartService.getCartItems(userId);
        return ResponseEntity.ok(items);
    }
    
    @DeleteMapping("/user/{userId}/clear")
    @Operation(summary = "Clear cart", description = "Remove all items from the user's cart")
    @ApiResponse(responseCode = "200", description = "Cart cleared successfully")
    public ResponseEntity<CartDTO> clearCart(
            @Parameter(description = "User ID") @PathVariable Long userId) {
        CartDTO cart = cartService.clearCart(userId);
        return ResponseEntity.ok(cart);
    }
}
