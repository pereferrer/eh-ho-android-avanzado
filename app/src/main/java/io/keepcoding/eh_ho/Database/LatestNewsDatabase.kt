package io.keepcoding.eh_ho.Database

import androidx.room.*

@Entity(tableName = "LatestNew")
data class LatestNewEntity(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    @ColumnInfo(name = "latest_new_id") val topicId: String,
    @ColumnInfo(name = "latest_new_topic_title") val title: String,
    @ColumnInfo(name = "latest_new_topic_slug") val slug: String,
    @ColumnInfo(name = "latest_new_created_at") val date: String,
    @ColumnInfo(name = "latest_new_post_number") val posts: Int,
    @ColumnInfo(name = "latest_new_score") val score: Double
)

@Dao
interface LatestNewDao {
    @Query("SELECT * FROM LatestNew")
    fun getLatestNews(): List<LatestNewEntity>

    @Query("SELECT * FROM LatestNew WHERE latest_new_id LIKE :id")
    fun getLatestNewsById(id: String): LatestNewEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(latestNewsList: List<LatestNewEntity>): List<Long>

    @Delete
    fun delete(LatestNew: LatestNewEntity)
}

@Database(entities = [LatestNewEntity::class], version = 3)
abstract class LatestNewsDatabase : RoomDatabase() {
    abstract fun latestNewDao(): LatestNewDao
}
