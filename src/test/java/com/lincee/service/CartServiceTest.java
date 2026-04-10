package com.lincee.service;

import com.lincee.dto.CartItemDTO;
import com.lincee.entity.Cart;
import com.lincee.entity.CartItem;
import com.lincee.entity.Product;
import com.lincee.entity.User;
import com.lincee.exception.ApiException;
import com.lincee.repository.CartItemRepository;
import com.lincee.repository.CartRepository;
import com.lincee.repository.ProductRepository;
import com.lincee.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class CartServiceTest {
    @Mock
    private CartRepository cartRepository;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private CartService cartService;

    private User user;
    private Product product;
    private Cart cart;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1L);
        product = new Product();
        product.setId(25L);
        product.setActive(true);
        product.setStockQuantity(10);
        product.setAvailableSizes(Arrays.asList("M", "L"));
        product.setAvailableColors(Arrays.asList("Black", "White"));
        product.setPrice(new BigDecimal("100.00"));
        cart = new Cart(user);
        cart.setId(100L);
    }

    @Test
    void addsItemWhenCartAbsent() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.empty());
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);
        when(productRepository.findById(25L)).thenReturn(Optional.of(product));
        when(cartItemRepository.findByCartIdAndProductIdAndSizeAndColor(100L, 25L, "M", "Black")).thenReturn(Optional.empty());
        when(cartItemRepository.save(any(CartItem.class))).thenAnswer(i -> i.getArgument(0));

        CartItemDTO dto = cartService.addItemToCart(1L, 25L, 1, "M", "Black");
        assertEquals(25L, dto.getProductId());
        assertEquals(1, dto.getQuantity());
        assertEquals("M", dto.getSize());
        assertEquals("Black", dto.getColor());
    }

    @Test
    void mergesDuplicateLineItem() {
        CartItem existing = new CartItem(cart, product, 2);
        existing.setSize("M");
        existing.setColor("Black");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));
        when(productRepository.findById(25L)).thenReturn(Optional.of(product));
        when(cartItemRepository.findByCartIdAndProductIdAndSizeAndColor(100L, 25L, "M", "Black")).thenReturn(Optional.of(existing));
        when(cartItemRepository.save(any(CartItem.class))).thenAnswer(i -> i.getArgument(0));

        CartItemDTO dto = cartService.addItemToCart(1L, 25L, 3, "M", "Black");
        assertEquals(5, dto.getQuantity());
    }

    @Test
    void invalidPayloadReturns400() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));
        when(productRepository.findById(25L)).thenReturn(Optional.of(product));
        ApiException ex = assertThrows(ApiException.class, () -> cartService.addItemToCart(1L, 25L, 0, "M", "Black"));
        assertEquals("INVALID_QUANTITY", ex.getErrorCode().getCode());
    }

    @Test
    void missingProductReturns404() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));
        when(productRepository.findById(99L)).thenReturn(Optional.empty());
        ApiException ex = assertThrows(ApiException.class, () -> cartService.addItemToCart(1L, 99L, 1, "M", "Black"));
        assertEquals("PRODUCT_NOT_FOUND", ex.getErrorCode().getCode());
    }

    @Test
    void stockConflictReturns409() {
        product.setStockQuantity(2);
        CartItem existing = new CartItem(cart, product, 2);
        existing.setSize("M");
        existing.setColor("Black");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));
        when(productRepository.findById(25L)).thenReturn(Optional.of(product));
        when(cartItemRepository.findByCartIdAndProductIdAndSizeAndColor(100L, 25L, "M", "Black")).thenReturn(Optional.of(existing));
        ApiException ex = assertThrows(ApiException.class, () -> cartService.addItemToCart(1L, 25L, 2, "M", "Black"));
        assertEquals("INSUFFICIENT_STOCK", ex.getErrorCode().getCode());
    }

    @Test
    void concurrentAddsDoNotCrash() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));
        when(productRepository.findById(25L)).thenReturn(Optional.of(product));
        when(cartItemRepository.findByCartIdAndProductIdAndSizeAndColor(100L, 25L, "M", "Black"))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.of(new CartItem(cart, product, 1)));
        when(cartItemRepository.save(any(CartItem.class))).thenThrow(new org.springframework.dao.DataIntegrityViolationException("Duplicate"))
                .thenAnswer(i -> i.getArgument(0));

        CartItemDTO dto = cartService.addItemToCart(1L, 25L, 1, "M", "Black");
        assertEquals(2, dto.getQuantity());
    }
}
