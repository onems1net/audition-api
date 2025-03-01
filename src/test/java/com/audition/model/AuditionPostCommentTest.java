package com.audition.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AuditionPostCommentTest {

    private transient AuditionPostComment auditionPostComment;

    @BeforeEach
    void setUp() {
        auditionPostComment = new AuditionPostComment();
    }

    @Test
    void testGettersAndSetters() {
        auditionPostComment.setPostId(1);
        auditionPostComment.setId(101);
        auditionPostComment.setName("Test Name");
        auditionPostComment.setEmail("test@example.com");
        auditionPostComment.setBody("Test Body");

        assertEquals(1, auditionPostComment.getPostId());
        assertEquals(101, auditionPostComment.getId());
        assertEquals("Test Name", auditionPostComment.getName());
        assertEquals("test@example.com", auditionPostComment.getEmail());
        assertEquals("Test Body", auditionPostComment.getBody());
    }
}