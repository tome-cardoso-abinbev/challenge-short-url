package com.roadmap.challenge_short_url.model;

import lombok.Data;

@Data
public class UrlDeletedResponse {
    private boolean deleted;
    private String url;
    private String shortUrlCode;
}
