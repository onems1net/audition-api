package com.audition.web;

import com.audition.model.AuditionPost;
import com.audition.model.AuditionPostComment;
import com.audition.service.AuditionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing audition posts and comments.
 */
@RestController
public class AuditionController {

    private final transient AuditionService auditionService;

    /**
     * Constructs a new AuditionController with the specified AuditionService.
     *
     * @param auditionService the audition service
     */
    public AuditionController(final AuditionService auditionService) {
        this.auditionService = auditionService;
    }

    /**
     * Retrieves a list of audition posts. Optionally filters posts by title.
     *
     * @param title the title to filter posts by (optional)
     * @return a list of audition posts
     */
    @RequestMapping(value = "/posts", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<AuditionPost> getPosts(
        @RequestParam(value = "title", required = false) final String title) {

        List<AuditionPost> posts = auditionService.getPosts();
        if (title != null && !title.isEmpty()) {
            posts = posts.stream()
                .filter(post -> post.getTitle().contains(title))
                .collect(Collectors.toList());
        }
        return posts;
    }

    /**
     * Retrieves an audition post by its ID.
     *
     * @param postId the ID of the post
     * @return the audition post
     */
    @RequestMapping(value = "/posts/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody AuditionPost getPostById(@PathVariable("id") @Valid @NotNull final String postId) {
        return auditionService.getPostById(postId);
    }

    /**
     * Retrieves an audition post along with its comments by the post ID.
     *
     * @param postId the ID of the post
     * @return the audition post with comments
     */
    @RequestMapping(value = "/posts/{id}/comments", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody AuditionPost getPostWithCommentsById(@PathVariable("id") @Valid @NotNull final String postId) {
        return auditionService.getPostWithCommentsById(postId);
    }

    /**
     * Retrieves comments for a specific audition post by the post ID.
     *
     * @param postId the ID of the post
     * @return a list of comments for the post
     */
    @RequestMapping(value = "/comments/{postId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<AuditionPostComment> getCommentsByPostId(
        @PathVariable("postId") @Valid @NotNull final String postId) {
        return auditionService.getCommentsByPostId(postId);
    }
}