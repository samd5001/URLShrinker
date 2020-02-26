package com.samdunkley.urlshrinker.service;

import com.samdunkley.urlshrinker.db.UrlIdRepository;
import com.samdunkley.urlshrinker.model.UrlIdCounter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class URLIDGenerationService {

    private UrlIdRepository urlIdRepository;

    @Value("${idstrategy.startid:1000}")
    private long startId;

    @Autowired
    public URLIDGenerationService(UrlIdRepository urlIdRepository) {
        this.urlIdRepository = urlIdRepository;
    }

    /**
     * A method that gets the id used for url shortening
     * The db calls are not concurrently safe and it is assumed only one person will be using this at a time
     *
     * @return String Next url id
     */
    @Transactional
    public String getNextId() {
        List<UrlIdCounter> results = urlIdRepository.findAll();

        UrlIdCounter urlIdCounter;

        if (results.isEmpty()) {
            urlIdCounter = new UrlIdCounter(startId);
        } else {
            urlIdCounter = results.get(0);
            urlIdCounter.setLastId(urlIdCounter.getLastId() + 1);
        }

        urlIdRepository.save(urlIdCounter);
        return String.valueOf(urlIdCounter.getLastId());
    }
}
