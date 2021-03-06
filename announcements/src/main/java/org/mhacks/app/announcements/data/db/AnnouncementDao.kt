package org.mhacks.app.announcements.data.db

import androidx.room.*
import io.reactivex.Single
import org.mhacks.app.announcements.data.model.Announcement

/**
 * Dao for Announcement model.
 */

@Dao
abstract class AnnouncementDao {

    @Query("SELECT * FROM announcement")
    abstract fun getAnnouncements(): Single<List<Announcement>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun updateAnnouncements(announcements: List<Announcement>)

    @Query("DELETE FROM announcement")
    abstract fun deleteAnnouncements()

    @Transaction
    open fun deleteAndUpdateAnnouncements(announcements: List<Announcement>) {
        deleteAnnouncements()
        updateAnnouncements(announcements)
    }
}