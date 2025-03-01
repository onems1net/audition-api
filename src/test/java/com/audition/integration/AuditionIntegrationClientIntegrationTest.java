package com.audition.integration;

import com.audition.common.exception.SystemException;
import com.audition.model.AuditionPost;
import com.audition.model.AuditionPostComment;
import com.github.tomakehurst.wiremock.client.WireMock;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import com.github.tomakehurst.wiremock.http.Fault;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@WireMockTest(httpPort = 8080)
@Disabled
class AuditionIntegrationClientIntegrationTest {

    @Autowired
    private AuditionIntegrationClient auditionIntegrationClient;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        WireMock.reset();
    }

    @Test
    void testGetPosts() {
        List<AuditionPost> posts = auditionIntegrationClient.getPosts();
        assertNotNull(posts);
        assertEquals(100, posts.size()); // Assuming the endpoint returns 100 posts
    }

    @Test
    void testGetPostById() {
        String postId = "1";
        AuditionPost post = auditionIntegrationClient.getPostById(postId);
        assertNotNull(post);
        assertEquals(postId, String.valueOf(post.getId()));
    }

    @Test
    void testGetPostWithCommentsById() {
        String postId = "1";
        AuditionPost post = auditionIntegrationClient.getPostWithCommentsById(postId);
        assertNotNull(post);
        assertEquals(postId, String.valueOf(post.getId()));
        assertNotNull(post.getComments());
    }

    @Test
    void testGetCommentsByPostId() {
        String postId = "1";
        List<AuditionPostComment> comments = auditionIntegrationClient.getCommentsByPostId(postId);
        assertNotNull(comments);
        assertEquals(5, comments.size()); // Assuming the post has 5 comments
    }

    @Test
    void testGetPostsThrowsHttpClientErrorException() {
        stubFor(get(urlEqualTo("/posts"))
            .willReturn(aResponse().withStatus(HttpStatus.BAD_REQUEST.value())));

        SystemException thrown = assertThrows(SystemException.class, () -> auditionIntegrationClient.getPosts());
        assertEquals("HTTP error", thrown.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST.value(), thrown.getStatusCode());
    }

    @Test
    void testGetPostsThrowsResourceAccessException() {
        stubFor(get(urlEqualTo("/posts"))
            .willReturn(aResponse().withFault(Fault.CONNECTION_RESET_BY_PEER)));

        SystemException thrown = assertThrows(SystemException.class, () -> auditionIntegrationClient.getPosts());
        assertEquals("Resource access error", thrown.getMessage());
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE.value(), thrown.getStatusCode());
    }

    @Test
    void testGetPostsThrowsRestClientException() {
        stubFor(get(urlEqualTo("/posts"))
            .willReturn(aResponse().withStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())));

        SystemException thrown = assertThrows(SystemException.class, () -> auditionIntegrationClient.getPosts());
        assertEquals("Client error", thrown.getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), thrown.getStatusCode());
    }

    @Test
    void testGetPostsThrowsGenericException() {
        stubFor(get(urlEqualTo("/posts"))
            .willReturn(aResponse().withStatus(HttpStatus.OK.value()).withBody("invalid json")));

        SystemException thrown = assertThrows(SystemException.class, () -> auditionIntegrationClient.getPosts());
        assertEquals("Unexpected error", thrown.getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), thrown.getStatusCode());
    }
}