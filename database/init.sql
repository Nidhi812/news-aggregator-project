-- News Aggregator Database Initialization Script

-- Create database if not exists
CREATE DATABASE IF NOT EXISTS news_aggregator;
USE news_aggregator;

-- Users table
CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('USER', 'ADMIN') DEFAULT 'USER',
    email_verified BOOLEAN DEFAULT FALSE,
    verification_token VARCHAR(255),
    reset_token VARCHAR(255),
    reset_token_expiry TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_email (email),
    INDEX idx_verification_token (verification_token)
);

-- User preferences table
CREATE TABLE IF NOT EXISTS user_preferences (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    category VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE KEY unique_user_category (user_id, category)
);

-- News sources table
CREATE TABLE IF NOT EXISTS news_sources (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    url VARCHAR(500) NOT NULL,
    rss_feed_url VARCHAR(500),
    category VARCHAR(50),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- News articles table
CREATE TABLE IF NOT EXISTS news_articles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(500) NOT NULL,
    description TEXT,
    content LONGTEXT,
    url VARCHAR(500),
    image_url VARCHAR(500),
    category VARCHAR(50),
    source_id BIGINT,
    source_name VARCHAR(100),
    author VARCHAR(100),
    published_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (source_id) REFERENCES news_sources(id) ON DELETE SET NULL,
    INDEX idx_category (category),
    INDEX idx_published_at (published_at),
    INDEX idx_source_id (source_id)
);

-- User interactions table
CREATE TABLE IF NOT EXISTS user_interactions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    article_id BIGINT NOT NULL,
    interaction_type ENUM('LIKE', 'BOOKMARK', 'COMMENT', 'VIEW') NOT NULL,
    comment_text TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (article_id) REFERENCES news_articles(id) ON DELETE CASCADE,
    UNIQUE KEY unique_user_article_interaction (user_id, article_id, interaction_type),
    INDEX idx_user_id (user_id),
    INDEX idx_article_id (article_id),
    INDEX idx_interaction_type (interaction_type)
);

-- Website analytics table
CREATE TABLE IF NOT EXISTS website_analytics (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    page_visited VARCHAR(100),
    user_id BIGINT NULL,
    ip_address VARCHAR(45),
    user_agent TEXT,
    session_id VARCHAR(255),
    visit_date DATE,
    visit_time TIME,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL,
    INDEX idx_visit_date (visit_date),
    INDEX idx_user_id (user_id)
);

-- Insert default admin user (password: admin123)
INSERT INTO users (name, email, password, role, email_verified) VALUES 
('Admin User', 'admin@newsaggregator.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'ADMIN', TRUE);

-- Insert sample news sources
INSERT INTO news_sources (name, url, rss_feed_url, category) VALUES
('BBC News', 'https://www.bbc.com/news', 'https://feeds.bbci.co.uk/news/rss.xml', 'general'),
('CNN', 'https://www.cnn.com', 'http://rss.cnn.com/rss/edition.rss', 'general'),
('TechCrunch', 'https://techcrunch.com', 'https://techcrunch.com/feed/', 'technology'),
('ESPN', 'https://www.espn.com', 'https://www.espn.com/espn/rss/news', 'sports'),
('Entertainment Weekly', 'https://ew.com', 'https://ew.com/feed/', 'entertainment'),
('Reuters', 'https://www.reuters.com', 'https://feeds.reuters.com/reuters/topNews', 'general'),
('The Verge', 'https://www.theverge.com', 'https://www.theverge.com/rss/index.xml', 'technology'),
('Sports Illustrated', 'https://www.si.com', 'https://www.si.com/rss/si_topstories.xml', 'sports');

-- Insert sample news categories
INSERT INTO user_preferences (user_id, category) VALUES
(1, 'technology'),
(1, 'sports'),
(1, 'entertainment');

-- Create indexes for better performance
CREATE INDEX idx_news_articles_category_published ON news_articles(category, published_at);
CREATE INDEX idx_user_interactions_user_type ON user_interactions(user_id, interaction_type);
CREATE INDEX idx_website_analytics_date_user ON website_analytics(visit_date, user_id); 