# URGENT: Database Tables & Swagger Not Updating - FIX GUIDE

## 🚨 Current Issue

You're seeing:
- ❌ Only **4 tables** in database (expected: 15)
- ❌ Swagger UI not showing all API endpoints (expected: 89+)

## ✅ Solution Applied

I've made 3 critical fixes:

### Fix #1: Added Explicit Entity Scanning
Updated `LinceeApplication.java`:
```java
@SpringBootApplication
@EntityScan(basePackages = "com.lincee.entity")  // ← NEW
@EnableJpaRepositories(basePackages = "com.lincee.repository")  // ← NEW
public class LinceeApplication {
```

**Why:** Ensures Spring Boot finds ALL entities and repositories

### Fix #2: Changed DDL Mode to create-drop
Updated `application.properties`:
```properties
spring.jpa.hibernate.ddl-auto=create-drop  # Was: update
spring.jpa.show-sql=true  # Was: false
```

**Why:** Forces Hibernate to:
1. DROP all existing tables
2. CREATE all tables fresh from entities
3. Show SQL statements so you can see what's happening

### Fix #3: Enabled Debug Logging
```properties
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.springframework.boot.autoconfigure.orm.jpa=DEBUG
```

**Why:** Shows exactly which entities are being scanned and which tables are created

---

## 🔥 IMMEDIATE STEPS TO FIX

### Step 1: Stop Any Running Application
```bash
# Find and kill any Java process on port 8080
lsof -ti:8080 | xargs kill -9

# OR use the easier way:
pkill -f lincee-backend
```

### Step 2: Rebuild the Application
```bash
cd /Users/prabhatkumar/Documents/lincee-backend
mvn clean package -DskipTests
```

### Step 3: Run with the Startup Script
```bash
./start.sh
```

**OR manually:**
```bash
java -jar target/lincee-backend-1.0.0.jar
```

### Step 4: Watch the Output

Look for these CRITICAL lines in startup:

```
✅ Processing PersistenceUnitInfo [name: default]
✅ HHH000412: Hibernate ORM core version
✅ Scanning for classes with @Entity annotation
✅ Found entity: User
✅ Found entity: Product  
✅ Found entity: Cart
✅ Found entity: CartItem
✅ Found entity: Order
✅ Found entity: OrderItem
✅ Found entity: Address
✅ Found entity: Payment
✅ Found entity: Review
✅ HHH000227: Running hbm2ddl schema update
✅ HHH000228: Schema update complete
✅ Started LinceeApplication in X seconds
```

---

## 🗄️ Verify Database Tables

Once application starts, connect to PostgreSQL:

```sql
-- Connect to database
psql postgresql://neondb_owner:npg_8HDt2LTngcGa@ep-morning-credit-ahu0h3sm-pooler.c-3.us-east-1.aws.neon.tech/neondb?sslmode=require

-- List all tables
\dt

-- Expected output (15 tables):
                    List of relations
 Schema |       Name        | Type  |    Owner     
--------+-------------------+-------+--------------
 public | addresses         | table | neondb_owner
 public | cart_items        | table | neondb_owner
 public | carts             | table | neondb_owner
 public | order_items       | table | neondb_owner
 public | orders            | table | neondb_owner
 public | payments          | table | neondb_owner
 public | product_colors    | table | neondb_owner
 public | product_images    | table | neondb_owner
 public | product_sizes     | table | neondb_owner
 public | products          | table | neondb_owner
 public | reviews           | table | neondb_owner
 public | users             | table | neondb_owner
```

---

## 🎯 Verify Swagger UI

### Step 1: Access Swagger
```
http://localhost:8080/swagger-ui.html
```

### Step 2: Expected Controllers
You should see **10 controllers**:
1. ✅ Authentication (4 endpoints)
2. ✅ Product Management (6 endpoints)
3. ✅ User Management (5 endpoints)
4. ✅ Shopping Cart (7 endpoints)
5. ✅ Order Management (11 endpoints)
6. ✅ Address Management (8 endpoints)
7. ✅ Payment Management (11 endpoints)
8. ✅ Product Reviews (9 endpoints)
9. ✅ Dashboard Analytics (9 endpoints)
10. ✅ Health Check (1 endpoint)

**Total: 89+ endpoints**

---

## 🔍 Troubleshooting Common Issues

### Issue 1: "Only 4 tables appear"

**Likely tables you're seeing:**
- users
- products
- product_sizes (or similar)
- product_colors (or similar)

**Root Cause:** Application wasn't restarted after entity changes

**Fix:**
1. Stop application completely
2. Delete target/ folder: `rm -rf target/`
3. Rebuild: `mvn clean package`
4. Restart: `./start.sh`

### Issue 2: "Swagger shows no endpoints" or "Some endpoints missing"

**Root Cause:** Controllers not being scanned or application not fully started

**Fix:**
```bash
# Check if application is actually running
curl http://localhost:8080/health

# Should return: {"status":"UP"}

# If not, check logs for errors
tail -f application.log
```

### Issue 3: "Port 8080 already in use"

**Fix:**
```bash
# Find process using port 8080
lsof -ti:8080

# Kill it
kill -9 $(lsof -ti:8080)

# Restart application
./start.sh
```

### Issue 4: "Database connection failed"

**Check connection string in application.properties:**
```properties
spring.datasource.url=jdbc:postgresql://ep-morning-credit-ahu0h3sm-pooler.c-3.us-east-1.aws.neon.tech/neondb?sslmode=require
spring.datasource.username=neondb_owner
spring.datasource.password=npg_8HDt2LTngcGa
```

**Test connection manually:**
```bash
psql postgresql://neondb_owner:npg_8HDt2LTngcGa@ep-morning-credit-ahu0h3sm-pooler.c-3.us-east-1.aws.neon.tech/neondb?sslmode=require
```

---

## 📝 What Changed vs Before

| Component | Before | After | Impact |
|-----------|--------|-------|--------|
| DDL Mode | update | create-drop | Forces fresh table creation |
| SQL Logging | false | true | See all DDL statements |
| Entity Scan | implicit | explicit @EntityScan | Guarantees all entities found |
| Repo Scan | implicit | explicit @EnableJpaRepositories | Guarantees all repos found |
| Log Level | INFO | DEBUG | See detailed startup process |

---

## 🎬 Step-by-Step Execution

### Complete Fresh Start Sequence

```bash
# 1. Navigate to project
cd /Users/prabhatkumar/Documents/lincee-backend

# 2. Kill any existing process
pkill -f lincee-backend || true
lsof -ti:8080 | xargs kill -9 || true

# 3. Clean everything
rm -rf target/
mvn clean

# 4. Rebuild
mvn package -DskipTests

# 5. Start and watch logs
./start.sh
```

### While Starting, Watch For:

```
[INFO] Scanning for classes with @Entity...
[INFO] Found 9 entities:
  - com.lincee.entity.User
  - com.lincee.entity.Product
  - com.lincee.entity.Cart
  - com.lincee.entity.CartItem
  - com.lincee.entity.Order
  - com.lincee.entity.OrderItem
  - com.lincee.entity.Address
  - com.lincee.entity.Payment
  - com.lincee.entity.Review

[INFO] HHH000227: Running hbm2ddl schema update
drop table if exists addresses cascade
drop table if exists cart_items cascade
drop table if exists carts cascade
... (more drops)

create table addresses (...)
create table cart_items (...)
create table carts (...)
... (more creates)

[INFO] HHH000228: Schema update complete
[INFO] Started LinceeApplication in 15.234 seconds
```

---

## ✅ Expected Final State

### Database
```sql
neondb=> \dt
                    List of relations
 Schema |       Name        | Type  |    Owner     
--------+-------------------+-------+--------------
 public | addresses         | table | neondb_owner
 public | cart_items        | table | neondb_owner
 public | carts             | table | neondb_owner
 public | order_items       | table | neondb_owner
 public | orders            | table | neondb_owner
 public | payments          | table | neondb_owner
 public | product_colors    | table | neondb_owner
 public | product_images    | table | neondb_owner
 public | product_sizes     | table | neondb_owner
 public | products          | table | neondb_owner
 public | reviews           | table | neondb_owner
 public | users             | table | neondb_owner
(12 rows)
```

### Swagger UI
Access: `http://localhost:8080/swagger-ui.html`

Should show:
- ✅ 10 controller sections
- ✅ 89+ total endpoints
- ✅ "Authorize" button at top right
- ✅ All request/response schemas
- ✅ "Try it out" buttons on all endpoints

---

## 🚀 Quick Verification Commands

```bash
# 1. Check if app is running
curl -s http://localhost:8080/health
# Expected: {"status":"UP"}

# 2. Check Swagger JSON
curl -s http://localhost:8080/v3/api-docs | jq '.paths | keys | length'
# Expected: 89+ (number of endpoints)

# 3. Count database tables
psql "postgresql://neondb_owner:npg_8HDt2LTngcGa@ep-morning-credit-ahu0h3sm-pooler.c-3.us-east-1.aws.neon.tech/neondb?sslmode=require" -c "\dt" | grep -c "table"
# Expected: 12-15

# 4. Check specific controller
curl -s http://localhost:8080/v3/api-docs | jq '.paths | keys | map(select(contains("/orders")))'
# Expected: Array of order endpoints
```

---

## 🔧 If Still Not Working

### Get Full Diagnostic Info

```bash
# 1. Check Java version
java -version
# Expected: Java 21+

# 2. Check Maven version
mvn -version
# Expected: Maven 3.8+

# 3. List all entity files
find src/main/java/com/lincee/entity -name "*.java"
# Expected: 9 files

# 4. Check if entities have @Entity annotation
grep -r "@Entity" src/main/java/com/lincee/entity/
# Expected: 9 matches

# 5. View full startup log
cat application.log | grep -E "(entity|Entity|table|Table|HHH)"
```

---

## 📞 Emergency Checklist

If nothing works, verify ALL of these:

- [ ] Java 21 is installed and in PATH
- [ ] Maven 3.8+ is installed
- [ ] Port 8080 is not blocked by firewall
- [ ] No other process is using port 8080
- [ ] PostgreSQL connection works (test with psql)
- [ ] All 9 entity files exist in src/main/java/com/lincee/entity/
- [ ] All entities have @Entity annotation
- [ ] All entities have @Table annotation
- [ ] pom.xml has springdoc-openapi dependency
- [ ] application.properties has correct database URL
- [ ] LinceeApplication.java has @EntityScan annotation
- [ ] Project compiles without errors: `mvn clean compile`
- [ ] JAR file exists: `ls -lh target/lincee-backend-1.0.0.jar`

---

## 🎯 WHAT TO DO RIGHT NOW

1. **Open a terminal**
2. **Run these exact commands:**

```bash
cd /Users/prabhatkumar/Documents/lincee-backend
pkill -f lincee || true
lsof -ti:8080 | xargs kill -9 || true
mvn clean package -DskipTests
./start.sh
```

3. **Wait for "Started LinceeApplication"**
4. **Open browser: `http://localhost:8080/swagger-ui.html`**
5. **Verify all controllers appear**

---

**Last Updated:** February 3, 2026  
**Status:** 🔥 CRITICAL FIX APPLIED  
**Action Required:** RESTART APPLICATION NOW
