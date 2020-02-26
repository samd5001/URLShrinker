package com.samdunkley.urlshrinker.service;

import com.samdunkley.urlshrinker.db.UrlIdRepository;
import com.samdunkley.urlshrinker.model.UrlIdCounter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { URLIDGenerationService.class })
@TestPropertySource(locations = "classpath:test.properties")
public class URLIDGenerationServiceTest {

    @Autowired
    private URLIDGenerationService urlidGenerationService;

    @MockBean
    private UrlIdRepository mockUrlIdRepo;

    @Test
    public void testCorrectNextIdWhenNoExistingIds() {
        when(mockUrlIdRepo.findAll()).thenReturn(new ArrayList<>());

        var result = urlidGenerationService.getNextId();

        verify(mockUrlIdRepo).findAll();

        assertEquals("500", result);
    }

    @Test
    public void testCorrectNextIdWithExistingIds() {

        var idCounter = new UrlIdCounter(1850);
        when(mockUrlIdRepo.findAll()).thenReturn(List.of(idCounter));

        var result = urlidGenerationService.getNextId();

        verify(mockUrlIdRepo).findAll();

        assertEquals("1851", result);
    }


}
