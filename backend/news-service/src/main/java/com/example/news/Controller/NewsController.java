package com.example.news.Controller;

import com.example.news.Article.NewsArticle;
import com.example.news.service.NewsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/news")
public class NewsController {

    private final NewsService service;

    public NewsController(NewsService service) {
        this.service = service;
    }

    @GetMapping("/articles")
    public List<NewsArticle> getAll(@RequestParam(required = false) String keyword) {
        if (keyword != null && !keyword.isBlank()) {
            return service.searchByKeyword(keyword);
        }
        return service.getAllArticles();
    }

    @PostMapping("/post")
    public  ResponseEntity<?> postArticle(@RequestBody NewsArticle article) {
        if (article.getTitle() == null || article.getTitle().isBlank()) {
            return ResponseEntity.badRequest().body("Title is required.");
        }
        article.setPublishedAt(LocalDateTime.now());
        NewsArticle saved = service.saveArticle(article);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/language/{lang}")
    public List<NewsArticle> getNewsByLanguage(@PathVariable String lang) {
        return service.getNewsByLanguage(lang);
    }

    @GetMapping("/search")
    public List<NewsArticle> searchByKeywordAndLanguage(@RequestParam String keyword,
                                                         @RequestParam String language) {
        return service.searchByKeywordAndLanguage(keyword, language);
    }
}



// package com.example.news.Controller;

// // import java.io.IOException;
// import java.time.LocalDateTime;

// import java.util.List;
// // import java.util.Arrays;
// // import java.util.Objects;
// // import java.util.stream.Collectors;

// // import com.fasterxml.jackson.core.JsonProcessingException;

// import lombok.extern.slf4j.Slf4j;

// // import org.apache.http.HttpStatus;

// // import org.springframework.beans.factory.annotation.Autowired;
// // import org.springframework.data.domain.Page;
// // import org.springframework.format.annotation.DateTimeFormat;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RestController;

// import com.example.news.Article.NewsArticle;
// import com.example.news.service.NewsService;




// @RestController
// @Slf4j
// @RequestMapping("/api/news")
// public class NewsController {

//     private final NewsService service;

//     public NewsController(NewsService service) {
//         this.service = service;
//     }

//     @GetMapping("/articles")
//     public List<NewsArticle> getAll(@RequestParam(required = false) String keyword) {
//         if (keyword != null && !keyword.isBlank()) {
//             return service.searchByKeyword(keyword);
//         }
//         return service.getAllArticles();
//     }

//     @PostMapping("/post")
//     public ResponseEntity<?> postArticle(@RequestBody NewsArticle article) {
//         if (article.getTitle() == null || article.getTitle().isBlank()) {
//             return ResponseEntity.badRequest().body("Title is required.");
//         }
//         article.setPublishedAt(LocalDateTime.now());
//         NewsArticle saved = service.saveArticle(article);
//         return ResponseEntity.ok(saved);
//     }

//     @GetMapping("/language/{lang}")
//     public List<NewsArticle> getNewsByLanguage(@PathVariable String lang) {
//         return service.getNewsByLanguage(lang);
//     }

//     @GetMapping("/search")
//     public List<NewsArticle> searchByKeywordAndLanguage(@RequestParam String keyword,
//                                                          @RequestParam String language) {
//         return service.searchByKeywordAndLanguage(keyword, language);
//     }
// }



    


