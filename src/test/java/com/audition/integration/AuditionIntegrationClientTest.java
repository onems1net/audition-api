package com.audition.integration;

import com.audition.common.exception.SystemException;
import com.audition.model.AuditionPost;
import com.audition.model.AuditionPostComment;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@SpringJUnitConfig
class AuditionIntegrationClientTest {

    private final static String BASE_URL = "https://jsonplaceholder.typicode.com/";

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private AuditionIntegrationClient auditionIntegrationClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(auditionIntegrationClient, "baseUrl", BASE_URL);
    }

    @Test
    void testGetPosts() {
        AuditionPost[] posts = new AuditionPost[]{new AuditionPost()};
        when(restTemplate.getForObject(BASE_URL + "posts", AuditionPost[].class))
            .thenReturn(posts);

        List<AuditionPost> actualPosts = auditionIntegrationClient.getPosts();
        assertEquals(Arrays.asList(posts), actualPosts);
    }

    @Test
    void testGetPostById() {
        String postId = "1";
        AuditionPost post = new AuditionPost();
        when(restTemplate.getForObject(BASE_URL + "posts/" + postId, AuditionPost.class))
            .thenReturn(post);

        AuditionPost actualPost = auditionIntegrationClient.getPostById(postId);
        assertEquals(post, actualPost);
    }

    @Test
    void testGetPostWithCommentsById() {
        String postId = "1";
        AuditionPost post = new AuditionPost();
        AuditionPostComment[] comments = new AuditionPostComment[]{new AuditionPostComment()};
        when(restTemplate.getForObject(BASE_URL + "posts/" + postId, AuditionPost.class))
            .thenReturn(post);
        when(restTemplate.getForObject(BASE_URL + "posts/" + postId + "/comments", AuditionPostComment[].class))
            .thenReturn(comments);

        AuditionPost actualPost = auditionIntegrationClient.getPostWithCommentsById(postId);
        assertEquals(post, actualPost);
        assertEquals(Arrays.asList(comments), actualPost.getComments());
    }

    @Test
    void testGetCommentsByPostId() {
        String postId = "1";
        AuditionPostComment[] comments = new AuditionPostComment[]{new AuditionPostComment()};
        when(restTemplate.getForObject(BASE_URL + "comments?postId=" + postId, AuditionPostComment[].class))
            .thenReturn(comments);

        List<AuditionPostComment> actualComments = auditionIntegrationClient.getCommentsByPostId(postId);
        assertEquals(Arrays.asList(comments), actualComments);
    }

    @Test
    void testGetPostsThrowsHttpClientErrorException() {
        HttpClientErrorException exception = HttpClientErrorException.create(HttpStatus.BAD_REQUEST, "Bad Request",
            new HttpHeaders(), new byte[0], null);
        when(restTemplate.getForObject(BASE_URL + "posts", AuditionPost[].class))
            .thenThrow(exception);

        SystemException thrown = assertThrows(SystemException.class, () -> auditionIntegrationClient.getPosts());
        assertEquals("HTTP error", thrown.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST.value(), thrown.getStatusCode());
    }

    @Test
    void testGetPostsThrowsResourceAccessException() {
        when(restTemplate.getForObject(BASE_URL + "posts", AuditionPost[].class))
            .thenThrow(ResourceAccessException.class);

        assertThrows(SystemException.class, () -> auditionIntegrationClient.getPosts());
    }

    @Test
    void testGetPostsThrowsRestClientException() {
        when(restTemplate.getForObject(BASE_URL + "posts", AuditionPost[].class))
            .thenThrow(RestClientException.class);

        assertThrows(SystemException.class, () -> auditionIntegrationClient.getPosts());
    }

    @Test
    void testGetPostByIdThrowsHttpClientErrorException() {
        String postId = "1";
        HttpClientErrorException exception = HttpClientErrorException.create(HttpStatus.BAD_REQUEST, "Bad Request",
            new HttpHeaders(), new byte[0], null);
        when(restTemplate.getForObject(BASE_URL + "posts/" + postId, AuditionPost.class))
            .thenThrow(exception);

        SystemException thrown = assertThrows(SystemException.class,
            () -> auditionIntegrationClient.getPostById(postId));
        assertEquals("HTTP error", thrown.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST.value(), thrown.getStatusCode());
    }

    @Test
    void testGetPostByIdThrowsResourceAccessException() {
        String postId = "1";
        when(restTemplate.getForObject(BASE_URL + "posts/" + postId, AuditionPost.class))
            .thenThrow(ResourceAccessException.class);

        assertThrows(SystemException.class, () -> auditionIntegrationClient.getPostById(postId));
    }

    @Test
    void testGetPostByIdThrowsRestClientException() {
        String postId = "1";
        when(restTemplate.getForObject(BASE_URL + "posts/" + postId, AuditionPost.class))
            .thenThrow(RestClientException.class);

        assertThrows(SystemException.class, () -> auditionIntegrationClient.getPostById(postId));
    }

    @Test
    void testGetPostWithCommentsByIdThrowsHttpClientErrorException() {
        String postId = "1";
        HttpClientErrorException exception = HttpClientErrorException.create(HttpStatus.BAD_REQUEST, "Bad Request",
            new HttpHeaders(), new byte[0], null);
        when(restTemplate.getForObject(BASE_URL + "posts/" + postId, AuditionPost.class))
            .thenThrow(exception);

        SystemException thrown = assertThrows(SystemException.class,
            () -> auditionIntegrationClient.getPostWithCommentsById(postId));
        assertEquals("HTTP error", thrown.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST.value(), thrown.getStatusCode());
    }

    @Test
    void testGetPostWithCommentsByIdThrowsResourceAccessException() {
        String postId = "1";
        when(restTemplate.getForObject(BASE_URL + "posts/" + postId, AuditionPost.class))
            .thenThrow(ResourceAccessException.class);

        assertThrows(SystemException.class, () -> auditionIntegrationClient.getPostWithCommentsById(postId));
    }

    @Test
    void testGetPostWithCommentsByIdThrowsRestClientException() {
        String postId = "1";
        when(restTemplate.getForObject(BASE_URL + "posts/" + postId, AuditionPost.class))
            .thenThrow(RestClientException.class);

        assertThrows(SystemException.class, () -> auditionIntegrationClient.getPostWithCommentsById(postId));
    }

    @Test
    void testGetCommentsByPostIdThrowsHttpClientErrorException() {
        String postId = "1";
        HttpClientErrorException exception = HttpClientErrorException.create(HttpStatus.BAD_REQUEST, "Bad Request",
            new HttpHeaders(), new byte[0], null);
        when(restTemplate.getForObject(BASE_URL + "comments?postId=" + postId, AuditionPostComment[].class))
            .thenThrow(exception);

        SystemException thrown = assertThrows(SystemException.class,
            () -> auditionIntegrationClient.getCommentsByPostId(postId));
        assertEquals("HTTP error", thrown.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST.value(), thrown.getStatusCode());
    }

    @Test
    void testGetCommentsByPostIdThrowsResourceAccessException() {
        String postId = "1";
        when(restTemplate.getForObject(BASE_URL + "comments?postId=" + postId, AuditionPostComment[].class))
            .thenThrow(ResourceAccessException.class);

        assertThrows(SystemException.class, () -> auditionIntegrationClient.getCommentsByPostId(postId));
    }

    @Test
    void testGetCommentsByPostIdThrowsRestClientException() {
        String postId = "1";
        when(restTemplate.getForObject(BASE_URL + "comments?postId=" + postId, AuditionPostComment[].class))
            .thenThrow(RestClientException.class);

        assertThrows(SystemException.class, () -> auditionIntegrationClient.getCommentsByPostId(postId));
    }
}