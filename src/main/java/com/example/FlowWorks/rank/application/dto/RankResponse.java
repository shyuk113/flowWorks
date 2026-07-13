package com.example.FlowWorks.rank.application.dto;

import com.example.FlowWorks.rank.domain.Rank;

public record RankResponse(Long id, String name, int level) {

    public static RankResponse from(Rank rank){
        return new RankResponse(rank.getId(), rank.getName(), rank.getLevel());
    }
}
