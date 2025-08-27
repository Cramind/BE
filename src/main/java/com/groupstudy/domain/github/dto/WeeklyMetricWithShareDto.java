package com.groupstudy.domain.github.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class WeeklyMetricWithShareDto {
    private String label;

    private int totalCount;
    private int myCount;

    private double sharePercent;
}