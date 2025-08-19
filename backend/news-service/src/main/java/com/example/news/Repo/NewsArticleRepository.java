package com.example.news.Repo;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.news.Article.NewsArticle;

public interface NewsArticleRepository extends JpaRepository<NewsArticle, Long> {
    List<NewsArticle> findByTitleContainingIgnoreCase(String keyword);
    List<NewsArticle> findByLanguageIgnoreCase(String language);
    List<NewsArticle> findByLanguageIgnoreCaseAndTitleContainingIgnoreCaseOrLanguageIgnoreCaseAndDescriptionContainingIgnoreCase(
        String lang1, String keyword1, String lang2, String keyword2);

    @Query("SELECT a FROM NewsArticle a WHERE (:keyword IS NULL OR LOWER(a.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(a.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
           "(:startDate IS NULL OR a.publishedAt >= :startDate) AND (:endDate IS NULL OR a.publishedAt <= :endDate)")
    Page<NewsArticle> searchByFilters(@Param("keyword") String keyword,
                                      @Param("startDate") LocalDateTime startDate,
                                      @Param("endDate") LocalDateTime endDate,
                                      Pageable pageable);

    List<NewsArticle> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String keyword, String keyword2);

  @Query(value = """
    SELECT * FROM news_article
    WHERE title LIKE CONCAT('%', :keyword, '%')
       OR description LIKE CONCAT('%', :keyword, '%')
       OR content LIKE CONCAT('%', :keyword, '%')
    """, nativeQuery = true)
List<NewsArticle>fullTextSearch(@Param("keyword") String keyword);


    
    boolean existsByTitle(String title);


    // ✅ New methods for category filtering
    List<NewsArticle> findByCategoryIgnoreCase(String category);

    List<NewsArticle> findByCategoryIgnoreCaseAndLanguageIgnoreCase(String category, String language);
}