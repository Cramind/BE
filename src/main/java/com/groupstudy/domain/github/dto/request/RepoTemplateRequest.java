package com.groupstudy.domain.github.dto.request;

public record RepoTemplateRequest(
        String category, String owner, String repo
) {
}