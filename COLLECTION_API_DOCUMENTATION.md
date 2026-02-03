# Collection Management API Documentation

## Overview
This document describes the Collection Management API endpoints. Collections allow you to group products together (e.g., "Summer Collection", "New Arrivals", "Best Sellers").

## Authentication
- **Public Endpoints**: No authentication required
- **Admin Endpoints**: Require JWT token with ADMIN role
  - Add `Authorization: Bearer <token>` header to requests

## Endpoints

### Public Endpoints

#### 1. Get Active Collections
```http
GET /api/v1/collections
```
Returns all active collections ordered by display order.

**Response:**
```json
[
  {
    "id": 1,
    "name": "Summer Collection",
    "slug": "summer-collection",
    "description": "Hot summer styles",
    "imageUrl": "https://example.com/summer.jpg",
    "active": true,
    "featured": true,
    "displayOrder": 1,
    "productCount": 15,
    "products": [
      {
        "id": 1,
        "name": "Cool T-Shirt",
        "imageUrl": "https://example.com/tshirt.jpg",
        "active": true
      }
    ],
    "createdAt": "2026-02-01T10:00:00",
    "updatedAt": "2026-02-01T10:00:00"
  }
]
```

#### 2. Get Featured Collections
```http
GET /api/v1/collections/featured
```
Returns all featured and active collections.

#### 3. Get Collection by ID
```http
GET /api/v1/collections/{id}
```
Returns a specific collection with all its products.

**Response:** Same as single collection object above

#### 4. Get Collection by Slug
```http
GET /api/v1/collections/slug/{slug}
```
Returns a specific collection by its URL-friendly slug.

**Example:**
```http
GET /api/v1/collections/slug/summer-collection
```

---

### Admin Endpoints (Require ADMIN Role)

#### 5. Get All Collections (Including Inactive)
```http
GET /api/v1/collections/admin/all
Authorization: Bearer <admin-jwt-token>
```
Returns all collections including inactive ones.

#### 6. Create Collection
```http
POST /api/v1/collections/admin
Authorization: Bearer <admin-jwt-token>
Content-Type: application/json
```

**Request Body:**
```json
{
  "name": "Winter Collection",
  "slug": "winter-collection",
  "description": "Cozy winter streetwear",
  "imageUrl": "https://example.com/winter.jpg",
  "active": true,
  "featured": false,
  "displayOrder": 1
}
```

**Response:**
```json
{
  "id": 2,
  "name": "Winter Collection",
  "slug": "winter-collection",
  "description": "Cozy winter streetwear",
  "imageUrl": "https://example.com/winter.jpg",
  "active": true,
  "featured": false,
  "displayOrder": 1,
  "productCount": 0,
  "products": [],
  "createdAt": "2026-02-03T10:00:00",
  "updatedAt": "2026-02-03T10:00:00"
}
```

**Validation Rules:**
- `name`: Required, 2-100 characters, must be unique
- `slug`: Required, 2-100 characters, must be unique
- `description`: Optional
- `imageUrl`: Optional
- `active`: Optional, default: true
- `featured`: Optional, default: false
- `displayOrder`: Optional, default: 0

#### 7. Update Collection
```http
PUT /api/v1/collections/admin/{id}
Authorization: Bearer <admin-jwt-token>
Content-Type: application/json
```

**Request Body:**
```json
{
  "name": "Winter Collection 2026",
  "slug": "winter-collection-2026",
  "description": "Updated description",
  "imageUrl": "https://example.com/winter-new.jpg",
  "active": true,
  "featured": true,
  "displayOrder": 1
}
```

#### 8. Delete Collection
```http
DELETE /api/v1/collections/admin/{id}
Authorization: Bearer <admin-jwt-token>
```

**Response:**
```json
{
  "message": "Collection deleted successfully"
}
```

**Note:** Deleting a collection will not delete the products, only remove them from the collection.

#### 9. Add Product to Collection
```http
POST /api/v1/collections/admin/{collectionId}/products/{productId}
Authorization: Bearer <admin-jwt-token>
```

**Example:**
```http
POST /api/v1/collections/admin/1/products/5
```

**Response:** Returns the updated collection with the new product added.

#### 10. Remove Product from Collection
```http
DELETE /api/v1/collections/admin/{collectionId}/products/{productId}
Authorization: Bearer <admin-jwt-token>
```

**Example:**
```http
DELETE /api/v1/collections/admin/1/products/5
```

**Response:** Returns the updated collection without the removed product.

#### 11. Add Multiple Products to Collection (Batch)
```http
POST /api/v1/collections/admin/{collectionId}/products/batch
Authorization: Bearer <admin-jwt-token>
Content-Type: application/json
```

**Request Body:**
```json
[1, 2, 3, 4, 5]
```

**Response:** Returns the updated collection with all new products added.

---

## Database Schema

### Collections Table
```sql
CREATE TABLE collections (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL UNIQUE,
    slug VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    image_url VARCHAR(255),
    active BOOLEAN NOT NULL DEFAULT true,
    featured BOOLEAN NOT NULL DEFAULT false,
    display_order INT DEFAULT 0,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);

CREATE INDEX idx_collection_name ON collections(name);
CREATE INDEX idx_collection_slug ON collections(slug);
```

### Collection-Products Junction Table
```sql
CREATE TABLE collection_products (
    collection_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    PRIMARY KEY (collection_id, product_id),
    FOREIGN KEY (collection_id) REFERENCES collections(id),
    FOREIGN KEY (product_id) REFERENCES products(id)
);
```

---

## Usage Examples

### Example 1: Create a Collection and Add Products
```bash
# Step 1: Create collection
curl -X POST http://localhost:8080/api/v1/collections/admin \
  -H "Authorization: Bearer YOUR_ADMIN_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "New Arrivals",
    "slug": "new-arrivals",
    "description": "Latest products in stock",
    "featured": true,
    "displayOrder": 1
  }'

# Step 2: Add multiple products
curl -X POST http://localhost:8080/api/v1/collections/admin/1/products/batch \
  -H "Authorization: Bearer YOUR_ADMIN_TOKEN" \
  -H "Content-Type: application/json" \
  -d '[1, 2, 3, 4, 5]'
```

### Example 2: Get Featured Collections (Public)
```bash
curl http://localhost:8080/api/v1/collections/featured
```

### Example 3: Get Collection by Slug (Public)
```bash
curl http://localhost:8080/api/v1/collections/slug/new-arrivals
```

---

## Error Responses

### 400 Bad Request
```json
{
  "error": "Collection with slug 'summer-collection' already exists"
}
```

### 401 Unauthorized
When admin authentication is required but not provided.

### 403 Forbidden
When user is authenticated but doesn't have ADMIN role.

### 404 Not Found
```json
{
  "error": "Collection not found with id: 999"
}
```

---

## Notes
- A product can belong to multiple collections
- Collections are ordered by `displayOrder` (ascending) then by `name` (alphabetically)
- Soft deletion is not implemented - deleted collections are permanently removed
- The `slug` field should be URL-friendly (lowercase, hyphens, no spaces)
- Featured collections appear in special homepage sections
