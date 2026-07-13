package com.example.FlowWorks.rank.application.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record UpdateRankRequest(
        @NotBlank
        String name,

        @Min(1)
        @Max(10)
        int level) {
}
