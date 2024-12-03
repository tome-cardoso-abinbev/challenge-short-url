package com.roadmap.challenge_short_url;

import com.roadmap.challenge_short_url.model.ShortUrl;
import com.roadmap.challenge_short_url.model.UrlDeletedResponse;
import com.roadmap.challenge_short_url.model.UrlRequest;
import com.roadmap.challenge_short_url.repository.ShortUrlRepository;
import com.roadmap.challenge_short_url.service.ShortUrlService;
import com.roadmap.challenge_short_url.utils.ShortCodeGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cache.annotation.EnableCaching;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@EnableCaching
public class ShortUrlServiceTest {

    @Mock
    private ShortUrlRepository shortUrlRepository;

    @InjectMocks
    private ShortUrlService shortUrlService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateShortUrl() {
        UrlRequest urlRequest = new UrlRequest();
        urlRequest.setUrl("http://example.com");

        ShortUrl shortUrl = new ShortUrl();
        shortUrl.setUrl(urlRequest.getUrl());
        shortUrl.setShortUrlCode(ShortCodeGenerator.generate());
        shortUrl.setCreatedAt(LocalDateTime.now());
        shortUrl.setUpdatedAt(LocalDateTime.now());
        shortUrl.setAccessCount(0);

        when(shortUrlRepository.save(any(ShortUrl.class))).thenReturn(shortUrl);

        ShortUrl createdShortUrl = shortUrlService.createShortUrl(urlRequest);

        assertNotNull(createdShortUrl);
        assertEquals(urlRequest.getUrl(), createdShortUrl.getUrl());
        verify(shortUrlRepository, times(1)).save(any(ShortUrl.class));
    }

    @Test
    public void testFindByShortUrlCode() {
        String shortUrlCode = "abc123";
        ShortUrl shortUrl = new ShortUrl();
        shortUrl.setShortUrlCode(shortUrlCode);

        when(shortUrlRepository.findByShortUrlCode(shortUrlCode)).thenReturn(shortUrl);

        ShortUrl foundShortUrl = shortUrlService.findByShortUrlCode(shortUrlCode);

        assertNotNull(foundShortUrl);
        assertEquals(shortUrlCode, foundShortUrl.getShortUrlCode());
        verify(shortUrlRepository, times(1)).findByShortUrlCode(shortUrlCode);
    }

    @Test
    public void testDeleteByShortUrlCode() {
        String shortUrlCode = "abc123";
        ShortUrl shortUrl = new ShortUrl();
        shortUrl.setShortUrlCode(shortUrlCode);

        when(shortUrlRepository.findByShortUrlCode(shortUrlCode)).thenReturn(shortUrl);
        doNothing().when(shortUrlRepository).delete(shortUrl);

        UrlDeletedResponse response = shortUrlService.deleteByShortUrlCode(shortUrlCode);

        assertNotNull(response);
        assertTrue(response.isDeleted());
        assertEquals(shortUrlCode, response.getShortUrlCode());
        verify(shortUrlRepository, times(1)).findByShortUrlCode(shortUrlCode);
        verify(shortUrlRepository, times(1)).delete(shortUrl);
    }

    @Test
    public void testDeleteById() {
        String id = "1";
        ShortUrl shortUrl = new ShortUrl();
        shortUrl.setId(id);
        shortUrl.setUrl("http://example.com");
        shortUrl.setShortUrlCode("abc123");

        when(shortUrlRepository.findById(id)).thenReturn(Optional.of(shortUrl));
        doNothing().when(shortUrlRepository).delete(shortUrl);

        UrlDeletedResponse response = shortUrlService.deleteById(id);

        assertNotNull(response);
        assertTrue(response.isDeleted());
        assertEquals("abc123", response.getShortUrlCode());
        verify(shortUrlRepository, times(1)).findById(id);
        verify(shortUrlRepository, times(1)).delete(shortUrl);
    }
}
