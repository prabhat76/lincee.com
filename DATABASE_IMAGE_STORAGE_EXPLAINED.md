# How Product Images Are Stored in Database

## The Problem You're Seeing ❌

You looked at the `products` table and didn't see an `image_url` column, right?

**That's correct!** The images are NOT stored in the `products` table.

## How It Actually Works ✅

Your product images use `@ElementCollection`, which creates a **separate table** to store multiple images per product.

### Database Structure:

```
┌─────────────────────────────┐
│     PRODUCTS TABLE          │
├─────────────────────────────┤
│ id (Primary Key)            │
│ name                        │
│ description                 │
│ price                       │
│ category                    │
│ brand                       │
│ stock_quantity              │
│ ...                         │
└─────────────────────────────┘
           │
           │ (one-to-many)
           ▼
┌─────────────────────────────┐
│   PRODUCT_IMAGES TABLE      │  ← Images stored HERE!
├─────────────────────────────┤
│ product_id (Foreign Key)    │
│ image_url                   │
└─────────────────────────────┘
```

### Example Data:

**products table:**
| id | name | price | category |
|----|------|-------|----------|
| 3 | Classic Grey Sweatshorts | 39.99 | Sweatshorts |
| 4 | Black Athletic Sweatshorts | 44.99 | Sweatshorts |

**product_images table:**
| product_id | image_url |
|------------|-----------|
| 3 | https://images.unsplash.com/photo-1591195853828...?w=800 |
| 3 | https://images.unsplash.com/photo-1574629810360...?w=800 |
| 4 | https://images.unsplash.com/photo-1556821840...?w=800 |
| 4 | https://images.unsplash.com/photo-1574629810360...?w=800 |

Notice how **product_id = 3** has **2 rows** in `product_images` (front and back images).

## Why This Design?

### ✅ Advantages:
1. **Multiple images per product** (front, back, side views)
2. **Flexible** - can have 1, 2, 5, or 10 images
3. **Clean separation** - images are separate from product data
4. **Easy to query** - JPA handles the joins automatically

### How JPA Does It:

**In Product.java:**
```java
@ElementCollection
@CollectionTable(name = "product_images", joinColumns = @JoinColumn(name = "product_id"))
@Column(name = "image_url")
private List<String> imageUrls = new ArrayList<>();
```

This tells Hibernate:
- Create a table called `product_images`
- Link it to products via `product_id` foreign key
- Store each URL in an `image_url` column
- Return all images as a `List<String>` when I fetch a product

## How to Query Both Tables

### Option 1: Using JPA (Automatic)
```java
Product product = productRepository.findById(3L);
List<String> images = product.getImageUrls(); 
// JPA automatically joins and fetches from product_images
```

### Option 2: Using SQL (Manual)
```sql
-- Get product with all images
SELECT 
    p.id,
    p.name,
    p.price,
    pi.image_url
FROM products p
LEFT JOIN product_images pi ON p.id = pi.product_id
WHERE p.id = 3;
```

Result:
| id | name | price | image_url |
|----|------|-------|-----------|
| 3 | Classic Grey | 39.99 | image1.jpg |
| 3 | Classic Grey | 39.99 | image2.jpg |

### Option 3: Using psql (Check database directly)
```bash
# Connect to your Neon database
psql "postgresql://neondb_owner:npg_8HDt2LTngcGa@ep-morning-credit-ahu0h3sm-pooler.c-3.us-east-1.aws.neon.tech/neondb?sslmode=require"

# List all tables
\dt

# You'll see:
# - products
# - product_images ← HERE!
# - product_sizes
# - product_colors
# - users
# - orders
# - etc.

# See product_images structure
\d product_images

# Query images
SELECT * FROM product_images WHERE product_id = 3;
```

## Currently in Your Database

Since you just updated the code but haven't restarted the app, your database still has:

```sql
SELECT pi.product_id, pi.image_url 
FROM product_images pi 
WHERE product_id IN (3,4,5,6,7,8);
```

**Old URLs (currently stored):**
```
product_id | image_url
-----------+------------------------------------------
3          | /images/sweatshorts/classic-grey-front.jpg
3          | /images/sweatshorts/classic-grey-back.jpg
4          | /images/sweatshorts/black-front.jpg
4          | /images/sweatshorts/black-back.jpg
...
```

## After You Restart (New URLs)

When you restart the application, `DataInitService` will run again and the images will be updated to:

```
product_id | image_url
-----------+------------------------------------------------------------------
3          | https://images.unsplash.com/photo-1591195853828...?w=800&h=800
3          | https://images.unsplash.com/photo-1574629810360...?w=800&h=800
4          | https://images.unsplash.com/photo-1556821840...?w=800&h=800
4          | https://images.unsplash.com/photo-1574629810360...?w=800&h=800
...
```

## Other @ElementCollection Tables

Your Product entity has 3 collection tables:

1. **product_images** - stores image URLs
   ```java
   @ElementCollection
   @CollectionTable(name = "product_images")
   private List<String> imageUrls;
   ```

2. **product_sizes** - stores available sizes (S, M, L, XL)
   ```java
   @ElementCollection
   @CollectionTable(name = "product_sizes")
   private List<String> availableSizes;
   ```

3. **product_colors** - stores available colors
   ```java
   @ElementCollection
   @CollectionTable(name = "product_colors")
   private List<String> availableColors;
   ```

## Alternative: Single Column Approach

If you wanted images in ONE column (not recommended), you could:

### Option A: JSON Column
```java
@Column(columnDefinition = "jsonb")
private String imageUrls; // Store as JSON: ["url1", "url2"]
```

### Option B: Comma-Separated
```java
@Column
private String imageUrls; // Store as: "url1,url2,url3"
```

### Why We DON'T Do This:
- ❌ Hard to query specific images
- ❌ No database constraints
- ❌ Manual parsing required
- ❌ Not normalized (database design best practice)

## Summary

**Your images ARE in the database**, just not where you expected!

- ✅ **products** table = product info (name, price, etc.)
- ✅ **product_images** table = image URLs
- ✅ JPA automatically joins them
- ✅ Your API returns images as part of the product object

**To verify:**
```bash
# Restart app to load new Unsplash URLs
./mvnw spring-boot:run

# Check product with images
curl 'http://localhost:8080/api/v1/products/3' | jq '.imageUrls'
```

You'll see the images right there in the JSON response! 🎉
