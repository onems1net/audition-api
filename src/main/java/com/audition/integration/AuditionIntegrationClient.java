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

@Component
public class AuditionIntegrationClient {

    @Autowired
    private transient RestTemplate restTemplate;

    @Value("${audition.integration.base-url}")
    private transient String baseUrl;

    public List<AuditionPost> getPosts() {
        return handleExceptions(() -> {
            AuditionPost[] posts = restTemplate.getForObject(baseUrl + "posts", AuditionPost[].class);
            return posts != null ? Arrays.asList(posts) : new ArrayList<>();
        });
    }

    public AuditionPost getPostById(final String id) {
        return handleExceptions(() -> restTemplate.getForObject(baseUrl + "posts/" + id, AuditionPost.class));
    }

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

    public List<AuditionPostComment> getCommentsByPostId(final String postId) {
        return handleExceptions(() -> {
            AuditionPostComment[] comments = restTemplate.getForObject(baseUrl + "comments?postId=" + postId,
                AuditionPostComment[].class);
            return comments != null ? Arrays.asList(comments) : new ArrayList<>();
        });
    }

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

    @FunctionalInterface
    private interface RestTemplateCall<T> {

        T execute() throws Exception;
    }
}