package com.audition.integration;

import com.audition.common.exception.SystemException;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
class AuditionIntegrationClientExceptionTest {

    @Autowired
    private transient WebApplicationContext webApplicationContext;

    @MockBean
    private transient AuditionIntegrationClient auditionIntegrationClient;

    @Test
    void testExceptionHandling() throws Exception {
        final MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        when(auditionIntegrationClient.getPosts()).thenThrow(
            new SystemException("Client error", "Error occurred", HttpStatus.INTERNAL_SERVER_ERROR.value()));

        mockMvc.perform(get("/posts"))
            .andExpect(status().isInternalServerError());
    }
}
