package com.lincee.service;

import com.lincee.entity.Product;
import com.lincee.entity.User;
import com.lincee.repository.ProductRepository;
import com.lincee.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;

@Service
public class DataInitService implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public void run(String... args) {
        initializeUsers();
        initializeSweatShortsProducts();
    }
    
    private void initializeUsers() {
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User("admin", "admin@lincee.com", passwordEncoder.encode("password123"));
            admin.setFirstName("Admin");
            admin.setLastName("User");
            admin.setRole(User.Role.ADMIN);
            userRepository.save(admin);
            System.out.println("✅ Admin user created: admin@lincee.com / password123");
        }
        
        // Create a test customer user
        if (!userRepository.existsByUsername("customer")) {
            User customer = new User("customer", "customer@example.com", passwordEncoder.encode("password123"));
            customer.setFirstName("Test");
            customer.setLastName("Customer");
            customer.setRole(User.Role.CUSTOMER);
            userRepository.save(customer);
            System.out.println("✅ Customer user created: customer@example.com / password123");
        }
    }
    
    private void initializeSweatShortsProducts() {
        // Check if sweatshorts products already exist (check for specific sweatshorts category)
        long sweatshortsCount = productRepository.findAll().stream()
                .filter(p -> "Sweatshorts".equalsIgnoreCase(p.getCategory()))
                .count();
        
        if (sweatshortsCount > 0) {
            System.out.println("ℹ️ Sweatshorts products already exist (" + sweatshortsCount + " found), skipping initialization");
            return;
        }
        
        System.out.println("🔄 Initializing Sweatshorts Products...");
        
        // Sweatshorts Product 1 - Classic Grey
        Product sweatShort1 = new Product();
        sweatShort1.setName("Classic Grey Sweatshorts");
        sweatShort1.setDescription("Comfortable cotton blend sweatshorts perfect for workouts and casual wear. Features elastic waistband with drawstring and side pockets.");
        sweatShort1.setPrice(new BigDecimal("29.99"));
        sweatShort1.setDiscountPrice(new BigDecimal("24.99"));
        sweatShort1.setCategory("Sweatshorts");
        sweatShort1.setSubCategory("Men's Activewear");
        sweatShort1.setBrand("Lincee Sport");
        sweatShort1.setStockQuantity(150);
        sweatShort1.setImageUrls(Arrays.asList(
            "https://images.unsplash.com/photo-1591195853828-11db59a44f6b?w=800&h=800&fit=crop&q=80",
            "https://images.unsplash.com/photo-1556821840-3a63f95609a7?w=800&h=800&fit=crop&q=80"
        ));
        sweatShort1.setAvailableSizes(Arrays.asList("S", "M", "L", "XL", "XXL"));
        sweatShort1.setAvailableColors(Arrays.asList("Grey", "Charcoal"));
        sweatShort1.setTags("sweatshorts,athletic,grey,cotton,activewear");
        sweatShort1.setActive(true);
        sweatShort1.setFeatured(true);
        productRepository.save(sweatShort1);
        
        // Sweatshorts Product 2 - Black Athletic
        Product sweatShort2 = new Product();
        sweatShort2.setName("Black Athletic Sweatshorts");
        sweatShort2.setDescription("Premium athletic sweatshorts with moisture-wicking fabric. Ideal for intense workouts and training sessions.");
        sweatShort2.setPrice(new BigDecimal("34.99"));
        sweatShort2.setDiscountPrice(new BigDecimal("27.99"));
        sweatShort2.setCategory("Sweatshorts");
        sweatShort2.setSubCategory("Men's Activewear");
        sweatShort2.setBrand("Lincee Sport");
        sweatShort2.setStockQuantity(120);
        sweatShort2.setImageUrls(Arrays.asList(
            "https://images.unsplash.com/photo-1556821840-3a63f95609a7?w=800&h=800&fit=crop&q=80",
            "https://images.unsplash.com/photo-1574629810360-7efbbe195018?w=800&h=800&fit=crop&q=80"
        ));
        sweatShort2.setAvailableSizes(Arrays.asList("S", "M", "L", "XL", "XXL"));
        sweatShort2.setAvailableColors(Arrays.asList("Black", "Navy"));
        sweatShort2.setTags("sweatshorts,athletic,black,moisture-wicking,training");
        sweatShort2.setActive(true);
        sweatShort2.setFeatured(true);
        productRepository.save(sweatShort2);
        
        // Sweatshorts Product 3 - Navy Blue
        Product sweatShort3 = new Product();
        sweatShort3.setName("Navy Blue Sweatshorts");
        sweatShort3.setDescription("Stylish navy blue sweatshorts with modern fit. Perfect for gym, running, or lounging at home.");
        sweatShort3.setPrice(new BigDecimal("32.99"));
        sweatShort3.setCategory("Sweatshorts");
        sweatShort3.setSubCategory("Men's Activewear");
        sweatShort3.setBrand("Lincee Sport");
        sweatShort3.setStockQuantity(100);
        sweatShort3.setImageUrls(Arrays.asList(
            "https://images.unsplash.com/photo-1519235106638-30cc49b5dbc5?w=800&h=800&fit=crop&q=80",
            "https://images.unsplash.com/photo-1598032895397-b9c644f4ccae?w=800&h=800&fit=crop&q=80"
        ));
        sweatShort3.setAvailableSizes(Arrays.asList("S", "M", "L", "XL"));
        sweatShort3.setAvailableColors(Arrays.asList("Navy", "Royal Blue"));
        sweatShort3.setTags("sweatshorts,navy,modern-fit,gym,running");
        sweatShort3.setActive(true);
        productRepository.save(sweatShort3);
        
        // Sweatshorts Product 4 - Olive Green
        Product sweatShort4 = new Product();
        sweatShort4.setName("Olive Green Sweatshorts");
        sweatShort4.setDescription("Trendy olive green sweatshorts made from soft French terry fabric. Comfortable and durable for everyday wear.");
        sweatShort4.setPrice(new BigDecimal("31.99"));
        sweatShort4.setDiscountPrice(new BigDecimal("25.99"));
        sweatShort4.setCategory("Sweatshorts");
        sweatShort4.setSubCategory("Men's Casual");
        sweatShort4.setBrand("Lincee Basics");
        sweatShort4.setStockQuantity(80);
        sweatShort4.setImageUrls(Arrays.asList(
            "https://images.unsplash.com/photo-1591195853828-11db59a44f6b?w=800&h=800&fit=crop&q=80",
            "https://images.unsplash.com/photo-1620799139507-2a76f79a2f4d?w=800&h=800&fit=crop&q=80"
        ));
        sweatShort4.setAvailableSizes(Arrays.asList("M", "L", "XL", "XXL"));
        sweatShort4.setAvailableColors(Arrays.asList("Olive", "Army Green"));
        sweatShort4.setTags("sweatshorts,olive,french-terry,casual,comfortable");
        sweatShort4.setActive(true);
        productRepository.save(sweatShort4);
        
        // Sweatshorts Product 5 - Maroon
        Product sweatShort5 = new Product();
        sweatShort5.setName("Maroon Sweatshorts");
        sweatShort5.setDescription("Classic maroon sweatshorts with tapered fit. Features deep pockets and reinforced stitching for long-lasting quality.");
        sweatShort5.setPrice(new BigDecimal("28.99"));
        sweatShort5.setCategory("Sweatshorts");
        sweatShort5.setSubCategory("Men's Casual");
        sweatShort5.setBrand("Lincee Basics");
        sweatShort5.setStockQuantity(90);
        sweatShort5.setImageUrls(Arrays.asList(
            "https://images.unsplash.com/photo-1620799140408-edc6dcb6d633?w=800&h=800&fit=crop&q=80",
            "https://images.unsplash.com/photo-1620799139834-6b8f844fbe29?w=800&h=800&fit=crop&q=80"
        ));
        sweatShort5.setAvailableSizes(Arrays.asList("S", "M", "L", "XL"));
        sweatShort5.setAvailableColors(Arrays.asList("Maroon", "Burgundy"));
        sweatShort5.setTags("sweatshorts,maroon,tapered-fit,casual,quality");
        sweatShort5.setActive(true);
        productRepository.save(sweatShort5);
        
        // Sweatshorts Product 6 - Light Grey
        Product sweatShort6 = new Product();
        sweatShort6.setName("Light Grey Sweatshorts");
        sweatShort6.setDescription("Lightweight grey sweatshorts perfect for summer. Breathable fabric with mesh lining for extra comfort.");
        sweatShort6.setPrice(new BigDecimal("27.99"));
        sweatShort6.setDiscountPrice(new BigDecimal("22.99"));
        sweatShort6.setCategory("Sweatshorts");
        sweatShort6.setSubCategory("Men's Summer Collection");
        sweatShort6.setBrand("Lincee Sport");
        sweatShort6.setStockQuantity(110);
        sweatShort6.setImageUrls(Arrays.asList(
            "https://images.unsplash.com/photo-1598032895397-b9c644f4ccae?w=800&h=800&fit=crop&q=80",
            "https://images.unsplash.com/photo-1519235106638-30cc49b5dbc5?w=800&h=800&fit=crop&q=80"
        ));
        sweatShort6.setAvailableSizes(Arrays.asList("S", "M", "L", "XL", "XXL"));
        sweatShort6.setAvailableColors(Arrays.asList("Light Grey", "Heather Grey"));
        sweatShort6.setTags("sweatshorts,summer,lightweight,breathable,mesh");
        sweatShort6.setActive(true);
        sweatShort6.setFeatured(true);
        productRepository.save(sweatShort6);
        
        System.out.println("✅ Successfully initialized 6 Sweatshorts products");
        System.out.println("📦 Products added with front and back images");
        System.out.println("🏷️  Categories: Sweatshorts (Men's Activewear, Casual, Summer Collection)");
    }
}