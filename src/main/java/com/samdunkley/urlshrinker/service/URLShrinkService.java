package com.samdunkley.urlshrinker.service;

import com.samdunkley.urlshrinker.db.UrlRepository;
import com.samdunkley.urlshrinker.model.URLShrinkRequest;
import com.samdunkley.urlshrinker.model.UrlEntry;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class URLShrinkService {

    private UrlRepository urlRepository;

    private URLIDGenerationService urlIdGenerationService;

    @Value("${api.hostname:http://localhost:}")
    private String hostName;

    @Value("${api.port:8080}")
    private String port;

    private UrlValidator urlValidator;

    private static final String[] urlConstraints = {"http", "https"};

    @Autowired
    public URLShrinkService(UrlRepository urlRepository, URLIDGenerationService urlIdGenerationService) {
        this.urlRepository = urlRepository;
        this.urlIdGenerationService = urlIdGenerationService;
        this.urlValidator = new UrlValidator(urlConstraints);
    }

    /**
     *
     * @param urlId
     * @return
     * @throws ResponseStatusException
     */
    public String expandUrl(String urlId) throws ResponseStatusException {
        Optional<UrlEntry> dbUrl =  urlRepository.findByUrlID(urlId);

        if (dbUrl.isPresent()) {
            return dbUrl.get().getFullURL();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "URL not found");
        }
    }


    /**
     * Takes a url string and returns the shrank url
     * Assumes all urls will be valid i.e. the url is not validated
     *
     * @param request
     * @return
     * @throws ResponseStatusException
     */
    @Transactional
    public ResponseEntity<String> shrinkUrl(URLShrinkRequest request) throws ResponseStatusException {

        if (request == null || request.getUrl() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request malformed");
        }

        // Adds http:// prefix if it is not on the url to correctly redirect to new host and removes any whitespace
        var fullUrl = request.getUrl().replace(" ", "").startsWith("http") ? request.getUrl().replace(" ", "") : "http://" + request.getUrl().replace(" ", "");

        if (!urlValidator.isValid(fullUrl)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Invalid URL");
        };

        Optional<UrlEntry> dbUrl = urlRepository.findByFullURL(fullUrl);

        if (dbUrl.isPresent()) {
            return new ResponseEntity<>(buildURLString(dbUrl.get()), HttpStatus.OK);
        }

        var urlEntry = new UrlEntry(urlIdGenerationService.getNextId(), fullUrl);

        urlRepository.save(urlEntry);

        return new ResponseEntity<>(buildURLString(urlEntry), HttpStatus.CREATED);
    }

    /**
     *
     * @param urlEntry
     * @return
     */
    private String buildURLString(UrlEntry urlEntry) {
        return hostName + port  + "/" + urlEntry.getUrlID();
    }

}
