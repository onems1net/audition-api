package com.audition.service;

import com.audition.integration.AuditionIntegrationClient;
import com.audition.model.AuditionPost;
import com.audition.model.AuditionPostComment;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

class AuditionServiceTest {

    @Mock
    private transient AuditionIntegrationClient auditionIntegrationClient;

    @InjectMocks
    private transient AuditionService auditionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetPosts() {
        final AuditionPost post = new AuditionPost();
        final List<AuditionPost> expectedPosts = Collections.singletonList(post);
        when(auditionIntegrationClient.getPosts()).thenReturn(expectedPosts);

        final List<AuditionPost> actualPosts = auditionService.getPosts();
        assertEquals(expectedPosts, actualPosts);
    }

    @Test
    void testGetPostById() {
        final String postId = "1";
        final AuditionPost expectedPost = new AuditionPost();
        when(auditionIntegrationClient.getPostById(postId)).thenReturn(expectedPost);

        final AuditionPost actualPost = auditionService.getPostById(postId);
        assertEquals(expectedPost, actualPost);
    }

    @Test
    void testGetPostWithCommentsById() {
        final String postId = "1";
        final AuditionPost expectedPost = new AuditionPost();
        when(auditionIntegrationClient.getPostWithCommentsById(postId)).thenReturn(expectedPost);

        final AuditionPost actualPost = auditionService.getPostWithCommentsById(postId);
        assertEquals(expectedPost, actualPost);
    }

    @Test
    void testGetCommentsByPostId() {
        final String postId = "1";
        final AuditionPostComment comment = new AuditionPostComment();
        final List<AuditionPostComment> expectedComments = Collections.singletonList(comment);
        when(auditionIntegrationClient.getCommentsByPostId(postId)).thenReturn(expectedComments);

        final List<AuditionPostComment> actualComments = auditionService.getCommentsByPostId(postId);
        assertEquals(expectedComments, actualComments);
    }
}