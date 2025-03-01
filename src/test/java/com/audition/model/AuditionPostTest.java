package com.audition.model;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AuditionPostTest {

    private AuditionPost auditionPost;

    @BeforeEach
    void setUp() {
        auditionPost = new AuditionPost();
    }

    @Test
    void testGettersAndSetters() {
        auditionPost.setUserId(1);
        auditionPost.setId(101);
        auditionPost.setTitle("Test Title");
        auditionPost.setBody("Test Body");

        assertEquals(1, auditionPost.getUserId());
        assertEquals(101, auditionPost.getId());
        assertEquals("Test Title", auditionPost.getTitle());
        assertEquals("Test Body", auditionPost.getBody());
    }

    @Test
    void testGetComments() {
        List<AuditionPostComment> comments = new ArrayList<>();
        comments.add(new AuditionPostComment());
        auditionPost.setComments(comments);

        List<AuditionPostComment> retrievedComments = auditionPost.getComments();
        assertEquals(1, retrievedComments.size());
        assertEquals(comments, retrievedComments);
    }

    @Test
    void testSetComments() {
        List<AuditionPostComment> comments = new ArrayList<>();
        comments.add(new AuditionPostComment());
        auditionPost.setComments(comments);

        List<AuditionPostComment> retrievedComments = auditionPost.getComments();
        assertEquals(1, retrievedComments.size());
        assertEquals(comments, retrievedComments);
    }

    @Test
    void testGetCommentsReturnsUnmodifiableList() {
        List<AuditionPostComment> comments = new ArrayList<>();
        comments.add(new AuditionPostComment());
        auditionPost.setComments(comments);

        List<AuditionPostComment> retrievedComments = auditionPost.getComments();
        assertThrows(UnsupportedOperationException.class, () -> {
            retrievedComments.add(new AuditionPostComment());
        });
    }
}