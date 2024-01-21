package com.arturmaslov.vismasound.data.source.local

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.arturmaslov.vismasound.data.models.TrackEntity

@Database(
    entities = [
        TrackEntity::class,
    ], version = 1
)
@TypeConverters(Converters::class)
abstract class LocalDatabase : RoomDatabase() {
    // The associated DAOs for the database
    abstract val trackDao: TrackDao?

    companion object {
        const val DATABASE_NAME = "VISMA_SOUND_DB"
    }
}

@Dao
interface TrackDao {
    @Query("SELECT * FROM trackentity")
    fun getTracks(): List<TrackEntity>?

    // returns row id of inserted item
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTrack(track: TrackEntity): Long

    // returns number of rows affected
    @Query("DELETE FROM trackentity")
    fun deleteTracks(): Int

    // returns number of rows affected
    @Query("DELETE FROM trackentity WHERE id = :trackId")
    fun deleteTrack(trackId: Int): Int

}