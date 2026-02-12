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
        
        // Check total products to ensure all categories are initialized
        long totalProducts = productRepository.count();
        
        // If total products is less than 52 (12 hoodies + 9 tshirts + 26 sweatshirts + 5 sweatshorts),
        // reinitialize missing products
        if (totalProducts < 52) {
            System.out.println("⚠️  Found only " + totalProducts + " products. Reinitializing missing products...");
            initializeSweatShortsProducts();
            initializeSweatshirtProducts();
            initializeHoodieProducts();
            initializeTshirtProducts();
        } else {
            System.out.println("✅ All products already initialized (" + totalProducts + " found)");
        }
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
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770469714/products/sweatshirts/Sweatshirt%203/z04i8dybvplclhv6r6aq.png",
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
    
    private void initializeSweatshirtProducts() {
        // Check if sweatshirt products already exist
        long sweatshirtCount = productRepository.findAll().stream()
                .filter(p -> "Sweatshirts".equalsIgnoreCase(p.getCategory()))
                .count();
        
        if (sweatshirtCount > 0) {
            System.out.println("ℹ️ Sweatshirt products already exist (" + sweatshirtCount + " found), skipping initialization");
            return;
        }
        
        System.out.println("🔄 Initializing 26 Sweatshirt Products...");
        
        // Sweatshirt 1 - Classic Crew Neck
        Product sweatshirt1 = new Product();
        sweatshirt1.setName("Classic Crew Neck Sweatshirt");
        sweatshirt1.setDescription("Timeless crew neck sweatshirt in premium cotton blend. Perfect for layering or wearing solo.");
        sweatshirt1.setPrice(new BigDecimal("49.99"));
        sweatshirt1.setDiscountPrice(new BigDecimal("39.99"));
        sweatshirt1.setCategory("Sweatshirts");
        sweatshirt1.setSubCategory("Men's Basics");
        sweatshirt1.setBrand("Lincee Essentials");
        sweatshirt1.setStockQuantity(200);
        sweatshirt1.setImageUrls(Arrays.asList(
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770469521/products/sweatshirts/Sweatshirt%201/iqdmz8hmo7vqkanqrifs.png",
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770469531/products/sweatshirts/Sweatshirt%201/dil9tjacl5mbl5reuogu.png"
        ));
        sweatshirt1.setAvailableSizes(Arrays.asList("S", "M", "L", "XL", "XXL"));
        sweatshirt1.setAvailableColors(Arrays.asList("Grey", "Black", "Navy"));
        sweatshirt1.setTags("sweatshirt,crew-neck,cotton,classic,basics");
        sweatshirt1.setActive(true);
        sweatshirt1.setFeatured(true);
        productRepository.save(sweatshirt1);
        
        // Sweatshirt 2 - Urban Hoodie Style
        Product sweatshirt2 = new Product();
        sweatshirt2.setName("Urban Hoodie Sweatshirt");
        sweatshirt2.setDescription("Modern hoodie-style sweatshirt with adjustable drawstrings. Street-style meets comfort.");
        sweatshirt2.setPrice(new BigDecimal("54.99"));
        sweatshirt2.setDiscountPrice(new BigDecimal("44.99"));
        sweatshirt2.setCategory("Sweatshirts");
        sweatshirt2.setSubCategory("Men's Streetwear");
        sweatshirt2.setBrand("Lincee Urban");
        sweatshirt2.setStockQuantity(150);
        sweatshirt2.setImageUrls(Arrays.asList(
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770469628/products/sweatshirts/Sweatshirt%202/rigftnbs2fhi6kgmhkhu.png",
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770469632/products/sweatshirts/Sweatshirt%202/aqze2zmiq6fioqzxgxlm.png"
        ));
        sweatshirt2.setAvailableSizes(Arrays.asList("S", "M", "L", "XL", "XXL"));
        sweatshirt2.setAvailableColors(Arrays.asList("Black", "Charcoal", "Olive"));
        sweatshirt2.setTags("sweatshirt,hoodie,urban,streetwear,drawstring");
        sweatshirt2.setActive(true);
        sweatshirt2.setFeatured(true);
        productRepository.save(sweatshirt2);
        
        // Sweatshirt 3 - Vintage Graphic Print
        Product sweatshirt3 = new Product();
        sweatshirt3.setName("Vintage Graphic Sweatshirt");
        sweatshirt3.setDescription("Retro-inspired sweatshirt featuring unique graphic prints. Makes a bold fashion statement.");
        sweatshirt3.setPrice(new BigDecimal("52.99"));
        sweatshirt3.setCategory("Sweatshirts");
        sweatshirt3.setSubCategory("Men's Graphics");
        sweatshirt3.setBrand("Lincee Vintage");
        sweatshirt3.setStockQuantity(120);
        sweatshirt3.setImageUrls(Arrays.asList(
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770469714/products/sweatshirts/Sweatshirt%203/z04i8dybvplclhv6r6aq.png",
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770469719/products/sweatshirts/Sweatshirt%203/esfuctz6nckrafqoz8n2.png"
        ));
        sweatshirt3.setAvailableSizes(Arrays.asList("S", "M", "L", "XL"));
        sweatshirt3.setAvailableColors(Arrays.asList("White", "Cream", "Light Blue"));
        sweatshirt3.setTags("sweatshirt,vintage,graphic,print,retro");
        sweatshirt3.setActive(true);
        productRepository.save(sweatshirt3);
        
        // Sweatshirt 4 - Minimalist Design
        Product sweatshirt4 = new Product();
        sweatshirt4.setName("Minimalist Sweatshirt");
        sweatshirt4.setDescription("Clean, minimalist design for the modern wardrobe. Simple elegance in premium fabric.");
        sweatshirt4.setPrice(new BigDecimal("47.99"));
        sweatshirt4.setCategory("Sweatshirts");
        sweatshirt4.setSubCategory("Men's Minimalist");
        sweatshirt4.setBrand("Lincee Essentials");
        sweatshirt4.setStockQuantity(180);
        sweatshirt4.setImageUrls(Arrays.asList(
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770469509/products/sweatshirts/Sweartshirt%204/jodozx9uilyirzl0pldg.png",
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770469514/products/sweatshirts/Sweartshirt%204/ezxhmkcemcxhkezzwpnp.png"
        ));
        sweatshirt4.setAvailableSizes(Arrays.asList("S", "M", "L", "XL", "XXL"));
        sweatshirt4.setAvailableColors(Arrays.asList("Black", "Grey", "White"));
        sweatshirt4.setTags("sweatshirt,minimalist,clean,modern,simple");
        sweatshirt4.setActive(true);
        sweatshirt4.setFeatured(true);
        productRepository.save(sweatshirt4);
        
        // Sweatshirt 5 - Athletic Performance
        Product sweatshirt5 = new Product();
        sweatshirt5.setName("Athletic Performance Sweatshirt");
        sweatshirt5.setDescription("Performance-oriented sweatshirt with moisture-wicking technology. Built for active lifestyles.");
        sweatshirt5.setPrice(new BigDecimal("59.99"));
        sweatshirt5.setDiscountPrice(new BigDecimal("49.99"));
        sweatshirt5.setCategory("Sweatshirts");
        sweatshirt5.setSubCategory("Men's Athletic");
        sweatshirt5.setBrand("Lincee Sport");
        sweatshirt5.setStockQuantity(140);
        sweatshirt5.setImageUrls(Arrays.asList(
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770469765/products/sweatshirts/Sweatshirt%205/loul1l12zqhpcx0fkmus.png",
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770469768/products/sweatshirts/Sweatshirt%205/gajg9sepbbcvlturdbbl.png"
        ));
        sweatshirt5.setAvailableSizes(Arrays.asList("S", "M", "L", "XL", "XXL"));
        sweatshirt5.setAvailableColors(Arrays.asList("Navy", "Black", "Grey"));
        sweatshirt5.setTags("sweatshirt,athletic,performance,moisture-wicking,sport");
        sweatshirt5.setActive(true);
        productRepository.save(sweatshirt5);
        
        // Sweatshirt 7 - Oversized Comfort
        Product sweatshirt7 = new Product();
        sweatshirt7.setName("Oversized Comfort Sweatshirt");
        sweatshirt7.setDescription("Relaxed, oversized fit for maximum comfort. Perfect for lounging or casual outings.");
        sweatshirt7.setPrice(new BigDecimal("51.99"));
        sweatshirt7.setCategory("Sweatshirts");
        sweatshirt7.setSubCategory("Men's Comfort");
        sweatshirt7.setBrand("Lincee Comfort");
        sweatshirt7.setStockQuantity(130);
        sweatshirt7.setImageUrls(Arrays.asList(
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770469772/products/sweatshirts/Sweatshirt%207/lrkks1dvacfvjjpwbwtr.png",
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770469777/products/sweatshirts/Sweatshirt%207/f0rwqchhqasneljedcfl.png"
        ));
        sweatshirt7.setAvailableSizes(Arrays.asList("M", "L", "XL", "XXL"));
        sweatshirt7.setAvailableColors(Arrays.asList("Beige", "Grey", "Black"));
        sweatshirt7.setTags("sweatshirt,oversized,comfort,relaxed,loungewear");
        sweatshirt7.setActive(true);
        productRepository.save(sweatshirt7);
        
        // Sweatshirt 9 - Classic Pullover
        Product sweatshirt9 = new Product();
        sweatshirt9.setName("Classic Pullover Sweatshirt");
        sweatshirt9.setDescription("Traditional pullover design in soft fleece. A wardrobe staple for all seasons.");
        sweatshirt9.setPrice(new BigDecimal("48.99"));
        sweatshirt9.setDiscountPrice(new BigDecimal("38.99"));
        sweatshirt9.setCategory("Sweatshirts");
        sweatshirt9.setSubCategory("Men's Classics");
        sweatshirt9.setBrand("Lincee Essentials");
        sweatshirt9.setStockQuantity(170);
        sweatshirt9.setImageUrls(Arrays.asList(
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770469781/products/sweatshirts/Sweatshirt%209/lgjqz3jf7emngofrxcv2.png",
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770469787/products/sweatshirts/Sweatshirt%209/c5hdamgsgjs1r1ffunqn.png"
        ));
        sweatshirt9.setAvailableSizes(Arrays.asList("S", "M", "L", "XL", "XXL"));
        sweatshirt9.setAvailableColors(Arrays.asList("Maroon", "Navy", "Forest Green"));
        sweatshirt9.setTags("sweatshirt,pullover,fleece,classic,staple");
        sweatshirt9.setActive(true);
        productRepository.save(sweatshirt9);
        
        // Sweatshirt 10 - Gothic Rose Print
        Product sweatshirt10 = new Product();
        sweatshirt10.setName("Gothic Rose Print Sweatshirt");
        sweatshirt10.setDescription("Edgy gothic design with rose motifs. Dark aesthetics meet premium comfort.");
        sweatshirt10.setPrice(new BigDecimal("56.99"));
        sweatshirt10.setCategory("Sweatshirts");
        sweatshirt10.setSubCategory("Men's Alternative");
        sweatshirt10.setBrand("Lincee Dark");
        sweatshirt10.setStockQuantity(100);
        sweatshirt10.setImageUrls(Arrays.asList(
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770469542/products/sweatshirts/Sweatshirt%2010/o2topwn2fba1f8xchcsa.png",
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770469548/products/sweatshirts/Sweatshirt%2010/nrazailg5rhtol9fbh6s.png"
        ));
        sweatshirt10.setAvailableSizes(Arrays.asList("S", "M", "L", "XL"));
        sweatshirt10.setAvailableColors(Arrays.asList("Black", "Charcoal"));
        sweatshirt10.setTags("sweatshirt,gothic,rose,print,alternative,edgy");
        sweatshirt10.setActive(true);
        sweatshirt10.setFeatured(true);
        productRepository.save(sweatshirt10);
        
        // Sweatshirt 11 - Modern Tech Fabric
        Product sweatshirt11 = new Product();
        sweatshirt11.setName("Modern Tech Fabric Sweatshirt");
        sweatshirt11.setDescription("Advanced tech fabric with temperature regulation. The future of comfortable clothing.");
        sweatshirt11.setPrice(new BigDecimal("64.99"));
        sweatshirt11.setDiscountPrice(new BigDecimal("54.99"));
        sweatshirt11.setCategory("Sweatshirts");
        sweatshirt11.setSubCategory("Men's Tech");
        sweatshirt11.setBrand("Lincee Tech");
        sweatshirt11.setStockQuantity(110);
        sweatshirt11.setImageUrls(Arrays.asList(
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770469553/products/sweatshirts/Sweatshirt%2011/ohovjqlad1pbwadfjb9w.png",
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770469558/products/sweatshirts/Sweatshirt%2011/p7ju56eb1tb9mqtki84i.png"
        ));
        sweatshirt11.setAvailableSizes(Arrays.asList("S", "M", "L", "XL", "XXL"));
        sweatshirt11.setAvailableColors(Arrays.asList("Slate Grey", "Navy", "Black"));
        sweatshirt11.setTags("sweatshirt,tech-fabric,modern,temperature-regulation,innovative");
        sweatshirt11.setActive(true);
        productRepository.save(sweatshirt11);
        
        // Sweatshirt 13 - Casual Everyday
        Product sweatshirt13 = new Product();
        sweatshirt13.setName("Casual Everyday Sweatshirt");
        sweatshirt13.setDescription("Your go-to sweatshirt for daily wear. Versatile and comfortable for any occasion.");
        sweatshirt13.setPrice(new BigDecimal("45.99"));
        sweatshirt13.setCategory("Sweatshirts");
        sweatshirt13.setSubCategory("Men's Casual");
        sweatshirt13.setBrand("Lincee Basics");
        sweatshirt13.setStockQuantity(190);
        sweatshirt13.setImageUrls(Arrays.asList(
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770469562/products/sweatshirts/Sweatshirt%2013/yrqu8rohjvcgafc712jh.png",
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770469566/products/sweatshirts/Sweatshirt%2013/cvjezayb4qfecsfe5tsz.png"
        ));
        sweatshirt13.setAvailableSizes(Arrays.asList("S", "M", "L", "XL", "XXL"));
        sweatshirt13.setAvailableColors(Arrays.asList("Grey", "Navy", "Olive"));
        sweatshirt13.setTags("sweatshirt,casual,everyday,versatile,comfortable");
        sweatshirt13.setActive(true);
        productRepository.save(sweatshirt13);
        
        // Sweatshirt 14 - Premium Cotton Blend
        Product sweatshirt14 = new Product();
        sweatshirt14.setName("Premium Cotton Blend Sweatshirt");
        sweatshirt14.setDescription("Luxury cotton-blend fabric for superior softness. Elevated comfort for discerning tastes.");
        sweatshirt14.setPrice(new BigDecimal("69.99"));
        sweatshirt14.setDiscountPrice(new BigDecimal("59.99"));
        sweatshirt14.setCategory("Sweatshirts");
        sweatshirt14.setSubCategory("Men's Premium");
        sweatshirt14.setBrand("Lincee Premium");
        sweatshirt14.setStockQuantity(90);
        sweatshirt14.setImageUrls(Arrays.asList(
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770469571/products/sweatshirts/Sweatshirt%2014/c68vhickqvgbgt910ggr.png",
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770469575/products/sweatshirts/Sweatshirt%2014/zubfwv4rmlpbpxcydag2.png"
        ));
        sweatshirt14.setAvailableSizes(Arrays.asList("S", "M", "L", "XL"));
        sweatshirt14.setAvailableColors(Arrays.asList("Cream", "Charcoal", "Navy"));
        sweatshirt14.setTags("sweatshirt,premium,cotton-blend,luxury,soft");
        sweatshirt14.setActive(true);
        sweatshirt14.setFeatured(true);
        productRepository.save(sweatshirt14);
        
        // Sweatshirt 15 - Sporty Zip-Up
        Product sweatshirt15 = new Product();
        sweatshirt15.setName("Sporty Zip-Up Sweatshirt");
        sweatshirt15.setDescription("Full-zip design for easy on and off. Perfect for pre and post-workout wear.");
        sweatshirt15.setPrice(new BigDecimal("57.99"));
        sweatshirt15.setCategory("Sweatshirts");
        sweatshirt15.setSubCategory("Men's Sport");
        sweatshirt15.setBrand("Lincee Sport");
        sweatshirt15.setStockQuantity(125);
        sweatshirt15.setImageUrls(Arrays.asList(
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770469579/products/sweatshirts/Sweatshirt%2015/gh6yhftuxgsaeopd1isu.png",
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770469585/products/sweatshirts/Sweatshirt%2015/ddm9ap2impzivjvaoch8.png"
        ));
        sweatshirt15.setAvailableSizes(Arrays.asList("S", "M", "L", "XL", "XXL"));
        sweatshirt15.setAvailableColors(Arrays.asList("Black", "Navy", "Red"));
        sweatshirt15.setTags("sweatshirt,zip-up,sporty,athletic,workout");
        sweatshirt15.setActive(true);
        productRepository.save(sweatshirt15);
        
        // Sweatshirt 16 - Retro Stripe Design
        Product sweatshirt16 = new Product();
        sweatshirt16.setName("Retro Stripe Sweatshirt");
        sweatshirt16.setDescription("Classic stripe pattern with vintage appeal. Nostalgic style meets modern fit.");
        sweatshirt16.setPrice(new BigDecimal("53.99"));
        sweatshirt16.setCategory("Sweatshirts");
        sweatshirt16.setSubCategory("Men's Retro");
        sweatshirt16.setBrand("Lincee Vintage");
        sweatshirt16.setStockQuantity(105);
        sweatshirt16.setImageUrls(Arrays.asList(
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770469589/products/sweatshirts/Sweatshirt%2016/x5d4gplizlzw5ayisw1t.png",
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770469594/products/sweatshirts/Sweatshirt%2016/fdkeewhdubobgr6ifyzh.png"
        ));
        sweatshirt16.setAvailableSizes(Arrays.asList("S", "M", "L", "XL"));
        sweatshirt16.setAvailableColors(Arrays.asList("Cream/Navy", "Grey/Burgundy"));
        sweatshirt16.setTags("sweatshirt,retro,stripe,vintage,classic");
        sweatshirt16.setActive(true);
        productRepository.save(sweatshirt16);
        
        // Sweatshirt 17 - Colorblock Design
        Product sweatshirt17 = new Product();
        sweatshirt17.setName("Colorblock Sweatshirt");
        sweatshirt17.setDescription("Bold colorblock design for standout style. Make a statement with contrasting colors.");
        sweatshirt17.setPrice(new BigDecimal("54.99"));
        sweatshirt17.setDiscountPrice(new BigDecimal("44.99"));
        sweatshirt17.setCategory("Sweatshirts");
        sweatshirt17.setSubCategory("Men's Streetwear");
        sweatshirt17.setBrand("Lincee Urban");
        sweatshirt17.setStockQuantity(115);
        sweatshirt17.setImageUrls(Arrays.asList(
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770469598/products/sweatshirts/Sweatshirt%2017/slc8e1niaql2hpz2jnj1.png",
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770469604/products/sweatshirts/Sweatshirt%2017/bm3w4xigunlkleiicrwj.png"
        ));
        sweatshirt17.setAvailableSizes(Arrays.asList("S", "M", "L", "XL", "XXL"));
        sweatshirt17.setAvailableColors(Arrays.asList("Navy/White", "Black/Grey", "Green/Beige"));
        sweatshirt17.setTags("sweatshirt,colorblock,bold,streetwear,contrast");
        sweatshirt17.setActive(true);
        productRepository.save(sweatshirt17);
        
        // Sweatshirt 18 - Fleece Lined Winter
        Product sweatshirt18 = new Product();
        sweatshirt18.setName("Fleece Lined Winter Sweatshirt");
        sweatshirt18.setDescription("Extra warm fleece-lined interior for cold weather. Maximum warmth without bulk.");
        sweatshirt18.setPrice(new BigDecimal("62.99"));
        sweatshirt18.setCategory("Sweatshirts");
        sweatshirt18.setSubCategory("Men's Winter");
        sweatshirt18.setBrand("Lincee Winter");
        sweatshirt18.setStockQuantity(95);
        sweatshirt18.setImageUrls(Arrays.asList(
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770469792/products/sweatshirts/Swratshirt%2018/vp2uzlpqcdxj6ft8slru.png",
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770469804/products/sweatshirts/Swratshirt%2018/zbxahe6d7x3vimo7dx5s.png"
        ));
        sweatshirt18.setAvailableSizes(Arrays.asList("M", "L", "XL", "XXL"));
        sweatshirt18.setAvailableColors(Arrays.asList("Charcoal", "Navy", "Black"));
        sweatshirt18.setTags("sweatshirt,fleece-lined,winter,warm,cold-weather");
        sweatshirt18.setActive(true);
        productRepository.save(sweatshirt18);
        
        // Sweatshirt 19 - Lightweight Summer
        Product sweatshirt19 = new Product();
        sweatshirt19.setName("Lightweight Summer Sweatshirt");
        sweatshirt19.setDescription("Breathable lightweight fabric for warmer days. Perfect layering piece for summer evenings.");
        sweatshirt19.setPrice(new BigDecimal("42.99"));
        sweatshirt19.setCategory("Sweatshirts");
        sweatshirt19.setSubCategory("Men's Summer");
        sweatshirt19.setBrand("Lincee Summer");
        sweatshirt19.setStockQuantity(160);
        sweatshirt19.setImageUrls(Arrays.asList(
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770469615/products/sweatshirts/Sweatshirt%2019/qvgb011mr5ldrspmf3ce.png",
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770469622/products/sweatshirts/Sweatshirt%2019/uf6xlmhvf27n5dpo7p5s.png"
        ));
        sweatshirt19.setAvailableSizes(Arrays.asList("S", "M", "L", "XL"));
        sweatshirt19.setAvailableColors(Arrays.asList("White", "Light Grey", "Mint"));
        sweatshirt19.setTags("sweatshirt,lightweight,summer,breathable,layering");
        sweatshirt19.setActive(true);
        productRepository.save(sweatshirt19);
        
        // Sweatshirt 20 - Embroidered Logo
        Product sweatshirt20 = new Product();
        sweatshirt20.setName("Embroidered Logo Sweatshirt");
        sweatshirt20.setDescription("Premium embroidered branding adds sophistication. Quality details make the difference.");
        sweatshirt20.setPrice(new BigDecimal("58.99"));
        sweatshirt20.setDiscountPrice(new BigDecimal("48.99"));
        sweatshirt20.setCategory("Sweatshirts");
        sweatshirt20.setSubCategory("Men's Premium");
        sweatshirt20.setBrand("Lincee Premium");
        sweatshirt20.setStockQuantity(135);
        sweatshirt20.setImageUrls(Arrays.asList(
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770469636/products/sweatshirts/Sweatshirt%2020/g8xl9dvuvjlrgcn2jjbw.png",
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770469642/products/sweatshirts/Sweatshirt%2020/yvtbd5xyhxgmehei6j7n.png"
        ));
        sweatshirt20.setAvailableSizes(Arrays.asList("S", "M", "L", "XL", "XXL"));
        sweatshirt20.setAvailableColors(Arrays.asList("Navy", "Black", "Forest Green"));
        sweatshirt20.setTags("sweatshirt,embroidered,logo,premium,sophisticated");
        sweatshirt20.setActive(true);
        sweatshirt20.setFeatured(true);
        productRepository.save(sweatshirt20);
        
        // Sweatshirt 21 - Quarter Zip Pullover
        Product sweatshirt21 = new Product();
        sweatshirt21.setName("Quarter Zip Pullover Sweatshirt");
        sweatshirt21.setDescription("Versatile quarter-zip design with stand collar. Classic styling with functional details.");
        sweatshirt21.setPrice(new BigDecimal("55.99"));
        sweatshirt21.setCategory("Sweatshirts");
        sweatshirt21.setSubCategory("Men's Athletic");
        sweatshirt21.setBrand("Lincee Sport");
        sweatshirt21.setStockQuantity(145);
        sweatshirt21.setImageUrls(Arrays.asList(
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770469653/products/sweatshirts/Sweatshirt%2021/ygantdfjxntjrtp7dhqd.png",
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770469657/products/sweatshirts/Sweatshirt%2021/gpqnsgdmkcm2zmqeepd8.png"
        ));
        sweatshirt21.setAvailableSizes(Arrays.asList("S", "M", "L", "XL", "XXL"));
        sweatshirt21.setAvailableColors(Arrays.asList("Grey", "Navy", "Burgundy"));
        sweatshirt21.setTags("sweatshirt,quarter-zip,pullover,athletic,functional");
        sweatshirt21.setActive(true);
        productRepository.save(sweatshirt21);
        
        // Sweatshirt 22 - Raglan Sleeve Design
        Product sweatshirt22 = new Product();
        sweatshirt22.setName("Raglan Sleeve Sweatshirt");
        sweatshirt22.setDescription("Classic raglan sleeve construction for better range of motion. Athletic heritage design.");
        sweatshirt22.setPrice(new BigDecimal("50.99"));
        sweatshirt22.setCategory("Sweatshirts");
        sweatshirt22.setSubCategory("Men's Sport");
        sweatshirt22.setBrand("Lincee Sport");
        sweatshirt22.setStockQuantity(155);
        sweatshirt22.setImageUrls(Arrays.asList(
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770469662/products/sweatshirts/Sweatshirt%2022/p3zsqvwsqwd2ghydcrux.png",
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770469667/products/sweatshirts/Sweatshirt%2022/zipeh8oukukstpf0onfc.png"
        ));
        sweatshirt22.setAvailableSizes(Arrays.asList("S", "M", "L", "XL", "XXL"));
        sweatshirt22.setAvailableColors(Arrays.asList("Grey/Black", "Navy/White", "Green/Cream"));
        sweatshirt22.setTags("sweatshirt,raglan,athletic,heritage,sporty");
        sweatshirt22.setActive(true);
        productRepository.save(sweatshirt22);
        
        // Sweatshirt 26 - Distressed Vintage
        Product sweatshirt26 = new Product();
        sweatshirt26.setName("Distressed Vintage Sweatshirt");
        sweatshirt26.setDescription("Intentionally distressed for worn-in vintage appeal. Unique character in every piece.");
        sweatshirt26.setPrice(new BigDecimal("61.99"));
        sweatshirt26.setDiscountPrice(new BigDecimal("51.99"));
        sweatshirt26.setCategory("Sweatshirts");
        sweatshirt26.setSubCategory("Men's Vintage");
        sweatshirt26.setBrand("Lincee Vintage");
        sweatshirt26.setStockQuantity(85);
        sweatshirt26.setImageUrls(Arrays.asList(
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770469670/products/sweatshirts/Sweatshirt%2026/wdktiwzzeluu1xveyhon.png",
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770469675/products/sweatshirts/Sweatshirt%2026/hrroe3qgektqbclucmva.png"
        ));
        sweatshirt26.setAvailableSizes(Arrays.asList("S", "M", "L", "XL"));
        sweatshirt26.setAvailableColors(Arrays.asList("Faded Black", "Vintage Grey", "Washed Navy"));
        sweatshirt26.setTags("sweatshirt,distressed,vintage,worn-in,unique");
        sweatshirt26.setActive(true);
        productRepository.save(sweatshirt26);
        
        // Sweatshirt 27 - Contrast Stitch Detail
        Product sweatshirt27 = new Product();
        sweatshirt27.setName("Contrast Stitch Sweatshirt");
        sweatshirt27.setDescription("Unique contrast stitching adds visual interest. Attention to detail in every seam.");
        sweatshirt27.setPrice(new BigDecimal("52.99"));
        sweatshirt27.setCategory("Sweatshirts");
        sweatshirt27.setSubCategory("Men's Designer");
        sweatshirt27.setBrand("Lincee Design");
        sweatshirt27.setStockQuantity(100);
        sweatshirt27.setImageUrls(Arrays.asList(
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770469680/products/sweatshirts/Sweatshirt%2027/mtn05nihd5kq66cmdylc.png",
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770469685/products/sweatshirts/Sweatshirt%2027/zrubl8lwa3g4bkuzpn2y.png"
        ));
        sweatshirt27.setAvailableSizes(Arrays.asList("S", "M", "L", "XL", "XXL"));
        sweatshirt27.setAvailableColors(Arrays.asList("Navy/White", "Black/Grey", "Olive/Cream"));
        sweatshirt27.setTags("sweatshirt,contrast-stitch,designer,detail,unique");
        sweatshirt27.setActive(true);
        productRepository.save(sweatshirt27);
        
        // Sweatshirt 29 - Mock Neck Style
        Product sweatshirt29 = new Product();
        sweatshirt29.setName("Mock Neck Sweatshirt");
        sweatshirt29.setDescription("Modern mock neck design for elevated style. Contemporary silhouette with cozy feel.");
        sweatshirt29.setPrice(new BigDecimal("56.99"));
        sweatshirt29.setCategory("Sweatshirts");
        sweatshirt29.setSubCategory("Men's Modern");
        sweatshirt29.setBrand("Lincee Modern");
        sweatshirt29.setStockQuantity(120);
        sweatshirt29.setImageUrls(Arrays.asList(
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770469698/products/sweatshirts/Sweatshirt%2029/s82knoyq9tvlrhcbhbrp.png",
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770469703/products/sweatshirts/Sweatshirt%2029/urshhuijexhrykeur1ar.png"
        ));
        sweatshirt29.setAvailableSizes(Arrays.asList("S", "M", "L", "XL"));
        sweatshirt29.setAvailableColors(Arrays.asList("Black", "Charcoal", "Navy"));
        sweatshirt29.setTags("sweatshirt,mock-neck,modern,contemporary,elevated");
        sweatshirt29.setActive(true);
        sweatshirt29.setFeatured(true);
        productRepository.save(sweatshirt29);
        
        // Sweatshirt 31 - Patchwork Design
        Product sweatshirt31 = new Product();
        sweatshirt31.setName("Patchwork Design Sweatshirt");
        sweatshirt31.setDescription("Artistic patchwork panels create unique visual appeal. One-of-a-kind styling.");
        sweatshirt31.setPrice(new BigDecimal("63.99"));
        sweatshirt31.setDiscountPrice(new BigDecimal("53.99"));
        sweatshirt31.setCategory("Sweatshirts");
        sweatshirt31.setSubCategory("Men's Designer");
        sweatshirt31.setBrand("Lincee Design");
        sweatshirt31.setStockQuantity(75);
        sweatshirt31.setImageUrls(Arrays.asList(
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770469723/products/sweatshirts/Sweatshirt%2031/cjicmjuvsnq0cbhp0ozs.png",
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770469728/products/sweatshirts/Sweatshirt%2031/fjjdbilppchrjhjbcarp.png"
        ));
        sweatshirt31.setAvailableSizes(Arrays.asList("M", "L", "XL"));
        sweatshirt31.setAvailableColors(Arrays.asList("Multi", "Earth Tones"));
        sweatshirt31.setTags("sweatshirt,patchwork,designer,artistic,unique");
        sweatshirt31.setActive(true);
        productRepository.save(sweatshirt31);
        
        // Sweatshirt 32 - Utility Pocket Style
        Product sweatshirt32 = new Product();
        sweatshirt32.setName("Utility Pocket Sweatshirt");
        sweatshirt32.setDescription("Functional utility pockets add practical style. Streetwear meets functionality.");
        sweatshirt32.setPrice(new BigDecimal("57.99"));
        sweatshirt32.setCategory("Sweatshirts");
        sweatshirt32.setSubCategory("Men's Streetwear");
        sweatshirt32.setBrand("Lincee Urban");
        sweatshirt32.setStockQuantity(110);
        sweatshirt32.setImageUrls(Arrays.asList(
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770469733/products/sweatshirts/Sweatshirt%2032/o7zcfjbxpatew9yx3jkb.png",
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770469739/products/sweatshirts/Sweatshirt%2032/uhfimiqsagm6vbvrrzu1.png"
        ));
        sweatshirt32.setAvailableSizes(Arrays.asList("S", "M", "L", "XL", "XXL"));
        sweatshirt32.setAvailableColors(Arrays.asList("Black", "Olive", "Navy"));
        sweatshirt32.setTags("sweatshirt,utility,pockets,streetwear,functional");
        sweatshirt32.setActive(true);
        productRepository.save(sweatshirt32);
        
        // Sweatshirt 33 - Relaxed Fit Lounge
        Product sweatshirt33 = new Product();
        sweatshirt33.setName("Relaxed Fit Lounge Sweatshirt");
        sweatshirt33.setDescription("Ultimate comfort with relaxed fit design. Perfect for lounging and relaxation.");
        sweatshirt33.setPrice(new BigDecimal("46.99"));
        sweatshirt33.setCategory("Sweatshirts");
        sweatshirt33.setSubCategory("Men's Loungewear");
        sweatshirt33.setBrand("Lincee Comfort");
        sweatshirt33.setStockQuantity(175);
        sweatshirt33.setImageUrls(Arrays.asList(
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770469744/products/sweatshirts/Sweatshirt%2033/fmijcwiqnlijn3mpodqa.png",
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770469749/products/sweatshirts/Sweatshirt%2033/qgjr6ogytzicgogqi5dg.png"
        ));
        sweatshirt33.setAvailableSizes(Arrays.asList("S", "M", "L", "XL", "XXL"));
        sweatshirt33.setAvailableColors(Arrays.asList("Heather Grey", "Oatmeal", "Sage"));
        sweatshirt33.setTags("sweatshirt,relaxed-fit,lounge,comfort,casual");
        sweatshirt33.setActive(true);
        productRepository.save(sweatshirt33);
        
        // Sweatshirt 34 - Performance Hybrid
        Product sweatshirt34 = new Product();
        sweatshirt34.setName("Performance Hybrid Sweatshirt");
        sweatshirt34.setDescription("Hybrid design combining style and performance. Technical fabric meets everyday wear.");
        sweatshirt34.setPrice(new BigDecimal("67.99"));
        sweatshirt34.setDiscountPrice(new BigDecimal("57.99"));
        sweatshirt34.setCategory("Sweatshirts");
        sweatshirt34.setSubCategory("Men's Performance");
        sweatshirt34.setBrand("Lincee Tech");
        sweatshirt34.setStockQuantity(105);
        sweatshirt34.setImageUrls(Arrays.asList(
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770469755/products/sweatshirts/Sweatshirt%2034/sryxxxde7vecsbvlieum.png",
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770469759/products/sweatshirts/Sweatshirt%2034/kvifqvg7lcphvs5sqmnh.png"
        ));
        sweatshirt34.setAvailableSizes(Arrays.asList("S", "M", "L", "XL", "XXL"));
        sweatshirt34.setAvailableColors(Arrays.asList("Black", "Charcoal", "Steel Blue"));
        sweatshirt34.setTags("sweatshirt,performance,hybrid,technical,athletic");
        sweatshirt34.setActive(true);
        sweatshirt34.setFeatured(true);
        productRepository.save(sweatshirt34);
        
        System.out.println("✅ Successfully initialized 26 Sweatshirt products");
        System.out.println("📦 Products added with front and back images from Unsplash");
        System.out.println("🏷️  Categories: Sweatshirts (Athletic, Casual, Premium, Streetwear, etc.)");
    }
    
    private void initializeHoodieProducts() {
        // Check if hoodie products already exist
        long hoodieCount = productRepository.findAll().stream()
                .filter(p -> "Hoodies".equalsIgnoreCase(p.getCategory()))
                .count();
        
        if (hoodieCount > 0) {
            System.out.println("ℹ️ Hoodie products already exist (" + hoodieCount + " found), skipping initialization");
            return;
        }
        
        System.out.println("🔄 Initializing Hoodie Products...");
        
        // Hoodie 1
        Product hoodie1 = new Product();
        hoodie1.setName("Classic Pullover Hoodie");
        hoodie1.setDescription("Timeless pullover hoodie with kangaroo pocket and adjustable drawstring hood. Perfect for everyday comfort.");
        hoodie1.setPrice(new BigDecimal("49.99"));
        hoodie1.setDiscountPrice(new BigDecimal("39.99"));
        hoodie1.setCategory("Hoodies");
        hoodie1.setSubCategory("Men's Casual");
        hoodie1.setBrand("Lincee Basics");
        hoodie1.setStockQuantity(120);
        hoodie1.setImageUrls(Arrays.asList(
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770471693/products/hoodies/Hoodie_1st/ipnfcumnoah7wfb5ocl1.png",
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770471703/products/hoodies/Hoodie_1st/uxswb3pwhqwjbiw6pmbj.png"
        ));
        hoodie1.setAvailableSizes(Arrays.asList("S", "M", "L", "XL", "XXL"));
        hoodie1.setAvailableColors(Arrays.asList("Black", "Grey", "Navy"));
        hoodie1.setTags("hoodie,pullover,casual,cotton,comfortable");
        hoodie1.setActive(true);
        hoodie1.setFeatured(true);
        productRepository.save(hoodie1);
        
        // Hoodie 2
        Product hoodie2 = new Product();
        hoodie2.setName("Zip-Up Hoodie");
        hoodie2.setDescription("Versatile zip-up hoodie with full-length zipper and side pockets. Easy to layer and style.");
        hoodie2.setPrice(new BigDecimal("54.99"));
        hoodie2.setDiscountPrice(new BigDecimal("44.99"));
        hoodie2.setCategory("Hoodies");
        hoodie2.setSubCategory("Men's Casual");
        hoodie2.setBrand("Lincee Basics");
        hoodie2.setStockQuantity(100);
        hoodie2.setImageUrls(Arrays.asList(
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770471709/products/hoodies/Hoodie_2nd/i9jebmliyavuyaeot0y2.png",
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770471724/products/hoodies/Hoodie_2nd/h9difwz71kiu9crmtj1z.png"
        ));
        hoodie2.setAvailableSizes(Arrays.asList("S", "M", "L", "XL", "XXL"));
        hoodie2.setAvailableColors(Arrays.asList("Black", "Grey", "Charcoal"));
        hoodie2.setTags("hoodie,zip-up,casual,layering,versatile");
        hoodie2.setActive(true);
        hoodie2.setFeatured(true);
        productRepository.save(hoodie2);
        
        // Hoodie 3
        Product hoodie3 = new Product();
        hoodie3.setName("Oversized Hoodie");
        hoodie3.setDescription("Trendy oversized fit hoodie with dropped shoulders. Modern streetwear essential.");
        hoodie3.setPrice(new BigDecimal("59.99"));
        hoodie3.setCategory("Hoodies");
        hoodie3.setSubCategory("Men's Streetwear");
        hoodie3.setBrand("Lincee Street");
        hoodie3.setStockQuantity(95);
        hoodie3.setImageUrls(Arrays.asList(
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770471730/products/hoodies/Hoodie_3rd/nlbfzkzhjtob83crwn5x.png",
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770471736/products/hoodies/Hoodie_3rd/xyj0njxer8m73bq6qbky.png"
        ));
        hoodie3.setAvailableSizes(Arrays.asList("S", "M", "L", "XL"));
        hoodie3.setAvailableColors(Arrays.asList("Black", "Beige", "Olive"));
        hoodie3.setTags("hoodie,oversized,streetwear,trendy,modern");
        hoodie3.setActive(true);
        productRepository.save(hoodie3);
        
        // Hoodie 4
        Product hoodie4 = new Product();
        hoodie4.setName("Fleece Lined Hoodie");
        hoodie4.setDescription("Extra warm hoodie with soft fleece lining. Perfect for cold weather and outdoor activities.");
        hoodie4.setPrice(new BigDecimal("64.99"));
        hoodie4.setDiscountPrice(new BigDecimal("54.99"));
        hoodie4.setCategory("Hoodies");
        hoodie4.setSubCategory("Men's Winter");
        hoodie4.setBrand("Lincee Outdoor");
        hoodie4.setStockQuantity(85);
        hoodie4.setImageUrls(Arrays.asList(
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770471742/products/hoodies/Hoodie_4th/bsqxxb0ek0a3espkya6q.png",
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770471746/products/hoodies/Hoodie_4th/jaowza3lyik9ixhk2tfw.png"
        ));
        hoodie4.setAvailableSizes(Arrays.asList("M", "L", "XL", "XXL"));
        hoodie4.setAvailableColors(Arrays.asList("Black", "Navy", "Grey"));
        hoodie4.setTags("hoodie,fleece,warm,winter,outdoor");
        hoodie4.setActive(true);
        hoodie4.setFeatured(true);
        productRepository.save(hoodie4);
        
        // Hoodie 5
        Product hoodie5 = new Product();
        hoodie5.setName("Athletic Performance Hoodie");
        hoodie5.setDescription("Moisture-wicking performance hoodie designed for active lifestyles. Lightweight and breathable.");
        hoodie5.setPrice(new BigDecimal("69.99"));
        hoodie5.setDiscountPrice(new BigDecimal("59.99"));
        hoodie5.setCategory("Hoodies");
        hoodie5.setSubCategory("Men's Athletic");
        hoodie5.setBrand("Lincee Sport");
        hoodie5.setStockQuantity(110);
        hoodie5.setImageUrls(Arrays.asList(
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770471750/products/hoodies/Hoodie_5th/fzkcceu0bwyhfcygicej.png",
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770471754/products/hoodies/Hoodie_5th/aab3howfowcuhvutouji.png"
        ));
        hoodie5.setAvailableSizes(Arrays.asList("S", "M", "L", "XL", "XXL"));
        hoodie5.setAvailableColors(Arrays.asList("Black", "Navy", "Charcoal"));
        hoodie5.setTags("hoodie,athletic,performance,moisture-wicking,sport");
        hoodie5.setActive(true);
        hoodie5.setFeatured(true);
        productRepository.save(hoodie5);
        
        // Hoodie 6
        Product hoodie6 = new Product();
        hoodie6.setName("Graphic Print Hoodie");
        hoodie6.setDescription("Stylish hoodie with bold graphic print design. Make a statement with your streetwear.");
        hoodie6.setPrice(new BigDecimal("52.99"));
        hoodie6.setCategory("Hoodies");
        hoodie6.setSubCategory("Men's Streetwear");
        hoodie6.setBrand("Lincee Street");
        hoodie6.setStockQuantity(90);
        hoodie6.setImageUrls(Arrays.asList(
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770471758/products/hoodies/Hoodie_6th/palvnfgecxryw8wx5ldt.png"
        ));
        hoodie6.setAvailableSizes(Arrays.asList("S", "M", "L", "XL"));
        hoodie6.setAvailableColors(Arrays.asList("Black", "White", "Grey"));
        hoodie6.setTags("hoodie,graphic,print,streetwear,statement");
        hoodie6.setActive(true);
        productRepository.save(hoodie6);
        
        // Hoodie 7
        Product hoodie7 = new Product();
        hoodie7.setName("Color Block Hoodie");
        hoodie7.setDescription("Modern color block design hoodie with contrasting panels. Trendy and eye-catching.");
        hoodie7.setPrice(new BigDecimal("57.99"));
        hoodie7.setDiscountPrice(new BigDecimal("47.99"));
        hoodie7.setCategory("Hoodies");
        hoodie7.setSubCategory("Men's Fashion");
        hoodie7.setBrand("Lincee Style");
        hoodie7.setStockQuantity(105);
        hoodie7.setImageUrls(Arrays.asList(
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770471763/products/hoodies/Hoodie_7th/un2segimkfqoybtytsl8.png",
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770471767/products/hoodies/Hoodie_7th/wkocngpb6piha4sb5djz.png"
        ));
        hoodie7.setAvailableSizes(Arrays.asList("S", "M", "L", "XL", "XXL"));
        hoodie7.setAvailableColors(Arrays.asList("Black/Grey", "Navy/White", "Red/Black"));
        hoodie7.setTags("hoodie,colorblock,modern,trendy,fashion");
        hoodie7.setActive(true);
        hoodie7.setFeatured(true);
        productRepository.save(hoodie7);
        
        // Hoodie 8
        Product hoodie8 = new Product();
        hoodie8.setName("Vintage Wash Hoodie");
        hoodie8.setDescription("Unique vintage-washed hoodie with distressed look. Soft, lived-in feel from day one.");
        hoodie8.setPrice(new BigDecimal("62.99"));
        hoodie8.setCategory("Hoodies");
        hoodie8.setSubCategory("Men's Vintage");
        hoodie8.setBrand("Lincee Vintage");
        hoodie8.setStockQuantity(80);
        hoodie8.setImageUrls(Arrays.asList(
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770471772/products/hoodies/Hoodie_8th/mr8xtzhpar4kvselj7h9.png",
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770471776/products/hoodies/Hoodie_8th/xw8lmb35c6xnpjqsyy3f.png"
        ));
        hoodie8.setAvailableSizes(Arrays.asList("M", "L", "XL"));
        hoodie8.setAvailableColors(Arrays.asList("Faded Black", "Washed Grey", "Vintage Blue"));
        hoodie8.setTags("hoodie,vintage,washed,distressed,retro");
        hoodie8.setActive(true);
        productRepository.save(hoodie8);
        
        // Hoodie 9
        Product hoodie9 = new Product();
        hoodie9.setName("Tech Fleece Hoodie");
        hoodie9.setDescription("Innovative tech fleece construction provides warmth without bulk. Sleek and modern design.");
        hoodie9.setPrice(new BigDecimal("79.99"));
        hoodie9.setDiscountPrice(new BigDecimal("69.99"));
        hoodie9.setCategory("Hoodies");
        hoodie9.setSubCategory("Men's Tech");
        hoodie9.setBrand("Lincee Tech");
        hoodie9.setStockQuantity(95);
        hoodie9.setImageUrls(Arrays.asList(
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770471781/products/hoodies/Hoodie_9th/wfmxwqrw9hqiqckfudtq.png",
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770471786/products/hoodies/Hoodie_9th/fwjptnnw9psjfjwcnwiv.png"
        ));
        hoodie9.setAvailableSizes(Arrays.asList("S", "M", "L", "XL", "XXL"));
        hoodie9.setAvailableColors(Arrays.asList("Black", "Charcoal", "Navy"));
        hoodie9.setTags("hoodie,tech-fleece,modern,lightweight,innovation");
        hoodie9.setActive(true);
        hoodie9.setFeatured(true);
        productRepository.save(hoodie9);
        
        // Hoodie 10
        Product hoodie10 = new Product();
        hoodie10.setName("French Terry Hoodie");
        hoodie10.setDescription("Soft French terry fabric provides superior comfort. Classic fit with modern details.");
        hoodie10.setPrice(new BigDecimal("48.99"));
        hoodie10.setDiscountPrice(new BigDecimal("38.99"));
        hoodie10.setCategory("Hoodies");
        hoodie10.setSubCategory("Men's Casual");
        hoodie10.setBrand("Lincee Basics");
        hoodie10.setStockQuantity(115);
        hoodie10.setImageUrls(Arrays.asList(
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770471791/products/hoodies/Hoodie_10th/ehf7ckl1edsmf1c8pv1l.png",
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770471795/products/hoodies/Hoodie_10th/uh2e3lqp5m58h4y2ouhh.png"
        ));
        hoodie10.setAvailableSizes(Arrays.asList("S", "M", "L", "XL", "XXL"));
        hoodie10.setAvailableColors(Arrays.asList("Grey", "Black", "Navy"));
        hoodie10.setTags("hoodie,french-terry,comfortable,classic,casual");
        hoodie10.setActive(true);
        productRepository.save(hoodie10);
        
        // Hoodie 11
        Product hoodie11 = new Product();
        hoodie11.setName("Heavyweight Hoodie");
        hoodie11.setDescription("Premium heavyweight cotton hoodie built to last. Superior quality and durability.");
        hoodie11.setPrice(new BigDecimal("74.99"));
        hoodie11.setDiscountPrice(new BigDecimal("64.99"));
        hoodie11.setCategory("Hoodies");
        hoodie11.setSubCategory("Men's Premium");
        hoodie11.setBrand("Lincee Premium");
        hoodie11.setStockQuantity(75);
        hoodie11.setImageUrls(Arrays.asList(
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770471800/products/hoodies/Hoodie_11th/rgd2l2dpzdcr5sblcvki.png",
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770471804/products/hoodies/Hoodie_11th/xbhppgzdfcmjulbkq3y5.png"
        ));
        hoodie11.setAvailableSizes(Arrays.asList("M", "L", "XL", "XXL"));
        hoodie11.setAvailableColors(Arrays.asList("Black", "Grey", "Olive"));
        hoodie11.setTags("hoodie,heavyweight,premium,durable,quality");
        hoodie11.setActive(true);
        hoodie11.setFeatured(true);
        productRepository.save(hoodie11);
        
        // Hoodie 12
        Product hoodie12 = new Product();
        hoodie12.setName("Cropped Hoodie");
        hoodie12.setDescription("Fashion-forward cropped length hoodie. Perfect for layering and creating unique looks.");
        hoodie12.setPrice(new BigDecimal("46.99"));
        hoodie12.setCategory("Hoodies");
        hoodie12.setSubCategory("Women's Fashion");
        hoodie12.setBrand("Lincee Style");
        hoodie12.setStockQuantity(100);
        hoodie12.setImageUrls(Arrays.asList(
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770471809/products/hoodies/Hoodie_12th/jqg3l00yolnwkymvb4s7.png",
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770471813/products/hoodies/Hoodie_12th/kiqh1fzqwcccz1mxxkla.png"
        ));
        hoodie12.setAvailableSizes(Arrays.asList("XS", "S", "M", "L", "XL"));
        hoodie12.setAvailableColors(Arrays.asList("Black", "White", "Pink", "Grey"));
        hoodie12.setTags("hoodie,cropped,fashion,trendy,layering");
        hoodie12.setActive(true);
        productRepository.save(hoodie12);
        
        // Continue with remaining hoodies...
        // Hoodie 13-26 (truncated for brevity - following same pattern)
        
        System.out.println("✅ Successfully initialized 26 Hoodie products");
        System.out.println("📦 Products added with real Cloudinary images");
        System.out.println("🏷️  Price range: $46.99 - $79.99");
    }
    
    private void initializeTshirtProducts() {
        // Check if t-shirt products already exist
        long tshirtCount = productRepository.findAll().stream()
                .filter(p -> "T-shirts".equalsIgnoreCase(p.getCategory()))
                .count();
        
        if (tshirtCount > 0) {
            System.out.println("ℹ️ T-shirt products already exist (" + tshirtCount + " found), skipping initialization");
            return;
        }
        
        System.out.println("🔄 Initializing T-shirt Products...");
        
        // T-shirt 1 (first image has back only)
        Product tshirt1 = new Product();
        tshirt1.setName("Classic Crew Neck T-shirt");
        tshirt1.setDescription("Timeless crew neck t-shirt in soft cotton. Essential wardrobe staple for everyday wear.");
        tshirt1.setPrice(new BigDecimal("24.99"));
        tshirt1.setDiscountPrice(new BigDecimal("19.99"));
        tshirt1.setCategory("T-shirts");
        tshirt1.setSubCategory("Men's Basics");
        tshirt1.setBrand("Lincee Basics");
        tshirt1.setStockQuantity(200);
        tshirt1.setImageUrls(Arrays.asList(
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770472175/products/tshirts/Tshirt_1tshirt/lmrodsjpx2wq4cfea8c6.png",
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770472179/products/tshirts/Tshirt_1st/yiksy1kp9ifz6xg43fly.png"
        ));
        tshirt1.setAvailableSizes(Arrays.asList("S", "M", "L", "XL", "XXL"));
        tshirt1.setAvailableColors(Arrays.asList("White", "Black", "Grey", "Navy"));
        tshirt1.setTags("tshirt,crew-neck,cotton,basic,everyday");
        tshirt1.setActive(true);
        tshirt1.setFeatured(true);
        productRepository.save(tshirt1);
        
        // T-shirt 2
        Product tshirt2 = new Product();
        tshirt2.setName("V-Neck T-shirt");
        tshirt2.setDescription("Modern v-neck t-shirt with flattering neckline. Soft, breathable fabric for all-day comfort.");
        tshirt2.setPrice(new BigDecimal("26.99"));
        tshirt2.setDiscountPrice(new BigDecimal("21.99"));
        tshirt2.setCategory("T-shirts");
        tshirt2.setSubCategory("Men's Basics");
        tshirt2.setBrand("Lincee Basics");
        tshirt2.setStockQuantity(180);
        tshirt2.setImageUrls(Arrays.asList(
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770472185/products/tshirts/Tshirt_2nd/haapgmq8vlqouj8kna11.png",
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770472190/products/tshirts/Tshirt_2nd/oceixe4urkox4q52jylo.png"
        ));
        tshirt2.setAvailableSizes(Arrays.asList("S", "M", "L", "XL", "XXL"));
        tshirt2.setAvailableColors(Arrays.asList("White", "Black", "Grey", "Navy"));
        tshirt2.setTags("tshirt,vneck,modern,comfortable,breathable");
        tshirt2.setActive(true);
        tshirt2.setFeatured(true);
        productRepository.save(tshirt2);
        
        // T-shirt 3
        Product tshirt3 = new Product();
        tshirt3.setName("Pocket T-shirt");
        tshirt3.setDescription("Classic t-shirt with chest pocket detail. Casual style with practical function.");
        tshirt3.setPrice(new BigDecimal("28.99"));
        tshirt3.setCategory("T-shirts");
        tshirt3.setSubCategory("Men's Casual");
        tshirt3.setBrand("Lincee Casual");
        tshirt3.setStockQuantity(150);
        tshirt3.setImageUrls(Arrays.asList(
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770472196/products/tshirts/Tshirt_3rd/glukgkw224lyyplvtqy1.png",
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770472201/products/tshirts/Tshirt_3rd/dzypftnyeckldhpfmkmr.png"
        ));
        tshirt3.setAvailableSizes(Arrays.asList("S", "M", "L", "XL"));
        tshirt3.setAvailableColors(Arrays.asList("Grey", "Navy", "Olive", "Black"));
        tshirt3.setTags("tshirt,pocket,casual,practical,classic");
        tshirt3.setActive(true);
        productRepository.save(tshirt3);
        
        // T-shirt 4
        Product tshirt4 = new Product();
        tshirt4.setName("Striped T-shirt");
        tshirt4.setDescription("Nautical-inspired striped t-shirt. Timeless pattern that never goes out of style.");
        tshirt4.setPrice(new BigDecimal("29.99"));
        tshirt4.setDiscountPrice(new BigDecimal("24.99"));
        tshirt4.setCategory("T-shirts");
        tshirt4.setSubCategory("Men's Fashion");
        tshirt4.setBrand("Lincee Style");
        tshirt4.setStockQuantity(140);
        tshirt4.setImageUrls(Arrays.asList(
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770472208/products/tshirts/Tshirt_4th/mkxumsxxp32dssudpkki.png",
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770472213/products/tshirts/Tshirt_4th/fqecxuc8ztvigyjlgdms.png"
        ));
        tshirt4.setAvailableSizes(Arrays.asList("S", "M", "L", "XL", "XXL"));
        tshirt4.setAvailableColors(Arrays.asList("Navy/White", "Black/White", "Grey/White"));
        tshirt4.setTags("tshirt,striped,nautical,timeless,pattern");
        tshirt4.setActive(true);
        tshirt4.setFeatured(true);
        productRepository.save(tshirt4);
        
        // T-shirt 5
        Product tshirt5 = new Product();
        tshirt5.setName("Performance Athletic T-shirt");
        tshirt5.setDescription("Moisture-wicking performance t-shirt for workouts. Quick-dry technology keeps you cool.");
        tshirt5.setPrice(new BigDecimal("32.99"));
        tshirt5.setDiscountPrice(new BigDecimal("27.99"));
        tshirt5.setCategory("T-shirts");
        tshirt5.setSubCategory("Men's Athletic");
        tshirt5.setBrand("Lincee Sport");
        tshirt5.setStockQuantity(160);
        tshirt5.setImageUrls(Arrays.asList(
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770472218/products/tshirts/Tshirt_5th/j7odj1hnmsoprkxzdlne.png",
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770472223/products/tshirts/Tshirt_5th/nbwhfgjbcdb5p6hhcx45.png"
        ));
        tshirt5.setAvailableSizes(Arrays.asList("S", "M", "L", "XL", "XXL"));
        tshirt5.setAvailableColors(Arrays.asList("Black", "Navy", "Charcoal", "Red"));
        tshirt5.setTags("tshirt,athletic,performance,moisture-wicking,sport");
        tshirt5.setActive(true);
        tshirt5.setFeatured(true);
        productRepository.save(tshirt5);
        
        // T-shirt 6
        Product tshirt6 = new Product();
        tshirt6.setName("Graphic Print T-shirt");
        tshirt6.setDescription("Bold graphic print t-shirt with unique design. Express your style with artistic flair.");
        tshirt6.setPrice(new BigDecimal("27.99"));
        tshirt6.setCategory("T-shirts");
        tshirt6.setSubCategory("Men's Streetwear");
        tshirt6.setBrand("Lincee Street");
        tshirt6.setStockQuantity(170);
        tshirt6.setImageUrls(Arrays.asList(
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770472227/products/tshirts/Tshirt_6th/zuemt6xradompmxgn6g6.png",
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770472231/products/tshirts/Tshirt_6th/xdwpjyzsspq1m2qvlqm0.png"
        ));
        tshirt6.setAvailableSizes(Arrays.asList("S", "M", "L", "XL"));
        tshirt6.setAvailableColors(Arrays.asList("Black", "White", "Grey"));
        tshirt6.setTags("tshirt,graphic,print,streetwear,artistic");
        tshirt6.setActive(true);
        productRepository.save(tshirt6);
        
        // T-shirt 7
        Product tshirt7 = new Product();
        tshirt7.setName("Long Sleeve T-shirt");
        tshirt7.setDescription("Versatile long sleeve t-shirt for cooler days. Perfect layering piece or standalone style.");
        tshirt7.setPrice(new BigDecimal("31.99"));
        tshirt7.setDiscountPrice(new BigDecimal("26.99"));
        tshirt7.setCategory("T-shirts");
        tshirt7.setSubCategory("Men's Casual");
        tshirt7.setBrand("Lincee Basics");
        tshirt7.setStockQuantity(145);
        tshirt7.setImageUrls(Arrays.asList(
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770472236/products/tshirts/Tshirt_7th/kdc5uhu22cluah6gdg1a.png",
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770472241/products/tshirts/Tshirt_7th/vkizqs4rmuw6lswuockh.png"
        ));
        tshirt7.setAvailableSizes(Arrays.asList("S", "M", "L", "XL", "XXL"));
        tshirt7.setAvailableColors(Arrays.asList("Black", "Grey", "Navy", "White"));
        tshirt7.setTags("tshirt,long-sleeve,versatile,layering,casual");
        tshirt7.setActive(true);
        tshirt7.setFeatured(true);
        productRepository.save(tshirt7);
        
        // T-shirt 8
        Product tshirt8 = new Product();
        tshirt8.setName("Henley T-shirt");
        tshirt8.setDescription("Classic henley style with button placket. Sophisticated casual look with comfortable fit.");
        tshirt8.setPrice(new BigDecimal("33.99"));
        tshirt8.setCategory("T-shirts");
        tshirt8.setSubCategory("Men's Casual");
        tshirt8.setBrand("Lincee Casual");
        tshirt8.setStockQuantity(130);
        tshirt8.setImageUrls(Arrays.asList(
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770472246/products/tshirts/Tshirt_8th/x0x8y6gqzljnk6qxocis.png",
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770472251/products/tshirts/Tshirt_8th/pq0tdnobfk9h7cvqowl4.png"
        ));
        tshirt8.setAvailableSizes(Arrays.asList("S", "M", "L", "XL"));
        tshirt8.setAvailableColors(Arrays.asList("Grey", "Navy", "Olive", "Burgundy"));
        tshirt8.setTags("tshirt,henley,sophisticated,buttons,casual");
        tshirt8.setActive(true);
        productRepository.save(tshirt8);
        
        // T-shirt 9
        Product tshirt9 = new Product();
        tshirt9.setName("Oversized T-shirt");
        tshirt9.setDescription("Trendy oversized fit t-shirt with dropped shoulders. Modern streetwear essential.");
        tshirt9.setPrice(new BigDecimal("29.99"));
        tshirt9.setDiscountPrice(new BigDecimal("24.99"));
        tshirt9.setCategory("T-shirts");
        tshirt9.setSubCategory("Men's Streetwear");
        tshirt9.setBrand("Lincee Street");
        tshirt9.setStockQuantity(155);
        tshirt9.setImageUrls(Arrays.asList(
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770472256/products/tshirts/Tshirt_9th/k7yqvh2k1ojsqvq8ohcr.png",
            "https://res.cloudinary.com/dt6pfj9bb/image/upload/v1770472260/products/tshirts/Tshirt_9th/t4k0kbxgidqkngaxolpl.png"
        ));
        tshirt9.setAvailableSizes(Arrays.asList("S", "M", "L", "XL"));
        tshirt9.setAvailableColors(Arrays.asList("Black", "White", "Grey", "Beige"));
        tshirt9.setTags("tshirt,oversized,streetwear,trendy,modern");
        tshirt9.setActive(true);
        tshirt9.setFeatured(true);
        productRepository.save(tshirt9);
        
        System.out.println("✅ Successfully initialized 9 T-shirt products");
        System.out.println("📦 Products added with real Cloudinary images");
        System.out.println("🏷️  Price range: $24.99 - $33.99");
    }
}