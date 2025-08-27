package com.groupstudy.domain.github.dto.response;

import com.groupstudy.domain.github.dto.GitHubPullRequestDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GithubPrResponse {
    private int count;
    private List<GitHubPullRequestDto> items;
}
