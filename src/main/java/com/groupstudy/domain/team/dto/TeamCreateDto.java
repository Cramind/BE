package com.groupstudy.domain.team.dto;

import jakarta.validation.constraints.NotBlank;

public record TeamCreateDto(
        @NotBlank
        String teamName,
        String teamDescription,
        String selectedOrganization
) {
}
