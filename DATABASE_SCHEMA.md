# Database Schema & Table Structure

## Overview

The Lincee E-Commerce Platform uses PostgreSQL with automatic DDL (Data Definition Language) management through Hibernate JPA. All tables are created and updated automatically when the application starts.

---

## Database Configuration

```properties
# Database: PostgreSQL (Neon)
spring.datasource.url=jdbc:postgresql://ep-morning-credit-ahu0h3sm-pooler.c-3.us-east-1.aws.neon.tech/neondb?sslmode=require
spring.datasource.driver-class-name=org.postgresql.Driver

# DDL Auto Mode: UPDATE (creates tables if not exist, updates if schema changes)
spring.jpa.hibernate.ddl-auto=update
spring.jpa.generate-ddl=true

# Connection Pool: HikariCP
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5
```

---

## Complete Table Structure

### 1. **users** Table
Primary table for user accounts and authentication.

| Column | Type | Constraints | Description |
|--------|------|-----------|-------------|
| id | BIGSERIAL | PRIMARY KEY, AUTO_INCREMENT | Unique user identifier |
| username | VARCHAR(50) | UNIQUE, NOT NULL | Login username |
| email | VARCHAR(255) | UNIQUE, NOT NULL | User email address |
| password | VARCHAR(255) | NOT NULL | Hashed password |
| first_name | VARCHAR(100) | - | User's first name |
| last_name | VARCHAR(100) | - | User's last name |
| phone_number | VARCHAR(20) | - | Contact phone number |
| role | VARCHAR(20) | NOT NULL | ENUM: CUSTOMER, ADMIN, MODERATOR |
| active | BOOLEAN | NOT NULL, DEFAULT true | Account active status |
| created_at | TIMESTAMP | NOT NULL | Account creation timestamp |
| updated_at | TIMESTAMP | NOT NULL | Last update timestamp |

**Indexes:**
- `idx_user_email` on (email)
- `idx_user_username` on (username)

**Relationships:**
- 1:1 with Cart (user_id)
- 1:N with Order (user_id)
- 1:N with Address (user_id)
- 1:N with Review (user_id)

---

### 2. **products** Table
Catalog of all available products.

| Column | Type | Constraints | Description |
|--------|------|-----------|-------------|
| id | BIGSERIAL | PRIMARY KEY, AUTO_INCREMENT | Product identifier |
| name | VARCHAR(100) | NOT NULL | Product name |
| description | TEXT | - | Product description |
| price | NUMERIC(10,2) | NOT NULL | Base price |
| discount_price | NUMERIC(10,2) | - | Discounted price |
| category | VARCHAR(100) | NOT NULL | Product category |
| sub_category | VARCHAR(100) | - | Sub-category |
| brand | VARCHAR(100) | NOT NULL | Brand name |
| stock_quantity | INTEGER | NOT NULL | Available stock |
| min_stock_level | INTEGER | DEFAULT 5 | Low stock alert level |
| active | BOOLEAN | NOT NULL, DEFAULT true | Product availability |
| featured | BOOLEAN | NOT NULL, DEFAULT false | Featured product flag |
| weight_grams | INTEGER | - | Product weight |
| tags | TEXT | - | Comma-separated tags |
| created_at | TIMESTAMP | NOT NULL | Creation timestamp |
| updated_at | TIMESTAMP | NOT NULL | Last update timestamp |

**Indexes:**
- `idx_product_name` on (name)
- `idx_product_category` on (category)
- `idx_product_price` on (price)

**Relationships:**
- 1:N with OrderItem (product_id)
- 1:N with CartItem (product_id)
- 1:N with Review (product_id)

---

### 3. **product_sizes** Table
ElementCollection table for available sizes per product.

| Column | Type | Constraints | Description |
|--------|------|-----------|-------------|
| product_id | BIGINT | FOREIGN KEY, NOT NULL | Reference to products |
| size | VARCHAR(50) | - | Size value (S, M, L, XL, etc.) |

---

### 4. **product_colors** Table
ElementCollection table for available colors per product.

| Column | Type | Constraints | Description |
|--------|------|-----------|-------------|
| product_id | BIGINT | FOREIGN KEY, NOT NULL | Reference to products |
| color | VARCHAR(50) | - | Color value (Black, White, Red, etc.) |

---

### 5. **product_images** Table
ElementCollection table for product images.

| Column | Type | Constraints | Description |
|--------|------|-----------|-------------|
| product_id | BIGINT | FOREIGN KEY, NOT NULL | Reference to products |
| image_url | TEXT | - | Image URL |

---

### 6. **carts** Table
Shopping cart for each user (1:1 relationship).

| Column | Type | Constraints | Description |
|--------|------|-----------|-------------|
| id | BIGSERIAL | PRIMARY KEY, AUTO_INCREMENT | Cart identifier |
| user_id | BIGINT | UNIQUE, FOREIGN KEY, NOT NULL | Reference to users |
| total_price | NUMERIC(12,2) | DEFAULT 0 | Total cart value |
| item_count | INTEGER | DEFAULT 0 | Number of items |
| created_at | TIMESTAMP | NOT NULL | Creation timestamp |
| updated_at | TIMESTAMP | NOT NULL | Last update timestamp |

**Indexes:**
- `idx_cart_user_id` on (user_id)
- `idx_cart_updated_at` on (updated_at)

**Relationships:**
- M:1 with User (user_id) - UNIQUE
- 1:N with CartItem (cart_id)

---

### 7. **cart_items** Table
Individual items in shopping carts.

| Column | Type | Constraints | Description |
|--------|------|-----------|-------------|
| id | BIGSERIAL | PRIMARY KEY, AUTO_INCREMENT | Cart item identifier |
| cart_id | BIGINT | FOREIGN KEY, NOT NULL | Reference to carts |
| product_id | BIGINT | FOREIGN KEY, NOT NULL | Reference to products |
| quantity | INTEGER | NOT NULL, MIN 1 | Quantity ordered |
| unit_price | NUMERIC(10,2) | NOT NULL | Price per unit |
| size | VARCHAR(50) | - | Selected size |
| color | VARCHAR(50) | - | Selected color |
| created_at | TIMESTAMP | NOT NULL | Addition timestamp |

**Indexes:**
- `idx_cart_item_cart_id` on (cart_id)
- `idx_cart_item_product_id` on (product_id)

**Relationships:**
- M:1 with Cart (cart_id)
- M:1 with Product (product_id)

---

### 8. **orders** Table
Customer orders with complete lifecycle management.

| Column | Type | Constraints | Description |
|--------|------|-----------|-------------|
| id | BIGSERIAL | PRIMARY KEY, AUTO_INCREMENT | Order identifier |
| user_id | BIGINT | FOREIGN KEY, NOT NULL | Reference to users |
| order_number | VARCHAR(100) | UNIQUE, NOT NULL | Unique order number (ORD-timestamp-uuid) |
| status | VARCHAR(50) | NOT NULL | ENUM: PENDING, CONFIRMED, PROCESSING, SHIPPED, DELIVERED, CANCELLED, RETURNED |
| total_amount | NUMERIC(12,2) | NOT NULL | Total order amount |
| discount_amount | NUMERIC(12,2) | DEFAULT 0 | Discount applied |
| shipping_cost | NUMERIC(12,2) | DEFAULT 0 | Shipping cost |
| tax_amount | NUMERIC(12,2) | DEFAULT 0 | Tax amount |
| shipping_address_id | BIGINT | FOREIGN KEY | Reference to addresses (shipping) |
| billing_address_id | BIGINT | FOREIGN KEY | Reference to addresses (billing) |
| estimated_delivery_date | TIMESTAMP | - | Expected delivery date |
| delivery_date | TIMESTAMP | - | Actual delivery date |
| tracking_number | VARCHAR(100) | - | Shipping tracking number |
| notes | TEXT | - | Order notes |
| created_at | TIMESTAMP | NOT NULL | Order creation timestamp |
| updated_at | TIMESTAMP | NOT NULL | Last update timestamp |

**Indexes:**
- `idx_order_user_id` on (user_id)
- `idx_order_status` on (status)
- `idx_order_created_date` on (created_at)

**Relationships:**
- M:1 with User (user_id)
- M:1 with Address (shipping_address_id)
- M:1 with Address (billing_address_id)
- 1:N with OrderItem (order_id)
- 1:1 with Payment (order_id)

---

### 9. **order_items** Table
Individual items in orders.

| Column | Type | Constraints | Description |
|--------|------|-----------|-------------|
| id | BIGSERIAL | PRIMARY KEY, AUTO_INCREMENT | Order item identifier |
| order_id | BIGINT | FOREIGN KEY, NOT NULL | Reference to orders |
| product_id | BIGINT | FOREIGN KEY, NOT NULL | Reference to products |
| quantity | INTEGER | NOT NULL, MIN 1 | Quantity ordered |
| unit_price | NUMERIC(10,2) | NOT NULL | Price per unit at time of order |
| discount_price | NUMERIC(10,2) | - | Discount price applied |
| total_price | NUMERIC(12,2) | NOT NULL | Total for this item |
| size | VARCHAR(50) | - | Selected size |
| color | VARCHAR(50) | - | Selected color |
| created_at | TIMESTAMP | NOT NULL | Item addition timestamp |

**Indexes:**
- `idx_order_item_order_id` on (order_id)
- `idx_order_item_product_id` on (product_id)

**Relationships:**
- M:1 with Order (order_id)
- M:1 with Product (product_id)

---

### 10. **addresses** Table
User shipping and billing addresses.

| Column | Type | Constraints | Description |
|--------|------|-----------|-------------|
| id | BIGSERIAL | PRIMARY KEY, AUTO_INCREMENT | Address identifier |
| user_id | BIGINT | FOREIGN KEY, NOT NULL | Reference to users |
| address_line1 | VARCHAR(255) | NOT NULL | Street address |
| address_line2 | VARCHAR(255) | - | Apartment/suite number |
| city | VARCHAR(100) | NOT NULL | City |
| state | VARCHAR(100) | NOT NULL | State/Province |
| zip_code | VARCHAR(20) | NOT NULL | Postal code |
| country | VARCHAR(100) | NOT NULL | Country |
| phone_number | VARCHAR(20) | - | Contact phone |
| is_default | BOOLEAN | NOT NULL, DEFAULT false | Default address flag |
| is_shipping_address | BOOLEAN | NOT NULL, DEFAULT true | Shipping address flag |
| is_billing_address | BOOLEAN | NOT NULL, DEFAULT false | Billing address flag |
| address_type | VARCHAR(50) | - | ENUM: HOME, OFFICE, OTHER |
| created_at | TIMESTAMP | NOT NULL | Creation timestamp |
| updated_at | TIMESTAMP | NOT NULL | Last update timestamp |

**Indexes:**
- `idx_address_user_id` on (user_id)

**Relationships:**
- M:1 with User (user_id)
- Used by Order as shipping_address_id
- Used by Order as billing_address_id

---

### 11. **payments** Table
Payment records and status tracking.

| Column | Type | Constraints | Description |
|--------|------|-----------|-------------|
| id | BIGSERIAL | PRIMARY KEY, AUTO_INCREMENT | Payment identifier |
| order_id | BIGINT | UNIQUE, FOREIGN KEY, NOT NULL | Reference to orders |
| payment_method | VARCHAR(50) | NOT NULL | ENUM: CREDIT_CARD, DEBIT_CARD, UPI, PAYPAL, DIGITAL_WALLET, etc. |
| status | VARCHAR(50) | NOT NULL, DEFAULT 'PENDING' | ENUM: PENDING, PROCESSING, COMPLETED, FAILED, CANCELLED, REFUNDED |
| amount | NUMERIC(12,2) | NOT NULL | Payment amount |
| transaction_id | VARCHAR(255) | UNIQUE | Payment gateway transaction ID |
| reference_number | VARCHAR(255) | - | Payment reference |
| card_last_four | VARCHAR(4) | - | Last 4 digits of card |
| payment_gateway | VARCHAR(100) | - | Gateway used (STRIPE, PAYPAL, etc.) |
| notes | TEXT | - | Payment notes |
| paid_at | TIMESTAMP | - | Payment completion timestamp |
| created_at | TIMESTAMP | NOT NULL | Record creation timestamp |
| updated_at | TIMESTAMP | NOT NULL | Last update timestamp |

**Indexes:**
- `idx_payment_order_id` on (order_id)
- `idx_payment_status` on (status)

**Relationships:**
- 1:1 with Order (order_id)

---

### 12. **reviews** Table
Product reviews and ratings.

| Column | Type | Constraints | Description |
|--------|------|-----------|-------------|
| id | BIGSERIAL | PRIMARY KEY, AUTO_INCREMENT | Review identifier |
| product_id | BIGINT | FOREIGN KEY, NOT NULL | Reference to products |
| user_id | BIGINT | FOREIGN KEY, NOT NULL | Reference to users |
| rating | INTEGER | NOT NULL, MIN 1, MAX 5 | Star rating (1-5) |
| title | VARCHAR(100) | - | Review title |
| comment | TEXT | NOT NULL | Review comment |
| helpful_count | INTEGER | DEFAULT 0 | Number of helpful votes |
| verified_purchase | BOOLEAN | DEFAULT false | Verified purchase flag |
| created_at | TIMESTAMP | NOT NULL | Creation timestamp |
| updated_at | TIMESTAMP | NOT NULL | Last update timestamp |

**Indexes:**
- `idx_review_product_id` on (product_id)
- `idx_review_user_id` on (user_id)
- `idx_review_rating` on (rating)

**Relationships:**
- M:1 with Product (product_id)
- M:1 with User (user_id)

---

## Entity Relationship Diagram (ERD)

```
┌──────────────────┐
│      USERS       │
│   (11 columns)   │
├──────────────────┤
│ PK: id           │
│ UK: username     │
│ UK: email        │
│ ForeignKeys: -   │
└────────┬─────────┘
         │
    ┌────┼────┬────────┬──────────┐
    │    │    │        │          │
    ▼    ▼    ▼        ▼          ▼
┌──────┐ ┌─────────┐ ┌─────────┐ ┌──────────┐
│CARTS │ │ ORDERS  │ │ADDRESSES│ │ REVIEWS  │
│(1:1) │ │(1:N)    │ │(1:N)    │ │(1:N)     │
└──────┘ └────┬────┘ └─────────┘ └────┬─────┘
              │                        │
              │                        ▼
              │                   ┌──────────┐
              │                   │ PRODUCTS │
              │                   │          │
              │                   └────┬─────┘
              │                        │
         ┌────┴────┬──────────┐        │
         │          │          │       │
         ▼          ▼          ▼       ▼
    ┌──────────┐ ┌─────────┐ ┌──────────┐
    │CART_ITEMS│ │PAYMENTS │ │ORDER_ITEMS│
    │(M:1 Cart)│ │(1:1)    │ │(M:1 Order)│
    │(M:1 Prod)│ └─────────┘ │(M:1 Prod) │
    └──────────┘             └──────────┘
         │                         │
         └─────────┬───────────────┘
                   │
            [Shared FK to PRODUCTS]

Additional Element Collections:
- PRODUCT_SIZES (product_id)
- PRODUCT_COLORS (product_id)
- PRODUCT_IMAGES (product_id)
```

---

## Key Constraints

### Unique Constraints
- users.username
- users.email
- products: None (multiple same products allowed)
- carts.user_id (1:1 relationship)
- orders.order_number
- payments.order_id (1:1 relationship)
- payments.transaction_id (nullable)

### Foreign Key Constraints
- carts.user_id → users.id (UNIQUE)
- cart_items.cart_id → carts.id
- cart_items.product_id → products.id
- orders.user_id → users.id
- orders.shipping_address_id → addresses.id
- orders.billing_address_id → addresses.id
- order_items.order_id → orders.id
- order_items.product_id → products.id
- addresses.user_id → users.id
- payments.order_id → orders.id (UNIQUE)
- reviews.product_id → products.id
- reviews.user_id → users.id

### Cascade Rules
- User → Cart: CASCADE ALL
- User → Orders: CASCADE ALL, ORPHAN REMOVAL
- User → Addresses: CASCADE ALL, ORPHAN REMOVAL
- User → Reviews: CASCADE ALL, ORPHAN REMOVAL
- Cart → CartItems: CASCADE ALL, ORPHAN REMOVAL
- Order → OrderItems: CASCADE ALL, ORPHAN REMOVAL
- Order → Payment: CASCADE ALL

---

## Indexes for Performance

### Single Column Indexes
1. users(email) - Fast user lookup
2. users(username) - Fast login
3. products(name) - Product search
4. products(category) - Category filtering
5. products(price) - Price range queries
6. orders(user_id) - User order history
7. orders(status) - Order status filtering
8. orders(created_at) - Time-based queries
9. addresses(user_id) - User address lookup
10. payments(order_id) - Order payment lookup
11. payments(status) - Payment status queries
12. reviews(product_id) - Product reviews
13. reviews(user_id) - User reviews
14. reviews(rating) - Rating filtering
15. cart_items(cart_id) - Cart contents
16. cart_items(product_id) - Product in carts
17. order_items(order_id) - Order contents
18. order_items(product_id) - Product in orders
19. carts(user_id) - User cart lookup
20. carts(updated_at) - Cart activity

---

## Data Types & Precision

### Numeric Fields
- **NUMERIC(10,2)**: Unit prices, discounts, tax (max 99,999,999.99)
- **NUMERIC(12,2)**: Total amounts (max 9,999,999,999.99)
- **BIGINT**: IDs (supports 64-bit integers)
- **INTEGER**: Quantities, stock levels
- **BOOLEAN**: Flags (active, featured, default, etc.)

### String Fields
- **VARCHAR(50)**: Short strings (username, role)
- **VARCHAR(100)**: Medium strings (names, categories)
- **VARCHAR(255)**: Long strings (email, URLs)
- **TEXT**: Long text content (descriptions, comments)
- **TIMESTAMP**: Date/time with timezone

---

## DDL Execution Flow

When the application starts:

1. **Connection Pool Initialization** (HikariCP)
   - Establishes 5-20 connections to PostgreSQL
   - Tests connectivity and SSL configuration

2. **Schema Scanning** (Hibernate)
   - Scans all @Entity classes in com.lincee.entity package
   - Reads JPA annotations (@Table, @Column, @Index, etc.)

3. **DDL Generation** (Hibernate/Spring)
   - `spring.jpa.hibernate.ddl-auto=update` mode:
     - Creates tables if they don't exist
     - Adds missing columns to existing tables
     - Updates indexes
     - Does NOT drop columns or tables

4. **Table Creation Order**
   - Base entities first: users, products
   - Element collections: product_sizes, product_colors, product_images
   - Dependent entities: addresses, carts, orders, payments, reviews
   - Junction entities: cart_items, order_items

5. **Index Creation**
   - All @Index annotations processed
   - Foreign key indexes created automatically

---

## Database Verification

To verify all tables are created, connect to PostgreSQL and run:

```sql
-- List all tables
SELECT table_name FROM information_schema.tables 
WHERE table_schema = 'public' ORDER BY table_name;

-- List columns in a table
SELECT column_name, data_type, is_nullable FROM information_schema.columns
WHERE table_name = 'users' ORDER BY ordinal_position;

-- List indexes
SELECT indexname FROM pg_indexes 
WHERE schemaname = 'public' ORDER BY indexname;

-- Count rows in each table
SELECT 
    schemaname,
    tablename,
    pg_size_pretty(pg_total_relation_size(schemaname||'.'||tablename)) AS size
FROM pg_tables
WHERE schemaname = 'public'
ORDER BY pg_total_relation_size(schemaname||'.'||tablename) DESC;
```

---

## Troubleshooting

### Tables Not Created
1. Check application startup logs for DDL errors
2. Verify database connection string and credentials
3. Ensure `spring.jpa.hibernate.ddl-auto=update` is set
4. Check that @Entity and @Table annotations are present
5. Restart application (DDL runs on startup)

### Missing Columns
1. Verify @Column annotations in entity
2. Check for naming mismatches (Java camelCase vs database snake_case)
3. Restart application to trigger DDL update
4. Check if column addition is blocked by constraints

### Index Issues
1. Verify @Index annotations syntax
2. Check for duplicate index names
3. Ensure column names in indexes exist
4. Restart application to rebuild indexes

### Relationship Issues
1. Verify @JoinColumn names match actual columns
2. Check @OneToMany/@ManyToOne consistency
3. Ensure mappedBy matches property names
4. Review cascade configuration

---

## Performance Considerations

### Batch Operations
- `hibernate.jdbc.batch_size=20` - Batch inserts/updates
- `hibernate.order_inserts=true` - Order insert statements
- `hibernate.order_updates=true` - Order update statements

### Connection Pooling
- Max pool size: 20 (configurable)
- Min idle: 5 (warm connections)
- Connection timeout: 20s
- Idle timeout: 5 minutes

### Query Optimization
- Lazy loading on relationships (FetchType.LAZY)
- Proper indexing on frequently queried columns
- Pagination for list endpoints
- Custom JPA queries for complex operations

---

## Backup & Maintenance

### Recommended Backup Strategy
```bash
# Backup database
pg_dump postgresql://user:password@host/neondb > backup.sql

# Restore database
psql postgresql://user:password@host/neondb < backup.sql
```

### Regular Maintenance
- Monitor connection pool usage
- Analyze query performance
- Rebuild indexes periodically
- Update table statistics
- Clean up old records (orders, reviews archive)

---

**Last Updated:** February 3, 2026
**Status:** Production Ready
