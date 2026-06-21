package com.uit.se104.feedback_system.controller;

import com.uit.se104.feedback_system.service.FeedbackService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FeedbackController.class)
public class FeedbackControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FeedbackService feedbackService;

    @Test
    public void testCreateFeedback_BadRequest_WhenMissingCustomerHeader() throws Exception {
        MockMultipartFile feedbackJson = new MockMultipartFile("feedback", "", "application/json", 
                "{\"content\":\"Hàng tốt\",\"topic\":\"PRODUCT\",\"rating\":5}".getBytes());

        // Cố tình không truyền header "X-Customer-Id"
        mockMvc.perform(multipart("/api/feedbacks")
                .file(feedbackJson))
                .andExpect(status().isBadRequest()); // Spring tự ném MissingRequestHeaderException (400)
    }
}