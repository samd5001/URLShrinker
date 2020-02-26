package com.samdunkley.urlshrinker.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class URLShrinkRequest {

    private String url;
}
