package com.codequest.service;

import com.codequest.dto.FriendDto;
import com.codequest.entity.Friendship;
import com.codequest.entity.User;
import com.codequest.repository.FriendshipRepository;
import com.codequest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendshipRepository friendshipRepository;
    private final UserRepository userRepository;

    @Transactional
    public void sendFriendRequest(Long senderId, Long receiverId) {
        if (senderId.equals(receiverId)) {
            throw new RuntimeException("Vous ne pouvez pas vous ajouter vous-même");
        }

        var existing = friendshipRepository.findBetweenUsers(senderId, receiverId);
        if (existing.isPresent()) {
            Friendship f = existing.get();
            if (f.getStatus() == Friendship.FriendshipStatus.ACCEPTED) {
                throw new RuntimeException("Vous êtes déjà amis");
            }
            if (f.getStatus() == Friendship.FriendshipStatus.PENDING) {
                throw new RuntimeException("Une demande d'ami est déjà en cours");
            }
        }

        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Utilisateur destinataire non trouvé"));

        Friendship friendship = Friendship.builder()
                .sender(sender)
                .receiver(receiver)
                .status(Friendship.FriendshipStatus.PENDING)
                .build();
        friendshipRepository.save(friendship);
    }

    @Transactional
    public void acceptFriendRequest(Long friendshipId, Long userId) {
        Friendship friendship = friendshipRepository.findById(friendshipId)
                .orElseThrow(() -> new RuntimeException("Demande d'ami non trouvée"));

        if (!friendship.getReceiver().getId().equals(userId)) {
            throw new RuntimeException("Vous ne pouvez pas accepter cette demande");
        }

        friendship.setStatus(Friendship.FriendshipStatus.ACCEPTED);
        friendshipRepository.save(friendship);
    }

    @Transactional
    public void rejectFriendRequest(Long friendshipId, Long userId) {
        Friendship friendship = friendshipRepository.findById(friendshipId)
                .orElseThrow(() -> new RuntimeException("Demande d'ami non trouvée"));

        if (!friendship.getReceiver().getId().equals(userId)) {
            throw new RuntimeException("Vous ne pouvez pas refuser cette demande");
        }

        friendship.setStatus(Friendship.FriendshipStatus.REJECTED);
        friendshipRepository.save(friendship);
    }

    public List<FriendDto> getFriends(Long userId) {
        return friendshipRepository.findAcceptedFriendships(userId).stream()
                .map(f -> {
                    User friend = f.getSender().getId().equals(userId)
                            ? f.getReceiver() : f.getSender();
                    return toFriendDto(f, friend);
                })
                .collect(Collectors.toList());
    }

    public List<FriendDto> getPendingRequests(Long userId) {
        return friendshipRepository.findByReceiverIdAndStatus(
                userId, Friendship.FriendshipStatus.PENDING
        ).stream()
                .map(f -> toFriendDto(f, f.getSender()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void removeFriend(Long friendshipId, Long userId) {
        Friendship friendship = friendshipRepository.findById(friendshipId)
                .orElseThrow(() -> new RuntimeException("Amitié non trouvée"));

        if (!friendship.getSender().getId().equals(userId)
                && !friendship.getReceiver().getId().equals(userId)) {
            throw new RuntimeException("Vous ne pouvez pas supprimer cette amitié");
        }

        friendshipRepository.delete(friendship);
    }

    private FriendDto toFriendDto(Friendship friendship, User friend) {
        return FriendDto.builder()
                .id(friend.getId())
                .pseudo(friend.getPseudo())
                .avatarUrl(friend.getAvatarUrl())
                .xp(friend.getXp())
                .level(friend.getLevel())
                .streak(friend.getStreak())
                .friendshipStatus(friendship.getStatus().name())
                .lastActivity(friend.getLastActivityDate() != null
                        ? friend.getLastActivityDate().toString() : null)
                .build();
    }
}
