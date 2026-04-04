package com.codequest.database.dao;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.codequest.database.entity.UserEntity;
@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(UserEntity user);
    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    UserEntity getUserById(long userId);
    @Query("SELECT * FROM users LIMIT 1")
    UserEntity getCurrentUser();
    @Query("DELETE FROM users")
    void clearAll();
    @Query("UPDATE users SET xp = :xp, level = :level, streak = :streak, " +
            "totalLessonsCompleted = :lessons, totalChallengesSolved = :challenges WHERE id = :userId")
    void updateStats(long userId, int xp, int level, int streak, int lessons, int challenges);
}
