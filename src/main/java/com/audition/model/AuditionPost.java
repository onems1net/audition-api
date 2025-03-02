package com.audition.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuditionPost {

    private int userId;
    private int id;
    private String title;
    private String body;
    private List<AuditionPostComment> comments = new ArrayList<>();

    public List<AuditionPostComment> getComments() {
        return Collections.unmodifiableList(comments);
    }

    public void setComments(final List<AuditionPostComment> comments) {
        this.comments = new ArrayList<>(comments);
    }
}
