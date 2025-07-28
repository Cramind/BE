package com.groupstudy.domain.github.api;

import com.groupstudy.domain.github.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;


import java.util.*;

@Component
@RequiredArgsConstructor
public class GitHubClient {

    @Value("${github.api-url}")
    private String apiUrl;

    @Value("${github.token}")
    private String token;

    private final RestTemplate restTemplate = new RestTemplate();

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