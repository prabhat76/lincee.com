# Authentication Role-Based Login - Quick Reference

## 🎯 What Changed

The login API now:
1. ✅ Checks user role (ADMIN or CUSTOMER)
2. ✅ Returns JWT token with embedded role information
3. ✅ Includes `isAdmin` flag for easy frontend routing
4. ✅ Provides `redirectTo` path for automatic navigation

## 🔐 Login API Response

### Customer Login Response
```json
{
  "token": "eyJhbGc...",
  "type": "Bearer",
  "userId": 1,
  "username": "john_doe",
  "email": "john@example.com",
  "role": "CUSTOMER",
  "isAdmin": false,
  "firstName": "John",
  "lastName": "Doe",
  "message": "Login successful",
  "redirectTo": "/"
}
```

### Admin Login Response
```json
{
  "token": "eyJhbGc...",
  "type": "Bearer",
  "userId": 2,
  "username": "admin",
  "email": "admin@lincee.com",
  "role": "ADMIN",
  "isAdmin": true,
  "firstName": "Admin",
  "lastName": "User",
  "message": "Login successful",
  "redirectTo": "/admin/dashboard"
}
```

## 💻 Frontend Integration

### Simple Login Flow
```javascript
// 1. Login
const response = await fetch('/api/v1/auth/login', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({ email, password })
});

const data = await response.json();

// 2. Store token
localStorage.setItem('authToken', data.token);
localStorage.setItem('isAdmin', data.isAdmin);

// 3. Route based on role
if (data.isAdmin) {
  window.location.href = '/admin/dashboard';
} else {
  window.location.href = '/';
}
```

### Use Token in API Calls
```javascript
fetch('/api/v1/collections/admin', {
  headers: {
    'Authorization': `Bearer ${localStorage.getItem('authToken')}`
  }
});
```

## 🔑 JWT Token Contents

The token now includes:
- `sub`: Username
- `role`: ADMIN/CUSTOMER/MODERATOR
- `userId`: User's ID
- `iat`: Issued at
- `exp`: Expires at

## 📋 Quick API Endpoints

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/v1/auth/login` | POST | Login with email/password |
| `/api/v1/auth/register` | POST | Register new customer |
| `/api/v1/auth/logout` | POST | Logout (client-side) |

## 🛡️ Access Control

### Public Endpoints
- Product listing: `GET /api/v1/products`
- Collection viewing: `GET /api/v1/collections`
- All auth endpoints

### Admin-Only Endpoints
- Collection management: `/api/v1/collections/admin/**`
- Product create/update/delete
- All admin operations

## 🧪 Testing

### Test Admin Login
```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@lincee.com","password":"Admin@123"}'
```

### Test Customer Login
```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"customer@example.com","password":"password123"}'
```

## 🔧 Create Admin User

### Option 1: Update Existing User (SQL)
```sql
UPDATE users SET role = 'ADMIN' WHERE email = 'your@email.com';
```

### Option 2: Add to DataInitService
```java
User admin = new User();
admin.setUsername("admin");
admin.setEmail("admin@lincee.com");
admin.setPassword(passwordEncoder.encode("Admin@123"));
admin.setRole(User.Role.ADMIN);
userRepository.save(admin);
```

## ⚡ Key Response Fields

| Field | Purpose |
|-------|---------|
| `isAdmin` | Quick boolean check for routing |
| `role` | Full role string (ADMIN/CUSTOMER) |
| `redirectTo` | Suggested redirect path |
| `token` | JWT for subsequent API calls |

## 📱 Frontend Route Protection Example

```javascript
// React Router
const ProtectedRoute = ({ children }) => {
  const isAdmin = localStorage.getItem('isAdmin') === 'true';
  return isAdmin ? children : <Navigate to="/" />;
};

// Usage
<Route path="/admin/*" element={
  <ProtectedRoute>
    <AdminDashboard />
  </ProtectedRoute>
} />
```

## ✅ What to Check After Login

1. **Check `isAdmin` flag** - Route to admin or customer flow
2. **Store token** - For subsequent API calls
3. **Use `redirectTo`** - Navigate to suggested path
4. **Handle errors** - Show message if login fails

## 📄 Full Documentation

See [AUTHENTICATION_ROLE_BASED_DOCUMENTATION.md](AUTHENTICATION_ROLE_BASED_DOCUMENTATION.md) for complete details.
