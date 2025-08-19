package com.example.news.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class NewsApiResponse {

    private String status;

    @JsonProperty("totalResults")
    private int totalResults;

    private List<NewsDTO> results;
}




// // package com.example.news.dto;

// // import com.fasterxml.jackson.annotation.JsonProperty;
// // import lombok.Data;

// // import java.util.List;

// // @Data
// // public class NewsResponse {
// //     private String status;

// //     @JsonProperty("totalResults")
// //     private int totalResults;

// //     private List<ArticleDTO> results;

// //     @Data
// //     public static class ArticleDTO {
// //         private String title;
// //         private String link;
// //         private String description;

// //         @JsonProperty("pubDate")
// //         private String pubDate;

// //         @JsonProperty("image_url")
// //         private String imageUrl;

// //         private List<String> category;

// //         @JsonProperty("source_id")
// //         private String sourceId;
// //     }
// // }
// // //

// package com.example.news.dto;

// import lombok.Data;
// import java.util.List;

// @Data
// public class NewsApiResponse {
//     private String status;
//     private List<NewsDTO> results;
// }


