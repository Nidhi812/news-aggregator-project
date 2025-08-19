// package com.example.news.Article;
// import java.time.LocalDateTime;
// // import java.util.Date;

// import jakarta.persistence.Column;
// import jakarta.persistence.Entity;
// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.GenerationType;
// import jakarta.persistence.Id;
// import lombok.AllArgsConstructor;
// import lombok.Data;
// import lombok.NoArgsConstructor;


// @Entity
// @Data
// @NoArgsConstructor
// @AllArgsConstructor
// public class NewsArticle {
// 	@Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long id;

//     private String title;
//     private String description;
//     private String source;
//     private LocalDateTime publishedAt;
//     @Column(columnDefinition = "TEXT")
//     private String content;   // 
//     @Column(length = 1000)
//     private String imageUrl;  // 
//     @Column(length = 50)
//     private String language;
// }



package com.example.news.Article;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Index;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "news_article", indexes = {
    @Index(name = "idx_news_category", columnList = "category")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsArticle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String source;
    private LocalDateTime publishedAt;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(length = 1000)
    private String imageUrl;

    @Column(length = 50)
    private String language;

    // NEW field for category filtering
    @Column(length = 50)
    private String category;
}
