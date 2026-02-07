# Image Storage Guide for Lincee E-Commerce

## Current Setup ✅

Your products now use **Unsplash** images (free, high-quality stock photos) as placeholders.

### Image URLs Format:
```
https://images.unsplash.com/photo-{id}?w=800&h=800&fit=crop&q=80
```
- `w=800&h=800` - 800x800px square images
- `fit=crop` - Crop to fit dimensions
- `q=80` - 80% quality (good balance)

## Recommended Production Solutions

### 🥇 Option 1: Cloudinary (RECOMMENDED)

**Why Cloudinary?**
- ✅ Free tier: 25GB storage, 25GB bandwidth/month
- ✅ Automatic image optimization
- ✅ On-the-fly transformations (resize, crop, quality)
- ✅ CDN delivery worldwide
- ✅ Easy Spring Boot integration

**Setup Steps:**

1. **Sign up**: https://cloudinary.com/users/register/free

2. **Get credentials** from Dashboard:
   ```
   Cloud Name: your-cloud-name
   API Key: your-api-key
   API Secret: your-api-secret
   ```

3. **Add to pom.xml:**
   ```xml
   <dependency>
       <groupId>com.cloudinary</groupId>
       <artifactId>cloudinary-http44</artifactId>
       <version>1.38.0</version>
   </dependency>
   ```

4. **Add to application.properties:**
   ```properties
   cloudinary.cloud-name=${CLOUDINARY_CLOUD_NAME:your-cloud-name}
   cloudinary.api-key=${CLOUDINARY_API_KEY:your-api-key}
   cloudinary.api-secret=${CLOUDINARY_API_SECRET:your-api-secret}
   ```

5. **Create CloudinaryConfig.java:**
   ```java
   @Configuration
   public class CloudinaryConfig {
       @Value("${cloudinary.cloud-name}")
       private String cloudName;
       
       @Value("${cloudinary.api-key}")
       private String apiKey;
       
       @Value("${cloudinary.api-secret}")
       private String apiSecret;
       
       @Bean
       public Cloudinary cloudinary() {
           return new Cloudinary(ObjectUtils.asMap(
               "cloud_name", cloudName,
               "api_key", apiKey,
               "api_secret", apiSecret
           ));
       }
   }
   ```

6. **Create ImageService.java:**
   ```java
   @Service
   public class ImageService {
       @Autowired
       private Cloudinary cloudinary;
       
       public String uploadImage(MultipartFile file, String folder) throws IOException {
           Map uploadResult = cloudinary.uploader().upload(file.getBytes(), 
               ObjectUtils.asMap(
                   "folder", folder,
                   "resource_type", "image",
                   "transformation", new Transformation()
                       .width(800).height(800).crop("fill")
                       .quality("auto")
               ));
           return uploadResult.get("secure_url").toString();
       }
       
       public void deleteImage(String publicId) throws IOException {
           cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
       }
   }
   ```

7. **Image URLs will look like:**
   ```
   https://res.cloudinary.com/your-cloud-name/image/upload/v1234567890/products/sweatshorts-grey-front.jpg
   ```

---

### 🥈 Option 2: AWS S3 + CloudFront

**Why AWS?**
- ✅ Industry standard
- ✅ Highly scalable
- ✅ Pay-as-you-go pricing
- ✅ Integrated with AWS ecosystem

**Setup Steps:**

1. **Create S3 Bucket:**
   - Name: `lincee-product-images`
   - Region: Choose closest to your users
   - Enable versioning
   - Block public access: OFF (for product images)

2. **Create CloudFront Distribution:**
   - Origin: Your S3 bucket
   - Viewer Protocol Policy: Redirect HTTP to HTTPS
   - Compress Objects: Yes

3. **Add to pom.xml:**
   ```xml
   <dependency>
       <groupId>com.amazonaws</groupId>
       <artifactId>aws-java-sdk-s3</artifactId>
       <version>1.12.565</version>
   </dependency>
   ```

4. **Configure in application.properties:**
   ```properties
   aws.s3.bucket-name=lincee-product-images
   aws.s3.region=us-east-1
   aws.cloudfront.url=https://d111111abcdef8.cloudfront.net
   ```

5. **Image URLs:**
   ```
   https://d111111abcdef8.cloudfront.net/products/sweatshorts/grey-front.jpg
   ```

**Cost Estimate:**
- Storage: $0.023 per GB/month (~$0.23 for 10GB)
- Data Transfer: $0.09 per GB (first 10TB/month)
- Requests: $0.0004 per 1,000 GET requests

---

### 🥉 Option 3: Azure Blob Storage

**Why Azure?**
- ✅ Good if using Railway (easy integration)
- ✅ Competitive pricing
- ✅ Built-in CDN

**Setup Steps:**

1. **Create Storage Account**
2. **Create Container:** `product-images`
3. **Enable CDN**
4. **Add dependency:**
   ```xml
   <dependency>
       <groupId>com.azure</groupId>
       <artifactId>azure-storage-blob</artifactId>
       <version>12.23.0</version>
   </dependency>
   ```

---

### 🔧 Option 4: Local Storage (Development Only)

**For testing only, NOT recommended for production**

1. **Create directory:**
   ```bash
   mkdir -p src/main/resources/static/images/products
   ```

2. **Configure Spring Boot:**
   ```properties
   spring.web.resources.static-locations=classpath:/static/
   ```

3. **Access images:**
   ```
   http://localhost:8080/images/products/sweatshorts-grey.jpg
   ```

**⚠️ Problems:**
- No CDN (slow for global users)
- Doesn't scale (limited by server disk)
- No image optimization
- Lost on server restart (if using Railway/Heroku)

---

## Image Upload API Example

Here's a complete implementation for uploading product images:

### ImageController.java
```java
@RestController
@RequestMapping("/api/v1/images")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @PostMapping("/upload")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Map<String, Object>> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "folder", defaultValue = "products") String folder) {
        
        try {
            // Validate file
            if (file.isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "File is empty"));
            }
            
            // Check file type
            String contentType = file.getContentType();
            if (!contentType.startsWith("image/")) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "File must be an image"));
            }
            
            // Check file size (max 5MB)
            if (file.getSize() > 5 * 1024 * 1024) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "File size must be less than 5MB"));
            }
            
            // Upload to cloud storage
            String imageUrl = imageService.uploadImage(file, folder);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "url", imageUrl,
                "fileName", file.getOriginalFilename()
            ));
            
        } catch (Exception e) {
            return ResponseEntity.status(500)
                .body(Map.of("error", "Failed to upload image: " + e.getMessage()));
        }
    }
    
    @DeleteMapping("/{publicId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Map<String, Object>> deleteImage(@PathVariable String publicId) {
        try {
            imageService.deleteImage(publicId);
            return ResponseEntity.ok(Map.of("success", true, "message", "Image deleted"));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                .body(Map.of("error", "Failed to delete image: " + e.getMessage()));
        }
    }
}
```

---

## Frontend Upload Example

### React/Next.js
```javascript
async function uploadProductImage(file) {
  const formData = new FormData();
  formData.append('file', file);
  formData.append('folder', 'products/sweatshorts');
  
  const response = await fetch('/api/v1/images/upload', {
    method: 'POST',
    headers: {
      'Authorization': `Bearer ${token}`
    },
    body: formData
  });
  
  const data = await response.json();
  return data.url; // Use this URL in your product
}
```

---

## Image Best Practices

### 1. Image Dimensions
- **Product Main Image**: 800x800px (1:1 ratio)
- **Product Thumbnails**: 200x200px
- **Product Gallery**: 1200x1200px
- **Category Banners**: 1920x600px

### 2. File Format
- **JPEG**: For photos (smaller file size)
- **PNG**: For images with transparency
- **WebP**: Modern format (best compression, not all browsers)

### 3. File Size
- **Main Images**: < 200KB
- **Thumbnails**: < 50KB
- **High-res**: < 500KB

### 4. Naming Convention
```
{category}-{color}-{view}-{timestamp}.jpg

Examples:
sweatshorts-grey-front-1234567890.jpg
sweatshorts-black-back-1234567891.jpg
```

### 5. URL Structure
```
https://your-cdn.com/
  └── products/
      ├── sweatshorts/
      │   ├── grey-front.jpg
      │   ├── grey-back.jpg
      │   ├── black-front.jpg
      │   └── black-back.jpg
      ├── tshirts/
      └── hoodies/
```

---

## Migration Steps (Current → Cloudinary)

1. **Keep Unsplash URLs** for now (they work!)
2. **Set up Cloudinary** account
3. **Upload your real product photos** to Cloudinary
4. **Update products** via API:
   ```bash
   curl -X PUT http://localhost:8080/api/v1/products/3 \
     -H "Authorization: Bearer $TOKEN" \
     -H "Content-Type: application/json" \
     -d '{
       "imageUrls": [
         "https://res.cloudinary.com/your-cloud/image/upload/v1/products/sweatshorts-grey-front.jpg",
         "https://res.cloudinary.com/your-cloud/image/upload/v1/products/sweatshorts-grey-back.jpg"
       ]
     }'
   ```

---

## Cost Comparison

| Solution | Free Tier | Cost (100GB storage + 1TB bandwidth) |
|----------|-----------|--------------------------------------|
| **Cloudinary** | 25GB + 25GB/mo | $89/month |
| **AWS S3 + CloudFront** | 12 months free | ~$25/month |
| **Azure Blob + CDN** | 12 months free | ~$30/month |
| **Unsplash** | Free forever | Free (but not your images) |

---

## My Recommendation

**For Your E-Commerce Store:**

1. **Start**: Use current Unsplash URLs (already done ✅)
2. **Next Week**: Set up Cloudinary free tier
3. **Before Launch**: Upload real product photos
4. **At Scale**: Consider AWS S3 + CloudFront

**Cloudinary is best for you because:**
- Free tier is generous (enough for ~100 products)
- Easy to integrate
- Automatic optimization saves bandwidth
- No DevOps complexity
- Can upgrade smoothly as you grow

---

## Current Status

✅ **Products use working Unsplash image URLs**
✅ **Images load instantly from CDN**
✅ **800x800px optimized for web**
✅ **Ready to deploy to Railway**

Next step: Replace Unsplash URLs with your actual product photos when ready!
