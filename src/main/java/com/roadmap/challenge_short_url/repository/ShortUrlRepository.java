package com.roadmap.challenge_short_url.repository;

import com.roadmap.challenge_short_url.model.ShortUrl;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ShortUrlRepository extends MongoRepository<ShortUrl, String> {
    ShortUrl findByShortUrlCode(String shortUrlCode);
    ShortUrl findByUrl(String url);
}
