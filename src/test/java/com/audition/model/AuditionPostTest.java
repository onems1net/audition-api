package com.audition.model;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AuditionPostTest {

    private transient AuditionPost auditionPost;

    @BeforeEach
    void setUp() {
        auditionPost = new AuditionPost();
    }

    @Test
    void testGettersAndSetters() {
        final int userId = 1;
        final int id = 101;
        final String title = "Test Title";
        final String body = "Test Body";

        auditionPost.setUserId(userId);
        auditionPost.setId(id);
        auditionPost.setTitle(title);
        auditionPost.setBody(body);

        assertEquals(userId, auditionPost.getUserId());
        assertEquals(id, auditionPost.getId());
        assertEquals(title, auditionPost.getTitle());
        assertEquals(body, auditionPost.getBody());
    }

    @Test
    void testGetComments() {
        final List<AuditionPostComment> comments = new ArrayList<>();
        comments.add(new AuditionPostComment());
        auditionPost.setComments(comments);

        final List<AuditionPostComment> retrievedComments = auditionPost.getComments();
        assertEquals(1, retrievedComments.size());
        assertEquals(comments, retrievedComments);
    }

    @Test
    void testSetComments() {
        final List<AuditionPostComment> comments = new ArrayList<>();
        comments.add(new AuditionPostComment());
        auditionPost.setComments(comments);

        final List<AuditionPostComment> retrievedComments = auditionPost.getComments();
        assertEquals(1, retrievedComments.size());
        assertEquals(comments, retrievedComments);
    }

    @Test
    void testGetCommentsReturnsUnmodifiableList() {
        final List<AuditionPostComment> comments = new ArrayList<>();
        comments.add(new AuditionPostComment());
        auditionPost.setComments(comments);

        final List<AuditionPostComment> retrievedComments = auditionPost.getComments();
        assertThrows(UnsupportedOperationException.class, () -> {
            retrievedComments.add(new AuditionPostComment());
        });
    }
}