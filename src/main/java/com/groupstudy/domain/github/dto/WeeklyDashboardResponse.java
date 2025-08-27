package com.groupstudy.domain.github.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeeklyDashboardResponse {
    private List<WeeklyMetricWithShareDto> items;
}