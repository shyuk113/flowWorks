package com.example.FlowWorks.rank.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Rank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private int level;

    @Builder
    private Rank(String name, int level) {
        this.name = name;
        this.level = level;
    }

    public static Rank createRank(String name, int level) {
        return Rank.builder()
                .name(name)
                .level(level)
                .build();
    }

    public void updateRank(String name, int level){
        this.name = name;
        this.level = level;
    }
}
