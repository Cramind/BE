package com.groupstudy.domain.github.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class GitHubIssueDto {
    private String title;
    private String state;

    @JsonProperty("html_url")
    private String url;

    private int number;

    private String body;
    private String created_at;

    private User user;
    private Assignee assignee;

    private List<Label> labels;

    @Getter
    public static class User {
        private String login;
        private String avatar_url;
    }

    @Getter
    public static class Assignee {
        private String login;
        private String avatar_url;
    }

    @Getter
    public static class Label {
        private long id;
        private String name;
        private String color;
    }
}