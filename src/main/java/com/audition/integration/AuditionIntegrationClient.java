package com.audition.integration;

import com.audition.common.exception.SystemException;
import com.audition.model.AuditionPost;
import com.audition.model.AuditionPostComment;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * Client for integrating with the Audition API. Provides methods to retrieve audition posts and comments.
 */
@Component
public class AuditionIntegrationClient {

    @Autowired
    private transient RestTemplate restTemplate;

    @Value("${audition.integration.base-url}")
    private transient String baseUrl;

    /**
     * Retrieves a list of audition posts.
     *
     * @return a list of audition posts
     */
    public List<AuditionPost> getPosts() {
        return handleExceptions(() -> {
            AuditionPost[] posts = restTemplate.getForObject(baseUrl + "posts", AuditionPost[].class);
            return posts != null ? Arrays.asList(posts) : new ArrayList<>();
        });
    }

    /**
     * Retrieves an audition post by its ID.
     *
     * @param id the ID of the post
     * @return the audition post
     */
    public AuditionPost getPostById(final String id) {
        return handleExceptions(() -> restTemplate.getForObject(baseUrl + "posts/" + id, AuditionPost.class));
    }

    /**
     * Retrieves an audition post along with its comments by the post ID.
     *
     * @param postId the ID of the post
     * @return the audition post with comments
     */
    public AuditionPost getPostWithCommentsById(final String postId) {
        return handleExceptions(() -> {
            AuditionPost post = restTemplate.getForObject(baseUrl + "posts/" + postId, AuditionPost.class);
            if (post != null) {
                AuditionPostComment[] comments = restTemplate.getForObject(baseUrl + "posts/" + postId + "/comments",
                    AuditionPostComment[].class);
                post.setComments(comments != null ? Arrays.asList(comments) : new ArrayList<>());
            }
            return post;
        });
    }

    /**
     * Retrieves comments for a specific audition post by the post ID.
     *
     * @param postId the ID of the post
     * @return a list of comments for the post
     */
    public List<AuditionPostComment> getCommentsByPostId(final String postId) {
        return handleExceptions(() -> {
            AuditionPostComment[] comments = restTemplate.getForObject(baseUrl + "comments?postId=" + postId,
                AuditionPostComment[].class);
            return comments != null ? Arrays.asList(comments) : new ArrayList<>();
        });
    }

    /**
     * Handles exceptions that occur during RestTemplate calls.
     *
     * @param call the RestTemplate call to execute
     * @param <T>  the type of the result
     * @return the result of the RestTemplate call
     * @throws SystemException if an error occurs during the RestTemplate call
     */
    private <T> T handleExceptions(RestTemplateCall<T> call) {
        try {
            return call.execute();
        } catch (HttpClientErrorException e) {
            throw new SystemException("HTTP error", e.getMessage(), e.getStatusCode().value());
        } catch (ResourceAccessException e) {
            throw new SystemException("Resource access error", e.getMessage(), HttpStatus.SERVICE_UNAVAILABLE.value());
        } catch (RestClientException e) {
            throw new SystemException("Client error", e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        } catch (Exception e) {
            throw new SystemException("Unexpected error", e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    /**
     * Functional interface for executing RestTemplate calls.
     *
     * @param <T> the type of the result
     */
    @FunctionalInterface
    private interface RestTemplateCall<T> {

        /**
         * Executes the RestTemplate call.
         *
         * @return the result of the RestTemplate call
         * @throws Exception if an error occurs during the RestTemplate call
         */
        T execute() throws Exception;
    }
}