package com.groupstudy.domain.github.api;

import com.groupstudy.domain.github.dto.*;
import com.groupstudy.domain.github.dto.request.OrgInvitationRequest;
import com.groupstudy.domain.github.dto.request.OrgRepoRequest;
import com.groupstudy.domain.github.dto.request.RepoTemplateRequest;
import com.groupstudy.domain.github.util.HttpSetter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class GitHubClient {

    @Value("${github.api-url}")
    private String apiUrl;

    @Value("${github.token}")
    private String token;

    private final RestTemplate restTemplate = new RestTemplate();

    public void createRepositoryInOrg(OrgRepoRequest request) {
        String url = String.format("https://api.github.com/orgs/%s/repos", request.orgName());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.set("Accept", "application/vnd.github+json");
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();
        body.put("name", request.repoName());
        body.put("private", request.isPrivate());
        body.put("auto_init", true);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                String.class
        );
    }

    public void inviteUsersToOrg(String orgName, OrgInvitationRequest orgInvitationRequest){
        String url = String.format("https://api.github.com//orgs/%s/invitations", orgName);
        HttpHeaders headers = HttpSetter.httpPreset(token);
        Map<String, Object> body = new HashMap<>();

        for(String email : orgInvitationRequest.email()){
            body.put("email", email);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    String.class
            );
        }
    }

    public void uploadUserIssueTemplate(MultipartFile file, RepoTemplateRequest repoTemplateRequest) throws IOException {
        String fileName = file.getOriginalFilename();
        if (fileName == null || !fileName.endsWith(".md")) {
            throw new IllegalArgumentException("이슈 템플릿은 .md 파일만 허용됩니다.");
        }
        String path;
        switch(repoTemplateRequest.category()){
            case "bug": path = ".github/ISSUE_TEMPLATE/bug_template.md";
            case "feat": path = ".github/ISSUE_TEMPLATE/feat_template.md";
            default: path = ".github/ISSUE_TEMPLATE/" + fileName;
        }

        String url = String.format("https://api.github.com/repos/%s/%s/contents/%s", repoTemplateRequest.owner(), repoTemplateRequest.repo(), path);

        String encodedContent = Base64.getEncoder().encodeToString(file.getBytes());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.set("Accept", "application/vnd.github+json");
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();
        body.put("message", "feat: 사용자 업로드 이슈 템플릿 추가");
        body.put("content", encodedContent);
        body.put("branch", "main");

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                request,
                String.class
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            log.info("✅ 이슈 템플릿 업로드 완료: {}", fileName);
        } else {
            log.error("❌ 업로드 실패: {} | {}", response.getStatusCode(), response.getBody());
        }
    }

    public List<GitHubOrgDto> getOrgs() {
        String url = String.format("https://api.github.com/user/orgs");

        HttpHeaders headers = HttpSetter.httpPreset(token);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<GitHubOrgDto[]> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                GitHubOrgDto[].class
        );

        return Arrays.asList(response.getBody());
    }

    public List<GitHubIssueDto> getIssues(String owner, String repo) {
        String url = String.format("%s/repos/%s/%s/issues", apiUrl, owner, repo);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.set("Accept", "application/vnd.github+json");

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<GitHubIssueDto[]> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                GitHubIssueDto[].class
        );

        return Arrays.asList(response.getBody());
    }
    public List<GitHubPullRequestDto> getPullRequests(String owner, String repo) {
        String url = String.format("%s/repos/%s/%s/pulls?state=all", apiUrl, owner, repo);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.set("Accept", "application/vnd.github+json");

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<GitHubPullRequestDto[]> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                GitHubPullRequestDto[].class
        );

        return Arrays.asList(response.getBody());
    }
    public List<GitHubReviewCommentDto> fetchReviewComments(String owner, String repo) {
        String url = String.format("https://api.github.com/repos/%s/%s/pulls/comments", owner, repo);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.set("Accept", "application/vnd.github+json");
        HttpEntity<Void> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<GitHubReviewCommentDto[]> response = restTemplate.exchange(
                    url, HttpMethod.GET, request, GitHubReviewCommentDto[].class
            );

            GitHubReviewCommentDto[] comments = response.getBody();
            return comments != null ? Arrays.asList(comments) : Collections.emptyList();

        } catch (HttpClientErrorException.NotFound e) {
            return Collections.emptyList();
        } catch (Exception e) {
            // 로그로 예외 확인 권장
            return Collections.emptyList();
        }
    }
    public ReviewStatsResponse fetchReviewStats(String owner, String repo, int prCount) {
        Map<String, UserReviewStatsDto> statsMap = new HashMap<>();
        int approved = 0;
        int changesRequested = 0;
        int commented = 0;

        for (int pr = 1; pr <= prCount; pr++) {
            String url = String.format("https://api.github.com/repos/%s/%s/pulls/%d/reviews", owner, repo, pr);

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            HttpEntity<Void> request = new HttpEntity<>(headers);

            try {
                ResponseEntity<GitHubReviewDto[]> response = restTemplate.exchange(
                        url, HttpMethod.GET, request, GitHubReviewDto[].class
                );

                for (GitHubReviewDto review : response.getBody()) {
                    if (review.getUser() == null ) continue;
                    String reviewer = review.getUser().getLogin();
                    String avatarUrl = review.getUser().getAvatarUrl();

                    UserReviewStatsDto stat = statsMap.getOrDefault(
                            reviewer,
                            new UserReviewStatsDto(reviewer, avatarUrl, 0, 0, 0)
                    );
                    if(!reviewer.contains("coderabbit")){
                        switch (review.getState()) {
                            case "APPROVED":
                                stat.setApproved(stat.getApproved() + 1);
                                approved++;
                            case "CHANGES_REQUESTED":
                                stat.setChangesRequested(stat.getChangesRequested() + 1);
                                changesRequested++;
                            case "COMMENTED":
                                stat.setCommented(stat.getChangesRequested() + 1);
                                commented++;
                        }
                        statsMap.put(reviewer, stat);
                    }

                }
            } catch (Exception e) {
                System.err.println("PR#" + pr + " 에 대한 리뷰 조회 실패: " + e.getMessage());
            }
        }

        TotalReviewStatsDto totalReviewStatsDto = new TotalReviewStatsDto(approved, changesRequested, commented);
        List<UserReviewStatsDto> userReviewStatsDtoList = new ArrayList<>(statsMap.values());

        return new ReviewStatsResponse(totalReviewStatsDto, userReviewStatsDtoList);
    }
}