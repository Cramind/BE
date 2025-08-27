package com.groupstudy.domain.github.controller;

import com.groupstudy.domain.github.api.GitHubClient;
import com.groupstudy.domain.github.dto.*;
import com.groupstudy.domain.github.dto.request.OrgInvitationRequest;
import com.groupstudy.domain.github.dto.request.OrgRepoRequest;
import com.groupstudy.domain.github.dto.request.RepoTemplateRequest;
import com.groupstudy.domain.github.dto.response.GitHubOrgEventResponse;
import com.groupstudy.domain.github.dto.response.GithubIssueResponse;
import com.groupstudy.domain.github.dto.response.GithubPrResponse;
import com.groupstudy.global.response.ApiResponse;
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
    public ResponseEntity<ApiResponse<List<GitHubOrgDto>>> getOrgList(){

        return ResponseEntity.ok(ApiResponse.onSuccess(gitHubClient.getOrgs()));
    }

    @PostMapping("/org/repo")
    public ResponseEntity<ApiResponse<Void>> postOrgRepo(@RequestBody OrgRepoRequest request){
        gitHubClient.createRepositoryInOrg(request);
        return ResponseEntity.ok(ApiResponse.onSuccessVoid());
    }

    @PostMapping("/org/repo/template")
    public ResponseEntity<ApiResponse<Void>> uploadTemplates(
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

        return ResponseEntity.ok(ApiResponse.onSuccessVoid());
    }

    @PostMapping("/org/invite/{orgName}")
    public ResponseEntity<ApiResponse<Void>> postOrgRepoTemplate(
            @PathVariable String orgName,
            @RequestBody OrgInvitationRequest orgInvitationRequest){
        gitHubClient.inviteUsersToOrg(orgName, orgInvitationRequest);
        return ResponseEntity.ok(ApiResponse.onSuccessVoid());
    }

    @GetMapping("/issues/{teamId}")
    public ResponseEntity<ApiResponse<GithubIssueResponse>> getGithubIssues(@PathVariable Long teamId) {
        return ResponseEntity.ok(ApiResponse.onSuccess(gitHubClient.getIssues(teamId)));
    }

//    @GetMapping("/issues/my")
//    public ResponseEntity<ApiResponse<List<GitHubIssueDto>>> getMyGithubIssues(
//            @RequestParam String owner,
//            @RequestParam String repo
//    ) {
//        return ResponseEntity.ok(ApiResponse.onSuccess(gitHubClient.getAssignedIssues(owner, repo)));
//    }

    @GetMapping("/prs")
    public ResponseEntity<ApiResponse<List<GitHubPullRequestDto>>> getGithubPulls(
            @RequestParam String owner,
            @RequestParam String repo
    ) {
        return ResponseEntity.ok(ApiResponse.onSuccess(gitHubClient.getPullRequests(owner, repo)));
    }

    @GetMapping("/prs/my/{teamId}")
    public ResponseEntity<ApiResponse<GithubPrResponse>> getMyGithubPulls(
        @PathVariable Long teamId) {
        return ResponseEntity.ok(ApiResponse.onSuccess(gitHubClient.getMyPullRequests(teamId)));
    }

    @GetMapping("/event/{teamId}")
    public ResponseEntity<ApiResponse<GitHubOrgEventResponse>> getMyEvents(
        @PathVariable Long teamId) {
        return ResponseEntity.ok(ApiResponse.onSuccess(gitHubClient.getIssueAndPrEvents(teamId)));
    }

    @GetMapping("/stat/{teamId}")
    public ResponseEntity<ApiResponse<WeeklyDashboardResponse>> getOrgStats(
            @PathVariable Long teamId) {
        return ResponseEntity.ok(ApiResponse.onSuccess(gitHubClient.getStats(teamId, "kamillcream")));
    }


    @GetMapping("/reviews")
    public ResponseEntity<ApiResponse<List<GitHubReviewCommentDto>>>  getReviews(
            @RequestParam String owner,
            @RequestParam String repo
    ) {
        return ResponseEntity.ok(ApiResponse.onSuccess(gitHubClient.fetchReviewComments(owner, repo)));
    }
    @GetMapping
    public ResponseEntity<ApiResponse<ReviewStatsResponse>> getReviewStats(
            @RequestParam String owner,
            @RequestParam String repo,
            @RequestParam(defaultValue = "10") int prCount
    ) {
        return ResponseEntity.ok(ApiResponse.onSuccess(gitHubClient.fetchReviewStats(owner, repo, prCount)));
    }
}