package com.samdunkley.urlshrinker.service;

import com.samdunkley.urlshrinker.db.UrlRepository;
import com.samdunkley.urlshrinker.model.URLShrinkRequest;
import com.samdunkley.urlshrinker.model.UrlEntry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { URLShrinkService.class })
@TestPropertySource(locations = "classpath:test.properties")
public class URLShrinkServiceTest {

    @Autowired
    private URLShrinkService shrinkService;

    @MockBean
    private UrlRepository mockUrlRepo;

    @MockBean
    private  URLIDGenerationService mockURLIDGenerationService;

    private static String testId = "1850";
    private static String testUrl = "www.americanexpress.com/gb";
    private static String testUrlWithHttp = "http://" + testUrl;
    private static UrlEntry testUrlEntry = new UrlEntry(testId, testUrlWithHttp);

    @Test
    public void testExpandUrlWhenFound() {

        when(mockUrlRepo.findByUrlID(testId)).thenReturn(Optional.of(testUrlEntry));

        var result = shrinkService.expandUrl(testId);

        assertEquals(testUrlWithHttp, result);
    }

    @Test
    public void testExpandUrlWhenNotFound() {

        when(mockUrlRepo.findByUrlID(testId)).thenReturn(Optional.empty());

        try {
            shrinkService.expandUrl(testId);
            fail("Exception not thrown");
        } catch (ResponseStatusException e) {
            assertResponseException(e, HttpStatus.NOT_FOUND, "URL not found");
        }
    }

    @Test
    public void testShrinkUrlNullRequest() {

        try {
            shrinkService.shrinkUrl(null);
        } catch (ResponseStatusException e) {
            assertResponseException(e, HttpStatus.BAD_REQUEST, "Request malformed");
        }
    }

    @Test
    public void testShrinkUrlRequestUrlNull() {

        try {
            shrinkService.shrinkUrl(new URLShrinkRequest());
            fail("Exception not thrown");
        } catch (ResponseStatusException e) {
            assertResponseException(e, HttpStatus.BAD_REQUEST, "Request malformed");
        }
    }

    private void assertResponseException(ResponseStatusException e, HttpStatus badRequest, String s) {
        assertAll("Exception is correct",
                () -> assertEquals(badRequest, e.getStatus()),
                () -> assertEquals(s, e.getReason()));
    }

    @Test
    public void testShrinkUrlSuccessWithHttpPrefix() {
        testShrinkSuccess(testUrlWithHttp);
    }

    @Test
    public void testShrinkUrlSuccessWithoutHttpPrefix() {
        testShrinkSuccess(testUrl, testUrlWithHttp);
    }

    @Test
    public void testShrinkUrlSuccessWithWhiteSpace() {
        testShrinkSuccess("http://   " + testUrl + "  ", testUrlWithHttp);
    }

    @Test
    public void testShrinkUrlExisting() {
        var testRequest = new URLShrinkRequest();
        testRequest.setUrl(testUrl);

        when(mockUrlRepo.findByFullURL(testUrlWithHttp)).thenReturn(Optional.of(testUrlEntry));

        var resultResponse = shrinkService.shrinkUrl(testRequest);

        verify(mockUrlRepo).findByFullURL(testUrlWithHttp);
        verify(mockUrlRepo, never()).save(testUrlEntry);
        verify(mockURLIDGenerationService, never()).getNextId();


        assertAll("Response is correct",
                () -> assertNotNull(resultResponse),
                () -> assertEquals(HttpStatus.OK, resultResponse.getStatusCode()),
                () -> assertEquals("http://americanexpress.com/1850", resultResponse.getBody()));

    }

    private void testShrinkSuccess(String testUrl) {
        testShrinkSuccess(testUrl, testUrl);
    }

    private void testShrinkSuccess(String testUrl, String expectedUrl) {
        var testRequest = new URLShrinkRequest();
        testRequest.setUrl(testUrl);

        when(mockUrlRepo.findByFullURL(testUrlWithHttp)).thenReturn(Optional.empty());
        when(mockURLIDGenerationService.getNextId()).thenReturn(testId);

        var resultResponse = shrinkService.shrinkUrl(testRequest);

        verify(mockUrlRepo).findByFullURL(expectedUrl);
        verify(mockURLIDGenerationService).getNextId();
        verify(mockUrlRepo).save(testUrlEntry);

        assertAll("Response is correct",
                () -> assertNotNull(resultResponse),
                () -> assertEquals(HttpStatus.CREATED, resultResponse.getStatusCode()),
                () -> assertEquals("http://americanexpress.com/1850", resultResponse.getBody()));
    }
}
