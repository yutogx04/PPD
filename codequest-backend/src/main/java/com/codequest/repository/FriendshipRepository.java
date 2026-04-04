package com.codequest.repository;

import com.codequest.entity.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    @Query("SELECT f FROM Friendship f WHERE (f.sender.id = :userId OR f.receiver.id = :userId) AND f.status = 'ACCEPTED'")
    List<Friendship> findAcceptedFriendships(Long userId);

    List<Friendship> findByReceiverIdAndStatus(Long receiverId, Friendship.FriendshipStatus status);

    Optional<Friendship> findBySenderIdAndReceiverId(Long senderId, Long receiverId);

    @Query("SELECT f FROM Friendship f WHERE ((f.sender.id = :userId1 AND f.receiver.id = :userId2) OR (f.sender.id = :userId2 AND f.receiver.id = :userId1))")
    Optional<Friendship> findBetweenUsers(Long userId1, Long userId2);
}
