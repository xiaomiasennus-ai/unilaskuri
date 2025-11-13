package com.example.unilaskuri

import androidx.room.*

@Entity(tableName = "sleep_sessions")
data class SleepSession(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: String,               // yyyy-MM-dd
    val totalMinutes: Int
)

@Dao
interface SleepDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(session: SleepSession)

    @Query("SELECT * FROM sleep_sessions ORDER BY id DESC LIMIT 30")
    suspend fun getAll(): List<SleepSession>
}

@Database(entities = [SleepSession::class], version = 1, exportSchema = false)
abstract class SleepDatabase : RoomDatabase() {
    abstract fun sleepDao(): SleepDao

    companion object {
        @Volatile private var INSTANCE: SleepDatabase? = null

        fun getDatabase(context: android.content.Context): SleepDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SleepDatabase::class.java,
                    "sleep_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
