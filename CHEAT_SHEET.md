# News Aggregator Project - Cheat Sheet

## 🚀 Quick Start Commands

### 1. Using Docker (Recommended)
```bash
# Build and start all services
docker-compose up --build

# Start in background
docker-compose up -d --build

# Stop all services
docker-compose down

# View logs
docker-compose logs -f

# View logs for specific service
docker-compose logs -f frontend
docker-compose logs -f gateway-service
docker-compose logs -f mysql
```

### 2. Manual Setup

#### Backend Services
```bash
# Build all backend services
cd backend
mvn clean install

# Start Gateway Service
cd gateway-service
mvn spring-boot:run

# Start Auth Service
cd auth-service
mvn spring-boot:run

# Start User Service
cd user-service
mvn spring-boot:run

# Start News Service
cd news-service
mvn spring-boot:run

# Start Analytics Service
cd analytics-service
mvn spring-boot:run
```

#### Frontend
```bash
# Install dependencies
cd frontend
npm install

# Start development server
npm start

# Build for production
npm run build
```

## 🌐 Application URLs

- **Frontend**: http://localhost:3000
- **Backend API**: http://localhost:8080
- **Admin Dashboard**: http://localhost:3000/admin
- **User Dashboard**: http://localhost:3000/dashboard

## 🔐 Default Credentials

### Admin Account
- **Email**: admin@newsaggregator.com
- **Password**: admin123
- **Role**: ADMIN

### Test User Account
- **Email**: user@example.com
- **Password**: user123
- **Role**: USER

## 📊 Database Information

### MySQL Connection
- **Host**: localhost
- **Port**: 3306
- **Database**: news_aggregator
- **Username**: root
- **Password**: root

### Database Commands
```bash
# Connect to MySQL
mysql -u root -p

# Show databases
SHOW DATABASES;

# Use news aggregator database
USE news_aggregator;

# Show tables
SHOW TABLES;

# View users
SELECT * FROM users;

# View news articles
SELECT * FROM news_articles;
```

## 🔧 Configuration Files

### Environment Variables
```bash
# Backend (.env)
DB_HOST=localhost
DB_PORT=3306
DB_NAME=news_aggregator
DB_USERNAME=root
DB_PASSWORD=root
JWT_SECRET=your-super-secret-jwt-key-for-news-aggregator-2024
SMTP_HOST=smtp.gmail.com
SMTP_PORT=587
SMTP_USERNAME=your-email@gmail.com
SMTP_PASSWORD=your-app-password

# Frontend (.env)
REACT_APP_API_URL=http://localhost:8080
REACT_APP_WS_URL=ws://localhost:8080
```

## 📋 API Endpoints

### Authentication
- `POST /api/auth/register` - User registration
- `POST /api/auth/login` - User login
- `POST /api/auth/verify-email` - Email verification
- `POST /api/auth/validate-token` - Token validation

### User Management
- `GET /api/user/profile` - Get user profile
- `PUT /api/user/profile` - Update user profile
- `GET /api/user/preferences` - Get user preferences
- `PUT /api/user/preferences` - Update user preferences

### News Management
- `GET /api/news/feed` - Get personalized news feed
- `GET /api/news/categories` - Get news categories
- `POST /api/news/like/{id}` - Like a news article
- `POST /api/news/bookmark/{id}` - Bookmark a news article
- `DELETE /api/news/bookmark/{id}` - Remove bookmark
- `POST /api/news/comment/{id}` - Comment on a news article

### Admin Analytics
- `GET /api/admin/analytics` - Get website analytics
- `GET /api/admin/users` - Get all users
- `GET /api/admin/news-stats` - Get news statistics

## 🐳 Docker Commands

### Container Management
```bash
# List running containers
docker ps

# Stop specific container
docker stop container_name

# Remove container
docker rm container_name

# View container logs
docker logs container_name

# Execute command in container
docker exec -it container_name bash
```

### Image Management
```bash
# List images
docker images

# Remove image
docker rmi image_name

# Build specific service
docker-compose build service_name
```

## 🔍 Troubleshooting

### Common Issues

#### 1. Port Already in Use
```bash
# Find process using port
netstat -ano | findstr :8080
netstat -ano | findstr :3000

# Kill process (Windows)
taskkill /PID <process_id> /F

# Kill process (Linux/Mac)
kill -9 <process_id>
```

#### 2. Database Connection Issues
```bash
# Check MySQL status
docker-compose logs mysql

# Restart MySQL
docker-compose restart mysql

# Reset database
docker-compose down -v
docker-compose up --build
```

#### 3. Frontend Build Issues
```bash
# Clear npm cache
npm cache clean --force

# Remove node_modules and reinstall
rm -rf node_modules package-lock.json
npm install
```

#### 4. Backend Build Issues
```bash
# Clean Maven cache
mvn clean

# Update dependencies
mvn dependency:resolve

# Skip tests
mvn install -DskipTests
```

## 📝 Development Workflow

### 1. Code Changes
```bash
# Frontend changes are hot-reloaded
# Backend changes require restart
docker-compose restart service_name
```

### 2. Database Changes
```bash
# Update schema
# Modify database/init.sql
# Restart MySQL container
docker-compose restart mysql
```

### 3. Adding New Features
1. Update backend service code
2. Add new API endpoints
3. Update frontend components
4. Test functionality
5. Commit changes

## 🧪 Testing

### Backend Tests
```bash
# Run all tests
mvn test

# Run specific test
mvn test -Dtest=TestClassName

# Run with coverage
mvn jacoco:report
```

### Frontend Tests
```bash
# Run tests
npm test

# Run tests with coverage
npm test -- --coverage

# Run tests in watch mode
npm test -- --watch
```

## 📦 Deployment

### Production Build
```bash
# Build frontend for production
cd frontend
npm run build

# Build backend services
cd backend
mvn clean package -DskipTests

# Create production Docker images
docker-compose -f docker-compose.prod.yml up --build
```

### Environment Variables for Production
```bash
# Update .env files with production values
JWT_SECRET=your-production-jwt-secret
DB_HOST=your-production-db-host
SMTP_USERNAME=your-production-email
SMTP_PASSWORD=your-production-password
```

## 🔒 Security Checklist

- [ ] Change default JWT secret
- [ ] Update email configuration
- [ ] Set strong database passwords
- [ ] Configure CORS properly
- [ ] Enable HTTPS in production
- [ ] Set up proper logging
- [ ] Configure rate limiting
- [ ] Set up monitoring

## 📊 Monitoring

### Health Checks
- Gateway: http://localhost:8080/actuator/health
- Auth Service: http://localhost:8081/actuator/health
- User Service: http://localhost:8082/actuator/health
- News Service: http://localhost:8083/actuator/health
- Analytics Service: http://localhost:8084/actuator/health

### Logs
```bash
# View all logs
docker-compose logs

# View specific service logs
docker-compose logs service_name

# Follow logs
docker-compose logs -f service_name
```

## 🚀 Performance Tips

1. **Database Optimization**
   - Add proper indexes
   - Use connection pooling
   - Optimize queries

2. **Frontend Optimization**
   - Use React.memo for components
   - Implement lazy loading
   - Optimize bundle size

3. **Backend Optimization**
   - Use caching (Redis)
   - Implement pagination
   - Optimize RSS feed parsing

## 📚 Useful Commands

### Git Commands
```bash
# Initialize repository
git init

# Add all files
git add .

# Commit changes
git commit -m "Initial commit"

# Create branch
git checkout -b feature-name

# Merge branch
git merge feature-name
```

### System Commands
```bash
# Check system resources
docker stats

# View disk usage
df -h

# Check memory usage
free -h

# View network connections
netstat -tulpn
```

---

**Note**: This cheat sheet contains the most commonly used commands and configurations. For detailed documentation, refer to the README.md file. 