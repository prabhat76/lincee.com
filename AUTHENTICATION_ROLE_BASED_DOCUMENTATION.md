# Authentication API - Role-Based Login Documentation

## Overview
The authentication system now includes role-based authentication that distinguishes between admin and customer users. The JWT token contains role information to enable proper routing to admin portal or customer flow.

## Login Flow

### 1. User Login Request
```http
POST /api/v1/auth/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password123"
}
```

### 2. Login Response (Customer)
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
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

### 3. Login Response (Admin)
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "userId": 2,
  "username": "admin_user",
  "email": "admin@example.com",
  "role": "ADMIN",
  "isAdmin": true,
  "firstName": "Admin",
  "lastName": "User",
  "message": "Login successful",
  "redirectTo": "/admin/dashboard"
}
```

## JWT Token Structure

The JWT token now includes:
- **Subject**: Username
- **role**: User role (ADMIN, CUSTOMER, MODERATOR)
- **userId**: User ID
- **iat**: Issued at timestamp
- **exp**: Expiration timestamp

### Token Claims Example
```json
{
  "sub": "admin_user",
  "role": "ADMIN",
  "userId": 2,
  "iat": 1675423200,
  "exp": 1675509600
}
```

## Frontend Integration

### Step 1: Login API Call
```javascript
const login = async (email, password) => {
  const response = await fetch('http://localhost:8080/api/v1/auth/login', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({ email, password }),
  });
  
  const data = await response.json();
  
  if (response.ok) {
    // Store token
    localStorage.setItem('authToken', data.token);
    localStorage.setItem('userRole', data.role);
    localStorage.setItem('isAdmin', data.isAdmin);
    localStorage.setItem('userId', data.userId);
    
    // Route based on role
    if (data.isAdmin) {
      window.location.href = data.redirectTo; // '/admin/dashboard'
    } else {
      window.location.href = data.redirectTo; // '/'
    }
  } else {
    console.error('Login failed:', data.message);
  }
};
```

### Step 2: Protected API Requests
```javascript
const fetchProtectedData = async () => {
  const token = localStorage.getItem('authToken');
  
  const response = await fetch('http://localhost:8080/api/v1/collections/admin', {
    method: 'GET',
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json',
    },
  });
  
  return await response.json();
};
```

### Step 3: Route Protection
```javascript
// React Router example
import { Navigate } from 'react-router-dom';

const ProtectedAdminRoute = ({ children }) => {
  const isAdmin = localStorage.getItem('isAdmin') === 'true';
  
  if (!isAdmin) {
    return <Navigate to="/" replace />;
  }
  
  return children;
};

// Usage
<Route
  path="/admin/*"
  element={
    <ProtectedAdminRoute>
      <AdminDashboard />
    </ProtectedAdminRoute>
  }
/>
```

## User Registration

### Registration Request
```http
POST /api/v1/auth/register
Content-Type: application/json

{
  "username": "newuser",
  "email": "newuser@example.com",
  "password": "SecurePass123!",
  "firstName": "New",
  "lastName": "User",
  "phone": "+1234567890"
}
```

### Registration Response
```json
{
  "message": "User registered successfully",
  "userId": 3,
  "username": "newuser",
  "email": "newuser@example.com",
  "role": "CUSTOMER",
  "isAdmin": false,
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "redirectTo": "/"
}
```

**Note**: New users are automatically assigned the `CUSTOMER` role.

## User Roles

### Available Roles
1. **CUSTOMER** (Default)
   - Normal user with shopping privileges
   - Can view products, add to cart, place orders
   - No access to admin endpoints

2. **ADMIN**
   - Full administrative privileges
   - Can manage collections, products, orders
   - Access to all admin endpoints

3. **MODERATOR**
   - Intermediate privileges (can be customized)
   - Limited admin access

### Role-Based Access Control

| Endpoint Pattern | Required Role | Description |
|------------------|---------------|-------------|
| `/api/v1/collections/admin/**` | ADMIN | Collection management |
| `POST /api/v1/products` | ADMIN | Create products |
| `PUT /api/v1/products/**` | ADMIN | Update products |
| `DELETE /api/v1/products/**` | ADMIN | Delete products |
| All other endpoints | Public | No authentication required |

## Error Responses

### 401 Unauthorized
```json
{
  "message": "Invalid credentials"
}
```

### 403 Forbidden
When user is authenticated but doesn't have required role:
```json
{
  "timestamp": "2026-02-03T10:00:00",
  "status": 403,
  "error": "Forbidden",
  "message": "Access Denied",
  "path": "/api/v1/collections/admin"
}
```

### 409 Conflict (Registration)
```json
{
  "message": "Email already registered"
}
```

## Creating an Admin User

### Option 1: Direct Database Modification
```sql
-- Update existing user to admin
UPDATE users 
SET role = 'ADMIN' 
WHERE email = 'admin@example.com';
```

### Option 2: Through Registration + Database Update
1. Register normally through API
2. Update the role in database to ADMIN
3. Login again to get new token with admin privileges

### Option 3: Seed Data (Recommended)
Add to DataInitService:
```java
User admin = new User();
admin.setUsername("admin");
admin.setEmail("admin@lincee.com");
admin.setPassword(passwordEncoder.encode("Admin@123"));
admin.setRole(User.Role.ADMIN);
admin.setActive(true);
userRepository.save(admin);
```

## Token Validation

The JWT token is automatically validated on each request through `JwtAuthenticationFilter`:

1. Extract token from `Authorization: Bearer <token>` header
2. Validate token signature and expiration
3. Extract username and role from token
4. Load user from database
5. Set authentication in Spring Security context with role authority
6. Allow/deny access based on role requirements

## Best Practices

### Frontend
1. **Store tokens securely** - Use httpOnly cookies or secure localStorage
2. **Check token expiration** - Implement token refresh mechanism
3. **Clear tokens on logout** - Remove all auth data from storage
4. **Route guards** - Protect admin routes based on `isAdmin` flag
5. **Handle 401/403 errors** - Redirect to login or show access denied

### Backend
1. **Password hashing** - Uses BCrypt (already implemented)
2. **Token expiration** - Configured in `application.properties`
3. **Role validation** - Enforced at security filter level
4. **Stateless sessions** - No server-side session storage

## Testing Examples

### Test Customer Login
```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "customer@example.com",
    "password": "password123"
  }'
```

### Test Admin Login
```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@lincee.com",
    "password": "Admin@123"
  }'
```

### Test Admin Endpoint with Token
```bash
TOKEN="<your-admin-jwt-token>"

curl -X GET http://localhost:8080/api/v1/collections/admin/all \
  -H "Authorization: Bearer $TOKEN"
```

### Test Unauthorized Access
```bash
# Try admin endpoint with customer token
TOKEN="<customer-jwt-token>"

curl -X GET http://localhost:8080/api/v1/collections/admin/all \
  -H "Authorization: Bearer $TOKEN"

# Should return 403 Forbidden
```

## Response Fields Explanation

| Field | Type | Description |
|-------|------|-------------|
| `token` | string | JWT access token |
| `type` | string | Token type (always "Bearer") |
| `userId` | number | User's unique ID |
| `username` | string | User's username |
| `email` | string | User's email address |
| `role` | string | User role (ADMIN/CUSTOMER/MODERATOR) |
| `isAdmin` | boolean | Quick check if user is admin |
| `firstName` | string | User's first name |
| `lastName` | string | User's last name |
| `message` | string | Success/error message |
| `redirectTo` | string | Suggested redirect path after login |

## Security Considerations

1. **HTTPS Required** - Always use HTTPS in production
2. **Token Storage** - Avoid storing tokens in localStorage in production (use httpOnly cookies)
3. **Password Policy** - Implement strong password requirements
4. **Rate Limiting** - Implement login attempt rate limiting
5. **Token Expiration** - Configure appropriate expiration times
6. **Refresh Tokens** - Implement refresh token mechanism for better security

## Configuration

Update `application.properties`:
```properties
# JWT Configuration
app.jwt.secret=your-super-secret-key-minimum-256-bits-long
app.jwt.expiration=86400000  # 24 hours in milliseconds

# For production, use environment variable:
# app.jwt.secret=${JWT_SECRET}
```
