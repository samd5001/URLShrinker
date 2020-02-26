package com.samdunkley.urlshrinker.controller;

import com.samdunkley.urlshrinker.model.URLShrinkRequest;
import com.samdunkley.urlshrinker.service.URLShrinkService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { RESTController.class })
public class RESTControllerTest {

    @Autowired
    private RESTController controller;

    @MockBean
    private URLShrinkService mockShrinkService;

    @Test
    public void testShrinkEndpointCalls() {
        var testRequest = new URLShrinkRequest();
        testRequest.setUrl("www.americanexpress.com/uk/");

        controller.shrinkURL(testRequest);

        Mockito.verify(mockShrinkService).shrinkUrl(testRequest);
    }

    @Test
    public void testGrowEndpoint() {
        var testId = "1100";

        controller.redirectURL(testId);

        Mockito.verify(mockShrinkService).expandUrl(testId);
    }
}
