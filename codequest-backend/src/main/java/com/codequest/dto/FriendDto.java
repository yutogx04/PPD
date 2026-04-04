package com.codequest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class FriendDto {

    private Long id;
    private String pseudo;
    private String avatarUrl;
    private int level;
    private int xp;
    private int streak;
    private String friendshipStatus;
    private String lastActivity;
}
