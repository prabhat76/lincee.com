# Collection Management - Quick Reference Guide

## 🎯 What's Been Implemented

Admin users can now:
1. ✅ Create product collections (e.g., "Summer 2026", "New Arrivals")
2. ✅ Add products to collections
3. ✅ Remove products from collections
4. ✅ Update and delete collections
5. ✅ Batch add multiple products at once

Public users can:
1. ✅ View active collections
2. ✅ View featured collections
3. ✅ Get collection details with all products

## 🔐 Authentication Required

Admin endpoints require JWT token with ADMIN role:
```
Authorization: Bearer <your-jwt-token>
```

## 📋 Quick API Reference

### Admin Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/v1/collections/admin` | Create collection |
| `PUT` | `/api/v1/collections/admin/{id}` | Update collection |
| `DELETE` | `/api/v1/collections/admin/{id}` | Delete collection |
| `POST` | `/api/v1/collections/admin/{collectionId}/products/{productId}` | Add single product |
| `DELETE` | `/api/v1/collections/admin/{collectionId}/products/{productId}` | Remove product |
| `POST` | `/api/v1/collections/admin/{collectionId}/products/batch` | Add multiple products |
| `GET` | `/api/v1/collections/admin/all` | Get all collections |

### Public Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/v1/collections` | Get active collections |
| `GET` | `/api/v1/collections/featured` | Get featured collections |
| `GET` | `/api/v1/collections/{id}` | Get by ID |
| `GET` | `/api/v1/collections/slug/{slug}` | Get by slug |

## 🚀 Quick Start Examples

### 1. Create a Collection
```bash
POST /api/v1/collections/admin
{
  "name": "Summer Collection 2026",
  "slug": "summer-2026",
  "description": "Hot summer styles",
  "featured": true,
  "displayOrder": 1
}
```

### 2. Add Multiple Products to Collection
```bash
POST /api/v1/collections/admin/1/products/batch
[1, 2, 3, 4, 5]
```

### 3. Get Featured Collections (Public)
```bash
GET /api/v1/collections/featured
```

## 📊 Data Structure

### Collection Object
```json
{
  "id": 1,
  "name": "Summer Collection",
  "slug": "summer-collection",
  "description": "Hot summer styles",
  "imageUrl": "https://...",
  "active": true,
  "featured": true,
  "displayOrder": 1,
  "productCount": 15,
  "products": [
    {
      "id": 1,
      "name": "Cool T-Shirt",
      "imageUrl": "https://...",
      "active": true
    }
  ],
  "createdAt": "2026-02-01T10:00:00",
  "updatedAt": "2026-02-01T10:00:00"
}
```

## 🔑 Key Features

- **Many-to-Many**: Products can belong to multiple collections
- **Ordering**: Collections ordered by `displayOrder` then name
- **Featured**: Flag for homepage/special display
- **Slug**: URL-friendly identifier for SEO
- **Active**: Show/hide collections without deleting

## 💡 Best Practices

1. **Slugs**: Use lowercase with hyphens (e.g., "summer-2026")
2. **Display Order**: Lower numbers appear first (0, 1, 2, ...)
3. **Featured**: Limit to 3-5 collections for homepage
4. **Images**: Use high-quality collection cover images
5. **Active**: Set to false to temporarily hide collections

## ⚠️ Important Notes

- Deleting a collection doesn't delete products
- Products are only removed from the collection
- Duplicate names/slugs are not allowed
- Admin role is required for all management operations

## 🧪 Testing

1. **Start Server**: `./mvnw spring-boot:run`
2. **Swagger UI**: `http://localhost:8080/swagger-ui.html`
3. **Get Admin Token**: Login with admin credentials
4. **Test Endpoints**: Use Swagger or Postman

## 📂 New Files

- `entity/Collection.java` - Collection entity
- `dto/CollectionDTO.java` - API response format
- `repository/CollectionRepository.java` - Data access
- `service/CollectionService.java` - Business logic
- `controller/CollectionController.java` - REST endpoints
- `config/JwtAuthenticationFilter.java` - JWT validation

## 📝 Documentation

- Full API docs: `COLLECTION_API_DOCUMENTATION.md`
- Implementation details: `COLLECTION_IMPLEMENTATION_SUMMARY.md`
