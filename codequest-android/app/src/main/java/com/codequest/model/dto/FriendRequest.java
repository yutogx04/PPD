package com.codequest.model.dto;
public class FriendRequest {
    private long targetUserId;
    public FriendRequest(long targetUserId) {
        this.targetUserId = targetUserId;
    }
    public long getTargetUserId() {
        return targetUserId;
    }
}
