package com.codequest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class LeaderboardEntry {

    private int rank;
    private String pseudo;
    private String avatarUrl;
    private int level;
    private int xp;
    private boolean isCurrentUser;
}
