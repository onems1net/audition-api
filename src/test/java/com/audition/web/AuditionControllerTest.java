package com.audition.web;

import com.audition.model.AuditionPost;
import com.audition.model.AuditionPostComment;
import com.audition.service.AuditionService;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class AuditionControllerTest {

    @Mock
    private transient AuditionService auditionService;

    @InjectMocks
    private transient AuditionController auditionController;

    private transient MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(auditionController).build();
    }

    @Test
    void testGetPosts() throws Exception {
        List<AuditionPost> posts = Collections.singletonList(new AuditionPost());
        when(auditionService.getPosts()).thenReturn(posts);

        mockMvc.perform(get("/posts")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json("[{}]"));
    }

    @Test
    void testGetPostById() throws Exception {
        AuditionPost post = new AuditionPost();
        when(auditionService.getPostById("1")).thenReturn(post);

        mockMvc.perform(get("/posts/1")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json("{}"));
    }

    @Test
    void testGetPostWithCommentsById() throws Exception {
        AuditionPost post = new AuditionPost();
        when(auditionService.getPostWithCommentsById("1")).thenReturn(post);

        mockMvc.perform(get("/posts/1/comments")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json("{}"));
    }

    @Test
    void testGetCommentsByPostId() throws Exception {
        List<AuditionPostComment> comments = Collections.singletonList(new AuditionPostComment());
        when(auditionService.getCommentsByPostId("1")).thenReturn(comments);

        mockMvc.perform(get("/comments/1")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json("[{}]"));
    }
}