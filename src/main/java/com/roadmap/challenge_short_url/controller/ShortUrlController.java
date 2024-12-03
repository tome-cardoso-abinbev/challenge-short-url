package com.roadmap.challenge_short_url.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.roadmap.challenge_short_url.model.ShortUrl;
import com.roadmap.challenge_short_url.model.UrlDeletedResponse;
import com.roadmap.challenge_short_url.model.UrlRequest;
import com.roadmap.challenge_short_url.service.ShortUrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/shorten")
public class ShortUrlController {

    @Autowired
    private ShortUrlService service;

    @PostMapping
        public ResponseEntity<ShortUrl> createShortUrl(@RequestBody UrlRequest url) {
        ShortUrl shortUrl = service.createShortUrl(url);
        return ResponseEntity.created(URI.create("/shorten/" +
                shortUrl.getShortUrlCode())).body(shortUrl);
    }

    @GetMapping("all")
    public  ResponseEntity<List <ShortUrl>> getShortUrlList() {
        List <ShortUrl> byShortUrlCode = service.findAllByUrl();
        if (byShortUrlCode != null) {
            return ResponseEntity.ok(byShortUrlCode);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<ShortUrl> getShortUrl(@PathVariable String shortCode) {
        ShortUrl byShortUrlCode = service.findByShortUrlCode(shortCode);
        if (byShortUrlCode != null) {
            service.incrementAccessCount(byShortUrlCode);
            return ResponseEntity.ok(byShortUrlCode);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{shortCode}")
    public ResponseEntity<ShortUrl> updateShortUrl(@PathVariable String shortCode,
                                                   @RequestBody UrlRequest updateUrlRequest) {
        String newUrl = updateUrlRequest.getUrl();
        ShortUrl optionalShortUrl = service.updateUrlFromShortUrl(shortCode, newUrl);
        if (optionalShortUrl != null) {
            return ResponseEntity.ok(optionalShortUrl);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{shortCode}")
    public ResponseEntity<UrlDeletedResponse> deleteShortUrl(@PathVariable String shortCode) {
        if(service.findByShortUrlCode(shortCode) != null) {
            var toBeDeleted = service.deleteByShortUrlCode(shortCode);
            return ResponseEntity.ok(toBeDeleted);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/byId/{id}")
    public ResponseEntity<UrlDeletedResponse> deleteShortUrlById(
            @PathVariable String id) {
        if(service.findById(id) != null) {
            var toBeDeleted = service.deleteById(id);
            return ResponseEntity.ok(toBeDeleted);
        }
        return ResponseEntity.notFound().build();
    }
}
