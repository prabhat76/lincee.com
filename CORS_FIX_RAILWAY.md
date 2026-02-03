# CORS Configuration Fix for Railway Deployment

## ✅ Issue Fixed

**Problem**: `Http failure response: 0 Unknown Error` when calling Railway production API from frontend.

**Root Cause**: CORS configuration was missing the `https://` protocol for the Railway URL.

## 🔧 Changes Made

### 1. Updated CorsConfig.java
- ✅ Added explicit allowed origins including Railway URL with HTTPS
- ✅ Enabled credentials support (`allowCredentials: true`)
- ✅ Added specific allowed headers including Authorization
- ✅ Added HEAD method support
- ✅ Exposed Authorization header in responses

### 2. Updated application.properties
- ✅ Fixed Railway URL to use `https://` protocol
- ✅ Removed extra space in origins list
- ✅ Added HEAD to allowed methods

## 🌐 Allowed Origins

The API now accepts requests from:
- `http://localhost:3000` (Local React/Next.js dev)
- `http://localhost:4200` (Local Angular dev)
- `http://localhost:8080` (Local backend)
- `https://linceecom-production.up.railway.app` (Production - HTTPS)
- `http://linceecom-production.up.railway.app` (Production - HTTP fallback)

## 📋 Deployment Steps

1. **Commit changes**:
   ```bash
   git add .
   git commit -m "Fix CORS configuration for Railway deployment"
   git push
   ```

2. **Railway will auto-deploy** (if connected to GitHub)

3. **Test after deployment**:
   ```bash
   curl -X OPTIONS https://linceecom-production.up.railway.app/api/v1/auth/login \
     -H "Origin: https://your-frontend-domain.com" \
     -H "Access-Control-Request-Method: POST" \
     -H "Access-Control-Request-Headers: Content-Type" \
     -v
   ```

## 🔍 CORS Headers Now Returned

The API will now return these headers:
```
Access-Control-Allow-Origin: https://linceecom-production.up.railway.app
Access-Control-Allow-Methods: GET, POST, PUT, DELETE, OPTIONS, PATCH, HEAD
Access-Control-Allow-Headers: Authorization, Content-Type, Accept, Origin
Access-Control-Allow-Credentials: true
Access-Control-Max-Age: 3600
```

## 💻 Frontend Usage

### Fetch API
```javascript
fetch('https://linceecom-production.up.railway.app/api/v1/auth/login', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
  },
  credentials: 'include', // Important for cookies
  body: JSON.stringify({ email, password })
})
```

### Axios
```javascript
axios.post('https://linceecom-production.up.railway.app/api/v1/auth/login', 
  { email, password },
  {
    withCredentials: true,
    headers: {
      'Content-Type': 'application/json'
    }
  }
)
```

### Angular HttpClient
```typescript
this.http.post('https://linceecom-production.up.railway.app/api/v1/auth/login',
  { email, password },
  { 
    withCredentials: true,
    headers: { 'Content-Type': 'application/json' }
  }
)
```

## 🚨 Common CORS Errors & Solutions

### Error: "0 Unknown Error"
- **Cause**: Wrong origin URL or missing HTTPS
- **Fix**: Ensure origin is in allowed list with correct protocol

### Error: "No 'Access-Control-Allow-Origin' header"
- **Cause**: Origin not in allowed list
- **Fix**: Add your frontend domain to `CorsConfig.java`

### Error: "Credentials flag is 'true', but 'Access-Control-Allow-Credentials' header is ''"
- **Cause**: allowCredentials set to false
- **Fix**: Already fixed - now set to `true`

### Error: "Method not allowed"
- **Cause**: HTTP method not in allowed methods
- **Fix**: Already fixed - added all common methods including HEAD

## 🔐 Security Notes

1. **Production**: Replace wildcard origins with specific frontend domains
2. **Credentials**: Only enable for trusted origins
3. **Headers**: Only expose necessary headers
4. **HTTPS**: Always use HTTPS in production

## 🧪 Test CORS Configuration

### Test from Browser Console
```javascript
fetch('https://linceecom-production.up.railway.app/api/v1/products', {
  method: 'GET',
  headers: { 'Content-Type': 'application/json' }
})
.then(r => r.json())
.then(console.log)
.catch(console.error)
```

### Test Preflight Request
```bash
curl -X OPTIONS \
  https://linceecom-production.up.railway.app/api/v1/auth/login \
  -H "Origin: https://linceecom-production.up.railway.app" \
  -H "Access-Control-Request-Method: POST" \
  -H "Access-Control-Request-Headers: Content-Type,Authorization" \
  -v
```

Expected response should include:
- Status: 200 OK
- `Access-Control-Allow-Origin` header
- `Access-Control-Allow-Methods` header
- `Access-Control-Allow-Headers` header

## 📝 Adding New Frontend Domain

To allow requests from a new frontend domain:

1. Edit `CorsConfig.java`:
```java
configuration.setAllowedOrigins(Arrays.asList(
    "http://localhost:3000",
    "https://linceecom-production.up.railway.app",
    "https://your-new-frontend.com"  // Add here
));
```

2. Rebuild and deploy:
```bash
./mvnw clean package -DskipTests
git add .
git commit -m "Add new frontend domain to CORS"
git push
```

## ✅ Checklist

After deploying, verify:
- [ ] Login works from frontend
- [ ] API requests include CORS headers
- [ ] Authorization header is accepted
- [ ] No console errors about CORS
- [ ] Both HTTP and HTTPS variants work (if needed)

## 🔗 Railway Deployment URL

Your API is deployed at:
- **HTTPS**: `https://linceecom-production.up.railway.app`
- **API Base**: `https://linceecom-production.up.railway.app/api/v1`

## 📞 Still Having Issues?

1. **Check Railway logs**: Look for CORS-related errors
2. **Verify origin**: Ensure your frontend URL matches exactly (no trailing slash)
3. **Clear cache**: Hard refresh your browser (Ctrl+Shift+R)
4. **Check network tab**: Look at preflight OPTIONS request
5. **Test with curl**: Verify server is returning CORS headers
