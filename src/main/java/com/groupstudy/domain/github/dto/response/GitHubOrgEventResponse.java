package com.groupstudy.domain.github.dto.response;

import com.groupstudy.domain.github.dto.GitHubOrgEventDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GitHubOrgEventResponse {
    private int count;
    private List<GitHubOrgEventDto> items;
}
