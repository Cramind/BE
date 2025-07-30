package com.groupstudy.domain.github.controller;

import com.groupstudy.domain.github.api.GitHubClient;
import com.groupstudy.domain.github.dto.*;
import com.groupstudy.domain.github.dto.request.OrgInvitationRequest;
import com.groupstudy.domain.github.dto.request.OrgRepoRequest;
import com.groupstudy.domain.github.dto.request.RepoTemplateRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/todo")
@RequiredArgsConstructor
public class ToDoController {

    private final GitHubClient gitHubClient;

    @GetMapping("/org")
    public List<GitHubOrgDto> getOrgList(){
        return gitHubClient.getOrgs();
    }

    @PostMapping("/org/repo")
    public void postOrgRepo(@RequestBody OrgRepoRequest request){
        gitHubClient.createRepositoryInOrg(request);
    }

    @PostMapping("/org/repo/template")
    public ResponseEntity<Void> uploadTemplates(
            @RequestPart("files") List<MultipartFile> files,
            @RequestPart("metadataList") List<RepoTemplateRequest> metadataList) throws IOException {

        if (files.size() != metadataList.size()) {
            throw new IllegalArgumentException("파일 수와 메타데이터 수가 일치하지 않습니다.");
        }

        for (int i = 0; i < files.size(); i++) {
            MultipartFile file = files.get(i);
            RepoTemplateRequest metadata = metadataList.get(i);

            gitHubClient.uploadUserIssueTemplate(file, metadata);
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping("/org/invite/{orgName}")
    public void postOrgRepoTemplate(
            @PathVariable String orgName,
            @RequestBody OrgInvitationRequest orgInvitationRequest){
        gitHubClient.inviteUsersToOrg(orgName, orgInvitationRequest);
    }

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