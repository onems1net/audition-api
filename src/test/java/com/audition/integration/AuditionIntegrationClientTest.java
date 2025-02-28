package com.audition.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.audition.common.exception.SystemException;
import com.audition.model.AuditionPost;
import com.audition.model.AuditionPostComment;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

class AuditionIntegrationClientTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private AuditionIntegrationClient auditionIntegrationClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetPosts() {
        AuditionPost[] posts = new AuditionPost[]{new AuditionPost()};
        when(restTemplate.getForObject("https://jsonplaceholder.typicode.com/posts", AuditionPost[].class))
            .thenReturn(posts);

        List<AuditionPost> actualPosts = auditionIntegrationClient.getPosts();
        assertEquals(Arrays.asList(posts), actualPosts);
    }

    @Test
    void testGetPostById() {
        String postId = "1";
        AuditionPost post = new AuditionPost();
        when(restTemplate.getForObject("https://jsonplaceholder.typicode.com/posts/" + postId, AuditionPost.class))
            .thenReturn(post);

        AuditionPost actualPost = auditionIntegrationClient.getPostById(postId);
        assertEquals(post, actualPost);
    }

    @Test
    void testGetPostWithCommentsById() {
        String postId = "1";
        AuditionPost post = new AuditionPost();
        AuditionPostComment[] comments = new AuditionPostComment[]{new AuditionPostComment()};
        when(restTemplate.getForObject("https://jsonplaceholder.typicode.com/posts/" + postId, AuditionPost.class))
            .thenReturn(post);
        when(restTemplate.getForObject("https://jsonplaceholder.typicode.com/posts/" + postId + "/comments",
            AuditionPostComment[].class))
            .thenReturn(comments);

        AuditionPost actualPost = auditionIntegrationClient.getPostWithCommentsById(postId);
        assertEquals(post, actualPost);
        assertEquals(Arrays.asList(comments), actualPost.getComments());
    }

    @Test
    void testGetCommentsByPostId() {
        String postId = "1";
        AuditionPostComment[] comments = new AuditionPostComment[]{new AuditionPostComment()};
        when(restTemplate.getForObject("https://jsonplaceholder.typicode.com/comments?postId=" + postId,
            AuditionPostComment[].class))
            .thenReturn(comments);

        List<AuditionPostComment> actualComments = auditionIntegrationClient.getCommentsByPostId(postId);
        assertEquals(Arrays.asList(comments), actualComments);
    }

    @Test
    void testGetPostsThrowsException() {
        when(restTemplate.getForObject("https://jsonplaceholder.typicode.com/posts", AuditionPost[].class))
            .thenThrow(HttpClientErrorException.class);

        assertThrows(SystemException.class, () -> auditionIntegrationClient.getPosts());
    }

    @Test
    void testGetPostByIdThrowsException() {
        String postId = "1";
        when(restTemplate.getForObject("https://jsonplaceholder.typicode.com/posts/" + postId, AuditionPost.class))
            .thenThrow(HttpClientErrorException.class);

        assertThrows(SystemException.class, () -> auditionIntegrationClient.getPostById(postId));
    }

    @Test
    void testGetPostWithCommentsByIdThrowsException() {
        String postId = "1";
        when(restTemplate.getForObject("https://jsonplaceholder.typicode.com/posts/" + postId, AuditionPost.class))
            .thenThrow(HttpClientErrorException.class);

        assertThrows(SystemException.class, () -> auditionIntegrationClient.getPostWithCommentsById(postId));
    }

    @Test
    void testGetCommentsByPostIdThrowsException() {
        String postId = "1";
        when(restTemplate.getForObject("https://jsonplaceholder.typicode.com/comments?postId=" + postId,
            AuditionPostComment[].class))
            .thenThrow(HttpClientErrorException.class);

        assertThrows(SystemException.class, () -> auditionIntegrationClient.getCommentsByPostId(postId));
    }
}
