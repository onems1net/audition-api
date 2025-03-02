package com.audition.integration;

import com.audition.model.AuditionPost;
import com.audition.model.AuditionPostComment;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class AuditionIntegrationClientIntegrationTest {

    @Autowired
    private transient AuditionIntegrationClient auditionIntegrationClient;

    @Test
    void testGetPosts() {
        final List<AuditionPost> posts = auditionIntegrationClient.getPosts();
        assertNotNull(posts);
        assertEquals(100, posts.size()); // Assuming the endpoint returns 100 posts
    }

    @Test
    void testGetPostById() {
        final String postId = "1";
        final AuditionPost post = auditionIntegrationClient.getPostById(postId);
        assertNotNull(post);
        assertEquals(postId, String.valueOf(post.getId()));
    }

    @Test
    void testGetPostWithCommentsById() {
        final String postId = "1";
        final AuditionPost post = auditionIntegrationClient.getPostWithCommentsById(postId);
        assertNotNull(post);
        assertEquals(postId, String.valueOf(post.getId()));
        assertNotNull(post.getComments());
    }

    @Test
    void testGetCommentsByPostId() {
        final String postId = "1";
        final List<AuditionPostComment> comments = auditionIntegrationClient.getCommentsByPostId(postId);
        assertNotNull(comments);
        assertEquals(5, comments.size()); // Assuming the post has 5 comments
    }
}