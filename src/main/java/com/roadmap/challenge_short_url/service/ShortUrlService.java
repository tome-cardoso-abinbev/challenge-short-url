package com.roadmap.challenge_short_url.service;

import com.roadmap.challenge_short_url.model.ShortUrl;
import com.roadmap.challenge_short_url.model.UrlDeletedResponse;
import com.roadmap.challenge_short_url.model.UrlRequest;
import com.roadmap.challenge_short_url.repository.ShortUrlRepository;
import com.roadmap.challenge_short_url.utils.ShortCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShortUrlService {

    @Autowired
    private ShortUrlRepository shortUrlRepository;

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

    public ShortUrl findByShortUrlCode(String shortUrlCode) {
        return shortUrlRepository.findByShortUrlCode(shortUrlCode);
    }

    public ShortUrl findByUrl(String url) {
        return shortUrlRepository.findByUrl(url);
    }

    public List<ShortUrl> findAllByUrl() {
        return shortUrlRepository.findAll();
    }

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


    public void delete(ShortUrl shortUrl) {
        shortUrlRepository.delete(shortUrl);
    }

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

    public void deleteByUrl(String url) {
        ShortUrl shortUrl = findByUrl(url);
        if (shortUrl != null) {
            delete(shortUrl);
        }
    }

    public void incrementAccessCount(ShortUrl shortUrl) {
        shortUrl.setAccessCount(shortUrl.getAccessCount() + 1);
        shortUrlRepository.save(shortUrl);
    }
}
