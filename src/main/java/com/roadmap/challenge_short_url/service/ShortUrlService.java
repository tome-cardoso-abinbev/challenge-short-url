package com.roadmap.challenge_short_url.service;

import com.roadmap.challenge_short_url.model.ShortUrl;
import com.roadmap.challenge_short_url.model.UrlDeletedResponse;
import com.roadmap.challenge_short_url.model.UrlRequest;
import com.roadmap.challenge_short_url.repository.ShortUrlRepository;
import com.roadmap.challenge_short_url.utils.ShortCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShortUrlService {

    @Autowired
    private final ShortUrlRepository shortUrlRepository;

    public ShortUrlService(ShortUrlRepository shortUrlRepository) {
        this.shortUrlRepository = shortUrlRepository;
    }

    public ShortUrl createShortUrl(UrlRequest url) {
        ShortUrl shortUrl = new ShortUrl();
        shortUrl.setUrl(url.getUrl());
        shortUrl.setShortUrlCode(ShortCodeGenerator.generate());
        shortUrl.setCreatedAt(LocalDateTime.now());
        shortUrl.setUpdatedAt(LocalDateTime.now());
        shortUrl.setAccessCount(0);
        return save(shortUrl);
    }

    @Cacheable(value = "shortUrl", key = "#shortUrlCode")
    public ShortUrl findByShortUrlCode(String shortUrlCode) {
        return shortUrlRepository.findByShortUrlCode(shortUrlCode);
    }

    @Cacheable(value = "shortUrl", key = "#url")
    public ShortUrl findByUrl(String url) {
        return shortUrlRepository.findByUrl(url);
    }

    @Cacheable(value = "shortUrl", key = "#id")
    public ShortUrl findById(String id) {
        return shortUrlRepository.findById(id).orElse(null);
    }

    public List<ShortUrl> findAllByUrl() {
        return shortUrlRepository.findAll();
    }

    @CachePut(value = "shortUrl", key = "#shortUrl.id")
    public ShortUrl save(ShortUrl shortUrl) {
        return shortUrlRepository.save(shortUrl);
    }

    public ShortUrl updateUrlFromShortUrl(String shortUrlCode, String newUrl) {

        ShortUrl shortUrlToUpdate = shortUrlRepository.findByShortUrlCode(shortUrlCode);

        if (shortUrlToUpdate != null) {
            shortUrlToUpdate.setUrl(newUrl);
            shortUrlToUpdate.setUpdatedAt(LocalDateTime.now());
            return shortUrlRepository.save(shortUrlToUpdate);
        }
        return null;
    }

    public ShortUrl updateShortUrlFromUrl(String urlCode) {

        ShortUrl urlToUpdate = shortUrlRepository.findByUrl(urlCode);

        if (urlToUpdate != null) {
            urlToUpdate.setShortUrlCode(ShortCodeGenerator.generate());
            urlToUpdate.setUpdatedAt(LocalDateTime.now());
            return shortUrlRepository.save(urlToUpdate);
        }
        return null;
    }


    private void delete(ShortUrl shortUrl) {
        shortUrlRepository.delete(shortUrl);
    }

    @CacheEvict(value = "shortUrl", key = "#shortUrlCode")
    public UrlDeletedResponse deleteByShortUrlCode(String shortUrlCode) {
        ShortUrl shortUrl = findByShortUrlCode(shortUrlCode);
        UrlDeletedResponse response = new UrlDeletedResponse();
        if (shortUrl != null) {
            response.setDeleted(true);
            response.setUrl(shortUrl.getUrl());
            response.setShortUrlCode(shortUrl.getShortUrlCode());
            delete(shortUrl);
            return response;
        }
        return null;
    }

    @CacheEvict(value = "shortUrl", key = "#url")
    public void deleteByUrl(String url) {
        ShortUrl shortUrl = findByUrl(url);
        if (shortUrl != null) {
            delete(shortUrl);
        }
    }

    @CacheEvict(value = "shortUrl", key = "#id")
    public UrlDeletedResponse deleteById(String id) {
        ShortUrl shortUrl = findById(id);
        UrlDeletedResponse response = new UrlDeletedResponse();
        if (shortUrl != null) {
            response.setDeleted(true);
            response.setUrl(shortUrl.getUrl());
            response.setShortUrlCode(shortUrl.getShortUrlCode());
            delete(shortUrl);
        }
        return response;
    }

    public void incrementAccessCount(ShortUrl shortUrl) {
        shortUrl.setAccessCount(shortUrl.getAccessCount() + 1);
        shortUrlRepository.save(shortUrl);
    }
}
