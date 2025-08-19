package com.example.news.service;

import com.example.news.Article.NewsArticle;
import com.example.news.Repo.NewsArticleRepository;
import com.rometools.rome.feed.module.Module;
import com.rometools.rome.feed.synd.*;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import com.rometools.modules.mediarss.MediaEntryModule;
import com.rometools.modules.mediarss.types.MediaContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;

@Service
public class NewsService {

    private static final Logger logger = LoggerFactory.getLogger(NewsService.class);

    @Autowired
    private NewsArticleRepository repo;

    // Feed source holder
    private static class FeedSource {
        String url;
        String language;
        FeedSource(String url, String language) {
            this.url = url;
            this.language = language;
        }
    }

    // List of RSS feeds with language mapping
    private final List<FeedSource> rssFeeds = Arrays.asList(
        new FeedSource("https://www.manatelangana.news/feed/", "Telugu"),
        new FeedSource("https://divyamarathi.bhaskar.com/rss-v1--category-5492.xml", "Marathi"),
        new FeedSource("https://www.amarujala.com/rss/india-news.xml", "Hindi"),
        new FeedSource("https://thekanal.in/ta-IN/feeds/category/general/rss.xml", "Tamil"),
        new FeedSource("https://www.indiatoday.in/rss/home", "English"),
        new FeedSource("https://feeds.feedburner.com/ndtvnews-top-stories", "English")
    );

    /**
     * Scheduled task: Fetch and store news from RSS feeds every 5 minutes
     */
    @Scheduled(fixedRate = 300000)
    public void fetchAndStoreFromRss() {
        for (FeedSource source : rssFeeds) {
            try {
                logger.info("Fetching feed: {}", source.url);

                URL url = new URL(source.url.trim());
                XmlReader reader = new XmlReader(url);
                SyndFeed feed = new SyndFeedInput().build(reader);

                if (feed.getEntries() == null || feed.getEntries().isEmpty()) {
                    logger.warn("No entries in feed: {}", source.url);
                    continue;
                }

                for (SyndEntry entry : feed.getEntries()) {
                    if (entry.getTitle() == null || entry.getTitle().isBlank()) {
                        logger.warn("Skipping entry with empty title");
                        continue;
                    }

                    if (repo.existsByTitle(entry.getTitle())) {
                        logger.info("Duplicate article found: {}", entry.getTitle());
                        continue;
                    }

                    NewsArticle article = new NewsArticle();
                    article.setTitle(entry.getTitle());
                    article.setDescription(entry.getDescription() != null ? entry.getDescription().getValue() : "");
                    article.setPublishedAt(entry.getPublishedDate() != null
                            ? entry.getPublishedDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
                            : LocalDateTime.now());
                    article.setSource(feed.getTitle());
                    article.setContent(
                            entry.getContents() != null && !entry.getContents().isEmpty()
                                    ? entry.getContents().get(0).getValue()
                                    : article.getDescription()
                    );
                    article.setImageUrl(extractImageUrl(entry));
                    article.setLanguage(source.language);

                    repo.save(article);
                    logger.info("Saved article: {}", entry.getTitle());
                }

            } catch (Exception e) {
                logger.error("Error fetching from: {}", source.url, e);
            }
        }
    }

    /**
     * Extract image URL from RSS entry
     */
    private String extractImageUrl(SyndEntry entry) {
        try {
            // Try Media RSS module
            Module module = entry.getModule(MediaEntryModule.URI);
            if (module instanceof MediaEntryModule) {
                MediaEntryModule media = (MediaEntryModule) module;
                MediaContent[] mediaContents = media.getMediaContents();
                if (mediaContents != null && mediaContents.length > 0 && mediaContents[0].getReference() != null) {
                    return mediaContents[0].getReference().toString();
                }
            }

            // Try Enclosure
            if (entry.getEnclosures() != null && !entry.getEnclosures().isEmpty()) {
                return entry.getEnclosures().get(0).getUrl();
            }

            // Try parsing from description HTML
            if (entry.getDescription() != null) {
                String desc = entry.getDescription().getValue();
                int start = desc.indexOf("<img");
                if (start != -1) {
                    start = desc.indexOf("src=\"", start);
                    if (start != -1) {
                        start += 5;
                        int end = desc.indexOf("\"", start);
                        if (end > start) {
                            return desc.substring(start, end);
                        }
                    }
                }
            }

        } catch (Exception e) {
            logger.warn("Image extraction error: {}", e.getMessage());
        }

        // Fallback placeholder
        return "https://via.placeholder.com/600x300?text=No+Image";
    }

    // -------------------- CRUD & Search Methods --------------------

    public NewsArticle saveArticle(NewsArticle article) {
        return repo.save(article);
    }

    public List<NewsArticle> getAllArticles() {
        return repo.findAll();
    }

    public List<NewsArticle> searchByKeyword(String text) {
        List<NewsArticle> results = repo.fullTextSearch(text);
        if (results.isEmpty()) {
            return repo.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(text, text);
        }
        return results;
    }

    public List<NewsArticle> getNewsByLanguage(String lang) {
        return repo.findByLanguageIgnoreCase(lang);
    }

    public List<NewsArticle> searchByKeywordAndLanguage(String keyword, String language) {
        return repo.findByLanguageIgnoreCaseAndTitleContainingIgnoreCaseOrLanguageIgnoreCaseAndDescriptionContainingIgnoreCase(
                language, keyword, language, keyword);
    }
}



// package com.example.news.service;

// import com.example.news.Article.NewsArticle;
// import com.example.news.Repo.NewsArticleRepository;
// import com.rometools.rome.feed.module.Module;
// import com.rometools.rome.feed.synd.*;
// import com.rometools.rome.io.SyndFeedInput;
// import com.rometools.rome.io.XmlReader;
// import com.rometools.modules.mediarss.MediaEntryModule;
// import com.rometools.modules.mediarss.types.MediaContent;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.scheduling.annotation.Scheduled;
// import org.springframework.stereotype.Service;

// import java.net.URL;
// import java.time.LocalDateTime;
// import java.time.ZoneId;
// import java.util.Arrays;
// import java.util.List;

// @Service
// public class NewsService {

//     private static final Logger logger = LoggerFactory.getLogger(NewsService.class);

//     @Autowired
//     private NewsArticleRepository repo;

//     // Helper class to store feed URL & language
//     private static class FeedSource {
//         String url;
//         String language;

//         FeedSource(String url, String language) {
//             this.url = url;
//             this.language = language;
//         }
//     }

//     // List of RSS feed sources
//     private final List<FeedSource> rssFeeds = Arrays.asList(
//             new FeedSource("https://www.manatelangana.news/feed/", "Telugu"),
//             new FeedSource("https://divyamarathi.bhaskar.com/rss-v1--category-5492.xml", "Marathi"),
//             new FeedSource("https://www.amarujala.com/rss/india-news.xml", "Hindi"),
//             new FeedSource("https://thekanal.in/ta-IN/feeds/category/general/rss.xml", "Tamil"),
//             new FeedSource("https://www.indiatoday.in/rss/home", "English"),
//             new FeedSource("https://feeds.feedburner.com/ndtvnews-top-stories", "English")
//     );

//     /**
//      * Fetch and store news articles from configured RSS feeds every 5 minutes.
//      */
//     @Scheduled(fixedRate = 300_000)
//     public void fetchAndStoreFromRss() {
//         for (FeedSource source : rssFeeds) {
//             processFeedSource(source);
//         }
//     }

//     private void processFeedSource(FeedSource source) {
//         try {
//             URL feedUrl = new URL(source.url.trim());
//             logger.info("Fetching feed from URL: {}", feedUrl);

//             SyndFeed feed = new SyndFeedInput().build(new XmlReader(feedUrl));

//             if (feed.getEntries() == null || feed.getEntries().isEmpty()) {
//                 logger.warn("No entries found in feed: {}", source.url);
//                 return;
//             }

//             for (SyndEntry entry : feed.getEntries()) {
//                 processFeedEntry(entry, feed.getTitle(), source.language);
//             }

//         } catch (Exception e) {
//             logger.error("Error fetching from: {}", source.url, e);
//         }
//     }

//     private void processFeedEntry(SyndEntry entry, String feedTitle, String language) {
//         if (entry.getTitle() == null || entry.getTitle().trim().isEmpty()) {
//             logger.warn("Skipping article with empty title");
//             return;
//         }

//         if (repo.existsByTitle(entry.getTitle())) {
//             logger.debug("Duplicate found, skipping: {}", entry.getTitle());
//             return;
//         }

//         NewsArticle article = mapToNewsArticle(entry, feedTitle, language);
//         repo.save(article);
//         logger.info("Saved article: {}", entry.getTitle());
//     }

//     private NewsArticle mapToNewsArticle(SyndEntry entry, String feedTitle, String language) {
//         NewsArticle article = new NewsArticle();
//         article.setTitle(entry.getTitle());
//         article.setDescription(entry.getDescription() != null ? entry.getDescription().getValue() : "");
//         article.setPublishedAt(entry.getPublishedDate() != null
//                 ? entry.getPublishedDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
//                 : LocalDateTime.now());
//         article.setSource(feedTitle);
//         article.setContent(entry.getContents() != null && !entry.getContents().isEmpty()
//                 ? entry.getContents().get(0).getValue()
//                 : article.getDescription());
//         article.setImageUrl(extractImageUrl(entry));
//         article.setLanguage(language);
//         return article;
//     }

//     private String extractImageUrl(SyndEntry entry) {
//         try {
//             // Check Media RSS module
//             Module module = entry.getModule(MediaEntryModule.URI);
//             if (module instanceof MediaEntryModule media) {
//                 MediaContent[] mediaContents = media.getMediaContents();
//                 if (mediaContents != null && mediaContents.length > 0 && mediaContents[0].getReference() != null) {
//                     return mediaContents[0].getReference().toString();
//                 }
//             }

//             // Check enclosures
//             if (entry.getEnclosures() != null && !entry.getEnclosures().isEmpty()) {
//                 return entry.getEnclosures().get(0).getUrl();
//             }

//             // Extract from description HTML
//             if (entry.getDescription() != null) {
//                 String desc = entry.getDescription().getValue();
//                 int start = desc.indexOf("<img");
//                 if (start != -1) {
//                     start = desc.indexOf("src=\"", start);
//                     if (start != -1) {
//                         start += 5;
//                         int end = desc.indexOf("\"", start);
//                         if (end > start) {
//                             return desc.substring(start, end);
//                         }
//                     }
//                 }
//             }

//         } catch (Exception e) {
//             logger.warn("Error extracting image: {}", e.getMessage());
//         }

//         return "https://via.placeholder.com/600x300?text=No+Image";
//     }

//     // --- CRUD & Search Service Methods ---

//     public NewsArticle saveArticle(NewsArticle article) {
//         return repo.save(article);
//     }

//     public List<NewsArticle> getAllArticles() {
//         return repo.findAll();
//     }

//     public List<NewsArticle> searchByKeyword(String text) {
//         List<NewsArticle> results = repo.fullTextSearch(text);
//         return results.isEmpty()
//                 ? repo.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(text, text)
//                 : results;
//     }

//     public List<NewsArticle> getNewsByLanguage(String lang) {
//         return repo.findByLanguageIgnoreCase(lang);
//     }

//     public List<NewsArticle> searchByKeywordAndLanguage(String keyword, String language) {
//         return repo.findByLanguageIgnoreCaseAndTitleContainingIgnoreCaseOrLanguageIgnoreCaseAndDescriptionContainingIgnoreCase(
//                 language, keyword, language, keyword
//         );
//     }
// }




// // package com.example.news.service;

// // import com.example.news.Article.NewsArticle;
// // import com.example.news.Repo.NewsArticleRepository;
// // import com.rometools.rome.feed.module.Module;
// // import com.rometools.rome.feed.synd.*;
// // import com.rometools.rome.io.SyndFeedInput;
// // import com.rometools.rome.io.XmlReader;
// // import com.rometools.modules.mediarss.MediaEntryModule;
// // import com.rometools.modules.mediarss.types.MediaContent;
// // import org.slf4j.Logger;
// // import org.slf4j.LoggerFactory;
// // import org.springframework.beans.factory.annotation.Autowired;
// // import org.springframework.scheduling.annotation.Scheduled;
// // import org.springframework.stereotype.Service;

// // import java.net.URL;
// // import java.time.LocalDateTime;
// // import java.time.ZoneId;
// // import java.util.Arrays;
// // import java.util.List;

// // @Service
// // public class NewsService {

// //     private static final Logger logger = LoggerFactory.getLogger(NewsService.class);

// //     @Autowired
// //     private NewsArticleRepository repo;

// //     private static class FeedSource {
// //         String url;
// //         String language;

// //         FeedSource(String url, String language) {
// //             this.url = url;
// //             this.language = language;
// //         }
// //     }
    
// //     private final List<FeedSource> rssFeeds = Arrays.asList(
// //         new FeedSource("https://www.manatelangana.news/feed/", "Telugu"),
// //         new FeedSource("https://divyamarathi.bhaskar.com/rss-v1--category-5492.xml", "Marathi"),
// //         new FeedSource("https://www.amarujala.com/rss/india-news.xml", "Hindi"),
// //         new FeedSource("https://thekanal.in/ta-IN/feeds/category/general/rss.xml", "Tamil"),
// //         new FeedSource("https://www.indiatoday.in/rss/home", "English"),
// //         new FeedSource("https://feeds.feedburner.com/ndtvnews-top-stories", "English")
// //     );

// //     @Scheduled(fixedRate = 300000)
// //     public void fetchAndStoreFromRss() {
// //         for (FeedSource source : rssFeeds) {
// //             try {
// //                 URL url = new URL(source.url.trim());
// //                 logger.info("Fetching feed from URL: {}", url);
// //                 XmlReader reader = new XmlReader(url);
// //                 SyndFeed feed = new SyndFeedInput().build(reader);

// //                 if (feed.getEntries() == null || feed.getEntries().isEmpty()) {
// //                     logger.warn("No entries found in feed: {}", source.url);
// //                     continue;
// //                 }

// //                 for (SyndEntry entry : feed.getEntries()) {
// //                     if (entry.getTitle() == null || entry.getTitle().trim().isEmpty()) {
// //                         logger.warn("Skipping article with empty title");
// //                         continue;
// //                     }

// //                     if (repo.existsByTitle(entry.getTitle())) {
// //                         logger.info("Duplicate found, skipping: {}", entry.getTitle());
// //                         continue;
// //                     }

// //                     NewsArticle article = new NewsArticle();
// //                     article.setTitle(entry.getTitle());
// //                     article.setDescription(entry.getDescription() != null ? entry.getDescription().getValue() : "");
// //                     article.setPublishedAt(entry.getPublishedDate() != null ?
// //                         entry.getPublishedDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime() :
// //                         LocalDateTime.now());
// //                     article.setSource(feed.getTitle());
// //                     article.setContent(entry.getContents() != null && !entry.getContents().isEmpty() ?
// //                         entry.getContents().get(0).getValue() : article.getDescription());
// //                     article.setImageUrl(extractImageUrl(entry));
// //                     article.setLanguage(source.language);

// //                     repo.save(article);
// //                     logger.info("Saved article: {}", entry.getTitle());
// //                 }

// //             } catch (Exception e) {
// //                 logger.error("Error fetching from: {}", source.url, e);
// //             }
// //         }
// //     }

// //     private String extractImageUrl(SyndEntry entry) {
// //         try {
// //             Module module = entry.getModule(MediaEntryModule.URI);
// //             if (module instanceof MediaEntryModule) {
// //                 MediaEntryModule media = (MediaEntryModule) module;
// //                 MediaContent[] mediaContents = media.getMediaContents();
// //                 if (mediaContents != null && mediaContents.length > 0 && mediaContents[0].getReference() != null) {
// //                     return mediaContents[0].getReference().toString();
// //                 }
// //             }

// //             if (entry.getEnclosures() != null && !entry.getEnclosures().isEmpty()) {
// //                 return entry.getEnclosures().get(0).getUrl();
// //             }

// //             if (entry.getDescription() != null) {
// //                 String desc = entry.getDescription().getValue();
// //                 int start = desc.indexOf("<img");
// //                 if (start != -1) {
// //                     start = desc.indexOf("src=\"", start);
// //                     if (start != -1) {
// //                         start += 5;
// //                         int end = desc.indexOf("\"", start);
// //                         if (end > start) {
// //                             return desc.substring(start, end);
// //                         }
// //                     }
// //                 }
// //             }

// //         } catch (Exception e) {
// //             logger.warn("Error extracting image: {}", e.getMessage());
// //         }

// //         return "https://via.placeholder.com/600x300?text=No+Image";
// //     }

// //     // --- Service Methods ---

// //     public NewsArticle saveArticle(NewsArticle article) {
// //         return repo.save(article);
// //     }

// //     public List<NewsArticle> getAllArticles() {
// //         return repo.findAll();
// //     }

// //     public List<NewsArticle> searchByKeyword(String text) {
// //         List<NewsArticle> results = repo.fullTextSearch(text);
// //         if (results.isEmpty()) {
// //             return repo.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(text, text);
// //         }
// //         return results;
// //     }

// //     public List<NewsArticle> getNewsByLanguage(String lang) {
// //         return repo.findByLanguageIgnoreCase(lang);
// //     }

// //     public List<NewsArticle> searchByKeywordAndLanguage(String keyword, String language) {
// //         return repo.findByLanguageIgnoreCaseAndTitleContainingIgnoreCaseOrLanguageIgnoreCaseAndDescriptionContainingIgnoreCase(
// //             language, keyword, language, keyword);
// //     }
// // }