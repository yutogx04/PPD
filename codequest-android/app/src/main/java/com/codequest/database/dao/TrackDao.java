package com.codequest.database.dao;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.codequest.database.entity.TrackEntity;
import java.util.List;
@Dao
public interface TrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<TrackEntity> tracks);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTrack(TrackEntity track);
    @Query("SELECT * FROM tracks ORDER BY id ASC")
    List<TrackEntity> getAllTracks();
    @Query("SELECT * FROM tracks WHERE id = :trackId LIMIT 1")
    TrackEntity getTrackById(long trackId);
    @Query("SELECT * FROM tracks WHERE difficulty = :difficulty")
    List<TrackEntity> getTracksByDifficulty(String difficulty);
    @Query("DELETE FROM tracks")
    void clearAll();
    @Query("UPDATE tracks SET progressPercent = :progress WHERE id = :trackId")
    void updateProgress(long trackId, int progress);
}
