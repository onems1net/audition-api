package com.audition.integration;

import com.audition.common.exception.SystemException;
import com.audition.model.AuditionPost;
import com.audition.model.AuditionPostComment;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class AuditionIntegrationClient {


    @Autowired
    private RestTemplate restTemplate;

    public List<AuditionPost> getPosts() {
        // TODO make RestTemplate call to get Posts from https://jsonplaceholder.typicode.com/posts

        try {
            AuditionPost[] posts = restTemplate.getForObject("https://jsonplaceholder.typicode.com/posts",
                AuditionPost[].class);
            return posts != null ? Arrays.asList(posts) : new ArrayList<>();
        } catch (HttpClientErrorException e) {
            throw new SystemException("Error fetching posts", e.getMessage(), e.getStatusCode().value());
        }
    }

    public AuditionPost getPostById(final String id) {
        // TODO get post by post ID call from https://jsonplaceholder.typicode.com/posts/
        try {
            return restTemplate.getForObject("https://jsonplaceholder.typicode.com/posts/" + id, AuditionPost.class);
        } catch (final HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new SystemException("Cannot find a Post with id " + id, "Resource Not Found", 404);
            } else {
                throw new SystemException("Error fetching post", e.getMessage(), e.getStatusCode().value());
            }
        }
    }

    // TODO Write a method GET comments for a post from https://jsonplaceholder.typicode.com/posts/{postId}/comments - the comments must be returned as part of the post.
    public AuditionPost getPostWithCommentsById(final String postId) {
        try {
            AuditionPost post = restTemplate.getForObject("https://jsonplaceholder.typicode.com/posts/" + postId,
                AuditionPost.class);
            if (post != null) {
                AuditionPostComment[] comments = restTemplate.getForObject(
                    "https://jsonplaceholder.typicode.com/posts/" + postId + "/comments", AuditionPostComment[].class);
                post.setComments(comments != null ? Arrays.asList(comments)
                    : new ArrayList<>());
            }
            return post;
        } catch (final HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new SystemException("Cannot find a Post with id " + postId, "Resource Not Found", 404);
            } else {
                throw new SystemException("Error fetching post or comments", e.getMessage(), e.getStatusCode().value());
            }
        }
    }

    // TODO write a method. GET comments for a particular Post from https://jsonplaceholder.typicode.com/comments?postId={postId}.
    // The comments are a separate list that needs to be returned to the API consumers. Hint: this is not part of the AuditionPost pojo.
    public List<AuditionPostComment> getCommentsByPostId(final String postId) {
        try {
            AuditionPostComment[] comments = restTemplate.getForObject(
                "https://jsonplaceholder.typicode.com/comments?postId=" + postId, AuditionPostComment[].class);
            return comments != null ? Arrays.asList(comments) : new ArrayList<>();
        } catch (final HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new SystemException("Cannot find comments for Post with id " + postId, "Resource Not Found", 404);
            } else {
                throw new SystemException("Error fetching comments", e.getMessage(), e.getStatusCode().value());
            }
        }
    }

}
