# News Aggregator Project

A comprehensive news aggregation platform built with React.js frontend, Spring Boot microservices backend, and MySQL database.

## 🚀 Features

### User Features
- User registration with email verification
- Personalized news feed based on interests
- News categories: Sports, Entertainment, Technology, Politics, Business, etc.
- Like, bookmark, and comment on news articles
- View saved/bookmarked news
- Responsive dashboard

### Admin Features
- Admin registration with email verification
- Analytics dashboard showing user interests
- Website traffic analytics
- User management
- News management

### Technical Features
- RSS feed integration for real-time news
- JWT token-based authentication
- Email verification system
- Microservices architecture
- Docker containerization
- Responsive UI with Tailwind CSS

## 🏗️ Architecture

```
news-aggregator/
├── frontend/                 # React.js application
├── backend/                  # Spring Boot microservices
│   ├── auth-service/        # Authentication & authorization
│   ├── user-service/        # User management
│   ├── news-service/        # News aggregation & RSS feeds
│   ├── analytics-service/   # Analytics & reporting
│   └── gateway-service/     # API Gateway
├── database/                # MySQL database scripts
└── docker/                 # Docker configuration
```

## 🛠️ Tech Stack

### Frontend
- React.js 18
- Tailwind CSS
- Axios for API calls
- React Router for navigation
- React Query for state management

### Backend
- Spring Boot 3.x
- Spring Security with JWT
- Spring Data JPA
- Spring Cloud Gateway
- Spring Mail for email verification

### Database
- MySQL 8.0

### DevOps
- Docker & Docker Compose
- Maven for Java dependencies
- npm for Node.js dependencies

## 🚀 Quick Start

### Prerequisites
- Docker and Docker Compose
- Java 17+
- Node.js 18+
- MySQL 8.0

### Running with Docker (Recommended)

1. Clone the repository
2. Navigate to the project directory
3. Run the following command:

```bash
docker-compose up --build
```

The application will be available at:
- Frontend: http://localhost:3000
- Backend API: http://localhost:8080
- Admin Dashboard: http://localhost:3000/admin

### Manual Setup

#### Backend Setup
1. Navigate to `backend/`
2. Update database configuration in `application.yml`
3. Run: `mvn clean install`
4. Start each service: `mvn spring-boot:run`

#### Frontend Setup
1. Navigate to `frontend/`
2. Install dependencies: `npm install`
3. Start development server: `npm start`

## 📋 API Documentation

### Authentication Endpoints
- `POST /api/auth/register` - User registration
- `POST /api/auth/login` - User login
- `POST /api/auth/verify-email` - Email verification
- `POST /api/auth/refresh` - Refresh JWT token

### User Endpoints
- `GET /api/user/profile` - Get user profile
- `PUT /api/user/profile` - Update user profile
- `GET /api/user/preferences` - Get user preferences
- `PUT /api/user/preferences` - Update user preferences

### News Endpoints
- `GET /api/news/feed` - Get personalized news feed
- `GET /api/news/categories` - Get news categories
- `POST /api/news/like/{id}` - Like a news article
- `POST /api/news/bookmark/{id}` - Bookmark a news article
- `POST /api/news/comment/{id}` - Comment on a news article

### Admin Endpoints
- `GET /api/admin/analytics` - Get website analytics
- `GET /api/admin/users` - Get all users
- `GET /api/admin/news-stats` - Get news statistics

## 🔧 Configuration

### Environment Variables

Create `.env` files in respective directories:

#### Backend (.env)
```
DB_HOST=localhost
DB_PORT=3306
DB_NAME=news_aggregator
DB_USERNAME=root
DB_PASSWORD=root
JWT_SECRET=your-jwt-secret
SMTP_HOST=smtp.gmail.com
SMTP_PORT=587
SMTP_USERNAME=your-email@gmail.com
SMTP_PASSWORD=your-app-password
```

#### Frontend (.env)
```
REACT_APP_API_URL=http://localhost:8080
REACT_APP_WS_URL=ws://localhost:8080
```

## 📊 Database Schema

### Users Table
```sql
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('USER', 'ADMIN') DEFAULT 'USER',
    email_verified BOOLEAN DEFAULT FALSE,
    verification_token VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### User Preferences Table
```sql
CREATE TABLE user_preferences (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    category VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
```

### News Articles Table
```sql
CREATE TABLE news_articles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(500) NOT NULL,
    description TEXT,
    content LONGTEXT,
    url VARCHAR(500),
    image_url VARCHAR(500),
    category VARCHAR(50),
    source VARCHAR(100),
    published_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### User Interactions Table
```sql
CREATE TABLE user_interactions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    article_id BIGINT NOT NULL,
    interaction_type ENUM('LIKE', 'BOOKMARK', 'COMMENT') NOT NULL,
    comment_text TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (article_id) REFERENCES news_articles(id)
);
```

## 🐳 Docker Configuration

The project includes Docker configurations for easy deployment:

- `docker-compose.yml` - Main orchestration file
- `Dockerfile` files for each service
- `.dockerignore` files for optimization

## 📈 Analytics Features

### User Analytics
- Most popular news categories
- User engagement metrics
- Peak usage times
- User retention rates

### News Analytics
- Most read articles
- Category popularity
- Source performance
- Trending topics

## 🔒 Security Features

- JWT token-based authentication
- Email verification for account activation
- Password encryption with BCrypt
- CORS configuration
- Input validation and sanitization
- Rate limiting

## 🧪 Testing

### Backend Tests
```bash
cd backend
mvn test
```

### Frontend Tests
```bash
cd frontend
npm test
```

## 📝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License.

## 🆘 Support

For support and questions, please create an issue in the repository.

---

**Note**: Make sure to update the email configuration and JWT secret in production environments. 