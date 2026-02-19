# 🚀 QUICK START SERVICES

## ⚡ Fast Commands to Start All Services

### Option 1: Open Terminal Windows for Each Service

**Terminal 1 - Eureka Server (Port 8761)**
```bash
cd eureka-server
mvn spring-boot:run
```
Wait for: `Started EurekaServerApplication`

**Terminal 2 - Auth Service (Port 8081)**
```bash
cd auth-service
mvn clean spring-boot:run -DskipTests
```
Wait for: `Started AuthServiceApplication`

**Terminal 3 - API Gateway (Port 8082)**
```bash
cd api-gateway
mvn spring-boot:run
```
Wait for: `Started ApiGatewayApplication`

**Terminal 4 - React Frontend (Port 3001)**
```bash
cd react-frontend
npm start
```
Wait for: `Compiled successfully`

---

## ✅ Verification Checklist After Starting

### 1. Eureka Server Health
```bash
curl http://localhost:8761
# Should show Eureka UI
```

### 2. Check Registered Services
```bash
curl http://localhost:8761/eureka/apps
# Should list auth-service and api-gateway
```

### 3. Auth Service Health
```bash
curl http://localhost:8081/actuator/health
# Response: {"status":"UP"}
```

### 4. API Gateway Health
```bash
curl http://localhost:8082/actuator/health
# Response: {"status":"UP"}
```

### 5. React Frontend
```
Open: http://localhost:3001 in browser
Should load login page
```

---

## 🧪 Quick Test After Startup

### Test 1: Register a User

**Via cURL:**
```bash
curl -X POST http://localhost:8082/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "TestPass123!"
  }'
```

**Expected Response:**
```json
{
  "success": true,
  "message": "User Registered Successfully"
}
```

### Test 2: Login with User

**Via cURL:**
```bash
curl -X POST http://localhost:8082/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "TestPass123!"
  }'
```

**Expected Response:**
```json
{
  "success": true,
  "message": "Login successful",
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

### Test 3: Via React Frontend
1. Open http://localhost:3001
2. Click "Don't have an account? Register"
3. Enter email: `test2@example.com`
4. Enter password: `TestPass123!`
5. Confirm password: `TestPass123!`
6. Click Register
7. Should see success message and redirect to login
8. Login with same credentials
9. Should see success message and token in response

---

## 🆘 Troubleshooting Commands

### Port Conflict on Windows

**Check if port is in use:**
```bash
netstat -ano | findstr :8082
```

**Kill process using port 8082:**
```bash
# Replace PID with the actual PID from above command
taskkill /PID <PID> /F
```

**Common Ports:**
- 8761: Eureka Server
- 8081: Auth Service
- 8082: API Gateway
- 3001: React Frontend

### Maven Clean Rebuild (if issues)

```bash
cd auth-service
mvn clean compile -DskipTests
mvn clean spring-boot:run -DskipTests
```

### Clear React Cache (if frontend issues)

```bash
cd react-frontend
rm -r node_modules              # Linux/Mac
rmdir /s node_modules           # Windows
npm install
npm start
```

---

## 📝 Service Port Summary

| Service | Port | URL |
|---------|------|-----|
| Eureka Server | 8761 | http://localhost:8761 |
| Auth Service | 8081 | http://localhost:8081 |
| API Gateway | 8082 | http://localhost:8082 |
| React Frontend | 3001 | http://localhost:3001 |

---

## 🎯 Common Issues Solved

### Issue: "Port 8082 already in use"
**Solution:**
```bash
# Stop previous gateway process
netstat -ano | findstr :8082
taskkill /PID <PID> /F

# Restart auth-service
```

### Issue: "Cannot find symbol: enableEurekaClient"
**Solution:** This is fixed in current code, should not occur

### Issue: "H2 console 404"
**Solution:** H2 console accessed at: http://localhost:8081/h2-console
- Username: sa
- Password: (blank)
- Default database should be selected

### Issue: "CORS error from React to Java"
**Solution:** Already configured in API Gateway, should work

### Issue: "React npm packages missing"
**Solution:**
```bash
cd react-frontend
npm install
npm start
```

---

## 💡 Tips for Running the System

1. **Always start Eureka first** - Other services register with it
2. **Start Auth Service before API Gateway** - Gateway needs to discover auth-service
3. **React can start anytime** - But backend services should be ready
4. **Use separate terminals** - Keep one for each service
5. **Monitor logs** - Watch terminal output for errors
6. **Don't use Ctrl+Z** - Use Ctrl+C to stop services gracefully

---

## 🔄 Restarting Everything

If something goes wrong:

```bash
# Stop all terminals (Ctrl+C in each)
# Kill any remaining processes
netstat -ano | findstr :8761
netstat -ano | findstr :8081
netstat -ano | findstr :8082
# taskkill /PID <PID> /F for each

# Wait 5 seconds
# Start fresh in order:
# 1. Eureka
# 2. Auth Service
# 3. API Gateway
# 4. React
```

---

## 📊 How to Know System is Ready

Look for these messages in terminal output:

**Eureka Server:**
```
Started EurekaServerApplication in X.XXX seconds
```

**Auth Service:**
```
Started AuthServiceApplication in X.XXX seconds
Tomcat started on port 8081 (http) with context path ''
```

**API Gateway:**
```
Started ApiGatewayApplication in X.XXX seconds
Tomcat started on port 8082 (http) with context path ''
```

**React Frontend:**
```
Compiled successfully!
On Your Network: http://192.168.X.X:3001
```

---

## 🎊 System Ready Verification Checklist

- [ ] Eureka Dashboard accessible (http://localhost:8761)
- [ ] Eureka shows "Instances currently registered with Eureka"
- [ ] Both auth-service and api-gateway listed
- [ ] React Frontend loads (http://localhost:3001)
- [ ] Login page displays
- [ ] Register link visible
- [ ] No errors in any terminal
- [ ] No errors in browser console (F12)

---

## 📚 Additional Resources

- **IMPLEMENTATION_GUIDE.md** - Full architecture and code details
- **API_TESTING_GUIDE.md** - Detailed test scenarios
- **DEPLOYMENT_CHECKLIST.md** - Deployment and viva prep
- **PROJECT_SUMMARY.md** - Project overview

---

**Last Updated**: February 19, 2026
**Version**: Quick Start v1.0
**Status**: Ready to Use ✅
