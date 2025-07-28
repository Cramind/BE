package com.groupstudy.domain.github.controller;

import com.groupstudy.domain.github.api.GitHubClient;
import com.groupstudy.domain.github.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todo")
@CrossOrigin("http://3.107.19.34")
@RequiredArgsConstructor
public class ToDoController {

    private final GitHubClient gitHubClient;

    // 예: /api/todo/issues?owner=spring-project&repo=spring-framework
    @GetMapping("/issues")
    public List<GitHubIssueDto> getGithubIssues(
            @RequestParam String owner,
            @RequestParam String repo
    ) {
        return gitHubClient.getIssues(owner, repo);
    }

    @GetMapping("/prs")
    public List<GitHubPullRequestDto> getGithubPulls(
            @RequestParam String owner,
            @RequestParam String repo
    ) {
        return gitHubClient.getPullRequests(owner, repo);
    }
    @GetMapping("/reviews")
    public List<GitHubReviewCommentDto> getReviews(
            @RequestParam String owner,
            @RequestParam String repo
    ) {
        return gitHubClient.fetchReviewComments(owner, repo);
    }
    @GetMapping
    public ReviewStatsResponse getReviewStats(
            @RequestParam String owner,
            @RequestParam String repo,
            @RequestParam(defaultValue = "10") int prCount
    ) {
        return gitHubClient.fetchReviewStats(owner, repo, prCount);
    }
}