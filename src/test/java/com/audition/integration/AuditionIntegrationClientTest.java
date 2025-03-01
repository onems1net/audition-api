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
@SuppressWarnings("PMD.TooManyMethods")
class AuditionIntegrationClientTest {

    private static final String BASE_URL = "https://jsonplaceholder.typicode.com/";
    private static final String POSTS = "posts";
    private static final String POSTS_SLASH = "posts/";
    private static final String COMMENTS_BY_POST_ID = "comments?postId=";
    private static final String BAD_REQUEST = "Bad Request";
    private static final String HTTP_ERROR = "HTTP error";

    @Mock
    private transient RestTemplate restTemplate;

    @InjectMocks
    private transient AuditionIntegrationClient auditionIntegrationClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(auditionIntegrationClient, "baseUrl", BASE_URL);
    }

    @Test
    void testGetPosts() {
        final AuditionPost[] posts = {new AuditionPost()};
        when(restTemplate.getForObject(BASE_URL + POSTS, AuditionPost[].class)).thenReturn(posts);

        final List<AuditionPost> actualPosts = auditionIntegrationClient.getPosts();
        assertEquals(Arrays.asList(posts), actualPosts);
    }

    @Test
    void testGetPostById() {
        final String postId = "1";
        final AuditionPost post = new AuditionPost();
        when(restTemplate.getForObject(BASE_URL + POSTS_SLASH + postId, AuditionPost.class)).thenReturn(post);

        final AuditionPost actualPost = auditionIntegrationClient.getPostById(postId);
        assertEquals(post, actualPost);
    }

    @Test
    void testGetPostWithCommentsById() {
        final String postId = "1";
        final AuditionPost post = new AuditionPost();
        final AuditionPostComment[] comments = {new AuditionPostComment()};
        when(restTemplate.getForObject(BASE_URL + POSTS_SLASH + postId, AuditionPost.class)).thenReturn(post);
        when(restTemplate.getForObject(BASE_URL + POSTS_SLASH + postId + "/comments",
            AuditionPostComment[].class)).thenReturn(comments);

        final AuditionPost actualPost = auditionIntegrationClient.getPostWithCommentsById(postId);
        assertEquals(post, actualPost);
        assertEquals(Arrays.asList(comments), actualPost.getComments());
    }

    @Test
    void testGetCommentsByPostId() {
        final String postId = "1";
        final AuditionPostComment[] comments = {new AuditionPostComment()};
        when(
            restTemplate.getForObject(BASE_URL + COMMENTS_BY_POST_ID + postId, AuditionPostComment[].class)).thenReturn(
            comments);

        final List<AuditionPostComment> actualComments = auditionIntegrationClient.getCommentsByPostId(postId);
        assertEquals(Arrays.asList(comments), actualComments);
    }

    @Test
    void testGetPostsThrowsHttpClientErrorException() {
        final HttpClientErrorException exception = HttpClientErrorException.create(HttpStatus.BAD_REQUEST, BAD_REQUEST,
            new HttpHeaders(), new byte[0], null);
        when(restTemplate.getForObject(BASE_URL + POSTS, AuditionPost[].class)).thenThrow(exception);

        final SystemException thrown = assertThrows(SystemException.class, () -> auditionIntegrationClient.getPosts());
        assertEquals(HTTP_ERROR, thrown.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST.value(), thrown.getStatusCode());
    }

    @Test
    void testGetPostsThrowsResourceAccessException() {
        when(restTemplate.getForObject(BASE_URL + POSTS, AuditionPost[].class)).thenThrow(
            ResourceAccessException.class);

        assertThrows(SystemException.class, () -> auditionIntegrationClient.getPosts());
    }

    @Test
    void testGetPostsThrowsRestClientException() {
        when(restTemplate.getForObject(BASE_URL + POSTS, AuditionPost[].class)).thenThrow(RestClientException.class);

        assertThrows(SystemException.class, () -> auditionIntegrationClient.getPosts());
    }

    @Test
    void testGetPostByIdThrowsHttpClientErrorException() {
        final String postId = "1";
        final HttpClientErrorException exception = HttpClientErrorException.create(HttpStatus.BAD_REQUEST, BAD_REQUEST,
            new HttpHeaders(), new byte[0], null);
        when(restTemplate.getForObject(BASE_URL + POSTS_SLASH + postId, AuditionPost.class)).thenThrow(exception);

        final SystemException thrown = assertThrows(SystemException.class,
            () -> auditionIntegrationClient.getPostById(postId));
        assertEquals(HTTP_ERROR, thrown.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST.value(), thrown.getStatusCode());
    }

    @Test
    void testGetPostByIdThrowsResourceAccessException() {
        final String postId = "1";
        when(restTemplate.getForObject(BASE_URL + POSTS_SLASH + postId, AuditionPost.class)).thenThrow(
            ResourceAccessException.class);

        assertThrows(SystemException.class, () -> auditionIntegrationClient.getPostById(postId));
    }

    @Test
    void testGetPostByIdThrowsRestClientException() {
        final String postId = "1";
        when(restTemplate.getForObject(BASE_URL + POSTS_SLASH + postId, AuditionPost.class)).thenThrow(
            RestClientException.class);

        assertThrows(SystemException.class, () -> auditionIntegrationClient.getPostById(postId));
    }

    @Test
    void testGetPostWithCommentsByIdThrowsHttpClientErrorException() {
        final String postId = "1";
        final HttpClientErrorException exception = HttpClientErrorException.create(HttpStatus.BAD_REQUEST, BAD_REQUEST,
            new HttpHeaders(), new byte[0], null);
        when(restTemplate.getForObject(BASE_URL + POSTS_SLASH + postId, AuditionPost.class)).thenThrow(exception);

        final SystemException thrown = assertThrows(SystemException.class,
            () -> auditionIntegrationClient.getPostWithCommentsById(postId));
        assertEquals(HTTP_ERROR, thrown.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST.value(), thrown.getStatusCode());
    }

    @Test
    void testGetPostWithCommentsByIdThrowsResourceAccessException() {
        final String postId = "1";
        when(restTemplate.getForObject(BASE_URL + POSTS_SLASH + postId, AuditionPost.class)).thenThrow(
            ResourceAccessException.class);

        assertThrows(SystemException.class, () -> auditionIntegrationClient.getPostWithCommentsById(postId));
    }

    @Test
    void testGetPostWithCommentsByIdThrowsRestClientException() {
        final String postId = "1";
        when(restTemplate.getForObject(BASE_URL + POSTS_SLASH + postId, AuditionPost.class)).thenThrow(
            RestClientException.class);

        assertThrows(SystemException.class, () -> auditionIntegrationClient.getPostWithCommentsById(postId));
    }

    @Test
    void testGetCommentsByPostIdThrowsHttpClientErrorException() {
        final String postId = "1";
        final HttpClientErrorException exception = HttpClientErrorException.create(HttpStatus.BAD_REQUEST, BAD_REQUEST,
            new HttpHeaders(), new byte[0], null);
        when(restTemplate.getForObject(BASE_URL + COMMENTS_BY_POST_ID + postId, AuditionPostComment[].class)).thenThrow(
            exception);

        final SystemException thrown = assertThrows(SystemException.class,
            () -> auditionIntegrationClient.getCommentsByPostId(postId));
        assertEquals(HTTP_ERROR, thrown.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST.value(), thrown.getStatusCode());
    }

    @Test
    void testGetCommentsByPostIdThrowsResourceAccessException() {
        final String postId = "1";
        when(restTemplate.getForObject(BASE_URL + COMMENTS_BY_POST_ID + postId, AuditionPostComment[].class)).thenThrow(
            ResourceAccessException.class);

        assertThrows(SystemException.class, () -> auditionIntegrationClient.getCommentsByPostId(postId));
    }

    @Test
    void testGetCommentsByPostIdThrowsRestClientException() {
        final String postId = "1";
        when(restTemplate.getForObject(BASE_URL + COMMENTS_BY_POST_ID + postId, AuditionPostComment[].class)).thenThrow(
            RestClientException.class);

        assertThrows(SystemException.class, () -> auditionIntegrationClient.getCommentsByPostId(postId));
    }
}