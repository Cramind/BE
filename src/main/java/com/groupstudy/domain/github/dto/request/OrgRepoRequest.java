package com.groupstudy.domain.github.dto.request;

public record OrgRepoRequest(
        String orgName,
        String repoName,
        boolean isPrivate
) {
}
