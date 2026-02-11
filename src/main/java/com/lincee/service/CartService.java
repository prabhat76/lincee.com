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
        Optional<Cart> cart = cartRepository.findByUserId(userId);
        if (!cart.isPresent()) {
            throw new RuntimeException("Cart not found");
        }
        
        Optional<Product> product = productRepository.findById(productId);
        if (!product.isPresent()) {
            throw new RuntimeException("Product not found");
        }

        if (quantity <= 0) {
            throw new RuntimeException("Quantity must be greater than 0");
        }
        
        Cart existingCart = cart.get();
        Optional<CartItem> existingItem = cartItemRepository.findByCartIdAndProductId(existingCart.getId(), productId);
        
        CartItem cartItem;
        if (existingItem.isPresent()) {
            cartItem = existingItem.get();
            int newQuantity = cartItem.getQuantity() + quantity;
            if (product.get().getStockQuantity() < newQuantity) {
                throw new RuntimeException("Insufficient stock. Available: " + product.get().getStockQuantity());
            }
            cartItem.setQuantity(newQuantity);
        } else {
            if (product.get().getStockQuantity() < quantity) {
                throw new RuntimeException("Insufficient stock. Available: " + product.get().getStockQuantity());
            }
            cartItem = new CartItem(existingCart, product.get(), quantity);
        }
        
        if (size != null) {
            cartItem.setSize(size);
        }
        if (color != null) {
            cartItem.setColor(color);
        }
        
        CartItem savedItem = cartItemRepository.save(cartItem);
        existingCart.updateTotals();
        cartRepository.save(existingCart);
        
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
