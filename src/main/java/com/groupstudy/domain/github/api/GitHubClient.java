package com.groupstudy.domain.github.api;

import com.groupstudy.domain.github.dto.*;
import com.groupstudy.domain.github.dto.request.OrgInvitationRequest;
import com.groupstudy.domain.github.dto.request.OrgRepoRequest;
import com.groupstudy.domain.github.dto.request.RepoTemplateRequest;
import com.groupstudy.domain.github.dto.response.GitHubOrgEventResponse;
import com.groupstudy.domain.github.dto.response.GithubIssueResponse;
import com.groupstudy.domain.github.dto.response.GithubPrResponse;
import com.groupstudy.domain.github.util.HttpSetter;
import com.groupstudy.domain.team.entity.Team;
import com.groupstudy.domain.team.entity.TeamOrganization;
import com.groupstudy.domain.team.entity.TeamRepo;
import com.groupstudy.domain.team.repository.TeamOrgRepository;
import com.groupstudy.domain.team.repository.TeamRepoRepository;
import com.groupstudy.domain.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class GitHubClient {

    private final TeamRepository teamRepository;
    private final TeamOrgRepository teamOrgRepository;
    private final TeamRepoRepository teamRepoRepository;

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

    public GithubIssueResponse getIssues(Long teamId) {
        Team team = teamRepository.findById(teamId).orElseThrow();

        List<GitHubIssueDto> all = new ArrayList<>();

        for (TeamOrganization org : Optional.ofNullable(team.getTeamOrganizations()).orElse(Collections.emptyList())) {
            String owner = org.getOrgName();
            for (TeamRepo repo : Optional.ofNullable(org.getChildrenRepos()).orElse(Collections.emptyList())) {
                all.addAll(fetchIssues(owner, repo.getRepoName()));
            }
        }

         all.removeIf(dto -> dto.getPullRequest() != null);

        return new GithubIssueResponse(all.size(), all);
    }

    private List<GitHubIssueDto> fetchIssues(String owner, String repo) {
        List<GitHubIssueDto> acc = new ArrayList<>();
        int page = 1;
        final int perPage = 100;

        while (true) {
            String url = String.format("%s/repos/%s/%s/issues?state=open&per_page=%d&page=%d",
                    apiUrl, owner, repo, perPage, page);

            ResponseEntity<GitHubIssueDto[]> res = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    githubEntity(),           // 공통 헤더 분리
                    GitHubIssueDto[].class
            );

            GitHubIssueDto[] body = res.getBody();
            if (body == null || body.length == 0) break;

            acc.addAll(Arrays.asList(body));

            // 마지막 페이지면 종료
            if (body.length < perPage) break;
            page++;
        }

        return acc;
    }

    private HttpEntity<Void> githubEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token); // = headers.set("Authorization", "Bearer " + token)
        headers.set("Accept", "application/vnd.github+json");
        return new HttpEntity<>(headers);
    }

//    public List<GitHubIssueDto> getAssignedIssues(String owner, String repo) {
//        String url = String.format("%s/repos/%s/%s/issues?creator=kamillcream", apiUrl, owner, repo);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", "Bearer " + token);
//        headers.set("Accept", "application/vnd.github+json");
//
//        HttpEntity<Void> entity = new HttpEntity<>(headers);
//
//        ResponseEntity<GitHubIssueDto[]> response = restTemplate.exchange(
//                url,
//                HttpMethod.GET,
//                entity,
//                GitHubIssueDto[].class
//        );
//
//        return Arrays.asList(response.getBody());
//    }


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

    public GithubPrResponse getMyPullRequests(Long teamId) {
        Team team = teamRepository.findById(teamId).orElseThrow();

        List<GitHubPullRequestDto> all = new ArrayList<>();

        for (TeamOrganization org : Optional.ofNullable(team.getTeamOrganizations()).orElse(Collections.emptyList())) {
            String owner = org.getOrgName();

            for (TeamRepo repo : Optional.ofNullable(org.getChildrenRepos()).orElse(Collections.emptyList())) {
                String repoName = repo.getRepoName(); // getter 이름 맞춰주세요

                all.addAll(fetchPullRequests(owner, repoName, "kamillcream"));
            }
        }

        return new GithubPrResponse(all.size(), all);
    }

    private List<GitHubPullRequestDto> fetchPullRequests(String owner, String repo, String username) {
        String url = String.format(
                "%s/search/issues?q=type:pr+state:open+repo:%s/%s+review-requested:%s",
                apiUrl, owner, repo, username
        );

        ResponseEntity<GitHubPRSearchResponse> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                githubEntity(),
                GitHubPRSearchResponse.class
        );

        if (response.getBody() == null || response.getBody().getItems() == null) {
            return Collections.emptyList();
        }

        return response.getBody().getItems();
    }

    public GitHubOrgEventResponse getIssueAndPrEvents(Long teamId) {
        Team team = teamRepository.findById(teamId).orElseThrow();

        List<GitHubOrgEventDto> all = new ArrayList<>();

        for (TeamOrganization org : Optional.ofNullable(team.getTeamOrganizations()).orElse(Collections.emptyList())) {
            String orgName = org.getOrgName();
            all.addAll(fetchOrgIssuePrEvents(orgName));
        }

        // created_at 기준 최신순 정렬 (필드명 맞게 수정)
        all.sort(Comparator.comparing(GitHubOrgEventDto::getCreatedAt).reversed());

        return new GitHubOrgEventResponse(all.size(), all);
    }

    private List<GitHubOrgEventDto> fetchOrgIssuePrEvents(String org) {
        final int perPage = 100;
        int page = 1;

        List<GitHubOrgEventDto> acc = new ArrayList<>();

        while (true) {
            String url = String.format("%s/orgs/%s/events?per_page=%d&page=%d", apiUrl, org, perPage, page);

            ResponseEntity<GitHubOrgEventDto[]> res = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    githubEntity(),
                    GitHubOrgEventDto[].class
            );

            GitHubOrgEventDto[] body = res.getBody();
            if (body == null || body.length == 0) break;

            Arrays.stream(body)
                    .filter(e -> "IssuesEvent".equals(e.getType())
                            || "PullRequestEvent".equals(e.getType())
                            || "PullRequestReviewEvent".equals(e.getType())
                            || "PullRequestReviewCommentEvent".equals(e.getType()))
                    .forEach(acc::add);

            if (body.length < perPage) break;
            page++;
        }

        return acc;
    }


    public WeeklyDashboardResponse getStats(Long teamId, String username) {
        Team team = teamRepository.findById(teamId).orElseThrow();

        int totIssues = 0, totPrs = 0;
        int myIssues = 0, myPrs = 0;

        for (TeamOrganization org : Optional.ofNullable(team.getTeamOrganizations()).orElse(Collections.emptyList())) {
            String orgName = org.getOrgName();

            // 전체
            totIssues  += searchIssuesPrCountOrg(orgName, "is:issue", null);
            totPrs     += searchIssuesPrCountOrg(orgName, "is:pr", null);

            // 내 기여
            String author = "author:" + username;
            myIssues   += searchIssuesPrCountOrg(orgName, "is:issue", author);
            myPrs      += searchIssuesPrCountOrg(orgName, "is:pr", author);
        }

        List<WeeklyMetricWithShareDto> metrics = List.of(
                WeeklyMetricWithShareDto.of("이슈 생성", totIssues, myIssues, pct(myIssues, totIssues)),
                WeeklyMetricWithShareDto.of("PR 생성", totPrs, myPrs, pct(myPrs, totPrs))
        );

        return new WeeklyDashboardResponse(metrics);
    }

    private int searchIssuesPrCountOrg(String org, String kindQualifier, String extra) {
        // org 단위 검색
        String q = "%s+org:%s".formatted(kindQualifier, org);
        if (extra != null && !extra.isBlank()) q += "+" + extra;

        String url = "%s/search/issues?q=%s&per_page=1".formatted(apiUrl, q);

        ResponseEntity<com.fasterxml.jackson.databind.JsonNode> resp =
                restTemplate.exchange(url, HttpMethod.GET, githubEntityJson(), com.fasterxml.jackson.databind.JsonNode.class);

        var body = resp.getBody();
        return (body == null || body.get("total_count") == null) ? 0 : body.get("total_count").asInt();
    }



    private HttpEntity<Void> githubEntityJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.set("Accept", "application/vnd.github+json");
        return new HttpEntity<>(headers);
    }

    private double pct(int mine, int total) {
        return total == 0 ? 0.0 : (mine * 100.0 / total);
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