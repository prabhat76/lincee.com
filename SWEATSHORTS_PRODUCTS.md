# Sweatshorts Products Added to Database

## Summary

Added 6 sweatshorts products to the database initialization with front and back images for each product.

## Products Added

### 1. Classic Grey Sweatshorts
- **Price**: $29.99 (Discount: $24.99)
- **Category**: Sweatshorts
- **Sub-Category**: Men's Activewear
- **Brand**: Lincee Sport
- **Stock**: 150 units
- **Sizes**: S, M, L, XL, XXL
- **Colors**: Grey, Charcoal
- **Images**: 
  - `/images/sweatshorts/classic-grey-front.jpg`
  - `/images/sweatshorts/classic-grey-back.jpg`
- **Features**: Featured product
- **Tags**: sweatshorts, athletic, grey, cotton, activewear

### 2. Black Athletic Sweatshorts
- **Price**: $34.99 (Discount: $27.99)
- **Category**: Sweatshorts
- **Sub-Category**: Men's Activewear
- **Brand**: Lincee Sport
- **Stock**: 120 units
- **Sizes**: S, M, L, XL, XXL
- **Colors**: Black, Navy
- **Images**:
  - `/images/sweatshorts/black-athletic-front.jpg`
  - `/images/sweatshorts/black-athletic-back.jpg`
- **Features**: Featured product
- **Tags**: sweatshorts, athletic, black, moisture-wicking, training

### 3. Navy Blue Sweatshorts
- **Price**: $32.99
- **Category**: Sweatshorts
- **Sub-Category**: Men's Activewear
- **Brand**: Lincee Sport
- **Stock**: 100 units
- **Sizes**: S, M, L, XL
- **Colors**: Navy, Royal Blue
- **Images**:
  - `/images/sweatshorts/navy-blue-front.jpg`
  - `/images/sweatshorts/navy-blue-back.jpg`
- **Tags**: sweatshorts, navy, modern-fit, gym, running

### 4. Olive Green Sweatshorts
- **Price**: $31.99 (Discount: $25.99)
- **Category**: Sweatshorts
- **Sub-Category**: Men's Casual
- **Brand**: Lincee Basics
- **Stock**: 80 units
- **Sizes**: M, L, XL, XXL
- **Colors**: Olive, Army Green
- **Images**:
  - `/images/sweatshorts/olive-green-front.jpg`
  - `/images/sweatshorts/olive-green-back.jpg`
- **Tags**: sweatshorts, olive, french-terry, casual, comfortable

### 5. Maroon Sweatshorts
- **Price**: $28.99
- **Category**: Sweatshorts
- **Sub-Category**: Men's Casual
- **Brand**: Lincee Basics
- **Stock**: 90 units
- **Sizes**: S, M, L, XL
- **Colors**: Maroon, Burgundy
- **Images**:
  - `/images/sweatshorts/maroon-front.jpg`
  - `/images/sweatshorts/maroon-back.jpg`
- **Tags**: sweatshorts, maroon, tapered-fit, casual, quality

### 6. Light Grey Sweatshorts
- **Price**: $27.99 (Discount: $22.99)
- **Category**: Sweatshorts
- **Sub-Category**: Men's Summer Collection
- **Brand**: Lincee Sport
- **Stock**: 110 units
- **Sizes**: S, M, L, XL, XXL
- **Colors**: Light Grey, Heather Grey
- **Images**:
  - `/images/sweatshorts/light-grey-front.jpg`
  - `/images/sweatshorts/light-grey-back.jpg`
- **Features**: Featured product
- **Tags**: sweatshorts, summer, lightweight, breathable, mesh

## Technical Details

### File Modified
- `src/main/java/com/lincee/service/DataInitService.java`

### Features Implemented
1. **Automatic Initialization**: Products are automatically added when the application starts if the database is empty
2. **Front and Back Images**: Each product has two images (front and back views)
3. **Multiple Sizes and Colors**: All products have size and color variations
4. **Tags for Search**: Each product has searchable tags
5. **Stock Management**: Initial stock quantities set for inventory
6. **Featured Products**: Some products marked as featured for homepage display
7. **Discounted Pricing**: Several products have discount pricing

### Database Fields Populated
- name
- description
- price
- discountPrice (where applicable)
- category ("Sweatshorts")
- subCategory (Men's Activewear, Men's Casual, Men's Summer Collection)
- brand (Lincee Sport, Lincee Basics)
- stockQuantity
- imageUrls (array with front and back images)
- availableSizes (array)
- availableColors (array)
- tags
- active (true)
- featured (true for products 1, 2, and 6)

## API Endpoints to Access Products

### Get All Products
```bash
GET /api/v1/products
```

### Get Products by Category
```bash
GET /api/v1/products?category=Sweatshorts
```

### Get Featured Products
```bash
GET /api/v1/products/featured
```

### Search Products
```bash
GET /api/v1/products/search?query=sweatshorts
```

## Next Steps

1. **Add Actual Images**: Place actual product images in the `/images/sweatshorts/` directory
2. **Deploy to Railway**: Push the updated code to Railway
3. **Test the API**: Use the endpoints above to verify products are accessible
4. **Update Frontend**: Connect the frontend to display these products

## Testing

After starting the application, you should see these log messages:
```
✅ Admin user created: admin@lincee.com / password123
✅ Customer user created: customer@example.com / password123
🔄 Initializing Sweatshorts Products...
✅ Successfully initialized 6 Sweatshorts products
📦 Products added with front and back images
🏷️  Categories: Sweatshorts (Men's Activewear, Casual, Summer Collection)
```

## Total Inventory
- 6 unique products
- 3 featured products
- 650 total units in stock
- Price range: $27.99 - $34.99
- 2 brands: Lincee Sport, Lincee Basics
- 3 sub-categories: Men's Activewear, Men's Casual, Men's Summer Collection
