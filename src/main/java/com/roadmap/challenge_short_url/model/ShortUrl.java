package com.roadmap.challenge_short_url.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "short_urls")
public class ShortUrl {

    @Id
    private String id;
    private String url;
    private String shortUrlCode;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer accessCount;

}
