package com.groupstudy.domain.github.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ReviewStatsResponse {
    TotalReviewStatsDto totalReviewStatsDto;
    List<UserReviewStatsDto> userReviewStatsDtoList;
}
