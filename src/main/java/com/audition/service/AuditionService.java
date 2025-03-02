package com.audition.service;

import com.audition.integration.AuditionIntegrationClient;
import com.audition.model.AuditionPost;
import com.audition.model.AuditionPostComment;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Service class for managing audition posts and comments.
 */
@Service
public class AuditionService {

    private final transient AuditionIntegrationClient auditionIntegrationClient;

    /**
     * Constructs a new AuditionService with the specified AuditionIntegrationClient.
     *
     * @param auditionIntegrationClient the audition integration client
     */
    public AuditionService(final AuditionIntegrationClient auditionIntegrationClient) {
        this.auditionIntegrationClient = auditionIntegrationClient;
    }

    /**
     * Retrieves a list of audition posts.
     *
     * @return a list of audition posts
     */
    public List<AuditionPost> getPosts() {
        return auditionIntegrationClient.getPosts();
    }

    /**
     * Retrieves an audition post by its ID.
     *
     * @param postId the ID of the post
     * @return the audition post
     */
    public AuditionPost getPostById(final String postId) {
        return auditionIntegrationClient.getPostById(postId);
    }

    /**
     * Retrieves an audition post along with its comments by the post ID.
     *
     * @param postId the ID of the post
     * @return the audition post with comments
     */
    public AuditionPost getPostWithCommentsById(final String postId) {
        return auditionIntegrationClient.getPostWithCommentsById(postId);
    }

    /**
     * Retrieves comments for a specific audition post by the post ID.
     *
     * @param postId the ID of the post
     * @return a list of comments for the post
     */
    public List<AuditionPostComment> getCommentsByPostId(final String postId) {
        return auditionIntegrationClient.getCommentsByPostId(postId);
    }
}