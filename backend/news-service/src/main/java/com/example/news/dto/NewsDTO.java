package com.example.news.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class NewsDTO {

    private String title;
    private String link;
    private String description;

    @JsonProperty("pubDate")
    private String pubDate;

    @JsonProperty("image_url")
    private String imageUrl;

    private List<String> category;

    @JsonProperty("source_id")
    private String sourceId;
}



// package com.example.news.dto;

// import com.fasterxml.jackson.annotation.JsonProperty;
// import lombok.Data;

// @Data
// public class NewsDTO {
//     private String title;
//     private String description;
//     private String link;

//     @JsonProperty("pubDate")
//     private String pubDate;

//     @JsonProperty("image_url")
//     private String imageUrl;

//     @JsonProperty("source_id")
//     private String sourceId;
// }
