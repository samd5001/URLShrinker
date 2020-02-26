package com.samdunkley.urlshrinker.controller;

import com.samdunkley.urlshrinker.model.URLShrinkRequest;
import com.samdunkley.urlshrinker.service.URLShrinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping
public class RESTController {

    private URLShrinkService shrinkService;

    @Autowired
    public RESTController(URLShrinkService shrinkService) {
        this.shrinkService = shrinkService;
    }

    @GetMapping("{urlId}")
    public ModelAndView redirectURL(@PathVariable String urlId) throws ResponseStatusException {
        return new ModelAndView(("redirect:" + shrinkService.expandUrl(urlId)));
    }

    @PostMapping("/smallurl")
    public ResponseEntity<String> shrinkURL(@RequestBody URLShrinkRequest url) throws ResponseStatusException {
        return shrinkService.shrinkUrl(url);
    }
}
