package com.lincee.service;

import com.lincee.dto.CartDTO;
import com.lincee.dto.CartItemDTO;
import com.lincee.entity.Cart;
import com.lincee.entity.CartItem;
import com.lincee.entity.Product;
import com.lincee.entity.User;
import com.lincee.repository.CartRepository;
import com.lincee.repository.CartItemRepository;
import com.lincee.repository.ProductRepository;
import com.lincee.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CartService {
    
    @Autowired
    private CartRepository cartRepository;
    
    @Autowired
    private CartItemRepository cartItemRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    public CartDTO getOrCreateCart(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (!user.isPresent()) {
            throw new RuntimeException("User not found");
        }
        
        Optional<Cart> existingCart = cartRepository.findByUserId(userId);
        if (existingCart.isPresent()) {
            return convertToDTO(existingCart.get());
        }
        
        Cart newCart = new Cart(user.get());
        Cart savedCart = cartRepository.save(newCart);
        return convertToDTO(savedCart);
    }
    
    public CartDTO getCartByUserId(Long userId) {
        Optional<Cart> cart = cartRepository.findByUserId(userId);
        if (!cart.isPresent()) {
            throw new RuntimeException("Cart not found");
        }
        return convertToDTO(cart.get());
    }
    
    public CartItemDTO addItemToCart(Long userId, Long productId, Integer quantity, String size, String color) {
        // 1. Resolve user
        Optional<User> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) {
            throw new com.lincee.exception.ApiException(com.lincee.exception.ErrorCode.USER_NOT_FOUND, "User not found");
        }

        // 2. Find or create cart
        Cart cart = cartRepository.findByUserId(userId).orElseGet(() -> {
            Cart newCart = new Cart(userOpt.get());
            return cartRepository.save(newCart);
        });

        // 3. Validate product exists and is active
        Optional<Product> productOpt = productRepository.findById(productId);
        if (!productOpt.isPresent()) {
            throw new com.lincee.exception.ApiException(com.lincee.exception.ErrorCode.PRODUCT_NOT_FOUND, "Product not found");
        }
        Product product = productOpt.get();
        if (!Boolean.TRUE.equals(product.getActive())) {
            throw new com.lincee.exception.ApiException(com.lincee.exception.ErrorCode.PRODUCT_NOT_FOUND, "Product is not active");
        }

        // 4. Validate quantity > 0
        if (quantity == null || quantity <= 0) {
            throw new com.lincee.exception.ApiException(com.lincee.exception.ErrorCode.INVALID_QUANTITY, "Quantity must be greater than 0");
        }

        // 5. Validate size/color if required
        if (product.getAvailableSizes() != null && !product.getAvailableSizes().isEmpty()) {
            if (size == null || !product.getAvailableSizes().contains(size)) {
                throw new com.lincee.exception.ApiException(com.lincee.exception.ErrorCode.VALIDATION_ERROR, "Invalid or missing size");
            }
        }
        if (product.getAvailableColors() != null && !product.getAvailableColors().isEmpty()) {
            if (color == null || !product.getAvailableColors().contains(color)) {
                throw new com.lincee.exception.ApiException(com.lincee.exception.ErrorCode.VALIDATION_ERROR, "Invalid or missing color");
            }
        }

        // 6. Upsert cart item by (cartId, productId, size, color)
        Optional<CartItem> existingItemOpt = cartItemRepository.findByCartIdAndProductIdAndSizeAndColor(cart.getId(), productId, size, color);
        CartItem cartItem;
        if (existingItemOpt.isPresent()) {
            cartItem = existingItemOpt.get();
            int newQuantity = cartItem.getQuantity() + quantity;
            if (product.getStockQuantity() < newQuantity) {
                throw new com.lincee.exception.ApiException(com.lincee.exception.ErrorCode.INSUFFICIENT_STOCK, "Insufficient stock. Available: " + product.getStockQuantity());
            }
            cartItem.setQuantity(newQuantity);
        } else {
            if (product.getStockQuantity() < quantity) {
                throw new com.lincee.exception.ApiException(com.lincee.exception.ErrorCode.INSUFFICIENT_STOCK, "Insufficient stock. Available: " + product.getStockQuantity());
            }
            cartItem = new CartItem(cart, product, quantity);
            cartItem.setSize(size);
            cartItem.setColor(color);
        }

        // 7. Save and return DTO
        CartItem savedItem;
        try {
            savedItem = cartItemRepository.save(cartItem);
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            // Handle unique constraint violation by merging
            existingItemOpt = cartItemRepository.findByCartIdAndProductIdAndSizeAndColor(cart.getId(), productId, size, color);
            if (existingItemOpt.isPresent()) {
                cartItem = existingItemOpt.get();
                int newQuantity = cartItem.getQuantity() + quantity;
                if (product.getStockQuantity() < newQuantity) {
                    throw new com.lincee.exception.ApiException(com.lincee.exception.ErrorCode.INSUFFICIENT_STOCK, "Insufficient stock. Available: " + product.getStockQuantity());
                }
                cartItem.setQuantity(newQuantity);
                savedItem = cartItemRepository.save(cartItem);
            } else {
                throw new com.lincee.exception.ApiException(com.lincee.exception.ErrorCode.INTERNAL_ERROR, "Failed to upsert cart item");
            }
        }
        cart.updateTotals();
        cartRepository.save(cart);
        return convertCartItemToDTO(savedItem);
    }
    
    public CartItemDTO updateCartItem(Long userId, Long cartItemId, Integer quantity) {
        Optional<Cart> cart = cartRepository.findByUserId(userId);
        if (!cart.isPresent()) {
            throw new RuntimeException("Cart not found");
        }
        
        Optional<CartItem> cartItem = cartItemRepository.findById(cartItemId);
        if (!cartItem.isPresent()) {
            throw new RuntimeException("Cart item not found");
        }
        
        if (quantity <= 0) {
            removeItemFromCart(userId, cartItemId);
            return convertCartItemToDTO(cartItem.get()); // Return the removed item state or handle differently
        }

        if (cartItem.get().getProduct().getStockQuantity() < quantity) {
            throw new RuntimeException("Insufficient stock. Available: " + cartItem.get().getProduct().getStockQuantity());
        }

        CartItem item = cartItem.get();
        item.setQuantity(quantity);
        CartItem updatedItem = cartItemRepository.save(item);
        
        Cart existingCart = cart.get();
        existingCart.updateTotals();
        cartRepository.save(existingCart);
        
        return convertCartItemToDTO(updatedItem);
    }
    
    public void removeItemFromCart(Long userId, Long cartItemId) {
        Optional<Cart> cart = cartRepository.findByUserId(userId);
        if (!cart.isPresent()) {
            throw new RuntimeException("Cart not found");
        }
        
        Optional<CartItem> cartItem = cartItemRepository.findById(cartItemId);
        if (!cartItem.isPresent()) {
            throw new RuntimeException("Cart item not found");
        }
        
        cartItemRepository.deleteById(cartItemId);
        
        Cart existingCart = cart.get();
        existingCart.updateTotals();
        cartRepository.save(existingCart);
    }
    
    public CartDTO clearCart(Long userId) {
        Optional<Cart> cart = cartRepository.findByUserId(userId);
        if (!cart.isPresent()) {
            throw new RuntimeException("Cart not found");
        }
        
        Cart existingCart = cart.get();
        existingCart.clearCart();
        cartItemRepository.deleteByCartId(existingCart.getId());
        Cart updatedCart = cartRepository.save(existingCart);
        return convertToDTO(updatedCart);
    }
    
    public List<CartItemDTO> getCartItems(Long userId) {
        Optional<Cart> cart = cartRepository.findByUserId(userId);
        if (!cart.isPresent()) {
            throw new RuntimeException("Cart not found");
        }
        
        return cart.get().getItems().stream()
                .map(this::convertCartItemToDTO)
                .collect(Collectors.toList());
    }
    
    public CartDTO getCartById(Long cartId) {
        Optional<Cart> cart = cartRepository.findById(cartId);
        if (!cart.isPresent()) {
            throw new RuntimeException("Cart not found");
        }
        return convertToDTO(cart.get());
    }
    
    private CartDTO convertToDTO(Cart cart) {
        CartDTO dto = new CartDTO();
        dto.setId(cart.getId());
        dto.setUserId(cart.getUser().getId());
        dto.setTotalPrice(cart.getTotalPrice());
        dto.setItemCount(cart.getItemCount());
        dto.setItems(cart.getItems().stream()
                .map(this::convertCartItemToDTO)
                .collect(Collectors.toList()));
        dto.setUpdatedAt(cart.getUpdatedAt());
        return dto;
    }
    
    private CartItemDTO convertCartItemToDTO(CartItem cartItem) {
        CartItemDTO dto = new CartItemDTO();
        dto.setId(cartItem.getId());
        dto.setCartId(cartItem.getCart().getId());
        dto.setProductId(cartItem.getProduct().getId());
        dto.setProductName(cartItem.getProduct().getName());
        dto.setProductImage(cartItem.getProduct().getImageUrls().isEmpty() ? null : cartItem.getProduct().getImageUrls().get(0));
        dto.setQuantity(cartItem.getQuantity());
        dto.setUnitPrice(cartItem.getUnitPrice());
        dto.setSubTotal(cartItem.getSubTotal());
        dto.setSize(cartItem.getSize());
        dto.setColor(cartItem.getColor());
        dto.setCreatedAt(cartItem.getCreatedAt());
        return dto;
    }
}
